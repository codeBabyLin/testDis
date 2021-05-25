package Visualization


import Util.CypherTransForm
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}

trait G6Node{
  override def toString: String = ""
  def map2String(m: Map[String, Any]): String ={
    m.map(rel => {
      val s = rel._1
      val a = rel._2
      a match {
        case x:String => s"$s->$a"
        case _ => s"$s->$a"
      }
    }).mkString(",")
  }
}
trait G6Edge{
  override def toString: String = ""
}

case class NodeL1(id:String) extends G6Node {
  override def toString: String = {
    s"""{id: '$id'}"""
  }
}
case class NodeL2(id:String, label:String) extends G6Node{
  def getPsString: String ={
    s"id->$id,label->$label"
  }
  override def toString: String = {
    s"""{id: '$id', label: '$getPsString'}"""
  }
}

case class NodeL3(id:String, label:String, ps: Map[String, Any]) extends G6Node{

  def getPsString(): String = {
    s"id->$id,label->$label{${map2String(ps)}}"
  }
  override def toString: String = {
    s"""{id: '$id', label: '$getPsString'}"""
  }
}

case class NodeL4(id:String, label:String, x: Int, y: Int) extends G6Node {
  override def toString: String = {
    s"""{id: '$id', label: '$label', x: $x, y: $y}"""
  }
}

case class NodeL5(id:String, label:String, ps: Map[String, Any], x: Int, y: Int) extends G6Node {
  def getPsString(): String = {
    s"id:$id,label:$label{${CypherTransForm.map2String(ps)}"
  }
  override def toString: String = {
    s"""{id: '$id', label: '$getPsString', x: $x, y: $y}"""
  }
}

case class EdgeL1(source: String, target: String) extends G6Edge {
  override def toString: String = {
    s"""{source: '$source', target: '$target'}"""
  }
}

case class EdgeL2(source: String, target: String, label: String) extends G6Edge {
  override def toString: String = {
    s"""{source: '$source', target: '$target', label:'$label'}"""
  }
}


case class Gdata(nodes: List[G6Node], edges: List[G6Edge]){
  override def toString: String = {
    s"""{nodes: [${nodes.map(_.toString).mkString(",")}], edges: [${edges.map(_.toString).mkString(",")}]}"""
  }
}


class ShowChanges {

  val options:ChromeOptions = new ChromeOptions
  val webDriver: ChromeDriver = new ChromeDriver(options)

  System.setProperty("webdriver.chrome.driver", "D:\\chromedriver\\chromedriver")
  val url:String = "F:\\IdCode\\testDis\\test.html"

  def produceJs(data: Gdata): String ={
    val js4 =
      s"""
          const s_ele = ${data.toString};
          const graph = new G6.Graph(
          {container: 'mountNode',
          width: 800,
          height: 500,
          defaultEdge:{
            style:{endArrow:true}
          }});
          graph.refresh();
          graph.data(s_ele);
          graph.render();
        """
    js4
  }

  def getHtml(url: String): Unit ={
    webDriver.get(url)
  }

  def show(data: Gdata): Unit ={
    webDriver.get(this.url)
    webDriver.executeScript(produceJs(data))
  }


}
