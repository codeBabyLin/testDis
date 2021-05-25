package DataStore

import java.io.File

import DataStore.EasyGraphStore.{LabelNameMemoryStore, LabelNameStore}
import org.junit.{After, Assert, Before, Test}

class LabelNameStoreTest {


  @Before
  def init(): Unit ={
    val pathName: String = "D:\\CypherLog2\\DynamicBlockStore\\labelNameStore"
    val file = new File(pathName)
    if (!file.exists()) {
      file.mkdirs()
    }
  }



  @Test
  def testLabelMemoryStore(): Unit ={
    val pathName: String = "D:\\CypherLog2\\DynamicBlockStore\\labelNameStore"
    val file = new File(pathName)
    val labelNameStore = new LabelNameMemoryStore(file)
    val id1 = labelNameStore.addLabelString("person")
    val id2 = labelNameStore.addLabelString("student")
    val id3 = labelNameStore.addLabelString("person")

    Assert.assertEquals(id1, 0)
    Assert.assertEquals(id2, 1)
    Assert.assertEquals(id3, 0)

    Assert.assertEquals("person", labelNameStore.getlabelStringById(0))

    labelNameStore.close()

    val labelNameStore2 = new LabelNameMemoryStore(file)
    Assert.assertEquals(1, labelNameStore2.getIdByString("student"))

    println(labelNameStore2.getlabelStringById(0L))

  }

  @Test
  def testLableStore(): Unit ={
    val pathName: String = "D:\\CypherLog2\\DynamicBlockStore\\labelNameStore"
    val file = new File(pathName)
    val labNameDtore = new LabelNameStore(file)
    val id = labNameDtore.writeLableName("sdkllllllllllwopeogpwegjk")
    println(id)
    val st = labNameDtore.readLableName(id)
    println(st)
    labNameDtore.close()
  }

  def delFiles(file: File): Unit ={
    if(file.isDirectory){
      val childrenFiles = file.listFiles()
      if(childrenFiles.nonEmpty){
        childrenFiles.map(delFiles)
      }
    }
    file.delete()
  }

  @After
  def close(): Unit ={
    val pathName: String = "D:\\CypherLog2\\DynamicBlockStore\\labelNameStore"
    val file = new File(pathName)

    delFiles(file)


  }


}
