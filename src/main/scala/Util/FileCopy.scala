package Util

import java.io.File
import java.nio.file.Files

object FileCopy {

  def copyDir(sourceDir: File, targetDir: File, clear: Boolean = true): Unit ={
    sourceDir.mkdirs()
    targetDir.mkdirs()
    if(clear) targetDir.listFiles().map(delFiles)
    sourceDir.listFiles().foreach(file => {
      Files.copy(file.toPath, new File(targetDir, file.getName).toPath)
      if(file.isDirectory){
        copyDir(file, new File(targetDir, file.getName))
      }
    })
  }

  def delFiles(file: File): Unit ={
    if(file.isDirectory){
      val childrenFiles = file.listFiles()
      if(childrenFiles.nonEmpty){
        childrenFiles.map(delFiles)
      }
    }
    file.delete()
  }

}
