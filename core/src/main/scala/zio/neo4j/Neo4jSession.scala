package zio.neo4j

import java.util
import java.util.concurrent.CompletionStage

import zio.Task

import org.neo4j.driver.*
import org.neo4j.driver.async.*

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
trait Neo4jSession:

  def beginTransaction: Task[Neo4jTransaction]

  def beginTransaction(config: TransactionConfig): Task[Neo4jTransaction]

  def readTransaction[T <: ResultCursor](work: AsyncTransactionWork[CompletionStage[T]]): Task[T]

  def readTransaction[T <: ResultCursor](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig
  ): Task[T]

  def writeTransaction[T <: ResultCursor](work: AsyncTransactionWork[CompletionStage[T]]): Task[T]

  def writeTransaction[T <: ResultCursor](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig
  ): Task[T]

  def run(query: String, config: TransactionConfig): Task[Neo4jResultCursor]

  def run(query: String, parameters: Map[String, Any], config: TransactionConfig): Task[Neo4jResultCursor]

  def run(query: Query, config: TransactionConfig): Task[Neo4jResultCursor]

  def lastBookmark: Task[Bookmark]

  def close: Task[Unit]

end Neo4jSession
