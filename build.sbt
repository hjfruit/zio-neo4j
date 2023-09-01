val zioVersion    = "2.0.15"
val scala3Version = "3.3.1-RC4"

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
    name           := "zio-neo4j",
    libraryDependencies ++= Seq(
      "org.neo4j.driver" % "neo4j-java-driver"          % "4.4.12",
      "dev.zio"         %% "zio"                        % zioVersion % Provided,
      "dev.zio"         %% "zio-test"                   % zioVersion % Test,
      "com.dimafeng"    %% "testcontainers-scala-neo4j" % "0.40.17"  % Test,
      "org.neo4j"        % "neo4j-cypher-dsl"           % "2023.6.0" % Test,
      "ch.qos.logback"   % "logback-classic"            % "1.4.5"    % Test
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

lazy val `zio-neo4j` = project
  .in(file("."))
  .settings(
    publish / skip := true,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
  .aggregate(core)
