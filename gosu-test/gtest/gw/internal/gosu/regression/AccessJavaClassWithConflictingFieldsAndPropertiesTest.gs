package gw.internal.gosu.regression

uses gw.test.*

class AccessJavaClassWithConflictingFieldsAndPropertiesTest extends TestClass {

  function testCanReadJavaPropertyCorrectly() {
    var foo = new JavaClassWithPublicFieldAndMatchingPublicGetter()
    assertEquals( 43, foo.AField )
    foo.AField = 43
    assertEquals( 44, foo.AField )    
  }

}