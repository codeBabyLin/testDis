package GenerateCypherLog

import java.io.{File, FileWriter, PrintWriter}

import GraphModel.{EasyGraph, EasyNode, EasyRelation}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source






class GenNodeAndRelation{
  /*
    val nodeName: Map[Int, String] = Map(0 -> "Alice Panda", 1 -> "Bob Panda", 2 -> "Chris Panda", 3 -> "David Panda", 4 -> "Ellis Panda", 5 -> "Frank Panda",
      6 -> "Gamma Panda", 7 -> "Harry Panda", 8 -> "Irving Panda", 9 -> "Jackson Panda")*/

  /*
  node label
  employee{name:'',age:'',saray:''}
 department{name:'', location:'', }
 Job{name:'', content:''}


  rel label
  friend{}  employee<->employee
  belong{}  employee -> department
  work{}   employee -> Job
  contain{}  department <- Job
*/

  def randomInt(n: Int): Int = {
    (new util.Random).nextInt(n)
  }
  def randomString(nameBase: Array[String], tint: Int): String ={
    s"${nameBase(randomInt(nameBase.size))}${tint}"
  }



  def randomPName(): String = {
    val nameBase: Array[String] = Array("Alice", "Bob","Chris","David","Ellis", "Frank", "Gamma", "Harry",  "Irving","Jackson")
    val tint = randomInt(1000000)
    randomString(nameBase, tint)
  }
  def randomDName(): String ={
    val nameBase: Array[String] = Array("Panda", "Monkey","Hoos","Elephont","Sheep", "Rabitts", "Tiger", "Lion",  "Snake","Bee")
    val tint = randomInt(1000000)
    randomString(nameBase, tint)
  }
  def randomJName(): String ={
    val nameBase: Array[String] = Array("Apple", "Peach","WaterMelon","Mango","Banana", "Grape", "Pear", "PineApple",  "Walnut","Cherry")
    val tint = randomInt(1000000)
    randomString(nameBase, tint)
  }

  def randomAge(): Int = {
    20 + randomInt(40)
  }

  def randomSaray(): Int = {
    5000 + randomInt(10000)
  }

  def randomLocation(): String = {
    val nameBase: Array[String] = Array("Beijing", "Shanghai","Guangdong","Jiangxi","Xian", "Guangxi", "Ganzhou", "Sichuan",  "Jilin","Henan")
    val tint = randomInt(1000000)
    randomString(nameBase, tint)
  }

