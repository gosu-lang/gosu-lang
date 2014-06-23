package gw.spec.core.expressions.beanmethodcall.generated

class BeanMethodCall_JavaClassFromUnrelatedClassTest extends BeanMethodCallTestBase {

  function testPublicInstanceStringJavaClassAccess() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doPublicInstanceStringJavaClassUnrelatedClassAccess())
  }

  function testPublicInstanceStringJavaClassAccessViaEval() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doPublicInstanceStringJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doPublicInstanceStringJavaClassUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntJavaClassAccess() {
    assertEquals(125, BeanMethodCall_UnrelatedClass.doPublicInstanceIntJavaClassUnrelatedClassAccess())
  }

  function testPublicInstanceIntJavaClassAccessViaEval() {
    assertEquals(125, BeanMethodCall_UnrelatedClass.doPublicInstanceIntJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntJavaClassAccessViaBlock() {
    assertEquals(125, BeanMethodCall_UnrelatedClass.doPublicInstanceIntJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceStringJavaClassAccess() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doInternalInstanceStringJavaClassUnrelatedClassAccess())
  }

  function testInternalInstanceStringJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doInternalInstanceStringJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doInternalInstanceStringJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceIntJavaClassAccess() {
    assertEquals(225, BeanMethodCall_UnrelatedClass.doInternalInstanceIntJavaClassUnrelatedClassAccess())
  }

  function testInternalInstanceIntJavaClassAccessViaEval() {
    assertEquals(225, BeanMethodCall_UnrelatedClass.doInternalInstanceIntJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceIntJavaClassAccessViaBlock() {
    assertEquals(225, BeanMethodCall_UnrelatedClass.doInternalInstanceIntJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceStringJavaClassAccess() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doProtectedInstanceStringJavaClassUnrelatedClassAccess())
  }

  function testProtectedInstanceStringJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doProtectedInstanceStringJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_UnrelatedClass.doProtectedInstanceStringJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceIntJavaClassAccess() {
    assertEquals(325, BeanMethodCall_UnrelatedClass.doProtectedInstanceIntJavaClassUnrelatedClassAccess())
  }

  function testProtectedInstanceIntJavaClassAccessViaEval() {
    assertEquals(325, BeanMethodCall_UnrelatedClass.doProtectedInstanceIntJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceIntJavaClassAccessViaBlock() {
    assertEquals(325, BeanMethodCall_UnrelatedClass.doProtectedInstanceIntJavaClassUnrelatedClassAccessViaBlock())
  }

}