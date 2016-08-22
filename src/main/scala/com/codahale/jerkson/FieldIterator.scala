package com.codahale.jerkson

import com.fasterxml.jackson.core.{JsonToken, JsonParser}
import com.codahale.jerkson.AST.JValue

class FieldIterator(parser: JsonParser) extends Iterator[(String, JValue)] {

  if (parser.getCurrentToken == null) {
    parser.nextToken()
  }
  require(parser.getCurrentToken == JsonToken.START_OBJECT)
  parser.nextToken()

  def hasNext = parser.getCurrentToken != JsonToken.END_OBJECT && !parser.isClosed
  def parse[A](parser: JsonParser)(implicit mf: Manifest[A]): A = Json.parse(parser, mf, false)

  def next() = if (hasNext) {
    require(parser.getCurrentToken == JsonToken.FIELD_NAME)
    val name = parser.getCurrentName
    parser.nextToken()
    val value = parse[JValue](parser)
    parser.nextToken()
    (name, value)
  } else Iterator.empty.next()

}