  def randomContent(): String = {
    val nameBase: Array[String] = Array("code", "test","design","check","QA", "Ad")
    val tint = randomInt(1000000)
    randomString(nameBase, tint)

  }
  def getNodeLable(): String = {
    val i = randomInt(1000)
    //println(i)
    if (i <=800) "employee"
    else if ( i <=900) "department"
    else "Job"
  }
  def createNode(graph: EasyGraph): String = {
    val id = graph.genNodeId()
    getNodeLable() match {
      case "employee" =>
        //val id = g.genNodeId()
        val name = randomPName()
        val age = randomAge()
        val saray = randomSaray()
        val node = EasyNode(id, "employee", Map("name" -> name, "age" -> age, "saray" -> saray))
        graph.addNode(node)
        s"create(n:employee{name:'$name',age:$age, saray:$saray})"

      case "department" =>
        val name = randomDName()
        val location = randomLocation
        val node =EasyNode(id, "department", Map("name" -> name, "location" -> location))
        graph.addNode(node)
        s"create(n:department{name:'$name',location:'$location'})"
      //s"create(n:Job{name:'${randomJName()}',content:'$randomContent'})"
      case "Job" =>
        val name = randomJName()
        val content = randomContent()
        val node =EasyNode(id, "Job", Map("name" -> name, "content" -> content))
        graph.addNode(node)
        s"create(n:Job{name:'$name',content:'$content'})"
    }
  }
  /*  rel label
      friend{}  employee<->employee
      belong{}  employee -> department
      work{}   employee -> Job
      contain{}  department <- Job*/
  def createRelation(graph: EasyGraph): String ={
    val id = graph.genRelationId()
    val sourceNode = graph.getRandomNode()
    val targetNode = graph.getRandomNode()
    (sourceNode.label, targetNode.label) match {
      case ("employee","employee") =>
        sourceNode.id == targetNode.id match {
          case  true => s"#################################################"
          case false =>
            val rel = EasyRelation(id,Map(), "friend", sourceNode.id, targetNode.id)
            graph.addRelation(rel)
            val name1 = sourceNode.p("name")
            val age1 = sourceNode.p("age")
            val saray1 = sourceNode.p("saray")
            val name2 = targetNode.p("name")
            val age2 = targetNode.p("age")
            val saray2 = targetNode.p("saray")
            s"match(n:employee{name:'$name1',age:$age1, saray:$saray1}),(m:employee{name:'$name2',age:$age2, saray:$saray2}) create(n)-[:friend]->(m)"
        }

      case ("employee","department") => {
        val rel = EasyRelation(id, Map(), "belong", sourceNode.id, targetNode.id)
        graph.addRelation(rel)
        val name1 = sourceNode.p("name")
        val age1 = sourceNode.p("age")
        val saray1 = sourceNode.p("saray")

        val name2 = targetNode.p("name")
        val location = targetNode.p("location")
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1}),(m:department{name:'$name2',location:'$location'}) create(n)-[:belong]->(m)"

      }
      case ("employee","Job") => {
        val rel = EasyRelation(id,Map(), "work", sourceNode.id, targetNode.id)
        graph.addRelation(rel)
        val name1 = sourceNode.p("name")
        val age1 = sourceNode.p("age")
        val saray1 = sourceNode.p("saray")

        val name2 = targetNode.p("name")
        val content = targetNode.p("content")
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1}),(m:Job{name:'$name2',content:'$content'}) create(n)-[:work]->(m)"


      }
      case ("department","Job") => {
        val rel = EasyRelation(id,Map(), "contain", sourceNode.id, targetNode.id)
        graph.addRelation(rel)
        val name1 = sourceNode.p("name")
        val location = sourceNode.p("location")

        val name2 = targetNode.p("name")
        val content = targetNode.p("content")
        s"match(n:department{name:'$name1',location:$location}),(m:Job{name:'$name2',content:'$content'}) create(n)-[:contain]->(m)"
      }

      case ("department", "employee") =>
      {
        //Relation(id,Map(), "belong", targetNode.id, sourceNode.id)

        val rel = EasyRelation(id,Map(), "belong", targetNode.id, sourceNode.id)
        graph.addRelation(rel)
        val name1 = targetNode.p("name")
        val age1 = targetNode.p("age")
        val saray1 = targetNode.p("saray")

        val name2 = sourceNode.p("name")
        val location = sourceNode.p("location")
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1}),(m:department{name:'$name2',location:'$location'}) create(n)-[:belong]->(m)"
      }
      case ("Job", "employee") => {
        val rel = EasyRelation(id,Map(), "work", targetNode.id, sourceNode.id)

        //val rel = Relation(id,Map(), "work", sourceNode.id, targetNode.id)
        graph.addRelation(rel)
        val name1 = targetNode.p("name")
        val age1 = targetNode.p("age")
        val saray1 = targetNode.p("saray")

        val name2 = sourceNode.p("name")
        val content = sourceNode.p("content")
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1}),(m:Job{name:'$name2',content:'$content'}) create(n)-[:work]->(m)"

      }
      case ("Job", "department") => {
        val rel = EasyRelation(id,Map(), "contain", targetNode.id, sourceNode.id)
        // val rel = Relation(id,Map(), "contain", sourceNode.id, targetNode.id)
        graph.addRelation(rel)
        val name1 = targetNode.p("name")
        val location = targetNode.p("location")

        val name2 = sourceNode.p("name")
        val content = sourceNode.p("content")
        s"match(n:department{name:'$name1',location:$location}),(m:Job{name:'$name2',content:'$content'}) create(n)-[:contain]->(m)"
      }
      case (_, _) => s"#################################################"//Relation(id,Map(), "GOOOOOOOOOD", targetNode.id, sourceNode.id)
    }
  }


  def updateNode(graph: EasyGraph): String = {
    if (graph.nodes.size<=0){
      createNode(graph)
    }
    val node1: EasyNode = graph.getRandomNode()
    //graph.deleteNode(node1)
    val id = node1.id
    node1.label match {
      case "employee" =>
        //val id = g.genNodeId()
        val name1 = node1.p("name")
        val age1 = node1.p("age")
        val saray1 = node1.p("saray")
        val name = randomPName()
        val age = randomAge()
        val saray = randomSaray()
        val node = EasyNode(id, "employee", Map("name" -> name, "age" -> age, "saray" -> saray))
        graph.updateNode(node1, node)
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1}) set n.name = '$name', n.age = $age, n.saray = $saray"

      case "department" =>

        val name1 = node1.p("name")
        val location1 = node1.p("location")

        val name = randomDName()
        val location = randomLocation
        val node =EasyNode(id, "department", Map("name" -> name, "location" -> location))
        graph.updateNode(node1, node)
        s"match(n:department{name:'$name1',location:'$location1'}) set n.name = '$name', n.location = '$location'"
      //s"create(n:Job{name:'${randomJName()}',content:'$randomContent'})"
      case "Job" =>
        val name1 = node1.p("name")
        val content1 = node1.p("content")

        val name = randomJName()
        val content = randomContent()
        val node =EasyNode(id, "Job", Map("name" -> name, "content" -> content))
        graph.updateNode(node1, node)
        s"match(n:Job{name:'$name1',content:'$content1'}) set n.name = '$name',n.content = '$content'"
    }


  }

  def deleteNode(graph: EasyGraph): String ={
    if (graph.nodes.size<=0){
      createNode(graph)
    }
    val node1: EasyNode = graph.getRandomCanDelNode
    graph.deleteNode(node1)
    node1.label match {
      case "employee" =>
        //val id = g.genNodeId()
        val name1 = node1.p("name")
        val age1 = node1.p("age")
        val saray1 = node1.p("saray")

        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1})  delete n"

      case "department" =>

        val name1 = node1.p("name")
        val location1 = node1.p("location")


        s"match(n:department{name:'$name1',location:'$location1'}) delete n"
      //s"create(n:Job{name:'${randomJName()}',content:'$randomContent'})"
      case "Job" =>
        val name1 = node1.p("name")
        val content1 = node1.p("content")

        s"match(n:Job{name:'$name1',content:'$content1'}) delete n "
    }

    //while()
  }

  def deleteRelation(graph: EasyGraph): String ={
    if(graph.rels.size<=0){
      createRelation(graph)
    }
    val rel = graph.getRandomRelation
    graph.deleteRelation(rel)
    //val id = rel.id
    val sourceNode:EasyNode = graph.getNodeById(rel.sourceNode)
    val targetNode: EasyNode = graph.getNodeById(rel.targetNode)

    (sourceNode.label, targetNode.label) match {
      case ("employee", "employee") =>
        val name1 = sourceNode.p("name")
        val age1 = sourceNode.p("age")
        val saray1 = sourceNode.p("saray")
        val name2 = targetNode.p("name")
        val age2 = targetNode.p("age")
        val saray2 = targetNode.p("saray")
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1})-[r]->(m:employee{name:'$name2',age:$age2, saray:$saray2}) delete r"


      case ("employee", "department") => {
        val name1 = sourceNode.p("name")
        val age1 = sourceNode.p("age")
        val saray1 = sourceNode.p("saray")

        val name2 = targetNode.p("name")
        val location = targetNode.p("location")
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1})-[r]->(m:department{name:'$name2',location:'$location'}) delete r"

      }
      case ("employee", "Job") => {
        val name1 = sourceNode.p("name")
        val age1 = sourceNode.p("age")
        val saray1 = sourceNode.p("saray")

        val name2 = targetNode.p("name")
        val content = targetNode.p("content")
        s"match(n:employee{name:'$name1',age:$age1, saray:$saray1})-[r]->(m:Job{name:'$name2',content:'$content'}) delete r"


      }
      case ("department", "Job") => {
        val name1 = sourceNode.p("name")
        val location = sourceNode.p("location")

        val name2 = targetNode.p("name")
        val content = targetNode.p("content")
        s"match(n:department{name:'$name1',location:$location})-[r]->(m:Job{name:'$name2',content:'$content'}) delete r"
      }
    }

  }


}


