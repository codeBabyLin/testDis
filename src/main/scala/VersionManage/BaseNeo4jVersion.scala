package VersionManage

import java.io.{File, RandomAccessFile}

import Util.{FileCopy, FptrFactory}
import org.neo4j.dbms.api.{DatabaseManagementService, DatabaseManagementServiceBuilder}
import org.neo4j.graphdb.{GraphDatabaseService, Result, ResultTransformer, Transaction}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Neo4jVersionDBFactory{
  private final val confName: String = "F:\\IdCode\\testDis\\"+"neo4j.conf"
  def getDB(dataPath: String, version: String): GraphDatabaseService = {
    getDB(new File(dataPath), version)
  }
  def getDB(dataDir: File, version: String): GraphDatabaseService = {
    getDBManage(dataDir, version).database("neo4j")
  }
  def getDBManage(dataDir: File, version: String): DatabaseManagementService = {
    val fileName = getFilePath(dataDir, version)
    new DatabaseManagementServiceBuilder(new File(fileName)).loadPropertiesFromFile(confName).build()
  }

   def getFilePath(file: File, version: String): String ={

    val path = new File(file, version).getAbsolutePath
    val isOkDir = new File(path).mkdirs()
    path

  }
}



class BaseNeo4jVersion(dataDir:File) {
  private var latestV: Long = 0
  private var currentV: Long = 0
  dataDir.mkdirs()
  private val fptr:RandomAccessFile = FptrFactory.getFptr(new File(dataDir, "lastVersion"), "rw")
  if(fptr.length() == 0){
    fptr.writeLong(latestV)
  }
  else{
    latestV = fptr.readLong()
    currentV = latestV
  }

  private var dbManage: DatabaseManagementService = Neo4jVersionDBFactory.getDBManage(dataDir, s"${currentV}")
  private var db: GraphDatabaseService = dbManage.database("neo4j")

  def executeCypher(cypher: String): Unit ={
    db.executeTransactionally(cypher)
  }

  def queryResult(cypher: String): String ={
    val tr: ResultTransformer[String] = new ResultTransformer[String] {
      override def apply(t: Result): String = t.resultAsString()
    }
    val mp: java.util.Map[String, Object] = Map.empty[String, Object].asJava
    val res: String = db.executeTransactionally(cypher, mp, tr)
    res
  }

  def getTransaction(): Transaction ={
    db.beginTx()
  }

  def getCurrentV(): Long ={
    currentV
  }
  def getLatestV(): Long ={
    latestV
  }

  def seekV(version: Long): Unit ={
    currentV = version
    //if(dbManage)
    dbManage.shutdown()
    dbManage = Neo4jVersionDBFactory.getDBManage(dataDir, s"${currentV}")
    db = dbManage.database("neo4j")
  }

  private def prepareData(currentV: Long, nextV: Long): Unit ={
    FileCopy.copyDir(new File(dataDir, s"${currentV}"), new File(dataDir, s"${nextV}"))
  }
  def markV(): Unit ={
    latestV += 1
    //currentV = latestV
    //dbManage.shutdownDatabase("neo4j")
    dbManage.shutdown()
    prepareData(latestV -1, latestV)

    seekV(latestV)
  }

  def close(): Unit ={
    fptr.seek(0)
    fptr.writeLong(latestV)
    dbManage.shutdown()
  }

}
//c1
//c2

class CypherLog(dataDir: File){
  val fptr: RandomAccessFile = FptrFactory.getFptr(new File(dataDir, "cypherLog"),"rw")
  def readCypher(offset: Long, counts: Long): Array[String] = {
    fptr.seek(offset)
    (1L to counts).toArray.map(x => fptr.readUTF())
  }
  def writeCypher(cyphers:Array[String]): Long = {
    val length = fptr.length()
    fptr.seek(length)
    cyphers.map(fptr.writeUTF)
    length
  }

}
//[versionsize][firstversion][lastversion][basicVersionSize][-1]
//[version][isBasicVersion][lastV][offset][counts]
class VersionRecord(dataDir: File){

  private val defaultRecordSize: Long = 8 * 5
  private val fptr: RandomAccessFile = FptrFactory.getFptr(new File(dataDir, "version"),"rw")
  private var versionSize: Long = 0
  private var firstVersion: Long = 0
  private var lastVersion: Long = 0
  private var basicVersionsize: Long = 0
  private var versionSeq: mutable.Seq[(Long, Long)] = mutable.Seq()
  if(fptr.length() ==0){
    fptr.writeLong(versionSize)
    fptr.writeLong(firstVersion)
    fptr.writeLong(lastVersion)
    fptr.writeLong(basicVersionsize)
    fptr.writeLong(-1)
  }
  else {
    versionSize = fptr.readLong()
    firstVersion = fptr.readLong()
    lastVersion = fptr.readLong()
    basicVersionsize = fptr.readLong()
  }

  def read(version: Long): (Long, Long, Long, Long, Long) = {
    fptr.seek(version * defaultRecordSize)
    (fptr.readLong(), fptr.readLong(), fptr.readLong(), fptr.readLong(),fptr.readLong())
  }

  //basic V, offset, counts
  def seekV(version: Long):Unit = {

  }

  def creaetNewVersion(): Long = {
    val version =  fptr.length() /defaultRecordSize
    lastVersion = version
    fptr.seek(fptr.length())
    fptr.writeLong(version)
    fptr.writeLong(-1)
    fptr.writeLong(-1)
    fptr.writeLong(-1)
    fptr.writeLong(-1)
    version
  }

