package gw.lang.parser

abstract class EnsureOuterMethodsDoNotCauseInnerMethodsToBeFinal {
  function foo() : void {}
  class Inner1 {
    function foo() : String { return null }
  }
  class Inner2 {
    function foo() : void { }
  }
}
