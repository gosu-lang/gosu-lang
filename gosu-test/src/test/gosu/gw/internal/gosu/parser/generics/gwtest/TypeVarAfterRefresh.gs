package gw.internal.gosu.parser.generics.gwtest

class TypeVarAfterRefresh
{
  function one<T>( type : Class<T> ) : T
  {
    return oneImpl( type )
  }

  function many<T>( type : Class<T> ) : T[]
  {
    return manyImpl( type )
  }

  private function oneImpl<T>( type : Class<T> ) : T
  {
    return null
  }

  private function manyImpl<T>( type : Class<T> ) : T[]
  {
    return null
  }
}
