package zio.neo4j.syntax

import scala.jdk.CollectionConverters.*

import zio.{ neo4j, Task, ZIO }
import zio.Exit.*
import zio.neo4j.{ Neo4jDriver, Neo4jResultCursor }
import zio.neo4j.impl.Neo4jResultCursorLive

import org.neo4j.driver.*
import org.neo4j.driver.async.ResultCursor

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
extension (resultCursor: ResultCursor) def wrapped = new Neo4jResultCursorLive(resultCursor)

extension (neo4jDriver: Neo4jDriver)

  def withTx[A](
    query: Query,
    sessionConfig: SessionConfig,
    config: TransactionConfig
  )(action: Neo4jResultCursor => Task[A]): Task[A] =
    withTxSimple(query.text(), query.parameters().asMap().asScala.toMap, sessionConfig, config)(action)

  def withTxSimple[A](
    query: String,
    parameters: Map[String, Any] = Map.empty,
    sessionConfig: SessionConfig = SessionConfig.defaultConfig(),
    config: TransactionConfig = TransactionConfig.empty()
  )(action: Neo4jResultCursor => Task[A]): Task[A] =
    ZIO.blocking {
      neo4jDriver.session(sessionConfig).flatMap { neo4jSession =>
        neo4jSession
          .beginTransaction(config)
          .flatMap { tx =>
            tx.run(query, parameters).flatMap(action.apply).onExit {
              case Failure(e) =>
                ZIO.logErrorCause(e) *> tx.rollback.onError(cause => ZIO.logErrorCause(cause)).ignoreLogged
              case Success(_) =>
                tx.commit.ignoreLogged
            }
          }
          .ensuring(neo4jSession.close.ignoreLogged)
      }
    }
