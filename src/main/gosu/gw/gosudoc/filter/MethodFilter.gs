package gw.gosudoc.filter

uses gw.lang.reflect.IMethodInfo
uses gw.lang.reflect.IPropertyInfo

structure MethodFilter {
  function shouldIncludeMethod( m : IMethodInfo ) : boolean
}