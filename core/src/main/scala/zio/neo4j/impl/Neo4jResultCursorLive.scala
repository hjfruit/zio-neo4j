package zio
package neo4j
package impl

import java.util.function.Consumer

import scala.jdk.CollectionConverters.*

import zio.*

import org.neo4j.driver.*
import org.neo4j.driver.async.ResultCursor
import org.neo4j.driver.summary.ResultSummary

private[neo4j] final class Neo4jResultCursorLive(underlying: ResultCursor) extends Neo4jResultCursor:
  override def keys: Task[List[String]] = ZIO.attempt(underlying.keys().asScala.toList)

  override def consume: Task[ResultSummary] = ZIO.fromCompletionStage(underlying.consumeAsync())

  override def next: Task[Record] = ZIO.fromCompletionStage(underlying.nextAsync())

  override def peek: Task[Record] = ZIO.fromCompletionStage(underlying.peekAsync())

  override def single: Task[Record] = ZIO.fromCompletionStage(underlying.singleAsync())

  override def foreach(action: Consumer[Record]): Task[ResultSummary] =
    ZIO.fromCompletionStage(underlying.forEachAsync(action))

  override def list: Task[List[Record]] =
    ZIO.fromCompletionStage(underlying.listAsync()).map(_.asScala.toList)

  override def list[T](mapFunction: Record => T): Task[List[T]] =
    ZIO.fromCompletionStage(underlying.listAsync(r => mapFunction.apply(r))).map(_.asScala.toList)

end Neo4jResultCursorLive
