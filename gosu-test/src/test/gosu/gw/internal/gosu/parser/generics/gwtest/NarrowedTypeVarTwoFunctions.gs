package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

class NarrowedTypeVarTwoFunctions
{
  reified function reifiedTotal<T>( items : List<T> ) : long
  {
    var result : long = 0
    for( item in items )
    {
      if( item typeis Date )
      {
        result += item.Time
      }
      else if( item typeis CharSequence )
      {
        result += item.length()
      }
    }
    return result
  }

  function total<T>( items : List<T> ) : long
  {
    var result : long = 0
    for( item in items )
    {
      if( item typeis Date )
      {
        result += item.Time
      }
      else if( item typeis CharSequence )
      {
        result += item.length()
      }
    }
    return result
  }
}
