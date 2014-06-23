package gw.internal.gosu.regression

class ImplementsGosuInterfaceThroughJavaSuperclasses extends JavaClassExtendsJavaClassWithMethod implements IGosuInterfaceImplementedImplicitly
{

  function aProtectedMethodOnTheBaseClass() : String {
    return super.aProtectedMethodOnTheBaseClass()
  }

  function aStaticMethodOnTheBaseClass() : String {
    return "Foo"
  }

}