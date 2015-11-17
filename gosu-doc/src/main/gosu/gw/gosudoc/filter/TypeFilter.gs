package gw.gosudoc.filter

uses gw.lang.reflect.IType

structure TypeFilter {
  function shouldIncludeType( t : IType ) : boolean
}