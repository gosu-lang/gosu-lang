package gw.specification.expressions.typeExpressions

uses gw.BaseVerifyErrantTest
uses java.util.*
uses java.lang.Runnable
uses gw.lang.reflect.IType

class TypeExpressionsTest extends BaseVerifyErrantTest {
  class A { function write() : int { return 8 } }
  class B extends A {}

  structure Writer {
    function write() : int
  }

  function testErrant_TypeExpressionsTest() {
    processErrantType(Errant_TypeExpressionsTest)
  }

  function testTypeisBasic() {
    var l : List
    var b : boolean

    l = new ArrayList()
    b = l typeis ArrayList
    assertTrue(b)

    l = new LinkedList()
    b = l typeis LinkedList
    assertTrue(b)

    var al : AbstractList
    al = new ArrayList()
    b = al typeis ArrayList
    assertTrue(b)

    al = new LinkedList()
    b = al typeis LinkedList
    assertTrue(b)

    l = new ArrayList()
    b = l typeis Map
    assertFalse(b)

    var o : Object
    o = new B()
    b = o typeis B
    assertTrue(b)
  }

  function testTypeofBasic() {
    var l : List
    var type : IType

    l = new ArrayList()
    type = typeof l
    assertEquals(type, ArrayList)

    l = new LinkedList()
    type = typeof l
    assertEquals(type, LinkedList)

    var al : AbstractList
    al = new ArrayList()
    type = typeof al
    assertEquals(type, ArrayList)

    al = new LinkedList()
    type = typeof al
    assertEquals(type, LinkedList)

    var o : Object
    o = new B()
    type = typeof o
    assertEquals(type, B)

    var i : int = 1
    type = typeof i
    assertEquals(type, int)

    o = \ ->  {}
    type = typeof o
    assertEquals(type.RelativeName, "block_0_")

    var w : Writer = new A()
    type = typeof w
    assertEquals(type, A)

    var d : dynamic.Dynamic = new A()
    type = typeof d
    assertEquals(type, A)

    var comp : Deque & List = new LinkedList()
    type = typeof comp
    assertEquals(type, LinkedList)
  }

  function testTypeisEvaluation() {
    var b : boolean
    var evaluated : boolean
    var b0 : block(x : int) : AbstractList
    b0 =  \ x  ->  {
      evaluated = true
      return x == 1 ? new ArrayList() : new LinkedList()
    }
    evaluated = false
    b = b0(1) typeis ArrayList
    assertTrue(evaluated)
    assertTrue(b)

    evaluated = false
    b = b0(2) typeis LinkedList
    assertTrue(evaluated)
    assertTrue(b)
  }

  function testTypeisWithNull() {
    var l : List
    var b : boolean

    l = null
    b = l typeis ArrayList
    assertFalse(b)
  }

  function testTypeOfWithNull() {
    var l : List
    var type : IType

    l = null
    type = typeof l
    assertEquals(type, void)

    type = typeof null
    assertEquals(type, void)
  }

  function testTypeisCompatibility() {
    var o : Object
    var b : boolean

    o = \ ->  {}
    b = o typeis Runnable
    assertFalse(b)
    b = o typeis block()
    assertTrue(b)

    o = new A()
    b = o typeis Writer
    assertTrue(b)
    b = o typeis dynamic.Dynamic
    assertTrue(b)

    var str : String = "123"
    b = str typeis dynamic.Dynamic
    assertTrue(b)

    o = new LinkedList()
    b = o typeis Deque & List
    assertTrue(b)
  }
}