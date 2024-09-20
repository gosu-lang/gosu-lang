package gw.internal.gosu.parser.classTests.gwtest.dynamic

uses gw.lang.reflect.json.Json
uses org.junit.Assert
uses org.junit.After
uses org.junit.Test

class ToStructureTest {

  var simpleArrayOfNumbersJson =
      "[42, 9999999999999]\n"
  
  var arrayOfNumbersJson =
      "{\n" + 
      "\"digits\": [\n" +
      "  {\n" +
      "    \"val\": 42\n" + //int
      "  },\n" +
      "  {\n" +
      "    \"val\": 999999999999\n" + //long
      "  }\n" +
      "]\n" +
      "}"

  @After
  function resetToDefaultParser() {
    Json.setParserName("gw.lang.reflect.json.DefaultParser")
  }

  @Test
  function emptyStructure() {
    var emptyness = '{\n  "emptyness": {}\n}'
    var x = Json.fromJson(emptyness)
    Assert.assertEquals(emptyness, x.toJson())

    var es = x.toStructure("EmptyStructureTest")
    Assert.assertTrue(es.contains("structure emptyness {\n  }"))
  }

  @Test
  function SimpleIntVsLongs() {
    print("About to evaluate: \n" + simpleArrayOfNumbersJson)

    var x = Json.fromJson(simpleArrayOfNumbersJson).toStructure("ArrayOfSimpleNumbers")

    print(x)

    Assert.assertTrue(x.contains("property get value(): List<Long>")) // values are auto-widened to Longs

    Json.setParserName("gw.lang.reflect.json.DefaultParser_Big")

    x = Json.fromJson(simpleArrayOfNumbersJson).toStructure("ArrayOfSimpleNumbers")

    print(x)
    
    Assert.assertTrue(x.contains("property get value(): List<BigInteger>"))
  }
  
  @Test
  function IntVsLongs() {
    print("About to evaluate: \n" + arrayOfNumbersJson)

    var x = Json.fromJson(arrayOfNumbersJson).toStructure("ArrayOfNumbers")

    print(x)
    
    Assert.assertTrue(x.contains("  structure digits {\n    property get val(): Long")) // values are auto-widened to Longs
    
    Json.setParserName("gw.lang.reflect.json.DefaultParser_Big")

    x = Json.fromJson(arrayOfNumbersJson).toStructure("ArrayOfNumbers")
    
    print(x)
    
    Assert.assertTrue(x.contains("  structure digits {\n    property get val(): BigInteger"))
  }
  
}