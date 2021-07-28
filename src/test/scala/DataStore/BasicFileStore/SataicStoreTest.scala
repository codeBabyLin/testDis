package DataStore.BasicFileStore

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream, File, RandomAccessFile}

import DataStore.EasyGraphStore.BasicFileStore.StaticStore
import Util.FptrFactory
import org.junit.Test

class SataicStoreTest {

  @Test
  def teststaticStore(): Unit ={
    val path = "D:\\graphStore\\staticStore"
    val file = new File(path)
    val store = new StaticStore {

      override val fptr: RandomAccessFile = FptrFactory.getFptr(file, "rw")
      override val block_length: Int = 24
    }

    val id: Long = store.nextId()
    val block = new ByteArrayOutputStream()
    val dos = new DataOutputStream(block)
    dos.writeLong(id)
    dos.writeLong(10L)
    dos.writeLong(100L)
    store.write(id, block.toByteArray)
    dos.close()

    //val bk = new ByteArrayInputStream(store.read(id))
    //val dosi  = new DataInputStream(bk)


    val dosi = new DataInputStream(new ByteArrayInputStream(store.read(id)))
    val s1 = dosi.readLong()
    val s2 = dosi.readLong()
    val s3 = dosi.readLong()
    println(s"id:${s1}, p1:${s2}, p2:${s3}")
    dosi.close()
    //println(store.read(id))
    store.fptr.close()

    file.delete()


  }

}
