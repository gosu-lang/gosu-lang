package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

class CompoundTypeVarTwoFunctions
{
  reified function one<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      return item
    }
    return null
  }

  reified function two<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      return item
    }
    return null
  }
}
