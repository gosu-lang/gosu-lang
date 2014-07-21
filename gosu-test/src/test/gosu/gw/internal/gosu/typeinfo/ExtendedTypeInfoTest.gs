package gw.internal.gosu.typeinfo

uses gw.lang.reflect.TypeSystem
uses gw.test.TestClass

class ExtendedTypeInfoTest extends TestClass {

  function beforeTestClass() {
    ExtendedEntityAccess.install()
  }

  function afterTestClass() {
    ExtendedEntityAccess.uninstall()
  }

  function testTypeExtended() {

    var typeInfo = TypeSystem.getByFullName("gw.internal.gosu.typeinfo.ExtendedJavaClass")
    assertTrue("Java type info must be extended!", typeInfo typeis IExtraTypeData)

    var typeInfo2 = typeInfo as IExtraTypeData
    assertEquals("ExtraData", typeInfo2.ExtraData)

    var prop = typeInfo.TypeInfo.getProperty("Value")
    assertTrue("Property type info must be extended!", prop typeis IExtraPropertyData)

    var prop2 = prop as IExtraPropertyData
    assertEquals("ExtraPropertyData", prop2.ExtraPropertyData)
  }

}
