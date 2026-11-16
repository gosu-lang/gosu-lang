package gw.internal.gosu.parser.generics.gwtest

enhancement TypeVarAfterRefreshEnhancement<T> : java.util.List<T>
{
  function fooOf( other : GenericClass<T> ) : T
  {
    return other.foo( null )
  }
}
