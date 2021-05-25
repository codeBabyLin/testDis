package Util

import org.neo4j.cypher.internal.ast._
import org.neo4j.cypher.internal.expressions._
import org.neo4j.cypher.internal.parser.CypherParser
import org.neo4j.cypher.internal.util.{CypherExceptionFactory, InputPosition}

object CypherTransForm {

  def eval(expression: Expression): Any ={
    expression match {
      case StringLiteral(value) => value
      case SignedDecimalIntegerLiteral(value) => value.toInt
    }
  }

  def map2String(m: Map[String, Any]): String ={
    m.map(rel => {
      val s = rel._1
      val a = rel._2
      a match {
        case x:String => s"$s:'$a'"
        case _ => s"$s:$a"
      }
    }).mkString(",")
  }
  def update2create(cypherString: String): String ={
    val cyPar:CypherParser = new CypherParser
    //val cypherString: String = "match(n:employee{name:'David163497',age:30, saray:11236}) set n.name = 'Ellis11338', n.age = 53, n.saray = 8909"
    val cef: CypherExceptionFactory = new CypherExceptionFactory {
      override def arithmeticException(message: String, cause: Exception): Exception = ???

      override def syntaxException(message: String, pos: InputPosition): Exception = ???
    }


    val state = cyPar.parse(cypherString, cef)
    var label: String = null
    var oldMap: Map[String, Any] = null
    var newMap: Map[String, Any] = null
    state match {
      case Query(periodicCommitHint, SingleQuery(clauses)) => clauses.foreach {
        case Match(optional, Pattern(patternParts), hints, where) =>
          patternParts.foreach{
            case EveryPath(NodePattern(variable, labels, Some(MapExpression(items)), baseNode)) =>
              label = labels.head.name
              oldMap = items.map(item => item._1.name -> eval(item._2)).toMap

          }
        case SetClause(items) => newMap = items.map {
          case SetPropertyItem(Property(map, PropertyKeyName(name)),expression) => name -> eval(expression)
        }.toMap
      }
    }
    val str1 = map2String(oldMap)

    val str2 = map2String(newMap)

    val newCypher = s"match(n:$label{$str1}) create (n)-[:nextVersion]->(m:$label{$str2})"
    newCypher

  }

}
