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

  /**
   * Begin a new unmanaged transaction with the specified configuration.
   */
  def beginTransaction(config: TransactionConfig = TransactionConfig.empty()): Task[Neo4jTransaction]

  /**
   * Execute given unit of asynchronous work in a read asynchronous transaction with the specified configuration.
   *
   * Transaction will automatically be committed unless given unit of work fails or async transaction commit fails.
   *
   * It will also not be committed if explicitly rolled back via AsyncTransaction.rollbackAsync().
   */
  def readTransaction[T <: ResultCursor](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[T]

  /**
   * Execute given unit of asynchronous work in a read asynchronous transaction with the specified configuration.
   *
   * Transaction will automatically be committed unless given unit of work fails or async transaction commit fails.
   *
   * It will also not be committed if explicitly rolled back via AsyncTransaction.rollbackAsync().
   */
  def writeTransaction[T <: ResultCursor](
    work: AsyncTransactionWork[CompletionStage[T]],
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[T]

  def run(
    query: QueryParameter,
    config: TransactionConfig = TransactionConfig.empty()
  ): Task[Neo4jResultCursor]

  def lastBookmark: Task[Bookmark]

  def close: Task[Unit]

end Neo4jSession
