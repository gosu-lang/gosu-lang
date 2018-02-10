package gw.specContrib.typemanifolds

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuProgram

class ImageTest extends BaseVerifyErrantTest {

  function testMe() {
    var benis = benis_png.get()
    assertEquals( benis.IconWidth, 32 )
  }

}