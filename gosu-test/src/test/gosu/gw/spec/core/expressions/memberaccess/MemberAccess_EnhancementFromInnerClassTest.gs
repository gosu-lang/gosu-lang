package gw.spec.core.expressions.memberaccess

class MemberAccess_EnhancementFromInnerClassTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyOnEnhancementAccess() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyOnEnhancementInnerClassAccess())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyOnEnhancementAccess() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyOnEnhancementInnerClassAccess())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(1112, MemberAccess_DeclaringGosuClass.InnerClass.doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticStringPropertyOnEnhancementAccess() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyOnEnhancementInnerClassAccess())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Internal-Static-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

  function testInternalStaticIntPropertyOnEnhancementAccess() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyOnEnhancementInnerClassAccess())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testInternalStaticIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(2112, MemberAccess_DeclaringGosuClass.InnerClass.doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccess() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyOnEnhancementInnerClassAccess())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccess() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyOnEnhancementInnerClassAccess())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBracketReflection() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaExplicitReflection() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(1212, MemberAccess_DeclaringGosuClass.InnerClass.doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccess() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyOnEnhancementInnerClassAccess())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaEval() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaBlock() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceStringPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals("Internal-Instance-Property-OnEnhancement", MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccess() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyOnEnhancementInnerClassAccess())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaEval() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaEval())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaBlock() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaBlock())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaEvalInBlock() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock())
  }

  function testInternalInstanceIntPropertyOnEnhancementAccessViaBlockInEval() {
    assertEquals(2212, MemberAccess_DeclaringGosuClass.InnerClass.doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaBlockInEval())
  }

}