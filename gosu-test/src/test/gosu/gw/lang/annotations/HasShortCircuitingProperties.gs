package gw.lang.annotations

class HasShortCircuitingProperties {

  construct() {

  }
  
  property get NonShortCircuitBooleanProperty() : boolean {
    return true  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitBooleanProperty() : boolean {
    return true  
  }
  
  property get NonShortCircuitByteProperty() : byte {
    return 11  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitByteProperty() : byte {
    return 11  
  }
  
  property get NonShortCircuitShortProperty() : short {
    return 11  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitShortProperty() : short {
    return 11  
  }
  
  property get NonShortCircuitCharProperty() : char {
    return 11 as char  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitCharProperty() : char {
    return 11 as char  
  }
  
  property get NonShortCircuitIntProperty() : int {
    return 11  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitIntProperty() : int {
    return 11
  }
  
  property get NonShortCircuitLongProperty() : long {
    return 11  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitLongProperty() : long {
    return 11
  }
  
  property get NonShortCircuitFloatProperty() : float {
    return 11  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitFloatProperty() : float {
    return 11
  }
  
  property get NonShortCircuitDoubleProperty() : double {
    return 11  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitDoubleProperty() : double {
    return 11
  }
  
  property get NonShortCircuitStringProperty() : String {
    return "11"  
  }
  
  @ShortCircuitingProperty
  property get ShortCircuitStringProperty() : String {
    return "11"
  }
  
  property get AList() : List {
    return null
  }
   
  property get AnArray() : Object[] {
    return null
  }

  property get Chained() : HasShortCircuitingProperties {
    return null
  }
}
