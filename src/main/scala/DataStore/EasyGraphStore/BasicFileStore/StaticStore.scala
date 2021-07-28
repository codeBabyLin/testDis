package DataStore.EasyGraphStore.BasicFileStore

import java.io.{ByteArrayInputStream, DataInputStream, File, RandomAccessFile}

trait StaticStore {
  val fptr: RandomAccessFile
  val block_length: Int

  val BLOCK_DATA_ARRAY:Array[Byte] = new Array[Byte](block_length)

  private def offset(recordId: Long): Long = {
    recordId * block_length
  }

  private def count(): Long = {
    fptr.length() / block_length
  }

  def read(recordId: Long): Array[Byte] ={
    fptr.seek(offset(recordId))
    val BLOCK_DATA_ARRAY_FOR_READ = new Array[Byte](block_length)
    fptr.readFully(BLOCK_DATA_ARRAY_FOR_READ)
    BLOCK_DATA_ARRAY_FOR_READ
  }

  def write(recordId: Long, dataArray: Array[Byte]): Unit ={
    fptr.seek(offset(recordId))
    fptr.write(dataArray)
  }

  def write(dataArray: Array[Byte]): Long ={
    val nextId = this.nextId()
    write(nextId, dataArray)
    nextId
  }

  def nextId(): Long = {
    val localId = count()
    fptr.seek(offset(localId))
    fptr.write(BLOCK_DATA_ARRAY)
    localId
  }

}
