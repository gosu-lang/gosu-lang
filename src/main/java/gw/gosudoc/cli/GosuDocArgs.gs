package gw.gosudoc.cli

uses gw.lang.cli.*

class GosuDocArgs {

  @LongName( "in" ) @ShortName( "i" ) @ArgOptional
  var _in : String as In

  @LongName( "out" ) @ShortName( "o" ) @ArgOptional
  var _out : String as Out

  function toString() : String {
    return (typeof this).TypeInfo.Properties.map( \ p -> "${p.Name} : ${p.Accessor.getValue(this)}" ).join( ", " )
  }
}