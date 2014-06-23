package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClass3a {

  public static var INSTANCE : ArgClass3a = new ArgClass3a()

  /**
   * A plain string argument with no argument needed
   */
   @ArgOptional
  var _plainStrArg : String as NoArgNeededStr

  /**
   * A named argument
   */
  @ArgNames( { "test name" } )
  var _namedArg : String as NamedArg

  /**
   * An array of named arguments
   */
  @ArgNames( { "name1", "name2", "name3" } )
  var _namedArgArray : String[] as NamedArgArray

  /**
   * An array of named arguments, with optional args
   */
  @ArgNames( { "name1", "name2", "name3" } )
  @ArgOptional
  var _namedArgArrayArgOptional : String[] as NamedArgArrayArgOptional

  /**
   * An array of arguments with a default value
   */
  @DefaultValue( "a b c" )
  var _arrayWithDefaults : String[] as ArrayArgWithDefaults

  /**
   * An array of arguments with a default value
   */
  @Separator( "," )
  var _arrayWithSeparator: String[] as ArrayWithSeparator
}