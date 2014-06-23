package gw.spec.core.expressions.memberaccess
uses gw.testharness.KnownBreak

@KnownBreak(:jira = "PL-16470", :targetBranch = "eng/emerald/pl/active/eclipse", :targetUser = "smckinney")
class MemberAccess_PureGosuTypeFromPureGosuTypeUnrelatedClassTest extends MemberAccessTestBase {

  function testPublicInstanceStringPropertyPureGosuTypeAccess() {
    assertEquals("Public-Instance-Property-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceStringPropertyPureGosuTypeAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyPureGosuTypeAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyPureGosuTypeAccessViaEval() {
    assertEquals("Public-Instance-Property-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyPureGosuTypeAccessViaBlock() {
    assertEquals("Public-Instance-Property-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyPureGosuTypeAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyPureGosuTypeAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyPureGosuTypeAccess() {
    assertEquals(12142, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceIntPropertyPureGosuTypeAccessViaBracketReflection() {
    assertEquals(12142, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyPureGosuTypeAccessViaExplicitReflection() {
    assertEquals(12142, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyPureGosuTypeAccessViaEval() {
    assertEquals(12142, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyPureGosuTypeAccessViaBlock() {
    assertEquals(12142, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyPureGosuTypeAccessViaEvalInBlock() {
    assertEquals(12142, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyPureGosuTypeAccessViaBlockInEval() {
    assertEquals(12142, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldPureGosuTypeAccess() {
    assertEquals("Public-Static-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticStringFieldPureGosuTypeAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldPureGosuTypeAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldPureGosuTypeAccessViaEval() {
    assertEquals("Public-Static-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringFieldPureGosuTypeAccessViaBlock() {
    assertEquals("Public-Static-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticStringFieldPureGosuTypeAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldPureGosuTypeAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldPureGosuTypeAccess() {
    assertEquals(11242, MemberAccess_UnrelatedClass.doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicStaticIntFieldPureGosuTypeAccessViaBracketReflection() {
    assertEquals(11242, MemberAccess_UnrelatedClass.doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldPureGosuTypeAccessViaExplicitReflection() {
    assertEquals(11242, MemberAccess_UnrelatedClass.doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldPureGosuTypeAccessViaEval() {
    assertEquals(11242, MemberAccess_UnrelatedClass.doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntFieldPureGosuTypeAccessViaBlock() {
    assertEquals(11242, MemberAccess_UnrelatedClass.doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntFieldPureGosuTypeAccessViaEvalInBlock() {
    assertEquals(11242, MemberAccess_UnrelatedClass.doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldPureGosuTypeAccessViaBlockInEval() {
    assertEquals(11242, MemberAccess_UnrelatedClass.doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldPureGosuTypeAccess() {
    assertEquals("Public-Instance-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceStringFieldPureGosuTypeAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldPureGosuTypeAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldPureGosuTypeAccessViaEval() {
    assertEquals("Public-Instance-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringFieldPureGosuTypeAccessViaBlock() {
    assertEquals("Public-Instance-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldPureGosuTypeAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldPureGosuTypeAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-PureGosuType", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldPureGosuTypeAccess() {
    assertEquals(12242, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccess())
  }

  function testPublicInstanceIntFieldPureGosuTypeAccessViaBracketReflection() {
    assertEquals(12242, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldPureGosuTypeAccessViaExplicitReflection() {
    assertEquals(12242, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldPureGosuTypeAccessViaEval() {
    assertEquals(12242, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntFieldPureGosuTypeAccessViaBlock() {
    assertEquals(12242, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldPureGosuTypeAccessViaEvalInBlock() {
    assertEquals(12242, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldPureGosuTypeAccessViaBlockInEval() {
    assertEquals(12242, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval())
  }

}