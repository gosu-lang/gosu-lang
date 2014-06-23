package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuClassFromSubclassRootOnUnrelatedClassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_UnrelatedClass.doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_UnrelatedClass.doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_UnrelatedClass.doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_UnrelatedClass.doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_UnrelatedClass.doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_UnrelatedClass.doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_UnrelatedClass.doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyGosuClassAccess() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyGosuClassAccess() {
    assertEquals(2111, MemberAccess_UnrelatedClass.doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(2111, MemberAccess_UnrelatedClass.doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2111, MemberAccess_UnrelatedClass.doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2111, MemberAccess_UnrelatedClass.doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2111, MemberAccess_UnrelatedClass.doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_UnrelatedClass.doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_UnrelatedClass.doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_UnrelatedClass.doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_UnrelatedClass.doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_UnrelatedClass.doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccess() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccess() {
    assertEquals(2211, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(2211, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2211, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2211, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2211, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldGosuClassAccess() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldGosuClassAccess() {
    assertEquals(2121, MemberAccess_UnrelatedClass.doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(2121, MemberAccess_UnrelatedClass.doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(2121, MemberAccess_UnrelatedClass.doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2121, MemberAccess_UnrelatedClass.doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2121, MemberAccess_UnrelatedClass.doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccess() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccess() {
    assertEquals(2221, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(2221, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(2221, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2221, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2221, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval())
  }

}