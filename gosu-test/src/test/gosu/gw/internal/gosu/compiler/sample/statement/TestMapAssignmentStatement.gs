package gw.internal.gosu.compiler.sample.statement

uses java.util.Map
uses java.util.HashMap
uses java.lang.Integer
  
class TestMapAssignmentStatement
{
  function testStringMap() : Integer
  {
    var map = new HashMap<String,Integer>() {"a"->8}
    map["a"] = 9
    return map["a"]
  }

  function testStringMapShort() : Integer
  {
    var map = {"a"->8}
    map["a"] = 9
    return map["a"]
  }

  function testStringMapNotAbstractMap() : Integer
  {
    var map : Map<String,Integer> = new HashMap<String,Integer>() {"a"->8}
    map["a"] = 9
    return map["a"]
  }

  function testNullException() : String
  {
    var map : Map = null
    map["a"] = 9 // should throw eval exception here indicating null ref on map
    return map["a"] as String
  }
}