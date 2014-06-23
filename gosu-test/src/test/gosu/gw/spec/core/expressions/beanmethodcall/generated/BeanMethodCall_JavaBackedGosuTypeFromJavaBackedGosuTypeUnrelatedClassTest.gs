package gw.spec.core.expressions.beanmethodcall.generated
uses gw.testharness.KnownBreak

@KnownBreak(:jira = "PL-16470", :targetBranch = "eng/emerald/pl/active/eclipse", :targetUser = "smckinney")
class BeanMethodCall_JavaBackedGosuTypeFromJavaBackedGosuTypeUnrelatedClassTest extends BeanMethodCallTestBase {

  function testPublicStaticStringJavaBackedGosuTypeAccess() {
    assertEquals("Public-Static-JavaBackedGosuType", BeanMethodCall_UnrelatedClass.doPublicStaticStringJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticStringJavaBackedGosuTypeAccessViaEval() {
    assertEquals("Public-Static-JavaBackedGosuType", BeanMethodCall_UnrelatedClass.doPublicStaticStringJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringJavaBackedGosuTypeAccessViaBlock() {
    assertEquals("Public-Static-JavaBackedGosuType", BeanMethodCall_UnrelatedClass.doPublicStaticStringJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntJavaBackedGosuTypeAccess() {
    assertEquals(1143, BeanMethodCall_UnrelatedClass.doPublicStaticIntJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticIntJavaBackedGosuTypeAccessViaEval() {
    assertEquals(1143, BeanMethodCall_UnrelatedClass.doPublicStaticIntJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntJavaBackedGosuTypeAccessViaBlock() {
    assertEquals(1143, BeanMethodCall_UnrelatedClass.doPublicStaticIntJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringJavaBackedGosuTypeAccess() {
    assertEquals("Public-Instance-JavaBackedGosuType", BeanMethodCall_UnrelatedClass.doPublicInstanceStringJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceStringJavaBackedGosuTypeAccessViaEval() {
    assertEquals("Public-Instance-JavaBackedGosuType", BeanMethodCall_UnrelatedClass.doPublicInstanceStringJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringJavaBackedGosuTypeAccessViaBlock() {
    assertEquals("Public-Instance-JavaBackedGosuType", BeanMethodCall_UnrelatedClass.doPublicInstanceStringJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntJavaBackedGosuTypeAccess() {
    assertEquals(1243, BeanMethodCall_UnrelatedClass.doPublicInstanceIntJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceIntJavaBackedGosuTypeAccessViaEval() {
    assertEquals(1243, BeanMethodCall_UnrelatedClass.doPublicInstanceIntJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntJavaBackedGosuTypeAccessViaBlock() {
    assertEquals(1243, BeanMethodCall_UnrelatedClass.doPublicInstanceIntJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

}