package Visuallization

import Visualization._
import com.google.gson.Gson
import org.junit.Test
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}


class ChromeDriverTest {



  @Test
  def testDriver(): Unit ={
    val url = "F:\\IdCode\\testDis\\test.html"
    val sc = new ShowChanges
    sc.getHtml(url)
  }

  @Test
  def testBr(): Unit ={
    val options:ChromeOptions = new ChromeOptions
    val webDriver: ChromeDriver = new ChromeDriver(options)
    System.setProperty("webdriver.chrome.driver", "D:\\chromedriver\\chromedriver")
    val url = "F:\\IdCode\\testDis\\test.html"
    webDriver.get(url)


    val js2 =
      s"""
          //var s_ele = arguments[0];

          const data = {
        // 点集
        nodes: [{id: 'node1',name: 'tt',x: 100,y: 200},
                {id: 'node2',name: 'tt',x: 300,y: 200}],
        // 边集
        edges: [
          // 表示一条从 node1 节点连接到 node2 节点的边
          {
            source: 'node1',
            target: 'node2'
          }
        ]
      };
          const graph = new G6.Graph({
        container: 'mountNode', // 指定图画布的容器 id，与第 9 行的容器对应
        // 画布宽高
        width: 800,
        height: 500
      });
      // 读取数据
      graph.data(data);
      // 渲染图
      graph.render();
        """





    case class Node(id:String, label:String, x: Int, y: Int){
      override def toString: String = {
        s"""{id: '$id', label: '$label'}"""
      }
    }
    case class Edge(source: String, target: String){
      override def toString: String = {
        s"""{source: '$source', target: '$target', label:'friend'}"""
      }
    }
    case class Gdata(nodes: List[Node], edges: List[Edge]){
      override def toString: String = {
        s"""{nodes: [${nodes.map(_.toString).mkString(",")}], edges: [${edges.map(_.toString).mkString(",")}]}"""
      }
    }

    val nodes = List(Node("1","ty1",100, 200), Node("2","ty2",300, 200))
    val nodes2 = List(Node("1","ty1",100, 200), Node("2","ty2",300, 200), Node("3","ty2",400, 200))
    val edegs = List(Edge("1","2"))
    val data: Gdata = Gdata(nodes, edegs)
    val data2: Gdata = Gdata(nodes2, edegs)

    val js3 =
      s"""
          const s_ele = ${data.toString};
          const graph = new G6.Graph({container: 'mountNode',
          width: 800,
          height: 500});
          graph.data(s_ele);
          graph.render();
        """

    val js4 =
      s"""
          const s_ele = ${data2.toString};
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


    webDriver.executeScript(js4)
    //webDriver.get(url)
    //webDriver.executeScript(js4)


  }


  @Test
  def testScala2Js(): Unit ={
    case class Node(id:String, name:String, x: Int, y: Int){
      override def toString: String = {
        s"""{"id": "$id", "name": "$name", "x": $x, "y": $y}"""
      }
    }
    case class Edge(source: String, target: String){
      override def toString: String = {
        s"""{"source": "$source", "target": "$target"}"""
      }
    }
    case class Gdata(nodes: List[Node], edges: List[Edge]){
      override def toString: String = {
        s"""{"nodes": [${nodes.map(_.toString).mkString(",")}], "edges": [${edges.map(_.toString).mkString(",")}]}"""
      }
    }


    val nodes = List(Node("1","ty1",100, 200), Node("2","ty2",300, 200))
    val edegs = List(Edge("1","2"))
    val data = Gdata(nodes, edegs)

    val gson = new Gson()
    println(gson.toJson(data))

  }
  @Test
  def testShowData(): Unit ={

    val nodes = List(NodeL2("1","ty1"), NodeL2("2","ty2"))
    val nodes2 = List(NodeL4("1","ty1",100, 200), NodeL4("2","ty2",300, 200), NodeL4("3","ty2",400, 200))
    val edegs = List(EdgeL2("1","2", "friend"))
    val data: Gdata = Gdata(nodes, edegs)
    val data2: Gdata = Gdata(nodes2, edegs)
    val sc = new ShowChanges

    sc.show(data)
    sc.show(data2)
  }

}
