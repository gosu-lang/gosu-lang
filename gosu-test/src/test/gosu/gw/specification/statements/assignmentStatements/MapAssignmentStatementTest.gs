package gw.specification.statements.assignmentStatements

uses gw.BaseVerifyErrantTest
uses java.lang.Integer
uses java.util.HashMap
uses java.util.Map
uses java.lang.Exception

class MapAssignmentStatementTest extends BaseVerifyErrantTest {

  function testStringMap()
  {
    var map = new HashMap<String,Integer>() {"a"->8}
    map["a"] = 9
    assertEquals( 9, map["a"])
  }

  function testStringMapShort()
  {
    var map = {"a"->8}
    map["a"] = 9
    assertEquals( 9, map["a"] )
  }

  function testStringMapNotAbstractMap()
  {
    var map : Map<String,Integer> = new HashMap<String,Integer>() {"a"->8}
    map["a"] = 9
    assertEquals( 9, map["a"] )
  }

  function testNullException()
  {
    var map : Map = null
    var x : int = 0
    try{
      map["a"] = 9
    }catch(e : Exception){
      x = 1
    }
    assertEquals(1, x)
  }

}