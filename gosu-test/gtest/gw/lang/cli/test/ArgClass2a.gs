package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClass2a {

  public static var INSTANCE : ArgClass2a = new ArgClass2a()

  /**
   * A required string argument with a short name and default
   */
  @Required
  var _strRequired : String as RequiredArg

}