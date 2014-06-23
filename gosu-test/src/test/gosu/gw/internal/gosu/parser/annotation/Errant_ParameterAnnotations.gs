package gw.internal.gosu.parser.annotation

uses java.lang.Deprecated
uses java.lang.Override
uses java.lang.SuppressWarnings
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ParameterAnnotations {

  function g() {
    var bl = \ @SuppressWarnings a: int -> print( a )  //## issuekeys: MSG_NO_DEFAULT_CTOR_IN
  }

  construct( p: int, @Override a: Object ) {}  //## issuekeys: MSG_ANNOTATION_WHEN_NONE_ALLOWED

  function asdf2( @Override a: Object ) {  //## issuekeys: MSG_ANNOTATION_WHEN_NONE_ALLOWED
  }

  property set F2( @Override a: Object ) {}  //## issuekeys: MSG_ANNOTATION_WHEN_NONE_ALLOWED

  function f2() {
    var bl = \ @Override a: int -> print( a )  //## issuekeys: MSG_ANNOTATION_WHEN_NONE_ALLOWED
  }

  function ff2() {
    var bl = \ @Deprecated @Override a: int -> print( a )  //## issuekeys: MSG_ANNOTATION_WHEN_NONE_ALLOWED
  }
}