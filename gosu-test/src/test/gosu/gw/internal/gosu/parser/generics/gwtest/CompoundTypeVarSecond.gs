package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

class CompoundTypeVarSecond<T>
{
  function narrow( item : T ) : T
  {
    if( item typeis Date )
    {
      return item
    }
    return null
  }
}
