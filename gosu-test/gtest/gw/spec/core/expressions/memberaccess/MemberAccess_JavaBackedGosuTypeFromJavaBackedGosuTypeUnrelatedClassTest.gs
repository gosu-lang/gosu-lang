package gw.spec.core.expressions.memberaccess
uses gw.testharness.KnownBreak

@KnownBreak(:jira = "PL-16470", :targetBranch = "eng/emerald/pl/active/eclipse", :targetUser = "smckinney")
class MemberAccess_JavaBackedGosuTypeFromJavaBackedGosuTypeUnrelatedClassTest extends MemberAccessTestBase {

  function testPublicInstanceStringPropertyJavaBackedGosuTypeAccess() {
    assertEquals("Public-Instance-Property-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceStringPropertyJavaBackedGosuTypeAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyJavaBackedGosuTypeAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyJavaBackedGosuTypeAccessViaEval() {
    assertEquals("Public-Instance-Property-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyJavaBackedGosuTypeAccessViaBlock() {
    assertEquals("Public-Instance-Property-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyJavaBackedGosuTypeAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyJavaBackedGosuTypeAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyJavaBackedGosuTypeAccess() {
    assertEquals(12143, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceIntPropertyJavaBackedGosuTypeAccessViaBracketReflection() {
    assertEquals(12143, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyJavaBackedGosuTypeAccessViaExplicitReflection() {
    assertEquals(12143, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyJavaBackedGosuTypeAccessViaEval() {
    assertEquals(12143, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyJavaBackedGosuTypeAccessViaBlock() {
    assertEquals(12143, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyJavaBackedGosuTypeAccessViaEvalInBlock() {
    assertEquals(12143, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyJavaBackedGosuTypeAccessViaBlockInEval() {
    assertEquals(12143, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldJavaBackedGosuTypeAccess() {
    assertEquals("Public-Static-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticStringFieldJavaBackedGosuTypeAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldJavaBackedGosuTypeAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldJavaBackedGosuTypeAccessViaEval() {
    assertEquals("Public-Static-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringFieldJavaBackedGosuTypeAccessViaBlock() {
    assertEquals("Public-Static-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticStringFieldJavaBackedGosuTypeAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldJavaBackedGosuTypeAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldJavaBackedGosuTypeAccess() {
    assertEquals(11243, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticIntFieldJavaBackedGosuTypeAccessViaBracketReflection() {
    assertEquals(11243, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldJavaBackedGosuTypeAccessViaExplicitReflection() {
    assertEquals(11243, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldJavaBackedGosuTypeAccessViaEval() {
    assertEquals(11243, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntFieldJavaBackedGosuTypeAccessViaBlock() {
    assertEquals(11243, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntFieldJavaBackedGosuTypeAccessViaEvalInBlock() {
    assertEquals(11243, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldJavaBackedGosuTypeAccessViaBlockInEval() {
    assertEquals(11243, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldJavaBackedGosuTypeAccess() {
    assertEquals("Public-Instance-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceStringFieldJavaBackedGosuTypeAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldJavaBackedGosuTypeAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldJavaBackedGosuTypeAccessViaEval() {
    assertEquals("Public-Instance-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringFieldJavaBackedGosuTypeAccessViaBlock() {
    assertEquals("Public-Instance-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldJavaBackedGosuTypeAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldJavaBackedGosuTypeAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-JavaBackedGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldJavaBackedGosuTypeAccess() {
    assertEquals(12243, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceIntFieldJavaBackedGosuTypeAccessViaBracketReflection() {
    assertEquals(12243, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldJavaBackedGosuTypeAccessViaExplicitReflection() {
    assertEquals(12243, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldJavaBackedGosuTypeAccessViaEval() {
    assertEquals(12243, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntFieldJavaBackedGosuTypeAccessViaBlock() {
    assertEquals(12243, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldJavaBackedGosuTypeAccessViaEvalInBlock() {
    assertEquals(12243, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldJavaBackedGosuTypeAccessViaBlockInEval() {
    assertEquals(12243, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

}