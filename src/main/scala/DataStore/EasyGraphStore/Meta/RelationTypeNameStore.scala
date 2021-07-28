package DataStore.EasyGraphStore.Meta
import java.io.{File, RandomAccessFile}

import Util.FptrFactory

class RelationTypeNameStore(file: File) extends NameStore {
  override val fptr: RandomAccessFile = FptrFactory.getFptr(new File(file, "relationTypeString"), "rw")
  init()

  def getRelationTypeById(Id: Long): String = {
    this.getlabelStringById(Id)
  }

  def getRelationIdByType(name: String): Long = {
    this.getIdByString(name)
  }

  def addRelationType(name: String): Long = {
    this.addLabelString(name)
  }
}
