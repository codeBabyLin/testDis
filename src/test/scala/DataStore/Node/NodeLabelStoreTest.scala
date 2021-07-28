package DataStore.Node

import java.io.File

import DataStore.EasyGraphStore.Node.NodeLabelStore
import org.junit.{After, Assert, Test}

class NodeLabelStoreTest {

  @Test
  def testNodeLabelTest(): Unit ={
    val path = "D:\\graphStore\\labeltore"
    val file = new File(path)
    val nodeLabelstore = new NodeLabelStore(file)

    val id = nodeLabelstore.nextId()

    println(id)

    nodeLabelstore.set(id,1)
    nodeLabelstore.set(id,2)
    nodeLabelstore.set(id,3)
    nodeLabelstore.set(id,Array(4L,5L,6L))

    val ak = nodeLabelstore.getAll(id)
    Assert.assertEquals(6, ak.length)

    nodeLabelstore.removeLabel(id, 6)

    val sk = nodeLabelstore.getAll(id)

    Assert.assertEquals(5, sk.length)



    //nodeLabelstore.

  }

  @After
  def delete(): Unit ={
    val path = "D:\\graphStore\\labeltore"
    val file = new File(path)
    file.deleteOnExit()
  }

}
