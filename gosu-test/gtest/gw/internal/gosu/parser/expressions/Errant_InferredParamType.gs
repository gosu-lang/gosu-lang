package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_InferredParamType
{
  final static var BOOL_FIELD_INFERRED = true
  final static var BOOL_FIELD_EXPLICIT : boolean = true

  function f1( p1 = BOOL_FIELD_INFERRED ) {  //## issuekeys: MSG_PARAM_TYPE_CANT_BE_INFERRED_FROM_LATE_BOUND_EXPRESSION
  }

  function f1_1( p1 = BOOL_FIELD_INFERRED || true ) {  //## issuekeys: MSG_PARAM_TYPE_CANT_BE_INFERRED_FROM_LATE_BOUND_EXPRESSION
  }

  function f2( p1 : boolean = BOOL_FIELD_INFERRED ) {
  }

  function f3( p1 = BOOL_FIELD_EXPLICIT ) {
  }

  function f4( p1 : boolean = BOOL_FIELD_EXPLICIT ) {
  }
}
