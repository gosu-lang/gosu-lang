package gw.internal.gosu.regression
uses gw.test.TestClass

class GosuInterfaceImplementedThroughSuperClassWorksTest extends TestClass {

  function testIt() {
    var x : IGosuInterfaceImplementedImplicitly = new ImplementsGosuInterfaceThroughJavaSuperclasses()
    assertEquals( "From JavaClassWithMethod", x.aMethodOnTheBaseClass() )
    assertEquals( "From JavaClassExtendsJavaClassWithMethod", x.aMethodOnTheJavaExtensionClass() )
    assertEquals( "Protected From JavaClassWithMethod", x.aProtectedMethodOnTheBaseClass() )
  }

}
