package gw.internal.gosu.regression

uses gw.test.TestClass

uses java.util.*
uses java.lang.*

class PL17495Test extends TestClass {

  function testRegression() {
    assertFalse( Errant_BadBlockReturnType.Type.Valid )
  }

}