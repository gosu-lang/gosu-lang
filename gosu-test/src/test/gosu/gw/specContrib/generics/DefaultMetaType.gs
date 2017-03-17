package gw.specContrib.generics

uses gw.lang.reflect.IType

class DefaultMetaType {
  function foo(): Object {
    var t: IType = String
    return assertOnPage( t as Type )
  }

  reified function assertOnPage<T>( pageType : Type<T>) : T {
    return (pageType as java.lang.Class<T>).newInstance();
  }
}