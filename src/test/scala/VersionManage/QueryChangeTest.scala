package VersionManage

import Util.CypherTransForm
import org.junit.Test

class QueryChangeTest {

  @Test
  def testQueryChange(): Unit ={
    val filePath = "D:\\CypherLog\\log.txt"
    val dataPathName: String = "F:\\CNIC\\data\\forAll\\"
    val qc = new QueryChangeImplNeo4jStoreChangeForAll
    qc.storeChange(filePath, dataPathName)

  }

  @Test
  def testQueryc(): Unit ={
    val dataPathName: String = "F:\\CNIC\\data\\forAll\\"
    val qc = new QueryChangeImplNeo4jStoreChangeForAll
    val cypher = "match(n) return n"

    val version1 = 2
    val res2 = qc.queryWithVersion(dataPathName,s"v${version1}", cypher)
    println(res2)

    val version2 = 3
    val res3 = qc.queryWithVersion(dataPathName,s"v${version2}", cypher)
    println(res3)
  }


  @Test
  def testCypherTransForm(): Unit ={
    val s = CypherTransForm.update2create("match(n:employee{name:'David163497',age:30, saray:11236}) set n.name = 'Ellis11338', n.age = 53, n.saray = 8909")
    println(s)
  }

  @Test
  def testQueryChangeForOne(): Unit ={
    val filePath = "D:\\CypherLog\\log.txt"
    val dataPathName: String = "F:\\CNIC\\data\\forOne\\"
    val qc = new QueryChangeImplNeo4jStoreChangeForOne
    qc.storeChange(filePath, dataPathName)
  }
}
