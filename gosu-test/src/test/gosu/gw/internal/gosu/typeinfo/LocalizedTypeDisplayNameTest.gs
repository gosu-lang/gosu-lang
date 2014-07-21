package gw.internal.gosu.typeinfo

uses gw.lang.reflect.TypeSystem
uses gw.test.TestClass

class LocalizedTypeDisplayNameTest extends TestClass {

  function beforeTestClass() {
    LocalizingEntityAccess.install()
  }

  function afterTestClass() {
    LocalizingEntityAccess.uninstall()
  }

  function testTypeExtended() {

    var type = TypeSystem.getByFullName("gw.internal.gosu.typeinfo.ExtendedJavaClass")
    assertEquals("GW.INTERNAL.GOSU.TYPEINFO.EXTENDEDJAVACLASS", type.DisplayName)
    assertEquals("extendedjavaclass", type.TypeInfo.DisplayName)
  }

}
