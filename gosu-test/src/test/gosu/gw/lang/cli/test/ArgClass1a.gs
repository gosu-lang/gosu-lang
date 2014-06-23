package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClass1a {

  public static var INSTANCE : ArgClass1a = new ArgClass1a()

  /**
   * A plain string argument
   */
  var _plainStrArg : String as PlainStrArg

  /**
   * A plain boolean argument
   */
  var _plainBoolArg : Boolean as PlainBoolArg

  /**
   * A string argument with a short name
   */
  @ShortName( "s" )
  var _strWithShortArg : String as StrWithShortArg

  /**
   * A string argument with a short name and default
   */
  @ShortName( "d" )
  @DefaultValue( "defaultValue" )
  var _strWithShortArgAndDefaultValue : String as StrWithShortArgAndDefaultValue

  /**
   * A string array argument
   */
  var _strings : String[] as StringsArg

}