  def write(version: Long, isBasicV: Long,  lastVersion: Long, offs: Long, counts: Long): Unit ={
    fptr.seek(version * defaultRecordSize)
    fptr.writeLong(version)
    fptr.writeLong(isBasicV)
    fptr.writeLong(lastVersion)
    fptr.writeLong(offs)
    fptr.writeLong(counts)
  }

  def getVersionSize: Long = fptr.length() / defaultRecordSize
  def getLatestV: Long = fptr.length() / defaultRecordSize
}


class VersioManage(dataDir: File){
  private val logStore: CypherLog = new CypherLog(dataDir)
  private val recordStore: VersionRecord = new VersionRecord(dataDir)

  def getNewVersion(): Long ={
    recordStore.creaetNewVersion()
  }

  def listAllVersion(): Map[Long, Long] ={
    val size = recordStore.getVersionSize
    (0L to size-1).map(recordStore.read).map(tup => tup._1 -> tup._3).toMap
  }

  def getLatestVersion(): Long = recordStore.getLatestV

  def markVersion(lastV: Long, newV: Long, cyphers: Array[String], isBasicV: Boolean): Unit = {
    val offset: Long = logStore.writeCypher(cyphers)
    val count: Long = cyphers.length.toLong
    val isBasicL: Long = if(isBasicV) 1L else 0
    recordStore.write(newV,isBasicL, lastV, offset, count)
  }

  //def seekVersion(seekv: Long): Unit = ???

  //def getCyphers(currentV: Long) : Seq[String] = ???

  private def getNameV(version: Long): Long = {
    val (_,_,nameV,_,_) = recordStore.read(version)
    nameV
  }

  private def getCyphersFromName(nameV: Long, currentV: Long): Seq[String] = {
    val (_,_,nameV,offset,count) = recordStore.read(currentV)
    if (nameV == currentV) Seq()
    else logStore.readCypher(offset, count).toSeq
  }

  def seekVersion(seekV: Long):(Long, Seq[String]) = {
    val (_,_,nameV,offset,count) = recordStore.read(seekV)
    if (nameV == seekV) (nameV, Seq())
    else (nameV, logStore.readCypher(offset, count).toSeq)
  }

  def close(): Unit ={
  }

}

class BaseNeo4jVersionWithLog(dataDir: File){


  //latestV must be a basicV
  private val vm: VersioManage = new VersioManage(dataDir)
  private var unCommitCyphers: ArrayBuffer[String] = ArrayBuffer[String]()
  private var latestBasicV: Long = 0
  private var currentV: Long = 0
  final private val  latestvName: String = "latest"

  dataDir.mkdirs()
  private val fptr:RandomAccessFile = FptrFactory.getFptr(new File(dataDir, "lastVersion"), "rw")
  if(fptr.length() == 0){
    //latestBasicV = vm.getNewVersion()
    //currentV = latestBasicV
    fptr.writeLong(latestBasicV)
  }
  else{
    latestBasicV = fptr.readLong()
    currentV = latestBasicV
    //val unCommitCypherCnt: Long = fptr.readLong()
    //(1L to unCommitCypherCnt).map(cnt => fptr.readUTF()).map(unCommitCyphers +=)
  }

  private var dbManage: DatabaseManagementService = _    //Neo4jVersionDBFactory.getDBManage(dataDir, s"${vm.getNameV(latestV)}")
  private var db: GraphDatabaseService = _          //dbManage.database("neo4j")
  private var tx: Transaction = _            //db.beginTx()

  init(latestvName)
  private def init(name: String): Unit ={
    dbManage = Neo4jVersionDBFactory.getDBManage(dataDir, name)
    db = dbManage.database("neo4j")
    tx= db.beginTx()
  }


  def executeCypher(cypher: String): Unit ={
    unCommitCyphers += cypher
    db.executeTransactionally(cypher)
    //tx.execute(cypher)
    //tx.
  }

  def queryResult(cypher: String): String ={
    tx.execute(cypher).resultAsString()

  }



  def getCurrentV(): Long ={
    currentV
  }
  def getLatestV(): Long ={
    vm.getLatestVersion()
  }

  def goBacktoLatestV(): Unit ={
    dbManage.shutdown()
    init(latestvName)
    currentV = vm.getLatestVersion()
  }

  def seekV(version: Long): Unit ={
    currentV = version
    dbManage.shutdown()
    val (basicV, cyphers) = vm.seekVersion(version)
    init(basicV.toString)
    cyphers.map(tx.execute)
  }

  def listAllVersion(): Map[Long, Long] ={
    vm.listAllVersion()
  }

  private def prepareData(currentV: Long, nextV: Long): Unit ={
    FileCopy.copyDir(new File(dataDir, s"${currentV}"), new File(dataDir, s"${nextV}"))
  }
  private def prepareData(latest: String, newV:Long): Unit ={
    FileCopy.copyDir(new File(dataDir, latest), new File(dataDir, s"${newV}"))
  }
  def markV(isMarkAsBasicVersion: Boolean = false): Long ={
    if(isMarkAsBasicVersion){
      //tx.commit()
      unCommitCyphers.clear()
      val newV: Long = vm.getNewVersion()
      latestBasicV = newV
      currentV = newV
      vm.markVersion(latestBasicV,newV,unCommitCyphers.toArray, true)
      dbManage.shutdown()
      prepareData(latestvName, newV)
      init(latestvName)

    }
    else{
      val newV: Long = vm.getNewVersion()
      currentV = newV
      vm.markVersion(latestBasicV,newV, unCommitCyphers.toArray, false )
    }
    currentV
  }

  def close(): Unit ={
    fptr.seek(0)
    fptr.writeLong(latestBasicV)
    dbManage.shutdown()
  }

}
