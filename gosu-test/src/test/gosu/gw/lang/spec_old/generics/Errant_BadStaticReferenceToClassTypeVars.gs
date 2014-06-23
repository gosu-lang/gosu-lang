package gw.lang.spec_old.generics
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_BadStaticReferenceToClassTypeVars<T>
{
  static function badStaticFunc( asdf : T ) : T
  {
    print( asdf typeis T )
    var tmp = T
    return asdf
  }  

  function goodNonStaticFunc( asdf : T ) : T
  {
    print( asdf typeis T )
    var tmp = T
    return asdf
  }
  
  static property get badStaticProp() : T
  {
    print( null typeis T )
    var tmp = T
    return null
  }
  
  static property set badStaticProp( asdf : T )
  {
    print( asdf typeis T )
    var tmp = T
  }  

  property get goodNonStaticProp() : T
  {
    print( null typeis T )
    var tmp = T
    return null
  }
  
  property set goodNonStaticProp( asdf : T )
  {
    print( asdf typeis T )
    var tmp = T
  }  
}