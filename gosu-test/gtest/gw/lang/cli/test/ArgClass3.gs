package gw.lang.cli.test

uses gw.lang.cli.*

class ArgClass3 {

  /**
   * A plain string argument with no argument needed
   */
   @ArgOptional
  static var _plainStrArg : String as NoArgNeededStr

  /**
   * A named argument
   */
  @ArgNames( { "test name" } )
  static var _namedArg : String as NamedArg

  /**
   * An array of named arguments
   */
  @ArgNames( { "name1", "name2", "name3" } )
  static var _namedArgArray : String[] as NamedArgArray

  /**
   * An array of named arguments, with optional args
   */
  @ArgNames( { "name1", "name2", "name3" } )
  @ArgOptional
  static var _namedArgArrayArgOptional : String[] as NamedArgArrayArgOptional

  /**
   * An array of arguments with a default value
   */
  @DefaultValue( "a b c" )
  static var _arrayWithDefaults : String[] as ArrayArgWithDefaults

  /**
   * An array of arguments with a default value
   */
  @Separator( "," )
  static var _arrayWithSeparator: String[] as ArrayWithSeparator
}