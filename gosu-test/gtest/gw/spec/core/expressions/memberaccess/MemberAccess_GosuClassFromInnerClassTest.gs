package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuClassFromInnerClassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyGosuClassInnerClassAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyGosuClassInnerClassAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyGosuClassAccess() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyGosuClassInnerClassAccess())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyGosuClassAccess() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyGosuClassInnerClassAccess())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringPropertyGosuClassInnerClassAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntPropertyGosuClassInnerClassAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticStringPropertyGosuClassAccess() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringPropertyGosuClassInnerClassAccess())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticIntPropertyGosuClassAccess() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntPropertyGosuClassInnerClassAccess())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyGosuClassInnerClassAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyGosuClassInnerClassAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccess() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyGosuClassInnerClassAccess())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccess() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyGosuClassInnerClassAccess())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringPropertyGosuClassInnerClassAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntPropertyGosuClassInnerClassAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceStringPropertyGosuClassAccess() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringPropertyGosuClassInnerClassAccess())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceIntPropertyGosuClassAccess() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntPropertyGosuClassInnerClassAccess())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaEval())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringFieldGosuClassInnerClassAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringFieldGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringFieldGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntFieldGosuClassInnerClassAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntFieldGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntFieldGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldGosuClassAccess() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringFieldGosuClassInnerClassAccess())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldGosuClassAccess() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntFieldGosuClassInnerClassAccess())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringFieldGosuClassInnerClassAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntFieldGosuClassInnerClassAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedStaticIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticStringFieldGosuClassAccess() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringFieldGosuClassInnerClassAccess())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticIntFieldGosuClassAccess() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntFieldGosuClassInnerClassAccess())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateStaticIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringFieldGosuClassInnerClassAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringFieldGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringFieldGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntFieldGosuClassInnerClassAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntFieldGosuClassInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntFieldGosuClassInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccess() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringFieldGosuClassInnerClassAccess())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccess() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntFieldGosuClassInnerClassAccess())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringFieldGosuClassInnerClassAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntFieldGosuClassInnerClassAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.InnerClass.doProtectedInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceStringFieldGosuClassAccess() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringFieldGosuClassInnerClassAccess())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringFieldGosuClassInnerClassAccessViaEval())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceIntFieldGosuClassAccess() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntFieldGosuClassInnerClassAccess())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntFieldGosuClassInnerClassAccessViaEval())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntFieldGosuClassInnerClassAccessViaBlock())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.InnerClass.doPrivateInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval())
  }

}