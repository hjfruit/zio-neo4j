package zio.neo4j

import zio.*
import zio.neo4j.QueryParameter

import org.neo4j.driver.{ Query as _, * }

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
trait Neo4jTransaction:

  def commit: Task[Unit]

  def rollback: Task[Unit]

  def close: Task[Unit]

  def run(query: QueryParameter): Task[Neo4jResultCursor]

end Neo4jTransaction
