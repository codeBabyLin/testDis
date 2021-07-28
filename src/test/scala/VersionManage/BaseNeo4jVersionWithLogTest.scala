package VersionManage

import java.io.File

import Util.FileCopy
import org.junit.{After, Before, Test}

class BaseNeo4jVersionWithLogTest {
  val filePath = "F:\\CNIC\\data\\BaseNeo4jVersionLog"


  @Before
  def init(): Unit ={

    val file = new File(filePath)
    file.mkdirs()
    FileCopy.delFiles(file)
    file.mkdirs()
  }




  @Test
  def testseek(): Unit ={
    //val filePath = "F:\\CNIC\\data\\BaseNeo4jVersion"
    val file = new File(filePath)
    val b4l: BaseNeo4jVersionWithLog = new BaseNeo4jVersionWithLog(file)
    val c1 = "create(n:test{name:'JoeJoe1', age: 1})"
    //v0  true
    val c2 = "create(n:test{name:'JoeJoe2', age: 2})"
    //v1 false
    val c3 = "create(n:test{name:'JoeJoe3', age: 3})"
    //v2 true
    val c4 = "create(n:test{name:'JoeJoe4', age: 4})"
    val c5 = "create(n:test{name:'JoeJoe5', age: 5})"

    b4l.executeCypher(c1)
    val v1 = b4l.markV(true)
    b4l.executeCypher(c2)
    val v2 = b4l.markV()
    b4l.executeCypher(c3)
    val v3 = b4l.markV(true)
    b4l.executeCypher(c4)
    val v4 = b4l.markV()
    b4l.executeCypher(c5)
    b4l.close()
    val vc = b4l.getCurrentV()
    println(s"v1:${v1}, v2:${v2}, v3:${v3}, v4:${v4}, vc:${vc}")

    val b4l2: BaseNeo4jVersionWithLog = new BaseNeo4jVersionWithLog(file)
    b4l2.seekV(v3)
    val q = "match(n) return n"

    println(b4l2.queryResult(q))

    b4l2.seekV(v4)

    println(b4l2.queryResult(q))

    b4l2.goBacktoLatestV()

    println(b4l2.queryResult(q))
    println(b4l2.getLatestV())
    b4l2.close()
  }


/*  @After
  def last(): Unit ={
    //val filePath = "F:\\CNIC\\data\\BaseNeo4jVersion"
    val file = new File(filePath)
    file.mkdirs()
    FileCopy.delFiles(file)
    file.mkdirs()
  }*/






}
