package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

class ParameterizedCompoundTypeVarSecond<T>
{
  function narrow( item : T ) : T
  {
    if( item typeis Date )
    {
      var items = {item}
      return items.get( 0 )
    }
    return null
  }
}
