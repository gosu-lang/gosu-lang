package gw.gosudoc.filter

uses gw.lang.reflect.IConstructorInfo

structure ConstructorFilter{
  function shouldIncludeConstructor( m: IConstructorInfo ) : boolean
}