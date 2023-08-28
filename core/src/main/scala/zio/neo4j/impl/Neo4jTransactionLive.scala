package zio
package neo4j
package impl

import scala.jdk.CollectionConverters.*

import zio.*

import org.neo4j.driver.{ Query, Record, Value }
import org.neo4j.driver.async.AsyncTransaction

private[neo4j] final class Neo4jTransactionLive(underlying: AsyncTransaction) extends Neo4jTransaction:

  override def close: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.closeAsync()).unit)

  override def commit: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.commitAsync()).unit)

  override def rollback: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.rollbackAsync()).unit)

  override def run(query: Query): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage(underlying.runAsync(query)).map(new Neo4jResultCursorLive(_))

  override def run(query: String): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage(underlying.runAsync(query)).map(new Neo4jResultCursorLive(_))

  override def run(query: String, parameters: Map[String, Any]): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage(underlying.runAsync(query, parameters.asJava)).map(new Neo4jResultCursorLive(_))

  override def run(query: String, parameters: Record): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage(underlying.runAsync(query, parameters)).map(new Neo4jResultCursorLive(_))

  override def run(query: String, parameters: Value): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage(underlying.runAsync(query, parameters)).map(new Neo4jResultCursorLive(_))

end Neo4jTransactionLive
