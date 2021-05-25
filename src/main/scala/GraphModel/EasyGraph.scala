package GraphModel

import scala.collection.mutable.ArrayBuffer

case class EasyNode(id: Int, label: String, p: Map[String, Any]){
  //var hLabel: ArrayBuffer[(Int,String)] = new ArrayBuffer[(Int,String)]()
  var curVer: Int = 0
  var children: EasyNode = _
  var parent: EasyNode = _

  def getFirstValue: EasyNode ={
    if(this.parent ==null)
      this
    else this.parent.getFirstValue
  }

  def getAllPastValue: Array[EasyNode] = {
    if(this.parent == null) Array()
    else Array(this.parent)++this.parent.getAllPastValue
  }
  def getAllFutureValue: Array[EasyNode] = {
    if(this.children == null) Array()
    else Array(this.children)++this.children.getAllFutureValue
  }

  def getLastValue: EasyNode = {
    if(this.children==null) this
    else this.children.getLastValue
  }

  def value(specifiVer: Int): EasyNode ={
    if (curVer > specifiVer) this.parent.value(specifiVer)
    else if (curVer == specifiVer) this
    else this.children.parent.value(specifiVer)

  }


}

case class EasyRelation(id: Int, p: Map[String, Any] = null, label: String, sourceNode: Int, targetNode: Int){
  var curVer: Int = 0
  var children: EasyRelation = _
  var parent: EasyRelation = _

  def value(specifiVer: Int): EasyRelation ={
    if (curVer > specifiVer) this.parent.value(specifiVer)
    else if (curVer == specifiVer) this
    else this.children.parent.value(specifiVer)
  }
}

class EasyGraph {
  val nodes: ArrayBuffer[EasyNode] = new ArrayBuffer[EasyNode]()
  val rels: ArrayBuffer[EasyRelation] = new ArrayBuffer[EasyRelation]()


  def getNodeById(): Unit ={

  }

  def genNodeId(): Int ={
    nodes.size
  }

  def genRelationId(): Int ={
    rels.size
  }

  def getRandomNode(): EasyNode ={
    val index = (new util.Random).nextInt(nodes.size)
    var node = nodes(index)
    node
  }

  def getNodeById(id: Int): EasyNode ={
    nodes.map(node => node.id -> node).toMap.get(id).get
  }

  def getRandomCanDelNode: EasyNode ={
    var node = getRandomNode
    while(!checkNodesCanDel(node)){
      node = getRandomNode
    }
    node
  }

  def getRandomRelation: EasyRelation ={
    val index = (new util.Random).nextInt(rels.size)
    rels(index)
  }

  def checkNodesCanRel(n1: EasyNode, n2: EasyNode): Boolean ={
    if (n1.id == n2.id) false
    else true
  }

  def checkNodesCanDel(node: EasyNode): Boolean = {
    if (rels.flatMap(rel => List(rel.sourceNode, rel.targetNode)).contains(node.id)) {
      false
    } else {
      true
    }
  }

  def addNode(node: EasyNode): Unit ={
    nodes += node
  }
  def deleteNode(id: Int): Unit ={
  }

  def deleteNode(node: EasyNode): Unit ={
    nodes -= node
  }
  def updateNode(nodeOld: EasyNode, nodeNew: EasyNode): Unit ={
    nodeNew.curVer = nodeOld.curVer + 1
    nodeOld.children = nodeNew
  }
  def addRelation(relation: EasyRelation): Unit ={
    rels += relation
  }
  def deleteRelation(relation: EasyRelation): Unit ={
    rels -= relation
  }
  def updateRelation(): Unit ={

  }

}