package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuClassFromSubclassStaticInnerClassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyGosuClassAccess() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyGosuClassAccess() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccess() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccess() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldGosuClassAccess() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldGosuClassAccess() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccess() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccess() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.SubclassStaticInnerClass.doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval())
  }

}