package Util

import java.io.{File, RandomAccessFile}

object FptrFactory{
  def getFptr(fileName: String, opMode: String): RandomAccessFile ={
    val fileMetaFile1 = new File(fileName)
    new RandomAccessFile(fileMetaFile1, opMode)
  }

  def getFptr(fileName: File, opMode: String): RandomAccessFile ={
    new RandomAccessFile(fileName, opMode)
  }
}