package gw.lang.spec_old.generics
uses java.lang.CharSequence

class SelfBound<T extends SelfBound>
{
  var _t : T as Tee

  function testDefaultParameterization() : SelfBound<SelfBound> {
    var defaultParameterization : SelfBound<SelfBound>
    defaultParameterization = this
    return defaultParameterization
  }
  
  function testGenericType() : SelfBound {
    var genericType : SelfBound
    genericType = this  
    return genericType
  } 
  
}
