package VersionManage

trait GraphDataVersion {
  def queryNodeChange()
  def queryRelationChange()
  def querySubGraphChange()
  def queryBigChange()
  def queryVersionDiff()
  def queryVersionGraph()
  def storeChange(logPath: String)
  def showChange()
}