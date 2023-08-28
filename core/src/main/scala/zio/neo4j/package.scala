package zio.neo4j

import zio.neo4j.impl.Neo4jResultCursorLive

import org.neo4j.driver.async.ResultCursor

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/28
 */
extension (resultCursor: ResultCursor) def wrapped = new Neo4jResultCursorLive(resultCursor)
