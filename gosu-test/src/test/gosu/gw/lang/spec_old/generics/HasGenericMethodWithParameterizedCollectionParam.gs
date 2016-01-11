package gw.lang.spec_old.generics

uses java.util.List

class HasGenericMethodWithParameterizedCollectionParam
{
  function genericMethod<M>( inputs : List<HasSimpleGenericMethod<M>>, refId(a:M) : String) : List<HasSimpleGenericMethod<M>>
  { return null }
}
