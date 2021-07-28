package DataStore.Meta

import java.io.{File, RandomAccessFile}

import DataStore.EasyGraphStore.Meta.{NameStore, NodeLabelNameStore}
import Util.FptrFactory
import org.junit.{Assert, Test}

class NameStoreTest {


  @Test
  def testNameStore(): Unit ={
    val path = "D:\\graphStore\\namestore"
    val file = new File(path)
    val store = new NameStore {
      override val fptr: RandomAccessFile = FptrFactory.getFptr(file, "rw")
      init()
    }

    val i1 = store.addLabelString("student")
    val i2 = store.addLabelString("person")
    val i3 = store.addLabelString("hahahahha")
    Assert.assertEquals(3, store.getLabelStringSize())
    store.close()

    val store1 =  new NameStore {
      override val fptr: RandomAccessFile = FptrFactory.getFptr(file, "rw")
      init()
    }

    Assert.assertEquals("person", store1.getlabelStringById(i2))
    Assert.assertEquals("student", store1.getlabelStringById(i1))
    Assert.assertEquals("hahahahha", store1.getlabelStringById(i3))

    store1.close()

    //file.delete()

  }

}
