package zio
package neo4j
package impl

import java.util
import java.util.concurrent.CompletionStage

import scala.jdk.CollectionConverters.*

import zio.*

import org.neo4j.driver.*
import org.neo4j.driver.async.*

private[neo4j] final class Neo4jSessionLive(underlying: AsyncSession) extends Neo4jSession:

  def beginTransaction(config: TransactionConfig = TransactionConfig.empty()): Task[Neo4jTransaction] =
    ZIO.blocking(ZIO.fromCompletionStage(underlying.beginTransactionAsync(config)).map(new Neo4jTransactionLive(_)))

  def readTransaction[T](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[T] =
    ZIO.blocking(ZIO.fromCompletionStage(underlying.readTransactionAsync(work, config)))

  def writeTransaction[T](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[T] =
    ZIO.blocking(ZIO.fromCompletionStage(underlying.writeTransactionAsync(work, config)))

  def run(
    query: QueryParameter,
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[Neo4jResultCursor] = ZIO.fromCompletionStage {
    query match
      case QueryParameter.QueryValueParameter(query, parameters)  =>
        underlying.runAsync(query, parameters)
      case QueryParameter.QueryMapParameter(query, parameters)    =>
        underlying.runAsync(query, parameters.asJava, config)
      case QueryParameter.QueryNoParameter(query)                 =>
        underlying.runAsync(query, config)
      case QueryParameter.QueryStringNoParameter(query)           =>
        underlying.runAsync(query, config)
      case QueryParameter.QueryRecordParameter(query, parameters) =>
        underlying.runAsync(query, parameters)
  }.map(new Neo4jResultCursorLive(_))

  def lastBookmark: Task[Bookmark] =
    ZIO.attempt(underlying.lastBookmark())

  def close: Task[Unit] = ZIO.fromCompletionStage(underlying.closeAsync()).unit
