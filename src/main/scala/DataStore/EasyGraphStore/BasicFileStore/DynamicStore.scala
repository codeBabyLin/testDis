package DataStore.EasyGraphStore.BasicFileStore

import java.io.RandomAccessFile


trait DynamicStore{
  val fptr: RandomAccessFile

  def read(offset:Long, length: Long): Array[Byte] ={
    fptr.seek(offset)
    val byteArray: Array[Byte] = new Array[Byte](length.toInt)
    fptr.read(byteArray)
    byteArray
  }

  def write(blocks: Array[Byte]): (Long,Long) ={
    val offset = fptr.length()
    fptr.seek(offset)
    fptr.write(blocks)
    (offset,blocks.length.toLong)
  }

  def close(): Unit ={
    fptr.close()
  }

}