package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClass1 {

  /**
   * A plain string argument
   */
  static var _plainStrArg : String as PlainStrArg

  /**
   * A plain boolean argument
   */
  static var _plainBoolArg : Boolean as PlainBoolArg

  /**
   * A string argument with a short name
   */
  @ShortName( "s" )
  static var _strWithShortArg : String as StrWithShortArg

  /**
   * A string argument with a short name and default
   */
  @ShortName( "d" )
  @DefaultValue( "defaultValue" )
  static var _strWithShortArgAndDefaultValue : String as StrWithShortArgAndDefaultValue

  /**
   * A string array argument
   */
  static var _strings : String[] as StringsArg

}