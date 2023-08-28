package zio.neo4j.example

import zio.Exit.*
import zio.ZIO
import zio.neo4j.Neo4jDriver
import zio.neo4j.withLocalTx

import org.neo4j.driver.{ Record, SessionConfig, TransactionConfig }

final class Neo4jServiceExample1(neo4jDriver: Neo4jDriver) {

  def list(query: String): ZIO[Any, Throwable, List[Record]] =
    neo4jDriver.withLocalTx(query) { cursor =>
      cursor.list
    }
}
