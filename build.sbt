val zioVersion    = "2.0.13"
val scala3Version = "3.3.0"
val scala2Version = "2.13.10"

inThisBuild(
  List(
    scalaVersion     := scala3Version,
    homepage         := Some(url("https://github.com/hjfruit/zio-neo4j")),
    licenses         := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
    organization     := "io.github.jxnu-liguobin",
    organizationName := "梦境迷离",
    developers       := List(
      Developer(
        id = "jxnu-liguobin",
        name = "梦境迷离",
        email = "dreamylost@outlook.com",
        url = url("https://github.com/jxnu-liguobin")
      )
    )
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val core = project
  .in(file("core"))
  .settings(
    name               := "zio-neo4j",
    libraryDependencies ++= Seq(
      "dev.zio"         %% "zio"               % zioVersion % Provided,
      "dev.zio"         %% "zio-test"          % zioVersion % Test,
      "org.neo4j"        % "neo4j-cypher-dsl"  % "2023.6.0",
      "org.neo4j.driver" % "neo4j-java-driver" % "4.4.12"
    ),
    crossScalaVersions := Seq(scala3Version, scala2Version),
    testFrameworks     := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

lazy val examples = project
  .in(file("examples"))
  .settings(
    publish / skip := true,
    libraryDependencies ++= Seq(
      "dev.zio"  %% "zio"              % zioVersion % Provided,
      "dev.zio"  %% "zio-test"         % zioVersion % Test,
      "org.neo4j" % "neo4j-cypher-dsl" % "2023.6.0"
    )
  )
  .dependsOn(core)

lazy val `zio-neo4j` = project
  .in(file("."))
  .settings(
    publish / skip := true
  )
  .aggregate(
    core,
    examples
  )
