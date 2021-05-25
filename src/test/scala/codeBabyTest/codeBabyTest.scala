package codeBabyTest

import java.io._

import DataStore.ReuseIdStore
import org.junit.{Assert, Test}

import scala.collection.mutable

class codeBabyTest {
  @Test
  def testop(): Unit ={
    val v = 8
    println(v << 3)
  }
  @Test
  def testStoreDynamicBlock(): Unit ={
    val fileName1 = "D:\\CypherLog\\DynamicBlockStore.body"
    val fileMetaFile1 = new File(fileName1)
    val fptr1 = new RandomAccessFile(fileMetaFile1, "rw")


    val fileName2 = "D:\\CypherLog\\DynamicBlockStore.meta"
    val fileMetaFile2 = new File(fileName2)
    val fptr2 = new RandomAccessFile(fileMetaFile2, "rw")

    val fileName3 = "D:\\CypherLog\\DynamicBlockStore.id"
    val fileMetaFile3 = new File(fileName3)
    val fptr3 = new RandomAccessFile(fileMetaFile3, "rw")
    if(fptr3.length() == 0){
      fptr3.writeLong(0)
    }
    else
      fptr3.readLong()

    //


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
    val fptr4 = new RandomAccessFile(fileMetaFile1, "rw")
    val id = 3

    fptr4.seek(id * block_size)
    fptr4.readFully(METADATA_BLOCK_FOR_READ)


    val dis = new DataInputStream(new ByteArrayInputStream(METADATA_BLOCK_FOR_READ))
    println(dis.readLong())
    println(dis.readBoolean())
    val si = dis.readInt()
    println(si)
    //val arr = new Array[Byte](si)
    val str = dis.readNBytes(si)
    println(new String(str))

  }


  @Test
  def testSeek(): Unit ={
    val fileName1 = "D:\\CypherLog\\DynamicBlockStore\\test.seek"
    val fileMetaFile1 = new File(fileName1)
    val fptr1 = new RandomAccessFile(fileMetaFile1, "rw")

    fptr1.writeLong(1)
    fptr1.writeLong(2)
    fptr1.writeLong(3)
    fptr1.writeLong(4)
    fptr1.writeLong(5)
    fptr1.seek(0)
    fptr1.writeLong(100)
    val is = fptr1.readLong()
    //fptr1
    println(is)

    fptr1.seek(1*8)
    println(fptr1.readLong())
    fptr1.seek(2*8)
    println(fptr1.readLong())
    fptr1.seek(3*8)
    println(fptr1.readLong())
    fptr1.seek(4*8)
    println(fptr1.readLong())

    fptr1.seek(fptr1.length())
    println(fptr1.getFilePointer)

  }

  @Test
  def testAr(): Unit ={
    val filename = "D:\\CypherLog\\DynamicBlockStore\\id.seek"
    val reuseIdf = new ReuseIdStore(new File(filename), "rw")
    reuseIdf.addReuseId(1)
    reuseIdf.addReuseId(2)
    reuseIdf.addReuseId(3)
    reuseIdf.addReuseId(4)
    reuseIdf.addReuseId(5)
    reuseIdf.addReuseId(6)
    Assert.assertEquals(6,reuseIdf.getReuseIdSize())

    reuseIdf.close()
    val reuseIdf2 = new ReuseIdStore(new File(filename), "rw")

    reuseIdf2.getAvaliableId()

    Assert.assertEquals(5,reuseIdf2.getReuseIdSize())
  }

  @Test
  def testLength(): Unit ={
    case class recordMeta(id: Long, status: Long, offset: Long, length: Long)
    val s2 = recordMeta(0,0,2,3)
    println(s2)
  }

  @Test
  def testMap(): Unit ={
    val msp: mutable.Map[Long,String] = mutable.Map()
    msp +=  (1.toLong->"1")
  }


}
