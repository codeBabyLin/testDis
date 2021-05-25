package DataStore

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream, File, RandomAccessFile}

import scala.collection.mutable.ArrayBuffer

object fptrFactory{
  def getFptr(fileName: String, opMode: String): RandomAccessFile ={
    val fileMetaFile1 = new File(fileName)
    new RandomAccessFile(fileMetaFile1, opMode)
  }

  def getFptr(fileName: File, opMode: String): RandomAccessFile ={
    new RandomAccessFile(fileName, opMode)
  }
}

class ReuseIdStore(fileName: File, opMode: String){
  val fptr: RandomAccessFile = fptrFactory.getFptr(fileName, opMode)
  var reuseIdArray: ArrayBuffer[Long] = new ArrayBuffer[Long]()
  var reuseIdsize: Long = 0
  if(fptr.length() == 0){
    fptr.writeLong(reuseIdsize)
  }
  else reuseIdsize = fptr.readLong()

  while(reuseIdsize >=1){
    reuseIdArray += fptr.readLong()
    reuseIdsize -= 1
  }

  reuseIdsize = reuseIdArray.size.toLong

  def hasReuseId(): Boolean ={
    if (reuseIdsize <= 0) false
    else true
  }

  def getReuseIdSize(): Long = {
    reuseIdArray.size.toLong
  }

  def getAvaliableId(): Long = {
    reuseIdArray.remove(0)
  }
  def addReuseId(id: Long): Unit ={
    reuseIdArray += id
  }

  def close(): Unit ={
    fptr.seek(0)
    fptr.writeLong(reuseIdArray.size.toLong)
    reuseIdArray.map(fptr.writeLong)
    fptr.close()
  }

}

class DynamicBlockStore(fileName: File, opMode: String){
  val fptr: RandomAccessFile = fptrFactory.getFptr(fileName, opMode)

  def read(offset:Long, length: Long): Array[Byte] ={
    fptr.seek(offset)
    val byteArray: Array[Byte] = new Array[Byte](length.toInt)
    fptr.read(byteArray)
    byteArray
  }

  def write(blocks: Array[Byte]): Long ={
    val offset = fptr.length()
    fptr.seek(offset)
    fptr.write(blocks)
    offset
  }

  def close(): Unit ={
    fptr.close()
  }

}


case class recordMeta(id: Long, status: Long, offset: Long, length: Long)

class RecorderMetaStore(fileName: File, opMode: String){
  val fptr: RandomAccessFile = fptrFactory.getFptr(fileName, opMode)

  val METADATA_BLOCK_FOR_READ = new Array[Byte](Constans.recordMetalength)

  def offsets(recordId: Long): Long ={
    recordId * Constans.recordMetalength
  }

  def read(recordId: Long): recordMeta ={
    fptr.seek(offsets(recordId))
    fptr.readFully(METADATA_BLOCK_FOR_READ)
    val dis = new DataInputStream(new ByteArrayInputStream(METADATA_BLOCK_FOR_READ))
    val info = recordMeta(dis.readLong(), dis.readLong(), dis.readLong(), dis.readLong())
    dis.close()
    info
  }


  def write(recordId:Long, status: Long, offset: Long, length: Long): Unit ={

    val block = new ByteArrayOutputStream()
    val dos = new DataOutputStream(block)
    dos.writeLong(recordId)
    dos.writeLong(status)
    dos.writeLong(offset)
    dos.writeLong(length)
    fptr.seek(offsets(recordId))
    fptr.write(block.toByteArray)
    dos.close()

  }

  def count(): Long = {
    fptr.length() / Constans.recordMetalength
  }

  def createNextId(): Long ={
    val localId = count()
    fptr.seek(localId * Constans.recordMetalength)
    write(localId, -1, -1, -1)
    localId
  }

  def close(): Unit ={
    fptr.close()
  }


}



class DynamicStore(storeDir: File) {
  private val reuseIdStore = new ReuseIdStore(new File(storeDir, "reuseId"), "rw")
  private val metaStore = new RecorderMetaStore(new File(storeDir, "meta"), "rw")
  private val bodyStore = new DynamicBlockStore(new File(storeDir, "body"), "rw")

  def writeBlock(blocks: Array[Byte]): Long ={
    val recordId = {
      if(reuseIdStore.hasReuseId())
        reuseIdStore.getAvaliableId()
      else metaStore.createNextId()
    }
    val offset = bodyStore.write(blocks)
    val length = blocks.length.toLong

    metaStore.write(recordId, 1, offset, length)
    recordId
  }

  def readBlock(blockId: Long): Array[Byte] = {
    val meta = metaStore.read(blockId)
    bodyStore.read(meta.offset, meta.length)
  }

  def close(): Unit ={
    reuseIdStore.close()
    metaStore.close()
    bodyStore.close()
  }
}



