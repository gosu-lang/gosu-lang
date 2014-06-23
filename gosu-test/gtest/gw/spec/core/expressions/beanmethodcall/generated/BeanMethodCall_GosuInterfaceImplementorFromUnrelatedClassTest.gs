package gw.spec.core.expressions.beanmethodcall.generated

class BeanMethodCall_GosuInterfaceImplementorFromUnrelatedClassTest extends BeanMethodCallTestBase {

  function testPublicInstanceStringGosuInterfaceAccess() {
    assertEquals("Public-Instance-GosuInterface", BeanMethodCall_UnrelatedClass.doPublicInstanceStringGosuInterfaceUnrelatedClassAccess())
  }

  function testPublicInstanceStringGosuInterfaceAccessViaEval() {
    assertEquals("Public-Instance-GosuInterface", BeanMethodCall_UnrelatedClass.doPublicInstanceStringGosuInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringGosuInterfaceAccessViaBlock() {
    assertEquals("Public-Instance-GosuInterface", BeanMethodCall_UnrelatedClass.doPublicInstanceStringGosuInterfaceUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntGosuInterfaceAccess() {
    assertEquals(126, BeanMethodCall_UnrelatedClass.doPublicInstanceIntGosuInterfaceUnrelatedClassAccess())
  }

  function testPublicInstanceIntGosuInterfaceAccessViaEval() {
    assertEquals(126, BeanMethodCall_UnrelatedClass.doPublicInstanceIntGosuInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntGosuInterfaceAccessViaBlock() {
    assertEquals(126, BeanMethodCall_UnrelatedClass.doPublicInstanceIntGosuInterfaceUnrelatedClassAccessViaBlock())
  }

}