package gw.spec.core.expressions.memberaccess

class MemberAccess_GosuInterfaceFromUnrelatedClassTest extends MemberAccessTestBase {

  function testPublicStaticStringFieldGosuInterfaceAccess() {
    assertEquals("Public-Static-Field-GosuInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccess())
  }

  function testPublicStaticStringFieldGosuInterfaceAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuInterfaceAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuInterfaceAccessViaEval() {
    assertEquals("Public-Static-Field-GosuInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringFieldGosuInterfaceAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuInterfaceAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuInterfaceAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuInterfaceAccess() {
    assertEquals(1126, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccess())
  }

  function testPublicStaticIntFieldGosuInterfaceAccessViaBracketReflection() {
    assertEquals(1126, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuInterfaceAccessViaExplicitReflection() {
    assertEquals(1126, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuInterfaceAccessViaEval() {
    assertEquals(1126, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntFieldGosuInterfaceAccessViaBlock() {
    assertEquals(1126, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuInterfaceAccessViaEvalInBlock() {
    assertEquals(1126, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuInterfaceAccessViaBlockInEval() {
    assertEquals(1126, MemberAccess_UnrelatedClass.doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaBlockInEval())
  }

}