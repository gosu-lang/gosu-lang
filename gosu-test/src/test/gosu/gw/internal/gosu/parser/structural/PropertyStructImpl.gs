package gw.internal.gosu.parser.structural

uses java.lang.CharSequence
uses java.util.Map
uses java.util.HashMap

class PropertyStructImpl {
  var _foo: String
  property get Foo() : String {
    return _foo
  }
  property set Foo( foo: String ) {
    _foo = foo
  }
}