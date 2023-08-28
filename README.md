ZIO Pulsar
---

![CI][Badge-CI] [![Nexus (Snapshots)][Badge-Snapshots]][Link-Snapshots] [![Sonatype Nexus (Releases)][Badge-Release]][Link-Release]


[Badge-CI]: https://github.com/hjfruit/zio-pulsar/actions/workflows/scala.yml/badge.svg
[Badge-Snapshots]: https://img.shields.io/nexus/s/io.github.jxnu-liguobin/zio-neo4j_3?server=https%3A%2F%2Foss.sonatype.org
[Link-Snapshots]: https://oss.sonatype.org/content/repositories/snapshots/io/github/jxnu-liguobin/zio-neo4j_3/
[Link-Release]: https://oss.sonatype.org/content/repositories/public/io/github/jxnu-liguobin/zio-neo4j_3/
[Badge-Release]: https://img.shields.io/nexus/r/io.github.jxnu-liguobin/zio-neo4j_3?server=https%3A%2F%2Foss.sonatype.org


## Dependency

Scala 3
```
libraryDependencies += "io.github.jxnu-liguobin" %% "zio-neo4j" % <latest version>
```

Scala 2.13.6+ (sbt 1.5.x)
```
libraryDependencies += 
  ("io.github.jxnu-liguobin" %% "zio-neo4j" % NewVersion).cross(CrossVersion.for2_13Use3)
```

These dependencies are required in the project classpath (ZIO projects only need to pay attention to whether they have imported zio-streams):
```
libraryDependencies ++= Seq(
  "dev.zio" %% "zio"         % zioVersion,
)
```

## Example1
```scala
final class Neo4jService(neo4jDriver: Neo4jDriver) {
  def list = {
    neo4jDriver.session.flatMap { neo4j =>
      neo4j.beginTransaction.flatMap { tx =>
        (for
          cursor <- neo4j.run(query, Map.empty, TransactionConfig.empty())
          records <- cursor.list
          _ <- ZIO.succeed(println(records))
          _ <- tx.commit
        yield {}
        ).onError(_ => tx.rollback.onError(cause => ZIO.logErrorCause(cause)).ignoreLogged)
      }
    }
  }
}
```