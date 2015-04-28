package gw.specification.expressions.typeExpressions

uses java.lang.Integer
uses java.util.*
uses java.lang.Runnable
uses java.lang.CharSequence
uses gw.lang.reflect.IType

class Errant_TypeExpressionsTest {
  class A { function write() : int { return 8 } }
  class B extends A {}

  structure Writer {
    function write() : int
  }

  function testTypeisBasic() {
    var l : List
    var b : boolean

    l = new ArrayList()
    b = l typeis ArrayList

    l = new LinkedList()
    b = l typeis LinkedList

    var al : AbstractList
    al = new ArrayList()
    b = al typeis ArrayList

    al = new LinkedList()
    b = al typeis LinkedList

    l = new ArrayList()
    b = l typeis Map

    var o : Object
    o = new B()
    b = o typeis B
    b = l typeis new ArrayList()  //## issuekeys: MSG_EXPECTING_TYPE_NAME
    b = l typeis 10  //## issuekeys: MSG_EXPECTING_TYPE_NAME, MSG_UNEXPECTED_TOKEN
    b = 10 typeis ArrayList  //## issuekeys: MSG_PRIMITIVES_NOT_ALLOWED_HERE, MSG_INCONVERTIBLE_TYPES

    var c: String & Comparator
    b = c typeis Integer  //## issuekeys: MSG_INCONVERTIBLE_TYPES
    b = c typeis String
    b = c typeis CharSequence

    var i: Integer
    b = i typeis String & Comparator  //## issuekeys: MSG_INCONVERTIBLE_TYPES
  }

  function testTypeofBasic() {
    var l : List
    var type : IType

    l = new ArrayList()
    type = typeof l

    l = new LinkedList()
    type = typeof l

    var al : AbstractList
    al = new ArrayList()
    type = typeof al

    al = new LinkedList()
    type = typeof al

    var o : Object
    o = new B()
    type = typeof o

    var i : int = 1
    type = typeof i

    o = \ ->  {}
    type = typeof o

    var w : Writer = new A()
    type = typeof w

    var d : dynamic.Dynamic = new A()
    type = typeof d

    var comp : Deque & List = new LinkedList()
    type = typeof comp
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

    evaluated = false
    b = b0(2) typeis LinkedList
  }

  function testTypeisWithNull() {
    var l : List
    var b : boolean

    l = null
    b = l typeis ArrayList

    b = null typeis ArrayList  //## issuekeys: MSG_PRIMITIVES_NOT_ALLOWED_HERE, MSG_INCONVERTIBLE_TYPES
  }


  function testTypeOfWithNull() {
    var l : List
    var type : IType

    l = null
    type = typeof l

    type = typeof null
  }

  function testTypeisCompatibility() {
    var o : Object
    var b : boolean

    o = \ ->  {}
    b = o typeis Runnable
    b = o typeis block()

    o = new A()
    b = o typeis Writer
    b = o typeis dynamic.Dynamic

    var str : String = "123"
    b = str typeis dynamic.Dynamic

    o = new LinkedList()
    b = o typeis Deque & List
  }
}