package gw.specContrib.generics

uses gw.test.TestClass
uses java.lang.Enum
uses java.lang.Class

class GenericsContribEnumTest extends TestClass {

  function testEnums() {
    assertEquals( GosuEnum.HI, Enum.valueOf( GosuEnum, "HI") )
    assertEquals( JavaEnum.HI, Enum.valueOf( JavaEnum, "HI") )

    assertEquals( GosuEnum.HI, omg( GosuEnum, "HI" ) )
    assertEquals( JavaEnum.HI, omg( JavaEnum, "HI" ) )

    assertEquals( GosuEnum.HI, omg<GosuEnum>( GosuEnum, "HI" ) )
    assertEquals( JavaEnum.HI, omg<JavaEnum>( JavaEnum, "HI" ) )
  }

  static function omg<T extends Enum<T>>( c: Class<T>, s: String ) : T {
    return T.valueOf( c, s )
  }
}