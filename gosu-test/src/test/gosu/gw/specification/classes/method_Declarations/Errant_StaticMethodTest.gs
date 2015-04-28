package gw.specification.classes.method_Declarations

class Errant_StaticMethodTest {
  var f0 : int = m0()
  var f2 : int = m2()
  static var f1 : int = m0()
  static var f3 : int = m2()  //## issuekeys: MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT

  construct() {
    m0()
  }

  static function m0() : int {
    return 0
  }

  static function m1() {
    m0()
    testStaticAndNonStaticCalls()  //## issuekeys: MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT
    print(f0)  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    print(f1)
    m2()  //## issuekeys: MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT
  }

  function m2() : int {
    m0()
    m1()
    m2()
    testStaticAndNonStaticCalls()
    print(f0)
    print(f1)
    print(f2)
    print(f3)
    return 1
  }

  function testStaticAndNonStaticCalls() {
    new A().m()
    new A().sm()
    A.sm()
    A.m()  //## issuekeys: MSG_METHOD_IS_NOT_STATIC, MSG_METHOD_IS_NOT_STATIC
    m0()
    new Errant_StaticMethodTest().m0()
    Errant_StaticMethodTest.m0()
    m2()
  }
}
