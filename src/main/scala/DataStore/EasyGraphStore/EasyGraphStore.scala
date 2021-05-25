package DataStore.EasyGraphStore

import java.io.{File, RandomAccessFile}

import GraphModel.{EasyGraph, EasyNode, EasyRelation}

import scala.collection.mutable

// 0 [][][][]:block_size,[][][][]:recorder_size+ [][][][]
// 1 [][][][],[][][][]+ [][][][]
// 2 [][][][],[][][][]+ [][][][]




class EasyGraphStore {

  def storeRelation(rels: Array[EasyRelation], dataPath: String): Unit ={

  }

  def familyNodes(node: EasyNode): Unit = {

  }
  def storeNodes(nodes: Array[EasyNode], dataPath: String): Unit ={

    val labelNameMeta: mutable.Set[String]  = mutable.Set()
    val pNameMeta: mutable.Set[String]  = mutable.Set()
    val nodeFile = new File(dataPath,"nodes")
    val fptr1 = new RandomAccessFile(nodeFile, "rw")

  }


  def storeGraph(g: EasyGraph, dataPath: String): Unit ={

  }

  def getGraph(dataPath: String): EasyGraph = {

    new EasyGraph
  }


}
