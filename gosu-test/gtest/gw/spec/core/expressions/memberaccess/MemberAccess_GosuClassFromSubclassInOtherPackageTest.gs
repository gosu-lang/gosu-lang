package gw.spec.core.expressions.memberaccess
uses gw.spec.core.expressions.memberaccess.other.MemberAccess_DeclaringClassSubclassInOtherPackage

class MemberAccess_GosuClassFromSubclassInOtherPackageTest extends MemberAccessTestBase {

  function testPublicStaticStringPropertyGosuClassAccess() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicStaticIntPropertyGosuClassAccess() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1111, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccess() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedStaticStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccess() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedStaticIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3111, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccess() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccess() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBracketReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaExplicitReflection() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(1211, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccess() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccess() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaEvalInBlock() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyGosuClassAccessViaBlockInEval() {
    assertEquals(3211, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldGosuClassAccess() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldGosuClassAccess() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1121, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldGosuClassAccess() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldGosuClassAccess() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3121, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldGosuClassAccess() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldGosuClassAccess() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBracketReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaExplicitReflection() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(1221, MemberAccess_DeclaringClassSubclassInOtherPackage.doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccess() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldGosuClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-GosuClass", MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccess() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaEvalInBlock() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldGosuClassAccessViaBlockInEval() {
    assertEquals(3221, MemberAccess_DeclaringClassSubclassInOtherPackage.doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval())
  }

}