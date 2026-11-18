package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

class NarrowedTypeVarUsedAsTypeVar
{
  function returned<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      return item
    }
    return null
  }

  function assigned<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      var result : T = item
      return result
    }
    return null
  }

  function inferred<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      var result = item
      return result
    }
    return null
  }

  function passed<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      return same( item )
    }
    return null
  }

  reified function reifiedReturned<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      return item
    }
    return null
  }

  private function same<U>( u : U ) : U
  {
    return u
  }
}
