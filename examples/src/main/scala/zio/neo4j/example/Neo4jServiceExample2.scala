package zio.neo4j.example

import zio.Exit.*
import zio.ZIO
import zio.neo4j.Neo4jDriver

import org.neo4j.driver.Record
import org.neo4j.driver.async.ResultCursor

final class Neo4jServiceExample2(neo4jDriver: Neo4jDriver) {

  def list: ZIO[Any, Throwable, List[Record]] =
    neo4jDriver.session.flatMap { neo4j =>
      neo4j
        .readTransaction[ResultCursor](tx => tx.runAsync(query, Map.empty.asJava))
        .map(_.wrapped)
        .flatMap(_.list)
        .flatMap(records => ZIO.succeed(println(records)))
    }
}
