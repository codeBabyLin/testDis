package VersionManage

import java.io.File

import Util.CypherTransForm
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb._

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source




class QueryQueryChangeImplNeo4j extends GraphDataVersion {
  def excuteCypher(dataVersion: String, cypherStringArray: Array[String], dataPathName: String): Unit ={
    //val file = new File("F:\\CNIC\\data\\" + dataVersion)
    val file = new File(dataPathName + dataVersion)
    val managementService = new DatabaseManagementServiceBuilder(file).loadPropertiesFromFile("F:\\IdCode\\testDis\\"+"neo4j.conf").build()
    val db = managementService.database("neo4j")
    cypherStringArray.foreach(db.executeTransactionally)
    managementService.shutdown()

  }

  def getResult(dataVersion: String, cypher: String, dataPathName: String): Any ={
    //val dataVersion = "testr"
    val file = new File(dataPathName + dataVersion)
    val managementService = new DatabaseManagementServiceBuilder(file).loadPropertiesFromFile("F:\\IdCode\\testDis\\"+"neo4j.conf").build()
    val db = managementService.database("neo4j")

    //val c1 = "create(n:test{name:'haha', age:5})"
    //    //db.executeTransactionally(c1)
    //    //val c2 = "match(n) return n"

    val tr: ResultTransformer[String] = new ResultTransformer[String] {
      override def apply(t: Result): String = t.resultAsString()
    }
    val mp: java.util.Map[String, Object] = Map.empty[String, Object].asJava
    val res = db.executeTransactionally(cypher, mp, tr)
    //println(res)

    managementService.shutdown()
    res
  }

  override def queryNodeChange(): Unit = ???

  override def queryRelationChange(): Unit = ???

  override def querySubGraphChange(): Unit = ???

  override def queryBigChange(): Unit = ???

  override def queryVersionDiff(): Unit = ???

  override def queryVersionGraph(): Unit = ???

  override def storeChange(logPath: String): Unit = ???

  override def showChange(): Unit = ???
}

class QueryChangeImplNeo4jStoreChangeForAll extends QueryQueryChangeImplNeo4j {

  def queryWithVersion(dataPathName: String, version: String, cypher: String): Any ={
    getResult(version, cypher, dataPathName)
  }

  def storeChange(logPath: String, dataPathName: String): Unit ={
    var vId: Int = 0
    var newCypherflag: Boolean = false
    val cyphers: ArrayBuffer[String] = new ArrayBuffer[String]()

    val file = Source.fromFile(logPath)
    for(line<- file.getLines()){
      if(line.contains("#####################")){
        if(newCypherflag) {
          excuteCypher(s"v$vId", cyphers.toArray, dataPathName)
          vId = vId + 1
          newCypherflag = false
          //cyphers.clear()
        }
      }
      else {
        cyphers += line
        newCypherflag = true
      }
    }
    if (cyphers.nonEmpty) excuteCypher(s"v$vId", cyphers.toArray, dataPathName)
    file.close()
  }

}


class QueryChangeImplNeo4jStoreChangeForOne extends QueryQueryChangeImplNeo4j {

  def queryWithVersion(dataPathName: String, version: String, cypher: String): Any ={
    getResult(version, cypher, dataPathName)
  }

  def storeChange(logPath: String, dataPathName: String): Unit ={
    val vId: Int = 1001

    val cyphers: ArrayBuffer[String] = new ArrayBuffer[String]()


    val file = Source.fromFile(logPath)
    for(line<- file.getLines()){
      if(line.contains("#####################")){
      }
      else {
        if(line.contains("set")){
          cyphers  += CypherTransForm.update2create(line)
        }
        else cyphers += line
      }
    }
    if (cyphers.nonEmpty) excuteCypher(s"v$vId", cyphers.toArray, dataPathName)
    file.close()
  }


}
