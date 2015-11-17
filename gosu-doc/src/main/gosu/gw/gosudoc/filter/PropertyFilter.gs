package gw.gosudoc.filter

uses gw.lang.reflect.IPropertyInfo

structure PropertyFilter {
  function shouldIncludeProperty( p : IPropertyInfo ) : boolean
}