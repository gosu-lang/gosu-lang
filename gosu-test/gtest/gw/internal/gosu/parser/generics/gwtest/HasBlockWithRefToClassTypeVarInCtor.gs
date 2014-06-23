package gw.internal.gosu.parser.generics.gwtest
uses java.lang.Comparable

class HasBlockWithRefToClassTypeVarInCtor<T>
{
  var _f( T ) : Comparable

  construct( f( T ) : Comparable )
  {
    _f = f
  }

  function callF( t : T ) : Comparable
  {
    return _f( t )
  }
}
