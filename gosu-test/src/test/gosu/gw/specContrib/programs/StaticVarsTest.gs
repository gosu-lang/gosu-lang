package gw.specContrib.programs

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuProgram

class StaticVarsTest extends BaseVerifyErrantTest {

  function testMe() {
    var res = (TestStaticVars as IGosuProgram).evaluate( null )
    assertEquals( "88", res )
  }

}