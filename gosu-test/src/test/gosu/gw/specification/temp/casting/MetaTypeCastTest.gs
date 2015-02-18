package gw.specification.temp.casting

uses gw.BaseVerifyErrantTest
uses java.lang.Runnable

class MetaTypeCastTest extends BaseVerifyErrantTest {
  function testMe() {
    assertEquals( "gw.specification.temp.casting.MetaTypeCastTest.A", A.Type.DisplayName )
    assertEquals( "java.lang.Runnable", Runnable.Type.DisplayName )

    var a2 = A
    assertEquals( "gw.specification.temp.casting.MetaTypeCastTest.A", a2.Type.DisplayName )

    var b2 = a2 as Type<Runnable>
    assertEquals( "gw.specification.temp.casting.MetaTypeCastTest.A", b2.Type.DisplayName )

    var b3 = A as Type<Runnable>
    assertEquals( "gw.specification.temp.casting.MetaTypeCastTest.A", b3.Type.DisplayName )
  }


  static class A implements Runnable {
    override function run() {
    }
  }
}
