package gw.spec.core.expressions.beanmethodcall.generated

class BeanMethodCall_JavaClassFromSubclassInnerClassTest extends BeanMethodCallTestBase {

  function testPublicInstanceStringJavaClassAccess() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doPublicInstanceStringJavaClassSubclassInnerClassAccess())
  }

  function testPublicInstanceStringJavaClassAccessViaEval() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doPublicInstanceStringJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doPublicInstanceStringJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntJavaClassAccess() {
    assertEquals(125, BeanMethodCall_JavaSubclass.SubclassInnerClass.doPublicInstanceIntJavaClassSubclassInnerClassAccess())
  }

  function testPublicInstanceIntJavaClassAccessViaEval() {
    assertEquals(125, BeanMethodCall_JavaSubclass.SubclassInnerClass.doPublicInstanceIntJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceIntJavaClassAccessViaBlock() {
    assertEquals(125, BeanMethodCall_JavaSubclass.SubclassInnerClass.doPublicInstanceIntJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringJavaClassAccess() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doInternalInstanceStringJavaClassSubclassInnerClassAccess())
  }

  function testInternalInstanceStringJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doInternalInstanceStringJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doInternalInstanceStringJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntJavaClassAccess() {
    assertEquals(225, BeanMethodCall_JavaSubclass.SubclassInnerClass.doInternalInstanceIntJavaClassSubclassInnerClassAccess())
  }

  function testInternalInstanceIntJavaClassAccessViaEval() {
    assertEquals(225, BeanMethodCall_JavaSubclass.SubclassInnerClass.doInternalInstanceIntJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceIntJavaClassAccessViaBlock() {
    assertEquals(225, BeanMethodCall_JavaSubclass.SubclassInnerClass.doInternalInstanceIntJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringJavaClassAccess() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringJavaClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceStringJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntJavaClassAccess() {
    assertEquals(325, BeanMethodCall_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntJavaClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceIntJavaClassAccessViaEval() {
    assertEquals(325, BeanMethodCall_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntJavaClassAccessViaBlock() {
    assertEquals(325, BeanMethodCall_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntJavaClassSubclassInnerClassAccessViaBlock())
  }

}