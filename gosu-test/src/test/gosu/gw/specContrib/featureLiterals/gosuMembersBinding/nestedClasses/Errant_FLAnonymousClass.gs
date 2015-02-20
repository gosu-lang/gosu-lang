package gw.specContrib.featureLiterals.gosuMembersBinding.nestedClasses

class Errant_FLAnonymousClass {

  function fooInBase() {}

  var a = new Errant_FLAnonymousClass() {
    function foo() {
      var fl1 = #foo()
      fl1.invoke(new Errant_FLAnonymousClass())      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.NESTEDCLASSES.ERRANT_FLANONYMOUSCLASS)'
      //But for 'this' it should not show any error as only 'this' can be passed
      //IDE-1587 - Parser Issue
      fl1.invoke(this)

      var fl2 = #fooInBase()
      fl2.invoke(new Errant_FLAnonymousClass())      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.NESTEDCLASSES.ERRANT_FLANONYMOUSCLASS)'
      //But for 'this' it should not show any error as only 'this' can be passed
      //IDE-1587 - Parser Issue
      fl2.invoke(this)
    }
  }
}