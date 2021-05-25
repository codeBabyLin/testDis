package VersionManage

import java.io.File

import org.junit.Test
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb.{Result, ResultTransformer}

import scala.collection.JavaConverters._

class CypherRunTest {

  @Test
  def testop(): Unit ={
    val dataVersion = "testr"
    val file = new File("F:\\CNIC\\data\\" + dataVersion)
    val managementService = new DatabaseManagementServiceBuilder(file).loadPropertiesFromFile("F:\\IdCode\\testDis\\"+"neo4j.conf").build()
    val db = managementService.database("neo4j")

    val c1 = "create(n:test{name:'haha', age:5})"
    db.executeTransactionally(c1)
    val c2 = "match(n) return n"

    val tr: ResultTransformer[String] = new ResultTransformer[String] {
      override def apply(t: Result): String = t.resultAsString()
    }
    val mp: java.util.Map[String, Object] = Map.empty[String, Object].asJava
    val res = db.executeTransactionally(c2, mp, tr)
    println(res)

    managementService.shutdown()
  }
}
