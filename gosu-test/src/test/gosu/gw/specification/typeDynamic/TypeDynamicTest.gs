package gw.specification.typeDynamic

uses gw.BaseVerifyErrantTest
uses gw.internal.gosu.parser.expressions.UnsupportedNumberTypeException

class TypeDynamicTest extends BaseVerifyErrantTest {
  function testErrant_TypeDynamicTest() {
    processErrantType(Errant_TypeDynamicTest)
  }

  function testDynamicBasic() {
    var x : Dynamic = "Hello"
    assertEquals(String, typeof x)
    x = null
    assertEquals(void, typeof x)
  }
  
  static class A {
    function hello(): int {
      return 1
    }
    
    function hello(j : int ): int {
      return j
    }
  }
  
  static interface I<T> {}
  
  function testDynamicMethods() {
    var a : Dynamic = new A()
    assertEquals(1, a.hello())
    assertEquals(2, a.hello(2))
    var flag = false
    try { a.world() } 
    catch(e : IllegalStateException) {
      flag = true
    }
    assertTrue(flag)
  }
  
  function testDynamicOperators() {
    var x : Dynamic = 1
    assertEquals(3, x+2)
    x = new A()
    var flag = false
    var c = x[2]
    assertEquals(null, c)
    try { var i : int = x*2 } 
    catch(e : UnsupportedNumberTypeException) {
      flag = true
    }
    assertTrue(flag)
  }
  
  function testTypeis() {
    var x : Dynamic = "Hello"
    assertEquals(boolean, typeof (x typeis String))
    assertTrue(x typeis String)
  }
  
   function testAssignments() {
    var x : Dynamic = 1
    var i : int = x
    assertEquals(1, i)
    i += x
    assertEquals(2, i)
    x = new A()
    var flag = false
    try { i = x } 
    catch(e : ClassCastException) {
      flag = true
    }
    assertTrue(flag)
  }
   
  static class B {
    var f : Dynamic
    
    function m(a : Dynamic) : Dynamic {
      var l1 : Dynamic = 1
      var l2 : Dynamic[] = new Dynamic[2]
      var l3 : Dynamic[] = new Integer[2]
      var l4 : List<Dynamic> = new ArrayList<Integer>()
      var l5 : Dynamic = 1 as Dynamic
      return null
    }
  }

  //static class C extends Dynamic {}    //## issuekeys: MSG_CANNOT_EXTEND_FINAL_TYPE
  static class D<T extends Dynamic> {} 
  static class E implements I<Dynamic> {} 
  //static class F implements Dynamic {}   //## issuekeys: MSG_CLASS_CANNOT_IMPLEMENT_CLASS, MSG_CANNOT_EXTEND_FINAL_TYPE
  
  static class G {
    function m(o : Object) {}
    //function m(o : Dynamic) {}  //## issuekeys: MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD
  }

  function testObject() {
    assertNotSame(typeof Dynamic[], typeof Object[])
    assertNotSame(typeof List<Dynamic>, typeof List<Object>)
  }

  function ternaryLub() {
    var a = false
    var b = 3
    var c : dynamic.Dynamic = "bye"
    var x = a ? b : c
    x.blah( 0 ) // x has to be dynamic for this to compile
  }

  function listLub() {
    var a : dynamic.Dynamic
    var x = { a, 4}
    x[1].blah() // x's component type has to be dynamic for this to compile
  }
}
