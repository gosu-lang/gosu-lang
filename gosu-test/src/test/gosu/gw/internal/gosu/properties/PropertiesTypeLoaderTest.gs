package gw.internal.gosu.properties
uses java.lang.System
uses gw.test.TestClass
uses gw.lang.SystemProperties

// FIXME: (PL-22163) does not verify with non-open source Gosu (gw.internal.gosu.properties is not available)
@gw.testharness.DoNotVerifyResource
class PropertiesTypeLoaderTest extends TestClass {

  function testSimpleLeafSystemProperty() {
    var prop = SystemProperties.path.separator
    assertEquals(String, typeof prop)
    assertEquals(System.getProperty("path.separator"), prop)
  }

  function testSimpleIntermediateSystemProperty() {
    var prop = SystemProperties.java
    assertNotNull(prop.version)
    assertEquals(System.getProperty("java.version"), prop.version)
    assertEquals(System.getProperty("java.version"), prop.getValueByName("version"))
  }

  function testIntermediateSystemPropertyWithValue() {
    var prop = SystemProperties.java.vendor
    assertEquals(System.getProperty("java.vendor"), prop.getValue())
    assertEquals(System.getProperty("java.vendor"), prop as String)
    assertNotNull(prop.url)
    assertEquals(System.getProperty("java.vendor.url"), prop.url)
    assertEquals(System.getProperty("java.vendor.url"), prop.getValueByName("url"))
  }

  function testSimpleFileProperty() {
    var prop = gw.internal.gosu.properties.Test.a.b.c
    assertEquals(String, typeof prop)
    assertEquals("a b c", prop)
  }
  
  function testSimpleIntermediateFileProperty() {
    var prop = gw.internal.gosu.properties.Test.a
    assertNotNull(prop)
    assertEquals("a b", prop.getValueByName("b"))
    assertEquals("a b c", prop.getValueByName("b.c"))
  }

  function testIntermediateFilePropertyWithValue() {
    var prop = gw.internal.gosu.properties.Test.a.b
    assertEquals("a b", prop.getValue())
    assertEquals("a b", prop as String)
    assertEquals("a b c", prop.c)
    assertEquals("a b c", prop.getValueByName("c"))
  }

  function testHiddenFileProperties() {
    assertEquals("dollar dollar", gw.internal.gosu.properties.Test.getValueByName("$$"))
    assertEquals("a dollar b", gw.internal.gosu.properties.Test.getValueByName("a.$b"))
    assertEquals("a dollar b", gw.internal.gosu.properties.Test.a.getValueByName("$b"))
  }
  
  function testPropertiesThatClashWithTypeName() {
    assertEquals("x y", gw.internal.gosu.properties.Test.x.y)
    assertEquals("x Type", gw.internal.gosu.properties.Test.x.Type)
  }

}
