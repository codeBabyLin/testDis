package VersionManage

import java.io.File

import Util.FileCopy
import org.junit.{After, Before, Test}
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb.{Result, ResultTransformer, Transaction}

import scala.collection.JavaConverters._

class CypherRunTest {

  @Before
  def init(): Unit ={
    val dataVersion = "testr"
    val file = new File("F:\\CNIC\\data\\" + dataVersion)
    file.mkdirs()
    FileCopy.delFiles(file)
    file.mkdirs()
  }

  @Test
  def testop(): Unit ={
    val dataVersion = "testr"
    val file = new File("F:\\CNIC\\data\\" + dataVersion)
    val managementService = new DatabaseManagementServiceBuilder(file).loadPropertiesFromFile("F:\\IdCode\\testDis\\"+"neo4j.conf").build()
    val db = managementService.database("neo4j")

    val c1 = "create(n:test{name:'haha', age:5})"
    db.executeTransactionally(c1)

    val tx: Transaction = db.beginTx()
    tx.execute("create(n:test{name:'joejoe', age: 20})")

    val c2 = "match(n) return n"

    val tr: ResultTransformer[String] = new ResultTransformer[String] {
      override def apply(t: Result): String = t.resultAsString()
    }
    val mp: java.util.Map[String, Object] = Map.empty[String, Object].asJava
    //val res = db.executeTransactionally(c2, mp, tr)
    val res  = tx.execute(c2).resultAsString()
    println(res)

    tx.rollback()

    val tx1 = db.beginTx()
    val res1  = tx1.execute(c2).resultAsString()
    println(res1)

    managementService.shutdown()
  }

  @After
  def last(): Unit ={
    val dataVersion = "testr"
    val file = new File("F:\\CNIC\\data\\" + dataVersion)
    file.mkdirs()
    FileCopy.delFiles(file)
    file.mkdirs()
  }
}
