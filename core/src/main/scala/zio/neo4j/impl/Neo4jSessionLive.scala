package zio
package neo4j
package impl

import java.util
import java.util.concurrent.CompletionStage

import scala.jdk.CollectionConverters.*

import zio.{ Task, ZIO }

import org.neo4j.driver.*
import org.neo4j.driver.async.*

final class Neo4jSessionLive(underlying: AsyncSession) extends Neo4jSession:

  def beginTransaction(config: TransactionConfig = TransactionConfig.empty()): Task[Neo4jTransaction] =
    ZIO.fromCompletionStage(underlying.beginTransactionAsync(config)).map(new Neo4jTransactionLive(_))

  def readTransaction[T](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[T] =
    ZIO.fromCompletionStage(underlying.readTransactionAsync(work, config))

  def writeTransaction[T](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[T] =
    ZIO.fromCompletionStage(underlying.writeTransactionAsync(work, config))

  def run(
    query: String,
    parameters: Map[String, Any] = Map.empty,
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage(underlying.runAsync(query, parameters.asJava, config)).map(new Neo4jResultCursorLive(_))

  def run(
    query: Query,
    config: TransactionConfig
  ): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage(underlying.runAsync(query, config)).map(new Neo4jResultCursorLive(_))

  def lastBookmark: Task[Bookmark] =
    ZIO.attempt(underlying.lastBookmark())

  def close: Task[Unit] = ZIO.fromCompletionStage(underlying.closeAsync()).unit
