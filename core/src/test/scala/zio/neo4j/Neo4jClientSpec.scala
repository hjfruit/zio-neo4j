package zio.neo4j

import java.util.Collections

import scala.jdk.CollectionConverters.*

import zio.ZIO
import zio.neo4j.*
import zio.neo4j.syntax.*
import zio.test.*

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
object Neo4jClientSpec extends Neo4jContainerSpec {

  def specLayered: Spec[Neo4jEnvironment, Throwable] =
    suite("Neo4j")(
      suite("Neo4jClient")(
        test("create and read query") {
          for {
            driver <- ZIO.service[Neo4jDriver]
            single <- driver.withTxSimple("CREATE (a:Person {name: $name})", Map("name" -> "a")) { rs =>
                        rs.consume.map(_.counters().nodesCreated())
                      }
            keys   <- driver.session
                        .flatMap(_.readTransaction(_.runAsync("""
                                                              |MATCH (p:Person) RETURN p.name
                                                              |""".stripMargin)).map(_.wrapped))
                        .flatMap(_.keys)
          } yield assertTrue(single == 1 && keys.size == 1)
        }
      )
    )

}
