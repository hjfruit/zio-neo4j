package zio.neo4j

import zio.*

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
trait Neo4jTransaction:
  def commit: Task[Unit]
  def rollback: Task[Unit]
  def close: Task[Unit]
end Neo4jTransaction
