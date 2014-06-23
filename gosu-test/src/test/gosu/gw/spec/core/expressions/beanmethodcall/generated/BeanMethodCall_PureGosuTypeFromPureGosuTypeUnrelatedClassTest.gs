package gw.spec.core.expressions.beanmethodcall.generated
uses gw.testharness.KnownBreak

@KnownBreak(:jira = "PL-16470", :targetBranch = "eng/emerald/pl/active/eclipse", :targetUser = "smckinney")
class BeanMethodCall_PureGosuTypeFromPureGosuTypeUnrelatedClassTest extends BeanMethodCallTestBase {

  function testPublicStaticStringPureGosuTypeAccess() {
    assertEquals("Public-Static-PureGosuType", BeanMethodCall_UnrelatedClass.doPublicStaticStringPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticStringPureGosuTypeAccessViaEval() {
    assertEquals("Public-Static-PureGosuType", BeanMethodCall_UnrelatedClass.doPublicStaticStringPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringPureGosuTypeAccessViaBlock() {
    assertEquals("Public-Static-PureGosuType", BeanMethodCall_UnrelatedClass.doPublicStaticStringPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntPureGosuTypeAccess() {
    assertEquals(1142, BeanMethodCall_UnrelatedClass.doPublicStaticIntPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticIntPureGosuTypeAccessViaEval() {
    assertEquals(1142, BeanMethodCall_UnrelatedClass.doPublicStaticIntPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntPureGosuTypeAccessViaBlock() {
    assertEquals(1142, BeanMethodCall_UnrelatedClass.doPublicStaticIntPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringPureGosuTypeAccess() {
    assertEquals("Public-Instance-PureGosuType", BeanMethodCall_UnrelatedClass.doPublicInstanceStringPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceStringPureGosuTypeAccessViaEval() {
    assertEquals("Public-Instance-PureGosuType", BeanMethodCall_UnrelatedClass.doPublicInstanceStringPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringPureGosuTypeAccessViaBlock() {
    assertEquals("Public-Instance-PureGosuType", BeanMethodCall_UnrelatedClass.doPublicInstanceStringPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntPureGosuTypeAccess() {
    assertEquals(1242, BeanMethodCall_UnrelatedClass.doPublicInstanceIntPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceIntPureGosuTypeAccessViaEval() {
    assertEquals(1242, BeanMethodCall_UnrelatedClass.doPublicInstanceIntPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntPureGosuTypeAccessViaBlock() {
    assertEquals(1242, BeanMethodCall_UnrelatedClass.doPublicInstanceIntPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

}