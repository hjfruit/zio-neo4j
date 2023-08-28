package zio.neo4j

import java.io.IOException
import java.net.URI

import zio.{ Scope, Task, URIO, URLayer, ZIO, ZLayer }
import zio.neo4j.impl.*

import org.neo4j.driver.*
import org.neo4j.driver.async.AsyncSession

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */

trait Neo4jDriver:
  def session: Task[Neo4jSession]

  def session(sessionConfig: SessionConfig): Task[Neo4jSession]

  def close(): Task[Unit]

  def isEncrypted: Task[Boolean]

  def metrics: Task[Metrics]

  def isMetricsEnabled: Task[Boolean]

  def verifyConnectivity: Task[Unit]

  def supportsMultiDb: Task[Boolean]

end Neo4jDriver

object Neo4jDriver:

  lazy val live: URLayer[Neo4jAuthConfig with Config, Neo4jDriver] = ZLayer.scoped {
    for
      authConf <- ZIO.service[Neo4jAuthConfig]
      conf     <- ZIO.service[Config]
      driver   <- create(authConf, conf)
    yield driver
  }.orDie

  def create(authConfig: Neo4jAuthConfig, config: Config): ZIO[Any & Scope, IOException, Neo4jDriver] =
    ZIO.acquireRelease(
      ZIO.attemptBlockingIO(
        Neo4jDriverLive(
          GraphDatabase
            .driver(URI.create(authConfig.uri), AuthTokens.basic(authConfig.user, authConfig.password), config)
        )
      )
    )(release => release.close().onError(cleanup => ZIO.logErrorCause(cleanup)).ignoreLogged)
end Neo4jDriver
