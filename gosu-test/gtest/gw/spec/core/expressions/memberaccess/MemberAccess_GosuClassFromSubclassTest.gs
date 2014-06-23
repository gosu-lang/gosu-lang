package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuClassFromSubclassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyGosuClassSubclassAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyGosuClassSubclassAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyGosuClassSubclassAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyGosuClassSubclassAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyGosuClassSubclassAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyGosuClassSubclassAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyGosuClassAccess() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyGosuClassSubclassAccess())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyGosuClassSubclassAccessViaEval())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyGosuClassSubclassAccessViaBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyGosuClassAccess() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyGosuClassSubclassAccess())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyGosuClassSubclassAccessViaEval())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyGosuClassSubclassAccessViaBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2111, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringPropertyGosuClassSubclassAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringPropertyGosuClassSubclassAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringPropertyGosuClassSubclassAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntPropertyGosuClassSubclassAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntPropertyGosuClassSubclassAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntPropertyGosuClassSubclassAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyGosuClassSubclassAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyGosuClassSubclassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyGosuClassSubclassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyGosuClassSubclassAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyGosuClassSubclassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyGosuClassSubclassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccess() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyGosuClassSubclassAccess())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyGosuClassSubclassAccessViaEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyGosuClassSubclassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccess() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyGosuClassSubclassAccess())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyGosuClassSubclassAccessViaEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyGosuClassSubclassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2211, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringPropertyGosuClassSubclassAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringPropertyGosuClassSubclassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringPropertyGosuClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntPropertyGosuClassSubclassAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntPropertyGosuClassSubclassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntPropertyGosuClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntPropertyGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntPropertyGosuClassSubclassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringFieldGosuClassSubclassAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringFieldGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringFieldGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringFieldGosuClassSubclassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringFieldGosuClassSubclassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicStaticStringFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.doPublicStaticIntFieldGosuClassSubclassAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.doPublicStaticIntFieldGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.doPublicStaticIntFieldGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.doPublicStaticIntFieldGosuClassSubclassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.doPublicStaticIntFieldGosuClassSubclassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.doPublicStaticIntFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclass.doPublicStaticIntFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldGosuClassAccess() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringFieldGosuClassSubclassAccess())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringFieldGosuClassSubclassAccessViaEval())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringFieldGosuClassSubclassAccessViaBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalStaticStringFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldGosuClassAccess() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.doInternalStaticIntFieldGosuClassSubclassAccess())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.doInternalStaticIntFieldGosuClassSubclassAccessViaEval())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.doInternalStaticIntFieldGosuClassSubclassAccessViaBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.doInternalStaticIntFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2121, MemberAccess_DeclaringClassSubclass.doInternalStaticIntFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringFieldGosuClassSubclassAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringFieldGosuClassSubclassAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringFieldGosuClassSubclassAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedStaticStringFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntFieldGosuClassSubclassAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntFieldGosuClassSubclassAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntFieldGosuClassSubclassAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclass.doProtectedStaticIntFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringFieldGosuClassSubclassAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringFieldGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringFieldGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringFieldGosuClassSubclassAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringFieldGosuClassSubclassAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntFieldGosuClassSubclassAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntFieldGosuClassSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntFieldGosuClassSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntFieldGosuClassSubclassAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntFieldGosuClassSubclassAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccess() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringFieldGosuClassSubclassAccess())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringFieldGosuClassSubclassAccessViaEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringFieldGosuClassSubclassAccessViaBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccess() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntFieldGosuClassSubclassAccess())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntFieldGosuClassSubclassAccessViaEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntFieldGosuClassSubclassAccessViaBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2221, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringFieldGosuClassSubclassAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringFieldGosuClassSubclassAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringFieldGosuClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclass.doProtectedInstanceStringFieldGosuClassSubclassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntFieldGosuClassSubclassAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntFieldGosuClassSubclassAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntFieldGosuClassSubclassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntFieldGosuClassSubclassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclass.doProtectedInstanceIntFieldGosuClassSubclassAccessViaBlockInEval())
  }

}