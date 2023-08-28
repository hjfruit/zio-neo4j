package zio.neo4j.example

import zio.ZIO
import zio.neo4j.*
import zio.neo4j.syntax.*

import org.neo4j.driver.*

final class Neo4jServiceExample1(neo4jDriver: Neo4jDriver) {

  def list(query: String): ZIO[Any, Throwable, List[Record]] =
    neo4jDriver.withTxSimple(query) { cursor =>
      cursor.list
    }

  def list(query: Query): ZIO[Any, Throwable, List[Record]] =
    neo4jDriver.withTx(query, SessionConfig.defaultConfig(), TransactionConfig.empty()) { cursor =>
      cursor.list
    }
}
