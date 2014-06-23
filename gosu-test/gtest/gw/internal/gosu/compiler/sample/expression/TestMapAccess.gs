package gw.internal.gosu.compiler.sample.expression

uses java.util.Map
uses java.util.HashMap
uses java.lang.Integer
  
class TestMapAccess
{
  function testStringMap() : Integer
  {
    var map = new HashMap<String,Integer>() {"a"->8}
    return map["a"]
  }

  function testStringMapShort() : Integer
  {
    var map = {"a"->8}
    return map["a"]
  }

  function testStringMapNotAbstractMap() : Integer
  {
    var map : Map<String,Integer> = new HashMap<String,Integer>() {"a"->8}
    return map["a"]
  }

  function testNullShortcircuit() : String
  {
    var map : Map = null
    return map?["a"] as String
  }
}