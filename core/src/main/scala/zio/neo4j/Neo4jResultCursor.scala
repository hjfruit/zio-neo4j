package zio.neo4j

import java.util.function.Consumer

import scala.jdk.CollectionConverters.*

import zio.Task

import org.neo4j.driver.*
import org.neo4j.driver.summary.ResultSummary

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
trait Neo4jResultCursor:

  def keys: Task[List[String]]

  def consume: Task[ResultSummary]

  def next: Task[Record]

  def peek: Task[Record]

  def single: Task[Record]

  def foreach(action: Consumer[Record]): Task[ResultSummary]

  def list: Task[List[Record]]

  def list[T](mapFunction: Record => T): Task[List[T]]

end Neo4jResultCursor
