package gw.internal.gosu.regression

uses gw.test.*

class GosuClassExtendingJavaClassWProtectedGetterTest extends TestClass {

  function testAllPublicPermutationsWork() {
    var x = new GosuClassThatExtendsJavaSuperClassWGetters()
    assertEquals( 42, x.referAsUnqualifiedPublicProp() )
    assertEquals( 42, x.referAsQualifiedPublicProp() )
    assertEquals( 42, x.referAsUnqualifiedPublicMethod() )
    assertEquals( 42, x.referAsQualifiedPublicMethod() )
  }

  function testAllProtectedPermutationsWork() {
    var x = new GosuClassThatExtendsJavaSuperClassWGetters()
    assertEquals( 42, x.referAsUnqualifiedProtectedProp() )
    assertEquals( 42, x.referAsQualifiedProtectedProp() )
    assertEquals( 42, x.referAsUnqualifiedProtectedMethod() )
    assertEquals( 42, x.referAsQualifiedProtectedMethod() )
  }

  function testAllProtectedPermutationsWOverloadWork() {
    var x = new GosuClassThatExtendsJavaSuperClassWGetters()
    assertEquals( 42, x.referAsUnqualifiedProtectedPropWOverload() )
    assertEquals( 42, x.referAsQualifiedProtectedPropWOverload() )
    assertEquals( 42, x.referAsUnqualifiedProtectedMethodWOverload() )
    assertEquals( 42, x.referAsQualifiedProtectedMethodWOverload() )
  }

}