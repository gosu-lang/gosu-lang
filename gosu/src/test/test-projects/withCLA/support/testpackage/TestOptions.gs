package testpackage

uses gw.lang.cli.LongName

class TestOptions {

  @LongName("foo")
  static var _foo : String as Foo

  @LongName("bar")
  static var _bar : boolean as Bar

}