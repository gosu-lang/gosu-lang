package gw.spec.core.expressions.memberaccess

class MemberAccess_JavaClassFromSubclassTest extends MemberAccessTestBase {

  function testPublicInstanceStringPropertyJavaClassAccess() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringPropertyJavaClassSubclassAccess())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringPropertyJavaClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringPropertyJavaClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringPropertyJavaClassSubclassAccessViaEval())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringPropertyJavaClassSubclassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringPropertyJavaClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringPropertyJavaClassSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccess() {
    assertEquals(1215, MemberAccess_JavaSubclass.doPublicInstanceIntPropertyJavaClassSubclassAccess())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBracketReflection() {
    assertEquals(1215, MemberAccess_JavaSubclass.doPublicInstanceIntPropertyJavaClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals(1215, MemberAccess_JavaSubclass.doPublicInstanceIntPropertyJavaClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(1215, MemberAccess_JavaSubclass.doPublicInstanceIntPropertyJavaClassSubclassAccessViaEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(1215, MemberAccess_JavaSubclass.doPublicInstanceIntPropertyJavaClassSubclassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(1215, MemberAccess_JavaSubclass.doPublicInstanceIntPropertyJavaClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(1215, MemberAccess_JavaSubclass.doPublicInstanceIntPropertyJavaClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyJavaClassAccess() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringPropertyJavaClassSubclassAccess())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringPropertyJavaClassSubclassAccessViaEval())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringPropertyJavaClassSubclassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringPropertyJavaClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringPropertyJavaClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyJavaClassAccess() {
    assertEquals(2215, MemberAccess_JavaSubclass.doInternalInstanceIntPropertyJavaClassSubclassAccess())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(2215, MemberAccess_JavaSubclass.doInternalInstanceIntPropertyJavaClassSubclassAccessViaEval())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(2215, MemberAccess_JavaSubclass.doInternalInstanceIntPropertyJavaClassSubclassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(2215, MemberAccess_JavaSubclass.doInternalInstanceIntPropertyJavaClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(2215, MemberAccess_JavaSubclass.doInternalInstanceIntPropertyJavaClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccess() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringPropertyJavaClassSubclassAccess())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringPropertyJavaClassSubclassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringPropertyJavaClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringPropertyJavaClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringPropertyJavaClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccess() {
    assertEquals(3215, MemberAccess_JavaSubclass.doProtectedInstanceIntPropertyJavaClassSubclassAccess())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(3215, MemberAccess_JavaSubclass.doProtectedInstanceIntPropertyJavaClassSubclassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(3215, MemberAccess_JavaSubclass.doProtectedInstanceIntPropertyJavaClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(3215, MemberAccess_JavaSubclass.doProtectedInstanceIntPropertyJavaClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(3215, MemberAccess_JavaSubclass.doProtectedInstanceIntPropertyJavaClassSubclassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldJavaClassAccess() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.doPublicStaticStringFieldJavaClassSubclassAccess())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.doPublicStaticStringFieldJavaClassSubclassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.doPublicStaticStringFieldJavaClassSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.doPublicStaticStringFieldJavaClassSubclassAccessViaEval())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.doPublicStaticStringFieldJavaClassSubclassAccessViaBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.doPublicStaticStringFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.doPublicStaticStringFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldJavaClassAccess() {
    assertEquals(1125, MemberAccess_JavaSubclass.doPublicStaticIntFieldJavaClassSubclassAccess())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1125, MemberAccess_JavaSubclass.doPublicStaticIntFieldJavaClassSubclassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1125, MemberAccess_JavaSubclass.doPublicStaticIntFieldJavaClassSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(1125, MemberAccess_JavaSubclass.doPublicStaticIntFieldJavaClassSubclassAccessViaEval())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(1125, MemberAccess_JavaSubclass.doPublicStaticIntFieldJavaClassSubclassAccessViaBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1125, MemberAccess_JavaSubclass.doPublicStaticIntFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1125, MemberAccess_JavaSubclass.doPublicStaticIntFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldJavaClassAccess() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.doInternalStaticStringFieldJavaClassSubclassAccess())
  }

  function testInternalStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.doInternalStaticStringFieldJavaClassSubclassAccessViaEval())
  }

  function testInternalStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.doInternalStaticStringFieldJavaClassSubclassAccessViaBlock())
  }

  function testInternalStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.doInternalStaticStringFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.doInternalStaticStringFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldJavaClassAccess() {
    assertEquals(2125, MemberAccess_JavaSubclass.doInternalStaticIntFieldJavaClassSubclassAccess())
  }

  function testInternalStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(2125, MemberAccess_JavaSubclass.doInternalStaticIntFieldJavaClassSubclassAccessViaEval())
  }

  function testInternalStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(2125, MemberAccess_JavaSubclass.doInternalStaticIntFieldJavaClassSubclassAccessViaBlock())
  }

  function testInternalStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(2125, MemberAccess_JavaSubclass.doInternalStaticIntFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(2125, MemberAccess_JavaSubclass.doInternalStaticIntFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldJavaClassAccess() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedStaticStringFieldJavaClassSubclassAccess())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedStaticStringFieldJavaClassSubclassAccessViaEval())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedStaticStringFieldJavaClassSubclassAccessViaBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedStaticStringFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedStaticStringFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldJavaClassAccess() {
    assertEquals(3125, MemberAccess_JavaSubclass.doProtectedStaticIntFieldJavaClassSubclassAccess())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(3125, MemberAccess_JavaSubclass.doProtectedStaticIntFieldJavaClassSubclassAccessViaEval())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(3125, MemberAccess_JavaSubclass.doProtectedStaticIntFieldJavaClassSubclassAccessViaBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3125, MemberAccess_JavaSubclass.doProtectedStaticIntFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3125, MemberAccess_JavaSubclass.doProtectedStaticIntFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldJavaClassAccess() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringFieldJavaClassSubclassAccess())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringFieldJavaClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringFieldJavaClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringFieldJavaClassSubclassAccessViaEval())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringFieldJavaClassSubclassAccessViaBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doPublicInstanceStringFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldJavaClassAccess() {
    assertEquals(1225, MemberAccess_JavaSubclass.doPublicInstanceIntFieldJavaClassSubclassAccess())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1225, MemberAccess_JavaSubclass.doPublicInstanceIntFieldJavaClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1225, MemberAccess_JavaSubclass.doPublicInstanceIntFieldJavaClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(1225, MemberAccess_JavaSubclass.doPublicInstanceIntFieldJavaClassSubclassAccessViaEval())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(1225, MemberAccess_JavaSubclass.doPublicInstanceIntFieldJavaClassSubclassAccessViaBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1225, MemberAccess_JavaSubclass.doPublicInstanceIntFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1225, MemberAccess_JavaSubclass.doPublicInstanceIntFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldJavaClassAccess() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringFieldJavaClassSubclassAccess())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringFieldJavaClassSubclassAccessViaEval())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringFieldJavaClassSubclassAccessViaBlock())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doInternalInstanceStringFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldJavaClassAccess() {
    assertEquals(2225, MemberAccess_JavaSubclass.doInternalInstanceIntFieldJavaClassSubclassAccess())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(2225, MemberAccess_JavaSubclass.doInternalInstanceIntFieldJavaClassSubclassAccessViaEval())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(2225, MemberAccess_JavaSubclass.doInternalInstanceIntFieldJavaClassSubclassAccessViaBlock())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(2225, MemberAccess_JavaSubclass.doInternalInstanceIntFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(2225, MemberAccess_JavaSubclass.doInternalInstanceIntFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccess() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringFieldJavaClassSubclassAccess())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringFieldJavaClassSubclassAccessViaEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringFieldJavaClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.doProtectedInstanceStringFieldJavaClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccess() {
    assertEquals(3225, MemberAccess_JavaSubclass.doProtectedInstanceIntFieldJavaClassSubclassAccess())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(3225, MemberAccess_JavaSubclass.doProtectedInstanceIntFieldJavaClassSubclassAccessViaEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(3225, MemberAccess_JavaSubclass.doProtectedInstanceIntFieldJavaClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3225, MemberAccess_JavaSubclass.doProtectedInstanceIntFieldJavaClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3225, MemberAccess_JavaSubclass.doProtectedInstanceIntFieldJavaClassSubclassAccessViaBlockInEval())
  }

}