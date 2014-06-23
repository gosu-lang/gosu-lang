package gw.spec.core.expressions.memberaccess

class MemberAccess_JavaClassFromUnrelatedClassTest extends MemberAccessTestBase {

  function testPublicInstanceStringPropertyJavaClassAccess() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaClassUnrelatedClassAccess())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccess() {
    assertEquals(1215, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaClassUnrelatedClassAccess())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBracketReflection() {
    assertEquals(1215, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals(1215, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(1215, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(1215, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(1215, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(1215, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyJavaClassAccess() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyJavaClassUnrelatedClassAccess())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyJavaClassAccess() {
    assertEquals(2215, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyJavaClassUnrelatedClassAccess())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(2215, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(2215, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(2215, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(2215, MemberAccess_UnrelatedClass.doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccess() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccess())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccess() {
    assertEquals(3215, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccess())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(3215, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(3215, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(3215, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(3215, MemberAccess_UnrelatedClass.doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldJavaClassAccess() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaClassUnrelatedClassAccess())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldJavaClassAccess() {
    assertEquals(1125, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaClassUnrelatedClassAccess())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1125, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1125, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(1125, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(1125, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1125, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1125, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldJavaClassAccess() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldJavaClassUnrelatedClassAccess())
  }

  function testInternalStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldJavaClassAccess() {
    assertEquals(2125, MemberAccess_UnrelatedClass.doInternalStaticIntFieldJavaClassUnrelatedClassAccess())
  }

  function testInternalStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(2125, MemberAccess_UnrelatedClass.doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(2125, MemberAccess_UnrelatedClass.doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(2125, MemberAccess_UnrelatedClass.doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(2125, MemberAccess_UnrelatedClass.doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldJavaClassAccess() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldJavaClassUnrelatedClassAccess())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldJavaClassAccess() {
    assertEquals(3125, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldJavaClassUnrelatedClassAccess())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(3125, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(3125, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3125, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3125, MemberAccess_UnrelatedClass.doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldJavaClassAccess() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaClassUnrelatedClassAccess())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldJavaClassAccess() {
    assertEquals(1225, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaClassUnrelatedClassAccess())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1225, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1225, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(1225, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(1225, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1225, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1225, MemberAccess_UnrelatedClass.doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldJavaClassAccess() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldJavaClassUnrelatedClassAccess())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldJavaClassAccess() {
    assertEquals(2225, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldJavaClassUnrelatedClassAccess())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(2225, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(2225, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(2225, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(2225, MemberAccess_UnrelatedClass.doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccess() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldJavaClassUnrelatedClassAccess())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_UnrelatedClass.doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccess() {
    assertEquals(3225, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldJavaClassUnrelatedClassAccess())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(3225, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(3225, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3225, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3225, MemberAccess_UnrelatedClass.doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaBlockInEval())
  }

}