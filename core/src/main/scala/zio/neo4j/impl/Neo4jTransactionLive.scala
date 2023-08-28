package zio
package neo4j
package impl

import zio.*

import org.neo4j.driver.async.AsyncTransaction

final class Neo4jTransactionLive(underlying: AsyncTransaction) extends Neo4jTransaction:

  override def close: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.closeAsync()).unit)

  override def commit: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.commitAsync()).unit)

  override def rollback: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.rollbackAsync()).unit)

end Neo4jTransactionLive
