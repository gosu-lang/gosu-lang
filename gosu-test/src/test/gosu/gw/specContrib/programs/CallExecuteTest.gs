package gw.specContrib.programs

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuProgram

class CallExecuteTest extends BaseVerifyErrantTest {

  function testMe() {
    var res = MyGosuProgram.execute()
    assertEquals( "hihihihi", res )
  }

}