package DataStore.Meta

import java.io.{File, RandomAccessFile}

import DataStore.EasyGraphStore.Meta.{NameStore, NodeLabelNameStore}
import Util.FptrFactory
import org.junit.{Assert, Test}

class NodeLabelNameStoreTest {
  @Test
  def testNameStore(): Unit ={
    val path = "D:\\graphStore\\labeltore"
    val file = new File(path)
    val store = new NodeLabelNameStore(file)

    val i1 = store.addLabelName("student")
    val i2 = store.addLabelName("person")
    val i3 = store.addLabelName("hahahahha")
    Assert.assertEquals(3, store.getLabelStringSize())
    store.close()

    val store1 =  new NodeLabelNameStore(file)

    Assert.assertEquals("person", store1.getNameById(i2))
    Assert.assertEquals("student", store1.getNameById(i1))
    Assert.assertEquals("hahahahha", store1.getNameById(i3))

    Assert.assertEquals(i1, store1.getIdByName("student"))
    Assert.assertEquals(i2, store1.getIdByName("person"))
    Assert.assertEquals(i3, store1.getIdByName("hahahahha"))

    store1.close()

    file.delete()

  }
}
