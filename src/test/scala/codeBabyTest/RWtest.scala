package codeBabyTest

import java.io._

import GraphModel.{EasyNode, EasyRelation}
import org.junit.Test

class RWtest {



  var labelNameMeta:Map[Int, String] = _
  var pNameMeta: Map[Int, String]  = _
  val fileName1 = "D:\\CypherLog\\data.node"
  val fileName2 = "D:\\CypherLog\\data.rel"


  val fileMetaFile1 = new File(fileName1)
  val fileMetaFile2 = new File(fileName2)
  val fptr1 = new RandomAccessFile(fileMetaFile1, "rw")
  val fptr2 = new RandomAccessFile(fileMetaFile1, "rw")

  val METADATA_BLOCK_FOR_READ = new Array[Byte](100)
  def read(fptr:RandomAccessFile): Unit ={
    fptr.synchronized {
      //fptr.seek(offsetOf(localId))
      fptr.readFully(METADATA_BLOCK_FOR_READ)
    }

    val dis = new DataInputStream(new ByteArrayInputStream(METADATA_BLOCK_FOR_READ))
    //val info = FileMetadata(dis.readLong(), dis.readLong(), dis.readLong(), dis.readLong(), dis.readLong(), dis.readByte())
    dis.close()

  }

  def write(fptr: RandomAccessFile,localId: Long, status: Byte, offset: Long, length: Long, crc32: Long): Unit = {
    val block = new ByteArrayOutputStream()
    val dos = new DataOutputStream(block)
    dos.writeLong(localId)
    val time = System.currentTimeMillis()
    dos.writeLong(time)
    dos.writeLong(offset)
    dos.writeLong(length)
    dos.writeLong(crc32)
    dos.writeByte(status)
    //padding
    //dos.write(new Array[Byte](Constants.METADATA_ENTRY_LENGTH_WITH_PADDING - dos.size()))

    fptr.synchronized {
      //maybe overwrite
      //fptr.seek(offsetOf(localId))
      fptr.write(block.toByteArray)
    }

    dos.close()

    //cache.put(localId, FileMetadata(localId, time, offset, length, crc32, status))
  }

  def writeNode(nodes: Array[EasyNode]): Unit ={
    nodes.foreach(node => {

    })
  }

  def writeRelation(rels: EasyRelation): Unit ={

  }


  @Test
  def testWRBinary(): Unit ={

  }

  @Test
  def testoo(): Unit ={
    val x : EasyNode = EasyNode(1, "string", Map())
    if(x.parent==null)
      println(true)
    else println(false)
  }

  @Test
  def testfptr(): Unit ={
    val fileName1 = "D:\\CypherLog\\data.test"

    val fileMetaFile1 = new File(fileName1)
    val fptr1 = new RandomAccessFile(fileMetaFile1, "rw")
    val METADATA_BLOCK_FOR_READ = new Array[Byte](100)


    val block_size = 100
    for(i <- 1 to 5){
      val block = new ByteArrayOutputStream()
      val dos = new DataOutputStream(block)

      //val block1 = new ByteArrayOutputStream()
      //val dos1 = new DataOutputStream(block1)



      dos.writeLong(i)
      dos.writeBoolean(true)
      val str = s"hello world ${i}"
      val strArr = str.getBytes
      //dos1.writeChars(str)
     // println(strArr)
      dos.writeInt(strArr.size)
      dos.write(strArr)
      //dos.writeChars(s"hello world ${i}")
      dos.write(new Array[Byte](block_size - dos.size()))
      fptr1.write(block.toByteArray)
      //dos.close()
      //dos1.close()
    }
    fptr1.close()

    //val fileMetaFile2 = new File(fileName1)
    val fptr2 = new RandomAccessFile(fileMetaFile1, "rw")
    val id = 3

    fptr2.seek(id * block_size)
    fptr2.readFully(METADATA_BLOCK_FOR_READ)


    val dis = new DataInputStream(new ByteArrayInputStream(METADATA_BLOCK_FOR_READ))
    println(dis.readLong())
    println(dis.readBoolean())
    val si = dis.readInt()
    println(si)
    //val arr = new Array[Byte](si)
    val str = dis.readNBytes(si)
    println(new String(str))

    //println(str)

  }


  @Test
  def testArray(): Unit ={
    val fileName1 = "D:\\CypherLog\\data.test"

    val fileMetaFile1 = new File(fileName1)
    val fptr1 = new RandomAccessFile(fileMetaFile1, "rw")
    val METADATA_BLOCK_FOR_READ = new Array[Byte](100)
    val block_size = 100

    val block = new ByteArrayOutputStream()
    val dos = new DataOutputStream(block)




  }









}
