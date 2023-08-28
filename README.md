ZIO Neo4j
---

![CI][Badge-CI] [![Nexus (Snapshots)][Badge-Snapshots]][Link-Snapshots] [![Sonatype Nexus (Releases)][Badge-Release]][Link-Release]


[Badge-CI]: https://github.com/hjfruit/zio-pulsar/actions/workflows/scala.yml/badge.svg
[Badge-Snapshots]: https://img.shields.io/nexus/s/io.github.jxnu-liguobin/zio-neo4j_3?server=https%3A%2F%2Foss.sonatype.org
[Link-Snapshots]: https://oss.sonatype.org/content/repositories/snapshots/io/github/jxnu-liguobin/zio-neo4j_3/
[Link-Release]: https://oss.sonatype.org/content/repositories/public/io/github/jxnu-liguobin/zio-neo4j_3/
[Badge-Release]: https://img.shields.io/nexus/r/io.github.jxnu-liguobin/zio-neo4j_3?server=https%3A%2F%2Foss.sonatype.org


## Dependency

Scala 3
```scala
libraryDependencies += "io.github.jxnu-liguobin" %% "zio-neo4j" % <latest version>
```

Scala 2.13.6+ (sbt 1.5.x)
```scala
libraryDependencies += 
  ("io.github.jxnu-liguobin" %% "zio-neo4j" % NewVersion).cross(CrossVersion.for2_13Use3)
```

These dependencies are required in the project classpath:
```scala
libraryDependencies ++= Seq(
  "dev.zio" %% "zio"         % zioVersion,
)
```

## Example1

Manually commit transactions:
```scala
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

```

## Example2

Automatically commit transactions:
```scala
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

```