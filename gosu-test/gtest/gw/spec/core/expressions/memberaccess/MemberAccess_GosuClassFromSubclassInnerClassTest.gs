package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuClassFromSubclassInnerClassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringPropertyGosuClassSubclassInnerClassAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntPropertyGosuClassSubclassInnerClassAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyGosuClassAccess() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringPropertyGosuClassSubclassInnerClassAccess())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyGosuClassAccess() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntPropertyGosuClassSubclassInnerClassAccess())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccess() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccess())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccess() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccess())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringFieldGosuClassSubclassInnerClassAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntFieldGosuClassSubclassInnerClassAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldGosuClassAccess() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringFieldGosuClassSubclassInnerClassAccess())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldGosuClassAccess() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntFieldGosuClassSubclassInnerClassAccess())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringFieldGosuClassSubclassInnerClassAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntFieldGosuClassSubclassInnerClassAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringFieldGosuClassSubclassInnerClassAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntFieldGosuClassSubclassInnerClassAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccess() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringFieldGosuClassSubclassInnerClassAccess())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccess() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntFieldGosuClassSubclassInnerClassAccess())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassInnerClass.doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval())
  }

}