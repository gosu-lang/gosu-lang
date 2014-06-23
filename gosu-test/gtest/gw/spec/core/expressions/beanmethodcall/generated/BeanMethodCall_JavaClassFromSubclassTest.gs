package gw.spec.core.expressions.beanmethodcall.generated

class BeanMethodCall_JavaClassFromSubclassTest extends BeanMethodCallTestBase {

  function testPublicInstanceStringJavaClassAccess() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaSubclass.doPublicInstanceStringJavaClassSubclassAccess())
  }

  function testPublicInstanceStringJavaClassAccessViaEval() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaSubclass.doPublicInstanceStringJavaClassSubclassAccessViaEval())
  }

  function testPublicInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaSubclass.doPublicInstanceStringJavaClassSubclassAccessViaBlock())
  }

  function testPublicInstanceIntJavaClassAccess() {
    assertEquals(125, BeanMethodCall_JavaSubclass.doPublicInstanceIntJavaClassSubclassAccess())
  }

  function testPublicInstanceIntJavaClassAccessViaEval() {
    assertEquals(125, BeanMethodCall_JavaSubclass.doPublicInstanceIntJavaClassSubclassAccessViaEval())
  }

  function testPublicInstanceIntJavaClassAccessViaBlock() {
    assertEquals(125, BeanMethodCall_JavaSubclass.doPublicInstanceIntJavaClassSubclassAccessViaBlock())
  }

  function testInternalInstanceStringJavaClassAccess() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_JavaSubclass.doInternalInstanceStringJavaClassSubclassAccess())
  }

  function testInternalInstanceStringJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_JavaSubclass.doInternalInstanceStringJavaClassSubclassAccessViaEval())
  }

  function testInternalInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_JavaSubclass.doInternalInstanceStringJavaClassSubclassAccessViaBlock())
  }

  function testInternalInstanceIntJavaClassAccess() {
    assertEquals(225, BeanMethodCall_JavaSubclass.doInternalInstanceIntJavaClassSubclassAccess())
  }

  function testInternalInstanceIntJavaClassAccessViaEval() {
    assertEquals(225, BeanMethodCall_JavaSubclass.doInternalInstanceIntJavaClassSubclassAccessViaEval())
  }

  function testInternalInstanceIntJavaClassAccessViaBlock() {
    assertEquals(225, BeanMethodCall_JavaSubclass.doInternalInstanceIntJavaClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceStringJavaClassAccess() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaSubclass.doProtectedInstanceStringJavaClassSubclassAccess())
  }

  function testProtectedInstanceStringJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaSubclass.doProtectedInstanceStringJavaClassSubclassAccessViaEval())
  }

  function testProtectedInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaSubclass.doProtectedInstanceStringJavaClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceIntJavaClassAccess() {
    assertEquals(325, BeanMethodCall_JavaSubclass.doProtectedInstanceIntJavaClassSubclassAccess())
  }

  function testProtectedInstanceIntJavaClassAccessViaEval() {
    assertEquals(325, BeanMethodCall_JavaSubclass.doProtectedInstanceIntJavaClassSubclassAccessViaEval())
  }

  function testProtectedInstanceIntJavaClassAccessViaBlock() {
    assertEquals(325, BeanMethodCall_JavaSubclass.doProtectedInstanceIntJavaClassSubclassAccessViaBlock())
  }

}