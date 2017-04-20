package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.ParsingException
import com.fasterxml.jackson.databind.node.IntNode
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class CaseClassSupportSpec extends FlatSpec with Matchers {

  behavior of "A basic case class"
  it should "generates a JSON object with matching field values" in {
    generate(CaseClass(1, "Coda")) shouldBe """{"id":1,"name":"Coda"}"""
  }

  it should "is parsable from a JSON object with corresponding fields" in {
    parse[CaseClass]("""{"id":1,"name":"Coda"}""") shouldBe CaseClass(1, "Coda")
  }

  it should "is parsable from a JSON object with extra fields" in {
    parse[CaseClass]("""{"id":1,"name":"Coda","derp":100}""") shouldBe CaseClass(1, "Coda")
  }

  it should "is not parsable from an incomplete JSON object" in {
    the [ParsingException] thrownBy parse[CaseClass]("""{"id":1}""") should have message "Invalid JSON. Needed [id, name], but found [id]."
  }


  behavior of "A case class with lazy fields"
  it should "generates a JSON object with those fields evaluated" in {
    generate(CaseClassWithLazyVal(1)) shouldBe """{"woo":"yeah","id":1}"""
  }

  it should "is parsable from a JSON object without those fields" in {
    parse[CaseClassWithLazyVal]("""{"id":1}""") shouldBe CaseClassWithLazyVal(1)
  }

  it should "is not parsable from an incomplete JSON object" in {
    the [ParsingException] thrownBy parse[CaseClassWithLazyVal]("""{}""") should have message "Invalid JSON. Needed [id], but found []."
  }


  behavior of "A case class with ignored members"
  it should "generates a JSON object without those fields" in {
    generate(CaseClassWithIgnoredField(1)) shouldBe """{"id":1}"""
    generate(CaseClassWithIgnoredFields(1)) shouldBe """{"id":1}"""
  }

  it should "is parsable from a JSON object without those fields" in {
    parse[CaseClassWithIgnoredField]("""{"id":1}""") shouldBe CaseClassWithIgnoredField(1)
    parse[CaseClassWithIgnoredFields]("""{"id":1}""") shouldBe CaseClassWithIgnoredFields(1)
  }

  it should "is not parsable from an incomplete JSON object" in {
    the [ParsingException] thrownBy parse[CaseClassWithIgnoredField]("""{}""") should have message "Invalid JSON. Needed [id], but found []."

    the [ParsingException] thrownBy parse[CaseClassWithIgnoredFields]("""{}""") should have message "Invalid JSON. Needed [id], but found []."
  }


  behavior of "A case class with transient members"
  it should "generates a JSON object without those fields" in {
    generate(CaseClassWithTransientField(1)) shouldBe """{"id":1}"""
  }

  it should "is parsable from a JSON object without those fields" in {
    parse[CaseClassWithTransientField]("""{"id":1}""") shouldBe CaseClassWithTransientField(1)
  }

  it should "is not parsable from an incomplete JSON object" in {
    the [ParsingException] thrownBy parse[CaseClassWithTransientField]("""{}""") should have message "Invalid JSON. Needed [id], but found []."
  }


  behavior of "A case class with an overloaded field"
  it should "generates a JSON object with the nullary version of that field" in {
    generate(CaseClassWithOverloadedField(1)) shouldBe """{"id":1}"""
  }


  behavior of "A case class with an Option[String] member"
  it should "generates a field if the member is Some" in {
    generate(CaseClassWithOption(Some("what"))) shouldBe """{"value":"what"}"""
  }

  it should "is parsable from a JSON object with that field" in {
    parse[CaseClassWithOption]("""{"value":"what"}""") shouldBe CaseClassWithOption(Some("what"))
  }

  it should "doesn't generate a field if the member is None" in {
    generate(CaseClassWithOption(None)) shouldBe """{}"""
  }

  it should "is parsable from a JSON object without that field" in {
    parse[CaseClassWithOption]("""{}""") shouldBe CaseClassWithOption(None)
  }

  it should "is parsable from a JSON object with a null value for that field" in {
    parse[CaseClassWithOption]("""{"value":null}""") shouldBe CaseClassWithOption(None)
  }


  behavior of "A case class with a JsonNode member"
  it should "generates a field of the given type" in {
    generate(CaseClassWithJsonNode(new IntNode(2))) shouldBe """{"value":2}"""
  }


  behavior of "A case class with members of all ScalaSig types"
  val json = """
               {
                 "map": {
                   "one": "two"
                 },
                 "set": [1, 2, 3],
                 "string": "woo",
                 "list": [4, 5, 6],
                 "seq": [7, 8, 9],
                 "sequence": [10, 11, 12],
                 "collection": [13, 14, 15],
                 "indexedSeq": [16, 17, 18],
                 "randomAccessSeq": [19, 20, 21],
                 "vector": [22, 23, 24],
                 "bigDecimal": 12.0,
                 "bigInt": 13,
                 "int": 1,
                 "long": 2,
                 "char": "x",
                 "bool": false,
                 "short": 14,
                 "byte": 15,
                 "float": 34.5,
                 "double": 44.9,
                 "any": true,
                 "anyRef": "wah",
                 "intMap": {
                   "1": "1"
                 },
                 "longMap": {
                   "2": 2
                 }
               }
               """


  it should "is parsable from a JSON object with those fields" in {
    parse[CaseClassWithAllTypes](json) shouldBe
      CaseClassWithAllTypes(
        map = Map("one" -> "two"),
        set = Set(1, 2, 3),
        string = "woo",
        list = List(4, 5, 6),
        seq = Seq(7, 8, 9),
        indexedSeq = IndexedSeq(16, 17, 18),
        vector = Vector(22, 23, 24),
        bigDecimal = BigDecimal("12.0"),
        bigInt = BigInt("13"),
        int = 1,
        long = 2L,
        char = 'x',
        bool = false,
        short = 14,
        byte = 15,
        float = 34.5f,
        double = 44.9d,
        any = true,
        anyRef = "wah",
        intMap = Map(1 -> 1),
        longMap = Map(2L -> 2L)
      )
  }


  behavior of "A case class nested inside of an object"
  it should "is parsable from a JSON object" in {
    parse[OuterObject.NestedCaseClass]("""{"id": 1}""") shouldBe OuterObject.NestedCaseClass(1)
  }


  behavior of "A case class nested inside of an object nested inside of an object"
  it should "is parsable from a JSON object" in {
    parse[OuterObject.InnerObject.SuperNestedCaseClass]("""{"id": 1}""") shouldBe OuterObject.InnerObject.SuperNestedCaseClass(1)
  }


  behavior of "A case class with two constructors"
  it should "is parsable from a JSON object with the same parameters as the case accessor" in {
    parse[CaseClassWithTwoConstructors]("""{"id":1,"name":"Bert"}""") shouldBe CaseClassWithTwoConstructors(1, "Bert")
  }

  it should "is parsable from a JSON object which works with the second constructor" in {
    a [ParsingException] should be thrownBy parse[CaseClassWithTwoConstructors]("""{"id":1}""")
  }


  behavior of "A case class with snake-cased fields"
  it should "is parsable from a snake-cased JSON object" in {
    parse[CaseClassWithSnakeCase]("""{"one_thing":"yes","two_thing":"good"}""") shouldBe CaseClassWithSnakeCase("yes", "good")
  }

  it should "generates a snake-cased JSON object" in {
    generate(CaseClassWithSnakeCase("yes", "good")) shouldBe """{"one_thing":"yes","two_thing":"good"}"""
  }

  it should "throws errors with the snake-cased field names present" in {
    the [ParsingException] thrownBy parse[CaseClassWithSnakeCase]("""{"one_thing":"yes"}""") should have message "Invalid JSON. Needed [one_thing, two_thing], but found [one_thing]."
  }


  behavior of "A case class with array members"
  it should "is parsable from a JSON object" in {
    val c = parse[CaseClassWithArrays]("""{"one":"1","two":["a","b","c"],"three":[1,2,3]}""")

    c.one shouldBe "1"
    c.two shouldBe Array("a", "b", "c")
    c.three shouldBe Array(1, 2, 3)
  }

  it should "generates a JSON object" in {
    generate(CaseClassWithArrays("1", Array("a", "b", "c"), Array(1, 2, 3))) shouldBe
    """{"one":"1","two":["a","b","c"],"three":[1,2,3]}"""
  }
}
