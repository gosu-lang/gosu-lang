package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

class NarrowedTypeVarOneFunction
{
  function total<T>( items : List<T> ) : long
  {
    var result : long = 0
    for( item in items )
    {
      if( item typeis Date )
      {
        result += item.Time
      }
    }
    return result
  }
}
