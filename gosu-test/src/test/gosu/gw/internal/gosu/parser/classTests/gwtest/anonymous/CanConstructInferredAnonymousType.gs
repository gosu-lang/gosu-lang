package gw.internal.gosu.parser.classTests.gwtest.anonymous

uses gw.internal.gosu.parser.classTests.gwtest.anonymous.JavaClass

class CanConstructInferredAnonymousType {
  function create() : Object {
    var a : Object = new () {}
    return a
  }
}
