package gw.spec.core.expressions.memberaccess

class MemberAccess_JavaInterfaceFromUnrelatedClassTest extends MemberAccessTestBase {

  function testPublicStaticStringFieldJavaInterfaceAccess() {
    assertEquals("Public-Static-Field-JavaInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccess())
  }

  function testPublicStaticStringFieldJavaInterfaceAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-JavaInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldJavaInterfaceAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-JavaInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldJavaInterfaceAccessViaEval() {
    assertEquals("Public-Static-Field-JavaInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicStaticStringFieldJavaInterfaceAccessViaBlock() {
    assertEquals("Public-Static-Field-JavaInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticStringFieldJavaInterfaceAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-JavaInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldJavaInterfaceAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-JavaInterface", MemberAccess_UnrelatedClass.doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldJavaInterfaceAccess() {
    assertEquals(1127, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccess())
  }

  function testPublicStaticIntFieldJavaInterfaceAccessViaBracketReflection() {
    assertEquals(1127, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldJavaInterfaceAccessViaExplicitReflection() {
    assertEquals(1127, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldJavaInterfaceAccessViaEval() {
    assertEquals(1127, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaEval())
  }

  function testPublicStaticIntFieldJavaInterfaceAccessViaBlock() {
    assertEquals(1127, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaBlock())
  }

  function testPublicStaticIntFieldJavaInterfaceAccessViaEvalInBlock() {
    assertEquals(1127, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldJavaInterfaceAccessViaBlockInEval() {
    assertEquals(1127, MemberAccess_UnrelatedClass.doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaBlockInEval())
  }

}