package gw.specContrib.shadowing

class Errant_ShadowingVarsOfNestedClass {
  static class A {
    private var priVar: int
    public var pubVar: int
    protected var protVar: int
    internal var interVar: int
    var lvar: int
  }

  //IDE-2234 - private members are accessible if the parent class and child class, both are nested
  //for methods, the tests are in Errant_OverridingStaticMethod.gs
  static class B extends A {
    var priVar: int      //## issuekeys: VARIABLE 'PRIVAR' IS ALREADY DEFINED IN THE SCOPE
    var pubVar: int      //## issuekeys: VARIABLE 'PUBVAR' IS ALREADY DEFINED IN THE SCOPE
    var protVar: int      //## issuekeys: VARIABLE 'PROTVAR' IS ALREADY DEFINED IN THE SCOPE
    var interVar: int      //## issuekeys: VARIABLE 'INTERVAR' IS ALREADY DEFINED IN THE SCOPE
    var lvar: int      //## issuekeys: VARIABLE 'LVAR' IS ALREADY DEFINED IN THE SCOPE
  }
}