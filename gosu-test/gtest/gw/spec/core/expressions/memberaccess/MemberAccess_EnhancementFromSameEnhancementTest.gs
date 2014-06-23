package gw.spec.core.expressions.memberaccess

class MemberAccess_EnhancementFromSameEnhancementTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyOnEnhancementAccess() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyOnEnhancementSameEnhancementAccess())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyOnEnhancementAccess() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyOnEnhancementSameEnhancementAccess())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyOnEnhancementAccess() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyOnEnhancementSameEnhancementAccess())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyOnEnhancementAccess() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyOnEnhancementSameEnhancementAccess())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyOnEnhancementAccess() {
    assertEquals("Protected-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccess())
  }

  function testProtectedStaticStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Protected-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testProtectedStaticStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Protected-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testProtectedStaticStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyOnEnhancementAccess() {
    assertEquals(3112, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccess())
  }

  function testProtectedStaticIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(3112, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testProtectedStaticIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(3112, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testProtectedStaticIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(3112, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(3112, MemberAccess_DeclaringGosuClass.doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccess() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccess())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccess() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccess())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccess() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccess())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccess() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccess())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyOnEnhancementAccess() {
    assertEquals("Protected-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccess())
  }

  function testProtectedInstanceStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Protected-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testProtectedInstanceStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Protected-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyOnEnhancementAccess() {
    assertEquals(3212, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccess())
  }

  function testProtectedInstanceIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(3212, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEval())
  }

  function testProtectedInstanceIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(3212, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(3212, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(3212, MemberAccess_DeclaringGosuClass.doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval())
  }

}