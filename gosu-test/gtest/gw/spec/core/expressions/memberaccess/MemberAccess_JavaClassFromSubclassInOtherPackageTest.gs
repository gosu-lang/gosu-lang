package gw.spec.core.expressions.memberaccess
uses gw.spec.core.expressions.memberaccess.other.MemberAccess_JavaSubclassInOtherPackage

class MemberAccess_JavaClassFromSubclassInOtherPackageTest extends MemberAccessTestBase {

  function testPublicInstanceStringPropertyJavaClassAccess() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccess() {
    assertEquals(1215, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBracketReflection() {
    assertEquals(1215, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaExplicitReflection() {
    assertEquals(1215, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(1215, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(1215, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(1215, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(1215, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccess() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringPropertyJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Property-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccess() {
    assertEquals(3215, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEval() {
    assertEquals(3215, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlock() {
    assertEquals(3215, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaEvalInBlock() {
    assertEquals(3215, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntPropertyJavaClassAccessViaBlockInEval() {
    assertEquals(3215, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicStaticStringFieldJavaClassAccess() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicStaticIntFieldJavaClassAccess() {
    assertEquals(1125, MemberAccess_JavaSubclassInOtherPackage.doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1125, MemberAccess_JavaSubclassInOtherPackage.doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1125, MemberAccess_JavaSubclassInOtherPackage.doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(1125, MemberAccess_JavaSubclassInOtherPackage.doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(1125, MemberAccess_JavaSubclassInOtherPackage.doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1125, MemberAccess_JavaSubclassInOtherPackage.doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1125, MemberAccess_JavaSubclassInOtherPackage.doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedStaticStringFieldJavaClassAccess() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedStaticStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Static-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedStaticIntFieldJavaClassAccess() {
    assertEquals(3125, MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEval() {
    assertEquals(3125, MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlock() {
    assertEquals(3125, MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3125, MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedStaticIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3125, MemberAccess_JavaSubclassInOtherPackage.doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicInstanceStringFieldJavaClassAccess() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBracketReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaExplicitReflection() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Public-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testPublicInstanceIntFieldJavaClassAccess() {
    assertEquals(1225, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBracketReflection() {
    assertEquals(1225, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaExplicitReflection() {
    assertEquals(1225, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(1225, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(1225, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(1225, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testPublicInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(1225, MemberAccess_JavaSubclassInOtherPackage.doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccess() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaEvalInBlock() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceStringFieldJavaClassAccessViaBlockInEval() {
    assertEquals("Protected-Instance-Field-JavaClass", MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccess() {
    assertEquals(3225, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccess())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEval() {
    assertEquals(3225, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEval())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlock() {
    assertEquals(3225, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaEvalInBlock() {
    assertEquals(3225, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock())
  }

  function testProtectedInstanceIntFieldJavaClassAccessViaBlockInEval() {
    assertEquals(3225, MemberAccess_JavaSubclassInOtherPackage.doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval())
  }

}