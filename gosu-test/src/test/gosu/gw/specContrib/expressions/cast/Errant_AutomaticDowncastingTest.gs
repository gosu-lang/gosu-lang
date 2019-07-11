package gw.specContrib.expressions.cast

uses java.lang.Integer
uses java.util.ArrayList
uses java.lang.Deprecated
/**
 * Created by Sky on 2015/02/02 with IntelliJ IDEA.
 */


class Errant_AutomaticDowncastingTest {
  static class A { }
  static class B extends A{
    function bar() : String {
      return "test"
    }
  }

  var a : A
  var b : B
  var c : C
  
  static class C {
    @Deprecated
    property get deprecatedProp() : A { return new B()}

    @Deprecated
    function deprecatedMethod() : A { return new B()}

    @Deprecated
    public var deprecatedField : A = new B()

    function nonDeprecatedMethod() : A {return new B()}
  }
  
  function testAutoDowncastingInDepreatedProperty() {
    var h = new C()
    if(h.deprecatedProp typeis B) {  //## issuekeys: MSG_DEPRECATED_MEMBER
      b = h.deprecatedProp  //## issuekeys: MSG_DEPRECATED_MEMBER
      h.deprecatedProp.bar()  //## issuekeys: MSG_DEPRECATED_MEMBER
    }
    a = h.deprecatedProp  //## issuekeys: MSG_DEPRECATED_MEMBER
  }


  function testAutoDowncastingInDepreatedMethod() {
    var h = new C()
    var h1 = h.deprecatedMethod()  //## issuekeys: MSG_DEPRECATED_MEMBER
    if(h1 typeis B){
      b = h1
      h1.bar()
    }
    a = h1

    if(h.deprecatedMethod() typeis B) {  //## issuekeys: MSG_DEPRECATED_MEMBER
      a = h.deprecatedMethod()    // By design, auto-downcasting works for property and field, but not method  //## issuekeys: MSG_DEPRECATED_MEMBER
    }
  }


  function testAutoDowncastingInDepreatedField() {
    var h = new C()

    if(h.deprecatedField typeis B) {  //## issuekeys: MSG_DEPRECATED_MEMBER
      b = h.deprecatedField  //## issuekeys: MSG_DEPRECATED_MEMBER
      h.deprecatedField.bar()  //## issuekeys: MSG_DEPRECATED_MEMBER
    }
   a = h.deprecatedField  //## issuekeys: MSG_DEPRECATED_MEMBER
  }


  function testAutoDowncastingBasic() {
    var h1 : A = new B()
    if(h1 typeis B) {
      b = h1
      h1.bar()
    }
    a = h1


  }


  function testAutoDowncastingInElseClause() {
    var h1 : A = new B()
    if(h1 typeis B && 1 != 0 ) {
    }else{
      a = h1         // By design, automatic downcasting only works in IF statement and downcasting ends when reaches the end of IF statement
    }

    a = h1
  }


  function testAutoDowncastingWithOrInLogicalExpression() {
    var h1 : A = new B()
    if(h1 typeis B || 1 == 1) {
      a = h1         // By design, automatic downcasting will be cancelled with OR keyword in the logical expressions
    }

    a = h1
  }


  function testAutoDowncastingWithNotInLogicalExpression() {
    var h1 : A = new B()

    if(!(h1 typeis B && 1==0)) {
      a = h1         // By design, automatic downcasting will be cancelled with NOT keyword in the logical expression
    }else{
    }
    a = h1


    if(!(h1 typeis B && 1==1)) {
    }else{
      a = h1         // By design, automatic downcasting will be cancelled with NOT keyword in the logical expression
    }
    a = h1
  }

  function callWithA(x : A) : int { return 0 }
  function callWithB(x : B) : int { return 1}

  function testAutoDowncastingInTernaryConditionalExpression(){
    var ret :int
    var h1 : A = new B()
    a = h1

    ret = h1 typeis B ? callWithB(h1): callWithA(h1)

    ret = h1 typeis B ? callWithB(h1): callWithA(h1)

    var objStaticType5 = (h1 typeis B && 1 == 1)? callWithB(h1): callWithA(h1)

    // 'not' keyword
    var objStaticType4 = (!(h1 typeis B && 1 == 0)) ? callWithA(h1): callWithA(h1)

    // else clause
    var objStaticType2 = (h1 typeis B && 1 == 0)? callWithA(h1): callWithA(h1)

    // 'or' keyword
    var objStaticType3 = (h1 typeis B || 1 == 1) ? callWithA(h1): callWithA(h1)
  }


  // test 'typeof' in switch statement
  function testAutoDowncastingInSwitchStatement(){
    var x : Object = "neat"
    switchStatementForTest(x)

    var y : Object = new ArrayList<String>({"one", "two", "three"})
    switchStatementForTest(y)
  }


  // test 'typeof' in switch statement with no 'break'
  function testAutoDowncastingWithNoBreakInSwitchStatement(){
    var x : Object = "neat"
    switchStatementForTest(x)
  }

  private function switchStatementForTest(x : Object){
    switch( typeof(x) ){
      case String :
          var s : String = x
          x.charAt(0)
          break
      case ArrayList<Integer> :
          var t : java.util.ArrayList = x
          x.add(1)
          break
    }

    var o : Object = x
  }


  private function switchStatementWithNoBreakForTest(x : Object){
    switch( typeof(x) ){
      case String :
          var s : String = x
          x.charAt(0)
      case ArrayList<Integer> :  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          var s2 : String = x  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
          break
    }

    var o : Object = x
  }

  function callMe() {foo("Hello")}

  function foo(x : String) {}

  function bar<T>( value: T) {
    if (value typeis String) {
      foo(value as String)
    }
  }

}

