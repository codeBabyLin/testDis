package DataStore.EasyGraphStore.Node

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream, File, RandomAccessFile}

import DataStore.EasyGraphStore.BasicFileStore.StaticStore
import Util.FptrFactory
//[nodeId,labelsize,labelId*8]
case class LabelNode(id: Long, label:Array[Long])
class NodeLabelStore(file: File) extends StaticStore{
  override val fptr: RandomAccessFile = FptrFactory.getFptr(file, "rw")
  override val block_length: Int = 80

  def set(nodeId:Long, labelId: Long): Unit ={
    val node = readRecord(nodeId)
    val labels = node.label ++ Array(labelId)

    writeRecord(LabelNode(nodeId,labels.distinct))

  }


  def set(nodeId: Long, labelIds: Array[Long]): Unit ={
    val node = readRecord(nodeId)
    val labels = node.label ++ labelIds

    writeRecord(LabelNode(nodeId,labels.distinct))
  }

   def removeLabel(nodeId:Long, labelId: Long): Unit ={
    val node = readRecord(nodeId)
    val labels = node.label.filter(!_.equals(labelId))

    writeRecord(LabelNode(nodeId, labels.distinct))
  }

  private def writeRecord(node: LabelNode){
    write(node.id, serialize(node))
  }

  private def readRecord(node: Long): LabelNode ={

    deSerialize(read(node))

  }
  private def serialize(node: LabelNode): Array[Byte] = {

    val block = new ByteArrayOutputStream()
    val dos = new DataOutputStream(block)
    dos.writeLong(node.id)
    dos.writeLong(node.label.size.toLong)
    node.label.foreach(dos.writeLong)
    //val byteDiff = new Array[Byte](block_length - dos.size())
    dos.write(new Array[Byte](block_length - dos.size()))
    val data = block.toByteArray
    //println(data.length)
    dos.close()
    data

  }

  private def deSerialize(data: Array[Byte]): LabelNode = {
    val dis = new DataInputStream(new ByteArrayInputStream(data))
    val nodeId = dis.readLong()
    val labelsize = dis.readLong()
    val labelIdArray = (1L to labelsize).map(x=> dis.readLong())
    val info = LabelNode(nodeId, labelIdArray.toArray)
    dis.close()
    info
  }

  override def nextId(): Long = {
    val localId = super.nextId()
    write(localId,serialize(LabelNode(localId,Array())))
    localId
  }



  def get(nodeId: Long): Long = {
    readRecord(nodeId).label.head
  }
  def getAll(nodeId: Long):Array[Long] = {
    readRecord(nodeId).label
  }

  def exist(nodeId: Long, labelId:Long): Boolean = {
    readRecord(nodeId).label.contains(labelId)
  }

}
