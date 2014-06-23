package gw.internal.gosu.parser.generics

uses gw.BaseVerifyErrantTest

class GenericsTest extends BaseVerifyErrantTest {
  function testErrant_() {
    processErrantType( Errant_AssignabilityAndComparability )
  }

  function testPl23787() {
    assertTrue( Pl23787.Type.Valid )
  }
}
