package zio.neo4j

import zio.{ neo4j, Task, ZIO }
import zio.Exit.*
import zio.neo4j.impl.Neo4jResultCursorLive

import org.neo4j.driver.{ SessionConfig, TransactionConfig }
import org.neo4j.driver.async.ResultCursor

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
extension (resultCursor: ResultCursor) def wrapped = new Neo4jResultCursorLive(resultCursor)

extension (neo4jDriver: Neo4jDriver)

  def withLocalTx[A](
    query: String,
    parameters: Map[String, Any] = Map.empty,
    sessionConfig: SessionConfig = SessionConfig.defaultConfig(),
    config: TransactionConfig = TransactionConfig.empty()
  )(action: Neo4jResultCursor => Task[A]): Task[A] =
    ZIO.blocking {
      neo4jDriver.session(sessionConfig).flatMap { neo4jSession =>
        neo4jSession.beginTransaction(config).flatMap { tx =>
          neo4jSession.run(query, parameters, config).flatMap(action.apply).onExit {
            case Failure(e) =>
              ZIO.logErrorCause(e) *> tx.rollback.onError(cause => ZIO.logErrorCause(cause)).ignoreLogged
            case Success(_) =>
              tx.commit.ignoreLogged
          }
        }
      }
    }
