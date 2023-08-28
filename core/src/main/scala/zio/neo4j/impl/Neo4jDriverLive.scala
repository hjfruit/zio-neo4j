package zio
package neo4j
package impl

import java.io.IOException
import java.net.URI
import java.util.concurrent.CompletionStage

import zio.{ Scope, Task, URIO, ZIO, ZLayer }

import org.neo4j.driver.*
import org.neo4j.driver.async.AsyncSession

final class Neo4jDriverLive(underlying: Driver) extends Neo4jDriver:

  override def session(sessionConfig: SessionConfig): Task[Neo4jSession] =
    ZIO.attempt(underlying.asyncSession(sessionConfig)).map(new Neo4jSessionLive(_))

  override def session: Task[Neo4jSession] = ZIO.attempt(underlying.asyncSession()).map(new Neo4jSessionLive(_))

  override def close(): Task[Unit] = ZIO.fromCompletionStage(underlying.closeAsync()).unit

  override def isEncrypted: Task[Boolean] = ZIO.attempt(underlying.isEncrypted())

  override def metrics: Task[Metrics] = ZIO.attempt(underlying.metrics())

  override def isMetricsEnabled: Task[Boolean] = ZIO.attempt(underlying.isMetricsEnabled())

  override def verifyConnectivity: Task[Unit] = ZIO.fromCompletionStage(underlying.verifyConnectivityAsync()).unit

  override def supportsMultiDb: Task[Boolean] =
    ZIO.fromCompletionStage(underlying.supportsMultiDbAsync()).map(f => f.booleanValue())

end Neo4jDriverLive
