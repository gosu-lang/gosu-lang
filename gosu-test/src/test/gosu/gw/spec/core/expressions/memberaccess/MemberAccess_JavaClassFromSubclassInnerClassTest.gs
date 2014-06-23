package gw.spec.core.expressions.memberaccess

class MemberAccess_JavaClassFromSubclassInnerClassTest extends MemberAccessTestBase {

  function testPublicInstanceStringPropertyJavaClassAccess() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccess())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccess() {
    assertEquals(1215, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccess())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBracketReflection() {
    assertEquals(1215, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals(1215, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(1215, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(1215, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(1215, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(1215, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyJavaClassAccess() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccess())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyJavaClassAccess() {
    assertEquals(2215, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccess())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(2215, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(2215, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(2215, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(2215, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccess() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccess() {
    assertEquals(3215, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(3215, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(3215, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(3215, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(3215, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldJavaClassAccess() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticStringFieldJavaClassSubclassInnerClassAccess())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldJavaClassAccess() {
    assertEquals(1125, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticIntFieldJavaClassSubclassInnerClassAccess())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1125, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1125, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(1125, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(1125, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1125, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1125, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldJavaClassAccess() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticStringFieldJavaClassSubclassInnerClassAccess())
  }

  function testInternalStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldJavaClassAccess() {
    assertEquals(2125, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticIntFieldJavaClassSubclassInnerClassAccess())
  }

  function testInternalStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(2125, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(2125, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(2125, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(2125, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldJavaClassAccess() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticStringFieldJavaClassSubclassInnerClassAccess())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldJavaClassAccess() {
    assertEquals(3125, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticIntFieldJavaClassSubclassInnerClassAccess())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(3125, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(3125, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3125, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3125, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldJavaClassAccess() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringFieldJavaClassSubclassInnerClassAccess())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldJavaClassAccess() {
    assertEquals(1225, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntFieldJavaClassSubclassInnerClassAccess())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1225, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1225, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(1225, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(1225, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1225, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1225, MemberAccess_JavaSubclass.SubclassInnerClass.doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldJavaClassAccess() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringFieldJavaClassSubclassInnerClassAccess())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldJavaClassAccess() {
    assertEquals(2225, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntFieldJavaClassSubclassInnerClassAccess())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(2225, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(2225, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(2225, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(2225, MemberAccess_JavaSubclass.SubclassInnerClass.doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccess() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccess() {
    assertEquals(3225, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(3225, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(3225, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3225, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3225, MemberAccess_JavaSubclass.SubclassInnerClass.doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval())
  }

}