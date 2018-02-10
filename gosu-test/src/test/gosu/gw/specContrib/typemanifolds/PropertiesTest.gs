package gw.specContrib.typemanifolds

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuProgram

class PropertiesTest extends BaseVerifyErrantTest {

  function testMe() {
    assertEquals( "My value", MyProperties.MyProperty.toString() )
    assertEquals( "1st", MyProperties.MyProperty.First )
    assertEquals( "2nd", MyProperties.MyProperty.Second )
    assertEquals( "10", MyProperties.MyProperty.Tens.Ten )
    assertEquals( "11", MyProperties.MyProperty.Tens.Eleven )
    assertEquals( "other property", MyProperties.Other )
  }

}