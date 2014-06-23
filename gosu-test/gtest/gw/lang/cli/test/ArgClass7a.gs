package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClass7a {

  public static var INSTANCE : ArgClass7a = new ArgClass7a()

  /**
   * A regular argument
   */
  var _strFoo : String as FooArg

  /**
   * A hidden argument
   */
  @Hidden
  var _strBar : String as BarArg

}