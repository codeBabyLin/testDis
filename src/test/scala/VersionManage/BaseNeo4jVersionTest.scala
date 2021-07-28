package VersionManage
import java.io.{File, RandomAccessFile}
import org.junit.Test

class BaseNeo4jVersionTest {

  @Test
  def testVersion(): Unit ={
    val filePath = "F:\\CNIC\\data\\BaseNeo4jVersion"
    val file = new File(filePath)
    val b4 = new BaseNeo4jVersion(file)
    b4.executeCypher("create(n:test{name:'JoeJoe',age:6})")
    val sk = b4.queryResult("match(n) return n")
    println(sk)
    b4.markV()
    b4.executeCypher("create(n:test{name:'Baby', age:10})")

    val res = b4.queryResult("match(n) return n")

    println(res)

    b4.seekV(0)

    println(b4.queryResult("match(n) return n"))
    b4.seekV(1)
    println(b4.queryResult("match(n) return n"))


  }

}
