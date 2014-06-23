package gw.internal.gosu.regression
uses java.util.ArrayList
uses java.lang.Integer
uses gw.test.TestClass

class NestedArrayAccessOnListTest extends TestClass {

  function testNestedAccessToArraysWorks() {
    assertEquals( 0, foo() ) 
  }

  function foo() : Integer { 
    var x : List<Integer> = new ArrayList<java.lang.Integer>(){0, 0, 0, 0}
    return x[x[3]]
  }

}
