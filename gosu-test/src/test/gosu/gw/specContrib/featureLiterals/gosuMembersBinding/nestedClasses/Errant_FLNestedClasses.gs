package gw.specContrib.featureLiterals.gosuMembersBinding.nestedClasses

class Errant_FLNestedClasses {
  class A {
    function foo() {
    }
  }

  class B extends A {
    function test() {
      var fl = #foo()
      //an instance of class B is expected so error
      fl.invoke(new A())      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.NESTEDCLASSES.ERRANT_FLNESTEDCLASSES.A)'
      //IDE-1587 should resolve the following issue in Parser. There should not be any error
      fl.invoke(new B())
    }
  }
}