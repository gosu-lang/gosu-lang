package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource
// warning: don't use imports in this class. You'll screw up the line numbers!
@DoNotVerifyResource
class Errant_BasicErrorsClass {

  function badArgsMethod() {
    methodWithSomeArgs( new Object() {}, 1, new Object() {} )
  }
  
  function methodWithSomeArgs( i : int, j : int, k : int ) {
  }

  function badCatchClause<T extends java.lang.Throwable>() {
    try {
    } catch( e : T ) {
    }
  }

  function foo( d : String ) {
    foo( CONST_STR ) // illegal forward reference of inferred variable
  }
  static var CONST_STR = "6/30/2007"

}
