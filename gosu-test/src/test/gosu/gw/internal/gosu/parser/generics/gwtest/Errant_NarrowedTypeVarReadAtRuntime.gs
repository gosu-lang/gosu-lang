package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

class Errant_NarrowedTypeVarReadAtRuntime
{
  function coerced<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      return item as T  //## issuekeys: MSG_TYPE_NOT_REIFIED
    }
    return null
  }

  function passedToReified<T>( item : T ) : T
  {
    if( item typeis Date )
    {
      return reifiedSame( item )  //## issuekeys: MSG_TYPE_NOT_REIFIED
    }
    return null
  }

  private reified function reifiedSame<U>( u : U ) : U
  {
    return u
  }
}
