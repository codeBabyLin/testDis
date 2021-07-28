package DataStore.EasyGraphStore

import java.io.{File, RandomAccessFile}

import DataStore.DynamicStore
import Util.FptrFactory


class LabelNameStore(storeDir: File){
  val dst: DynamicStore = new DynamicStore(storeDir)

  def writeLableName(lable: String): Long ={
    val byteArray = lable.getBytes()
    dst.writeBlock(byteArray)
  }
  def readLableName(labelId: Long): String = {
    new String(dst.readBlock(labelId))
  }

  def close(): Unit ={
    dst.close()
  }

}

class LabelNameMemoryStore(storeDir: File){
  private val fptr: RandomAccessFile = FptrFactory.getFptr(new File(storeDir,"labelNameStore"), "rw")
  private var labelStrings: Map[Long, String]  = Map()
  private var stringsLabel: Map[String, Long]  = Map()
  private var labelStringsize: Long = 0
  if(fptr.length() == 0){
    fptr.writeLong(labelStringsize)
  }
  else labelStringsize = fptr.readLong()

  while(labelStringsize >=1){
    val id: Long = fptr.readLong()
    val labelString: String = fptr.readUTF()
    labelStrings += (id->labelString)
    stringsLabel += (labelString->id)
    labelStringsize -= 1
  }

  labelStringsize = labelStrings.size.toLong


  def hasLabel(): Boolean ={
    if (labelStringsize <= 0) false
    else true
  }

  def getLabelStringSize(): Long = {
    labelStrings.size.toLong

  }


  def addLabelString(labelString: String): Long ={
    if(!stringsLabel.keys.exists(_.equals(labelString))){
      val id = stringsLabel.size.toLong
      labelStrings += (id->labelString)
      stringsLabel += (labelString->id)
      id
    }
    else stringsLabel(labelString)
  }

  def getIdByString(label: String): Long = {
    stringsLabel(label)
  }

  def getlabelStringById(id: Long): String = {
    labelStrings(id)
  }

  def close(): Unit ={
    fptr.seek(0)
    fptr.writeLong(labelStrings.size.toLong)
    labelStrings.foreach(u=> {
      fptr.writeLong(u._1)
      fptr.writeUTF(u._2)
    })
    fptr.close()
  }
}
