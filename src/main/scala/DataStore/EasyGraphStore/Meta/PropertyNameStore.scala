package DataStore.EasyGraphStore.Meta

import java.io.{File, RandomAccessFile}

import Util.FptrFactory

class PropertyNameStore(file: File) extends NameStore {
  override val fptr: RandomAccessFile = FptrFactory.getFptr(new File(file,"propertyNameString"), "rw")
  init()

  def getPropertyNameById(Id: Long): String = {
    this.getlabelStringById(Id)
  }

  def getPropertyIdByName(name: String): Long = {
    this.getIdByString(name)
  }

  def addPropertyName(name: String): Long = {
    this.addLabelString(name)
  }

}
