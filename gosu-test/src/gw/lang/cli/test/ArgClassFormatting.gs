package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClassFormatting {

  /**
   * A plain string argument. The line is long but the message is short. This should be broken better.
   */
  static var _plainStrArg : String as PlainStrArg

  /**
   * A plain boolean argument.
   * The second line of a short boolean argument.
   */
  static var _plainBoolArg : Boolean as PlainBoolArg

  /**
   * A string argument with a short name.
   * A second long line of an argument with a short name but lots of description. This could be said better with less.
   */
  @ShortName( "s" )
  static var _strWithShortArg : String as StrWithShortArg

  /**
   * Run the database consistency checks as a batch job. See the Admin Guide page for details.
   */
  @ShortName( "checkdbconsistencyasbatch" )
  @LongName( "checkdbconsistencyasbatch" )
  @ArgNames( {"tableselection", "checktypeselection"} )
  @ArgOptional
  static var _checkDBConsistencyAsBatch : String[] as CheckDBConsistencyAsBatch

  /**
   * A string array argument
   * that
   * has too many
   * comment lines.
   */
  static var _strings : String[] as StringsArg

}
