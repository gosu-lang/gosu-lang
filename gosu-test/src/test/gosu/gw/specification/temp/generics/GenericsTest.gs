package gw.specification.temp.generics

uses gw.BaseVerifyErrantTest

class GenericsTest extends BaseVerifyErrantTest {
  function testRecursiveType() {
    assertTrue( gw.specification.temp.generics.AssAssert.Valid )
  }

  function testIDE_572() {
    assertTrue( IDE_572.Valid )
  }
}
