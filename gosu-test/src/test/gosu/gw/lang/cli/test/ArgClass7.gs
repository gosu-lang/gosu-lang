package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClass7 {

  /**
   * A regular argument
   */
  static var _strFoo : String as FooArg

  /**
   * A hidden argument
   */
  @Hidden
  static var _strBar : String as BarArg

}