package zio.neo4j

import org.neo4j.driver.{ Query as NQuery, * }

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/9/1
 */
sealed trait QueryParameter

object QueryParameter:
  final case class QueryValueParameter(query: String, parameters: Value) extends QueryParameter

  final case class QueryMapParameter(query: String, parameters: Map[String, Any]) extends QueryParameter

  final case class QueryRecordParameter(query: String, parameters: Record) extends QueryParameter

  final case class QueryNoParameter(query: String) extends QueryParameter

  final case class QueryStringNoParameter(query: NQuery) extends QueryParameter
