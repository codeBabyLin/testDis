package DataStore.EasyGraphStore.Meta
import java.io.{File, RandomAccessFile}

import Util.FptrFactory

class NodeLabelNameStore(file: File) extends NameStore {

  override val fptr: RandomAccessFile = FptrFactory.getFptr(new File(file, "nodeLabelString"), "rw")
  init()
  def getIdByName(name: String): Long = {
    this.getIdByString(name)
  }

  def getNameById(Id: Long): String = {
    this.getlabelStringById(Id)
  }

  def addLabelName(name: String): Long = {
    this.addLabelString(name)
  }


}
