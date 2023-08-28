package zio.neo4j.example

import zio.Exit.*
import zio.ZIO
import zio.neo4j.Neo4jDriver

import org.neo4j.driver.Record

final class Neo4jServiceExample1(neo4jDriver: Neo4jDriver) {

  def list: ZIO[Any, Throwable, List[Record]] =
    neo4jDriver.session.flatMap { neo4j =>
      neo4j.beginTransaction.flatMap { tx =>
        neo4j.run(query, Map.empty, TransactionConfig.empty()).flatMap(_.list).onExit {
          case Failure(e) =>
            ZIO.logErrorCause(e) *> tx.rollback.onError(cause => ZIO.logErrorCause(cause)).ignoreLogged
          case Success(_) =>
            tx.commit
        }
      }
    }
}
