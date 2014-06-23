package gw.spec.core.expressions.beanmethodcall.generated
uses gw.spec.core.expressions.beanmethodcall.generated.other.BeanMethodCall_JavaClassSubclassInOtherPackage

class BeanMethodCall_JavaClassFromSubclassInOtherPackageTest extends BeanMethodCallTestBase {

  function testPublicInstanceStringJavaClassAccess() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaClassSubclassInOtherPackage.doPublicInstanceStringJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceStringJavaClassAccessViaEval() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaClassSubclassInOtherPackage.doPublicInstanceStringJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-JavaClass", BeanMethodCall_JavaClassSubclassInOtherPackage.doPublicInstanceStringJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceIntJavaClassAccess() {
    assertEquals(125, BeanMethodCall_JavaClassSubclassInOtherPackage.doPublicInstanceIntJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceIntJavaClassAccessViaEval() {
    assertEquals(125, BeanMethodCall_JavaClassSubclassInOtherPackage.doPublicInstanceIntJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceIntJavaClassAccessViaBlock() {
    assertEquals(125, BeanMethodCall_JavaClassSubclassInOtherPackage.doPublicInstanceIntJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceStringJavaClassAccess() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaClassSubclassInOtherPackage.doProtectedInstanceStringJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceStringJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaClassSubclassInOtherPackage.doProtectedInstanceStringJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceStringJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-JavaClass", BeanMethodCall_JavaClassSubclassInOtherPackage.doProtectedInstanceStringJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceIntJavaClassAccess() {
    assertEquals(325, BeanMethodCall_JavaClassSubclassInOtherPackage.doProtectedInstanceIntJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceIntJavaClassAccessViaEval() {
    assertEquals(325, BeanMethodCall_JavaClassSubclassInOtherPackage.doProtectedInstanceIntJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceIntJavaClassAccessViaBlock() {
    assertEquals(325, BeanMethodCall_JavaClassSubclassInOtherPackage.doProtectedInstanceIntJavaClassSubclassInOtherPackageAccessViaBlock())
  }

}