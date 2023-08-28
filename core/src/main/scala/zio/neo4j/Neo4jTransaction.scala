package zio.neo4j

import java.util.concurrent.CompletionStage

import zio.*

import org.neo4j.driver.{ Query, Record, Value }
import org.neo4j.driver.async.ResultCursor

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
trait Neo4jTransaction:

  def commit: Task[Unit]

  def rollback: Task[Unit]

  def close: Task[Unit]

  def run(query: String, parameters: Value): Task[Neo4jResultCursor]
  def run(query: String, parameters: Map[String, Any]): Task[Neo4jResultCursor]
  def run(query: String, parameters: Record): Task[Neo4jResultCursor]

  def run(query: String): Task[Neo4jResultCursor]

  def run(query: Query): Task[Neo4jResultCursor]

end Neo4jTransaction
