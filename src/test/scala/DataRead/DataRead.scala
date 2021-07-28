package DataRead
import com.github.jsonldjava.core.JsonLdOptions
import com.github.jsonldjava.core.JsonLdProcessor
import com.github.jsonldjava.utils.JsonUtils
import java.io.{File, FileInputStream, InputStream}
import java.util

import org.junit.Test

import scala.util.parsing.json.JSON._
import scala.collection.mutable
import scala.io.Source
import scala.util.parsing.json.{JSON, JSONObject}
import java.io.BufferedReader
import java.io.FileReader

import Util.FptrFactory
import VersionManage.BaseNeo4jVersionWithLog
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.{Node, NodeList}


case class Paper(title: String, year: Int, authors: String, jconf: String, id: Int, label: Int, oranization: String)

class DataRead {




  @Test
  def testLoadData(): Unit ={
    val path = "F:\\CNIC\\SciKG_min_1.0\\SciKG_min_1.0\\SciKG_min_1.0.txt"

    val file = new File(path)
    val fptr = FptrFactory.getFptr(path, "r")
    val c = fptr.readUTF()
    val d = fptr.readUTF()


    println(d)



  }



  def getPaper(nodes: NodeList): Paper = {
    val node = (1 to nodes.getLength).map(i => nodes.item(i-1)).filter(_.getNodeType == Node.ELEMENT_NODE)
    Paper(node(0).getTextContent, node(1).getTextContent.toInt, node(2).getTextContent,node(3).getTextContent,
      node(4).getTextContent.toInt, node(5).getNodeType.toInt, node(6).getTextContent)

  }

/*  def getPapers(path: String): Array[Paper] ={
    getPapers(new File(path))
  }*/

  def getPapers(file: File): Array[Paper] ={
    val factory = DocumentBuilderFactory.newInstance()

    val builder = factory.newDocumentBuilder()
    val d = builder.parse(file)


    val s = d.getElementsByTagName("publication")


    val publications = (1 to s.getLength).map(i => s.item(i-1)).filter(_.getNodeType == Node.ELEMENT_NODE)



    val papers = publications.map(p => getPaper(p.getChildNodes))
    papers.toArray

  }

  @Test
  def testOP(): Unit ={
    val path = "F:\\CNIC\\raw-data\\Ajay Gupta.xml"
    val dirPath = "F:\\CNIC\\raw-data\\"
    val dir = new File(dirPath)

    val papers = dir.listFiles().flatMap(getPapers)


    println(papers.size)

  }

 // case class Paper(title: String, year: Int, authors: String, jconf: String, id: Int, label: Int, oranization: String)
  def class2cypher(node: Paper): String ={
    //s'''create(n:paper{title:'${node.title}',year:${node.year},authors: '${node.authors}'})'''

    val title = node.title.replaceAll("\"","").replace("\\","")
    s"""create(n:paper{title: "$title",year:${node.year},authors: "${node.authors}"})"""
  }

  @Test
  def writeData(): Unit ={
    val filePath = "F:\\CNIC\\data\\BaseNeo4jVersionLog"
    //val path = "F:\\CNIC\\raw-data\\Ajay Gupta.xml"
    val dirPath = "F:\\CNIC\\raw-data\\"
    val dir = new File(dirPath)

    val papers = dir.listFiles().flatMap(getPapers)

    val file = new File(filePath)
    val b4l: BaseNeo4jVersionWithLog = new BaseNeo4jVersionWithLog(file)
    var cnt = 0
    papers.map(class2cypher).foreach(s => {
      b4l.executeCypher(s)
      cnt = cnt + 1
      if (cnt>=100){
        b4l.markV()
      }
      if(cnt >=200){
        b4l.markV(true)
        cnt = 0
      }
    })

    b4l.listAllVersion().foreach(tup => println(s"${tup._1} -> ${tup._2}"))


  }

  @Test
  def testr(): Unit ={
    val title = "Finite Identification of Functions by Teams with Success Ratio 1 over2 and Above"
    println(title)
    println(title.replace("\\", ""))
  }

}
