package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuClassFromStaticInnerClassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyGosuClassAccess() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyGosuClassAccess() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticStringPropertyGosuClassAccess() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticIntPropertyGosuClassAccess() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccess() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccess() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceStringPropertyGosuClassAccess() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccess())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceIntPropertyGosuClassAccess() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccess())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringFieldGosuClassStaticInnerClassAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntFieldGosuClassStaticInnerClassAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldGosuClassAccess() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringFieldGosuClassStaticInnerClassAccess())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldGosuClassAccess() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntFieldGosuClassStaticInnerClassAccess())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringFieldGosuClassStaticInnerClassAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntFieldGosuClassStaticInnerClassAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticStringFieldGosuClassAccess() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringFieldGosuClassStaticInnerClassAccess())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateStaticIntFieldGosuClassAccess() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntFieldGosuClassStaticInnerClassAccess())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringFieldGosuClassStaticInnerClassAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntFieldGosuClassStaticInnerClassAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccess() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringFieldGosuClassStaticInnerClassAccess())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccess() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntFieldGosuClassStaticInnerClassAccess())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringFieldGosuClassStaticInnerClassAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntFieldGosuClassStaticInnerClassAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceStringFieldGosuClassAccess() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringFieldGosuClassStaticInnerClassAccess())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

  function testPrivateInstanceIntFieldGosuClassAccess() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntFieldGosuClassStaticInnerClassAccess())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaEval())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.StaticInnerClass.doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval())
  }

}