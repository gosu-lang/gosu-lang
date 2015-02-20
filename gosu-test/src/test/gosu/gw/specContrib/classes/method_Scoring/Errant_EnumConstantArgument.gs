package gw.specContrib.classes.method_Scoring

class Errant_EnumConstantArgument {
  enum MyEnum1 { CONSTANT }
  enum MyEnum2 { CONSTANT }

  class X {}
  class Y {}

  function fun1(p: MyEnum1): X { return null }
  function fun1(p: MyEnum2): Y { return null }

  function fun2(p: MyEnum1, i: int): X { return null }
  function fun2(p: MyEnum2, d: double): Y { return null }

  function test() {
    // IDE-1778
    fun1(CONSTANT)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION

    var x111: X = fun2(CONSTANT, 1)
  }
}
