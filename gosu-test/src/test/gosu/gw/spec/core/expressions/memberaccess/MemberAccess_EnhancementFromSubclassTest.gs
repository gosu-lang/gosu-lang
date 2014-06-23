package gw.spec.core.expressions.memberaccess

class MemberAccess_EnhancementFromSubclassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyOnEnhancementAccess() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyOnEnhancementSubclassAccess())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyOnEnhancementSubclassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyOnEnhancementSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicStaticStringPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyOnEnhancementAccess() {
    assertEquals(1112, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyOnEnhancementSubclassAccess())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals(1112, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyOnEnhancementSubclassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals(1112, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyOnEnhancementSubclassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(1112, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(1112, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(1112, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(1112, MemberAccess_DeclaringClassSubclass.doPublicStaticIntPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyOnEnhancementAccess() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyOnEnhancementSubclassAccess())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalStaticStringPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyOnEnhancementAccess() {
    assertEquals(2112, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyOnEnhancementSubclassAccess())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(2112, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(2112, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(2112, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(2112, MemberAccess_DeclaringClassSubclass.doInternalStaticIntPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccess() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyOnEnhancementSubclassAccess())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccess() {
    assertEquals(1212, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyOnEnhancementSubclassAccess())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals(1212, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals(1212, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(1212, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(1212, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(1212, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(1212, MemberAccess_DeclaringClassSubclass.doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccess() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyOnEnhancementSubclassAccess())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringClassSubclass.doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccess() {
    assertEquals(2212, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyOnEnhancementSubclassAccess())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(2212, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaEval())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(2212, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(2212, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(2212, MemberAccess_DeclaringClassSubclass.doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaBlockInEval())
  }

}