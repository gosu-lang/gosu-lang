package gw.specContrib.programs

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuProgram

class ConstructorChainingTest extends BaseVerifyErrantTest {

  function testMe() {
    var res = (TestConstructorChaining as IGosuProgram).evaluate( null )
    assertEquals( "hi", res )
  }

}