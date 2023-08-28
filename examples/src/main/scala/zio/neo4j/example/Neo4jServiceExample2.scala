package zio.neo4j.example

import scala.jdk.CollectionConverters.*

import zio.ZIO
import zio.neo4j.*
import zio.neo4j.syntax.*

import org.neo4j.driver.Record
import org.neo4j.driver.async.ResultCursor

final class Neo4jServiceExample2(neo4jDriver: Neo4jDriver) {

  def list(query: String): ZIO[Any, Throwable, List[Record]] =
    neo4jDriver.session.flatMap { neo4j =>
      neo4j
        .readTransaction[ResultCursor](tx => tx.runAsync(query, Map.empty.asJava))
        .map(_.wrapped)
        .flatMap(_.list)
    }
}
