package codeBabyTest

import org.junit.Test

import scala.io.Source

class TestRpc {


  @Test
  def testTxt(): Unit ={

    val filePath = "F:\\test\\eventdata.txt"
    val source = Source.fromFile(filePath, "UTF-8")


    val lines = source.getLines()
    var cnt = 100
    while (cnt >=0){
      if(lines.hasNext){
        println(lines.next())
      }
      cnt = cnt -1
    }
    source.close()

  }

}
