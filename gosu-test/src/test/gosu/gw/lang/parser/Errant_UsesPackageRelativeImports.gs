package gw.lang.parser

uses java.*
uses java.util.*
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource 
class Errant_UsesPackageRelativeImports {

  var v1 : util.Map  //## issuekeys: MSG_EXPECTING_TYPE_NAME, MSG_NO_TYPE_ON_NAMESPACE
  var v2 : util.Map.Entry  //## issuekeys: MSG_EXPECTING_TYPE_NAME, MSG_NO_TYPE_ON_NAMESPACE
  var v3 : java.util.Map
  var v4 : java.util.Map.Entry
  var v5 : Map
  var v6 : Map.Entry
  var v7 : Foo
  var v8 : Foo.Bar
  var makeSureNothingShifts : NotAValidTypeName  //## issuekeys: MSG_INVALID_TYPE

  static class Foo {
    static class Bar {

    }
  }

}
