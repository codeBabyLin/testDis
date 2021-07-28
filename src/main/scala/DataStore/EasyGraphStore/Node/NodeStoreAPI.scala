package DataStore.EasyGraphStore.Node

import java.io.File

class NodeStoreAPI(file: File) extends NodeStoreSPI {
  override def allLabels(): Array[String] = ???

  override def allLabelIds(): Array[Int] = ???

  override def getLabelName(labelId: Int): Option[String] = ???

  override def getLabelId(labelName: String): Option[Int] = ???

  override def addLabel(labelName: String): Int = ???

  override def allPropertyKeys(): Array[String] = ???

  override def allPropertyKeyIds(): Array[Int] = ???

  override def getPropertyKeyName(keyId: Int): Option[String] = ???

  override def getPropertyKeyId(keyName: String): Option[Int] = ???

  override def addPropertyKey(keyName: String): Int = ???

  override def getNodeById(nodeId: Long): Option[StoredNodeWithProperty] = ???

  override def getNodeById(nodeId: Long, label: Int): Option[StoredNodeWithProperty] = ???

  override def getNodeById(nodeId: Long, label: Option[Int]): Option[StoredNodeWithProperty] = ???

  override def getNodesByLabel(labelId: Int): Iterator[StoredNodeWithProperty] = ???

  override def getNodeIdsByLabel(labelId: Int): Iterator[Long] = ???

  override def getNodeLabelsById(nodeId: Long): Array[Int] = ???

  override def hasLabel(nodeId: Long, label: Int): Boolean = ???

  override def newNodeId(): Long = ???

  override def nodeAddLabel(nodeId: Long, labelId: Int): Unit = ???

  override def nodeRemoveLabel(nodeId: Long, labelId: Int): Unit = ???

  override def nodeSetProperty(nodeId: Long, propertyKeyId: Int, propertyValue: Any): Unit = ???

  override def nodeRemoveProperty(nodeId: Long, propertyKeyId: Int): Any = ???

  override def deleteNode(nodeId: Long): Unit = ???

  override def allNodes(): Iterator[StoredNodeWithProperty] = ???

  override def nodesCount: Long = ???

  override def deleteNodesByLabel(labelId: Int): Unit = ???

  override def addNode(node: StoredNodeWithProperty): Unit = ???

  override def getLabelIds(labelNames: Set[String]): Set[Int] = ???

  override def close(): Unit = ???
}