class GraphStore{
  def storeGraph(filePath: String, g: EasyGraph): Unit ={

  }

  def readGraph(filePath: String): EasyGraph = {
    new EasyGraph
  }
}


class CypherLogGen {

  def printLog(filePath: String): Unit ={
    val file = Source.fromFile(filePath)
    for(line<- file.getLines()){
      println(line)
    }
    file.close()
  }

  def writeLog1(filePath: String, cnt: Int): Unit ={
    val out = new FileWriter(filePath)

    //out.write(i.toString)
    cypherGen(cnt).foreach(out.write)
    out.close()
  }

  def writeLog2(filePath: String, cnt: Int): Unit ={
    val out = new PrintWriter(new File(filePath))
    cypherGen(cnt).foreach(out.println)
    out.close()
  }

  def randomInt(n: Int): Int = {
    (new util.Random).nextInt(n)
  }


  def cypherGen(cnt: Int): Array[String] ={

    //val nodes: ArrayBuffer[Node] = new ArrayBuffer[Node]()
    ///val rels: ArrayBuffer[Relation] = new ArrayBuffer[Relation]()
    val g: EasyGraph = new EasyGraph
    val cypherArray: ArrayBuffer[String] = ArrayBuffer[String]()
    val gener = new GenNodeAndRelation




    def getType(): String = {
      val i = randomInt(1000)
      //println(i)
      if (i <=500) "create"
      else if ( i <=800) "update"
      else if (i<=900) "delete"
      else "match"
    }



    def create(): String ={
      randomInt(4) match {
        case 3 =>
          if (g.nodes.size<=2) s"#################################################"
          else gener.createRelation(g)

        case _ => gener.createNode(g)
      }
    }
    def update(): String = {
      if (g.nodes.size <=0) s"#################################################"
      else gener.updateNode(g)
    }
    def delete(): String ={
      randomInt(4) match {
        case 3 =>
          if (g.nodes.size <=0) s"#################################################"
          else gener.deleteNode(g)

        case _ =>
          if(g.rels.size<=0) s"#################################################"
          else gener.deleteRelation(g)
      }
    }

    /*   def getRelLabel(): String ={

       }*/

  /*  val nodeLabel: Array[String] = Array("employee", "department", "Job")
    val relLabel: Array[String] = Array("friend", "belong", "work", "contain")
    val cypherType: Array[String] = Array("create", "update", "delete", "match")*/

    (1 to cnt).map{ x =>
      getType() match {
        case "match" => s"#################################################"
        case "create" => create()

        case "update" => update()

        case "delete" => delete()
      }
    }.toArray

  }

}


