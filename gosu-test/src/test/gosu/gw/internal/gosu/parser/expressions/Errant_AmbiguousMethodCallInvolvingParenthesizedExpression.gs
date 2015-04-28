package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_AmbiguousMethodCallInvolvingParenthesizedExpression {
  function blah() {
    var fred = "hello"
    var barney = fred.Bytes
    (barney).toString()  // because the parenthesized expr is on a new line we avoid parsing is as the parenthesis of an errant function call as: fred.Bytes(barney)
  }

  function blah2() {
    var fred = "hello"
    // because the parenthesized expr is on the SAME line we parse it as the parenthesis of an errant function call as: fred.Bytes(barney)
    var barney = fred.Bytes (barney).toString()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_SUCH_FUNCTION, MSG_BAD_IDENTIFIER_NAME
  }

  function blah2_1() {
    var fred = "hello"
    var barney = fred.substring
        (1).toString()
  }

  function blah3() {
    var fred = "hello"
    var barney = fred
    (barney).toString()  // because the parenthesized expr is on a new line we avoid parsing is as the parenthesis of an errant function call as: fred(barney)
  }

  function blah4() {
    var fred = "hello"
    // because the parenthesized expr is on the SAME line we parse it as the parenthesis of an errant function call as: fred(barney)
    var barney = fred (barney).toString()  //## issuekeys: MSG_NO_SUCH_FUNCTION, MSG_BAD_IDENTIFIER_NAME
  }

  function blah5() {
    var fred = "hello"
    var barney = String
      (fred).toString()
  }

  function blah5_1() {
    var fred = "hello"
    var barney = String (fred).toString()  //## issuekeys: MSG_NO_SUCH_FUNCTION
  }

  function blah6() {
    var fred = "hello"
    foo<String> (fred).toString()
  }

  function blah7() {
    var fred = "hello"
    foo<String>
        (fred).toString()
  }

  function blah8() {
    var fred = "hello"
    var ret = foo(fred).toString()
  }

  function blah9() {
    var fred = "hello"
    var ret = foo
        (fred).toString()
  }


  function foo<F>( f: F ) : String {
    return null
  }
}