package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuInterfaceImplementorFromUnrelatedClassTest extends MemberAccessTestBase {

  function testPublicInstanceStringPropertyGosuInterfaceAccess() {
    assertEquals("Public-Instance-Property-GosuInterface", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccess())
  }

  function testPublicInstanceStringPropertyGosuInterfaceAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuInterface", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuInterfaceAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuInterface", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuInterfaceAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuInterface", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuInterfaceAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuInterface", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuInterfaceAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuInterface", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuInterfaceAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuInterface", MemberAccess_UnrelatedClass.doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuInterfaceAccess() {
    assertEquals(1216, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccess())
  }

  function testPublicInstanceIntPropertyGosuInterfaceAccessViaBracketReflection() {
    assertEquals(1216, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuInterfaceAccessViaExplicitReflection() {
    assertEquals(1216, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuInterfaceAccessViaEval() {
    assertEquals(1216, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuInterfaceAccessViaBlock() {
    assertEquals(1216, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuInterfaceAccessViaEvalInBlock() {
    assertEquals(1216, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuInterfaceAccessViaBlockInEval() {
    assertEquals(1216, MemberAccess_UnrelatedClass.doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaBlockInEval())
  }

}