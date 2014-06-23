package gw.specification.classes.method_Declarations

uses gw.BaseVerifyErrantTest
uses java.util.ArrayList
uses java.lang.Integer

class MethodDeclarationTest extends BaseVerifyErrantTest {
  function testErrant_MethodDeclarationTest() {
    processErrantType(Errant_MethodDeclarationTest)
  }

  function m25(i: int, j : int = 8) : ArrayList<Integer> {return {i, j} }
  function m27(i: int, j : int = 8, k : int = 7) : ArrayList<Integer> {return {i, j, k} }
  function m28(i: int = 8, j : int = 7) : ArrayList<Integer> {return {i, j} }

  function testDefaultMethodCalls() {
    assertTrue(m25(1, 2) == {1,2})
    assertTrue(m25(1) == {1, 8})
    assertTrue(m27(1, 2, 3) == {1, 2, 3})
    assertTrue(m27(1, 2) == {1, 2, 7})
    assertTrue(m27(1) == {1, 8, 7})
    assertTrue(m28() == {8, 7})
    assertTrue(m28(1) == {1, 7})
    assertTrue(m28(1, 2) == {1, 2})
  }

  function testErrant_MethodModifiersTest() {
    processErrantType(Errant_MethodModifiersTest)
  }

  function testErrant_StaticMethodTest() {
    processErrantType(Errant_StaticMethodTest)
  }

  function testErrant_FinalMethodTest() {
    processErrantType(Errant_FinalMethodTest)
  }

  function testErrant_AbstractMethodTest() {
    processErrantType(Errant_AbstractMethodTest)
  }
}
