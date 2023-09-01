package zio
package neo4j
package impl

import scala.jdk.CollectionConverters.*

import zio.*

import org.neo4j.driver.{ Query as _, * }
import org.neo4j.driver.async.AsyncTransaction

private[neo4j] final class Neo4jTransactionLive(underlying: AsyncTransaction) extends Neo4jTransaction:

  override def close: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.closeAsync()).unit)

  override def commit: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.commitAsync()).unit)

  override def rollback: Task[Unit] = ZIO.blocking(ZIO.fromCompletionStage(underlying.rollbackAsync()).unit)

  override def run(query: QueryParameter): Task[Neo4jResultCursor] =
    ZIO.fromCompletionStage {
      query match
        case QueryParameter.QueryValueParameter(query, parameters)  =>
          underlying.runAsync(query, parameters)
        case QueryParameter.QueryMapParameter(query, parameters)    =>
          underlying.runAsync(query, parameters.asJava)
        case QueryParameter.QueryRecordParameter(query, parameters) =>
          underlying.runAsync(query, parameters)
        case QueryParameter.QueryNoParameter(query)                 =>
          underlying.runAsync(query)
        case QueryParameter.QueryStringNoParameter(query)           =>
          underlying.runAsync(query)
    }.map(new Neo4jResultCursorLive(_))

end Neo4jTransactionLive
