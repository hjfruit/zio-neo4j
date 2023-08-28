package zio.neo4j

import zio.*

import org.testcontainers.utility.DockerImageName

import com.dimafeng.testcontainers.Neo4jContainer

object TestContainer {

  lazy val neo4j: ZLayer[Scope, Throwable, Neo4jContainer] =
    ZLayer(ZIO.acquireRelease {
      val c = new Neo4jContainer(neo4jImageVersion = Some(DockerImageName.parse("neo4j:4.4.25-community")))
      ZIO.attempt(c.start()).as(c)
    }(container => ZIO.succeed(container.stop())))

}
