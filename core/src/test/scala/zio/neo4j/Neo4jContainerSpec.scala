package zio.neo4j

import java.util.Properties

import zio.{ Scope, ZLayer }
import zio.test.*
import zio.test.ZIOSpecDefault

import org.neo4j.driver.Config

trait Neo4jContainerSpec extends ZIOSpecDefault {

  override def spec =
    specLayered.provideLayerShared(
      ZLayer.make[Neo4jEnvironment](
        Scope.default,
        testEnvironment,
        TestContainer.neo4j
          .flatMap(a =>
            ZLayer.fromZIO(
              Neo4jDriver.create(
                Neo4jAuthConfig(a.get.boltUrl, a.get.username, a.get.password),
                Config.defaultConfig()
              )
            )
          )
          .orDie
      )
    )

  def specLayered: Spec[Neo4jEnvironment, Throwable]

}
