package GenerateCypherLog

import org.junit.Test



class TestLogGen{
  @Test
  def testlogGen(): Unit ={
    val gen = new CypherLogGen
    val filePath = "D:\\CypherLog\\log2.txt"
    gen.writeLog2(filePath,100)
    gen.printLog(filePath)

  }



}
