package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuClassFromSameClassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyGosuClassSameClassAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyGosuClassSameClassAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyGosuClassAccess() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyGosuClassSameClassAccess())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyGosuClassAccess() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyGosuClassSameClassAccess())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2111, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyGosuClassSameClassAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyGosuClassSameClassAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateStaticStringPropertyGosuClassAccess() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringPropertyGosuClassSameClassAccess())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Static-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateStaticIntPropertyGosuClassAccess() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.doPrivateStaticIntPropertyGosuClassSameClassAccess())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.doPrivateStaticIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.doPrivateStaticIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.doPrivateStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(4111, MemberAccess_DeclaringGosuClass.doPrivateStaticIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyGosuClassSameClassAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaThis() {
    assertEquals("Public-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doPublicInstanceStringPropertyGosuClassSameClassAccessViaThis())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaThisInEval() {
    assertEquals("Public-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doPublicInstanceStringPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaThisInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doPublicInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyGosuClassSameClassAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaThis() {
    assertEquals(1211, new MemberAccess_DeclaringGosuClass().doPublicInstanceIntPropertyGosuClassSameClassAccessViaThis())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaThisInEval() {
    assertEquals(1211, new MemberAccess_DeclaringGosuClass().doPublicInstanceIntPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaThisInBlock() {
    assertEquals(1211, new MemberAccess_DeclaringGosuClass().doPublicInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccess() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyGosuClassSameClassAccess())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaThis() {
    assertEquals("Internal-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doInternalInstanceStringPropertyGosuClassSameClassAccessViaThis())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaThisInEval() {
    assertEquals("Internal-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doInternalInstanceStringPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testInternalInstanceStringPropertyGosuClassAccessViaThisInBlock() {
    assertEquals("Internal-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doInternalInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccess() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyGosuClassSameClassAccess())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(2211, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaThis() {
    assertEquals(2211, new MemberAccess_DeclaringGosuClass().doInternalInstanceIntPropertyGosuClassSameClassAccessViaThis())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaThisInEval() {
    assertEquals(2211, new MemberAccess_DeclaringGosuClass().doInternalInstanceIntPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testInternalInstanceIntPropertyGosuClassAccessViaThisInBlock() {
    assertEquals(2211, new MemberAccess_DeclaringGosuClass().doInternalInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyGosuClassSameClassAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaThis() {
    assertEquals("Protected-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doProtectedInstanceStringPropertyGosuClassSameClassAccessViaThis())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaThisInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doProtectedInstanceStringPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaThisInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doProtectedInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyGosuClassSameClassAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaThis() {
    assertEquals(3211, new MemberAccess_DeclaringGosuClass().doProtectedInstanceIntPropertyGosuClassSameClassAccessViaThis())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaThisInEval() {
    assertEquals(3211, new MemberAccess_DeclaringGosuClass().doProtectedInstanceIntPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaThisInBlock() {
    assertEquals(3211, new MemberAccess_DeclaringGosuClass().doProtectedInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testPrivateInstanceStringPropertyGosuClassAccess() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringPropertyGosuClassSameClassAccess())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringPropertyGosuClassSameClassAccessViaEval())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Instance-Property-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaThis() {
    assertEquals("Private-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doPrivateInstanceStringPropertyGosuClassSameClassAccessViaThis())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaThisInEval() {
    assertEquals("Private-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doPrivateInstanceStringPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testPrivateInstanceStringPropertyGosuClassAccessViaThisInBlock() {
    assertEquals("Private-Instance-Property-GosuClass", new MemberAccess_DeclaringGosuClass().doPrivateInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testPrivateInstanceIntPropertyGosuClassAccess() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntPropertyGosuClassSameClassAccess())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntPropertyGosuClassSameClassAccessViaEval())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntPropertyGosuClassSameClassAccessViaBlock())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(4211, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaThis() {
    assertEquals(4211, new MemberAccess_DeclaringGosuClass().doPrivateInstanceIntPropertyGosuClassSameClassAccessViaThis())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaThisInEval() {
    assertEquals(4211, new MemberAccess_DeclaringGosuClass().doPrivateInstanceIntPropertyGosuClassSameClassAccessViaThisInEval())
  }

  function testPrivateInstanceIntPropertyGosuClassAccessViaThisInBlock() {
    assertEquals(4211, new MemberAccess_DeclaringGosuClass().doPrivateInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringFieldGosuClassSameClassAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringFieldGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringFieldGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringFieldGosuClassSameClassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicStaticStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.doPublicStaticIntFieldGosuClassSameClassAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.doPublicStaticIntFieldGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.doPublicStaticIntFieldGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.doPublicStaticIntFieldGosuClassSameClassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.doPublicStaticIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.doPublicStaticIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_DeclaringGosuClass.doPublicStaticIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalStaticStringFieldGosuClassAccess() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringFieldGosuClassSameClassAccess())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringFieldGosuClassSameClassAccessViaEval())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalStaticStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalStaticIntFieldGosuClassAccess() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.doInternalStaticIntFieldGosuClassSameClassAccess())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.doInternalStaticIntFieldGosuClassSameClassAccessViaEval())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.doInternalStaticIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.doInternalStaticIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2121, MemberAccess_DeclaringGosuClass.doInternalStaticIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringFieldGosuClassSameClassAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringFieldGosuClassSameClassAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedStaticStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.doProtectedStaticIntFieldGosuClassSameClassAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.doProtectedStaticIntFieldGosuClassSameClassAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.doProtectedStaticIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.doProtectedStaticIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_DeclaringGosuClass.doProtectedStaticIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateStaticStringFieldGosuClassAccess() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringFieldGosuClassSameClassAccess())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringFieldGosuClassSameClassAccessViaEval())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Static-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateStaticStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateStaticIntFieldGosuClassAccess() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.doPrivateStaticIntFieldGosuClassSameClassAccess())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.doPrivateStaticIntFieldGosuClassSameClassAccessViaEval())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.doPrivateStaticIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.doPrivateStaticIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(4121, MemberAccess_DeclaringGosuClass.doPrivateStaticIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringFieldGosuClassSameClassAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringFieldGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringFieldGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringFieldGosuClassSameClassAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPublicInstanceStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaThis() {
    assertEquals("Public-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doPublicInstanceStringFieldGosuClassSameClassAccessViaThis())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaThisInEval() {
    assertEquals("Public-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doPublicInstanceStringFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaThisInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doPublicInstanceStringFieldGosuClassSameClassAccessViaThisInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.doPublicInstanceIntFieldGosuClassSameClassAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.doPublicInstanceIntFieldGosuClassSameClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.doPublicInstanceIntFieldGosuClassSameClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.doPublicInstanceIntFieldGosuClassSameClassAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.doPublicInstanceIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.doPublicInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_DeclaringGosuClass.doPublicInstanceIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaThis() {
    assertEquals(1221, new MemberAccess_DeclaringGosuClass().doPublicInstanceIntFieldGosuClassSameClassAccessViaThis())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaThisInEval() {
    assertEquals(1221, new MemberAccess_DeclaringGosuClass().doPublicInstanceIntFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaThisInBlock() {
    assertEquals(1221, new MemberAccess_DeclaringGosuClass().doPublicInstanceIntFieldGosuClassSameClassAccessViaThisInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccess() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringFieldGosuClassSameClassAccess())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringFieldGosuClassSameClassAccessViaEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doInternalInstanceStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaThis() {
    assertEquals("Internal-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doInternalInstanceStringFieldGosuClassSameClassAccessViaThis())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaThisInEval() {
    assertEquals("Internal-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doInternalInstanceStringFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testInternalInstanceStringFieldGosuClassAccessViaThisInBlock() {
    assertEquals("Internal-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doInternalInstanceStringFieldGosuClassSameClassAccessViaThisInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccess() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.doInternalInstanceIntFieldGosuClassSameClassAccess())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.doInternalInstanceIntFieldGosuClassSameClassAccessViaEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.doInternalInstanceIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.doInternalInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(2221, MemberAccess_DeclaringGosuClass.doInternalInstanceIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaThis() {
    assertEquals(2221, new MemberAccess_DeclaringGosuClass().doInternalInstanceIntFieldGosuClassSameClassAccessViaThis())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaThisInEval() {
    assertEquals(2221, new MemberAccess_DeclaringGosuClass().doInternalInstanceIntFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testInternalInstanceIntFieldGosuClassAccessViaThisInBlock() {
    assertEquals(2221, new MemberAccess_DeclaringGosuClass().doInternalInstanceIntFieldGosuClassSameClassAccessViaThisInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringFieldGosuClassSameClassAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringFieldGosuClassSameClassAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaThis() {
    assertEquals("Protected-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doProtectedInstanceStringFieldGosuClassSameClassAccessViaThis())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaThisInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doProtectedInstanceStringFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaThisInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doProtectedInstanceStringFieldGosuClassSameClassAccessViaThisInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntFieldGosuClassSameClassAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntFieldGosuClassSameClassAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaThis() {
    assertEquals(3221, new MemberAccess_DeclaringGosuClass().doProtectedInstanceIntFieldGosuClassSameClassAccessViaThis())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaThisInEval() {
    assertEquals(3221, new MemberAccess_DeclaringGosuClass().doProtectedInstanceIntFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaThisInBlock() {
    assertEquals(3221, new MemberAccess_DeclaringGosuClass().doProtectedInstanceIntFieldGosuClassSameClassAccessViaThisInBlock())
  }

  function testPrivateInstanceStringFieldGosuClassAccess() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringFieldGosuClassSameClassAccess())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringFieldGosuClassSameClassAccessViaEval())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringFieldGosuClassSameClassAccessViaBlock())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Private-Instance-Field-GosuClass", MemberAccess_DeclaringGosuClass.doPrivateInstanceStringFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaThis() {
    assertEquals("Private-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doPrivateInstanceStringFieldGosuClassSameClassAccessViaThis())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaThisInEval() {
    assertEquals("Private-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doPrivateInstanceStringFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testPrivateInstanceStringFieldGosuClassAccessViaThisInBlock() {
    assertEquals("Private-Instance-Field-GosuClass", new MemberAccess_DeclaringGosuClass().doPrivateInstanceStringFieldGosuClassSameClassAccessViaThisInBlock())
  }

  function testPrivateInstanceIntFieldGosuClassAccess() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntFieldGosuClassSameClassAccess())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntFieldGosuClassSameClassAccessViaEval())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntFieldGosuClassSameClassAccessViaBlock())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(4221, MemberAccess_DeclaringGosuClass.doPrivateInstanceIntFieldGosuClassSameClassAccessViaBlockInEval())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaThis() {
    assertEquals(4221, new MemberAccess_DeclaringGosuClass().doPrivateInstanceIntFieldGosuClassSameClassAccessViaThis())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaThisInEval() {
    assertEquals(4221, new MemberAccess_DeclaringGosuClass().doPrivateInstanceIntFieldGosuClassSameClassAccessViaThisInEval())
  }

  function testPrivateInstanceIntFieldGosuClassAccessViaThisInBlock() {
    assertEquals(4221, new MemberAccess_DeclaringGosuClass().doPrivateInstanceIntFieldGosuClassSameClassAccessViaThisInBlock())
  }

}