package gw.internal.gosu.regression
uses gw.test.TestClass
uses java.awt.Point

class JavaReadonlyProperty_FieldConflictTest extends TestClass {

  function testCanWriteToJavaPropertyBackedByGetterAndField() {
    var pt = new Point()
    pt.x = 8 as int
    assertEquals( 8.0, pt.X, 0.01 )
  }
}
