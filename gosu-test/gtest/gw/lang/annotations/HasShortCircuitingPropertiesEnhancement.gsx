package gw.lang.annotations

enhancement HasShortCircuitingPropertiesEnhancement : gw.lang.annotations.HasShortCircuitingProperties {

  function callShortCircuitingIntPropInEnhancement() : int {
    return this?.Chained?.ShortCircuitIntProperty
  }

  @ShortCircuitingProperty
  property get ShortCircuitIntPropertyEnh() : int {
    return 11
  }
  
  function callCountMethodOnList() : Object {
    return this?.AList?.Count
  }
  
  function callCountMethodOnArray() : Object {
    return this?.AnArray?.Count
  }  
}
