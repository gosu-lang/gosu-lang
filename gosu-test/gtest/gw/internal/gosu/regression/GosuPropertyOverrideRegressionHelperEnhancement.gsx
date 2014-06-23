package gw.internal.gosu.regression

enhancement GosuPropertyOverrideRegressionHelperEnhancement : gw.internal.gosu.regression.GosuPropertyOverrideRegressionHelper {
  
  // Instance properties
  
  property get EnhancementBoolean() : Boolean {
    return true  
  }
  
  property set EnhancementBoolean(value : Boolean) {
    
  }
  
  property get EnhancementPBoolean() : boolean {
    return true  
  }
  
  property set EnhancementPBoolean(value : boolean) {
    
  }
  
  property get EnhancementPInt() : int {
    return 11  
  }
  
  property set EnhancementPInt(value : int) {
    
  }
  
  property get EnhancementString() : String {
    return "test"  
  }
  
  property set EnhancementString(value : String) {
    
  }
  
  // Static properties
  
  static property get StaticEnhancementBoolean() : Boolean {
    return true  
  }
  
  static property set StaticEnhancementBoolean(value : Boolean) {
    
  }
  
  static property get StaticEnhancementPBoolean() : boolean {
    return true  
  }
  
  static property set StaticEnhancementPBoolean(value : boolean) {
    
  }
  
  static property get StaticEnhancementPInt() : int {
    return 11  
  }
  
  static property set StaticEnhancementPInt(value : int) {
    
  }
  
  static property get StaticEnhancementString() : String {
    return "test"  
  }
  
  static property set StaticEnhancementString(value : String) {
    
  }
  
  // Instance property get/set methods
  
  function localEnhancementAccessBoolean() : Boolean {
    return EnhancementBoolean  
  }
  
  function localEnhancementAccessPBoolean() : boolean {
    return EnhancementPBoolean  
  }
  
  function localEnhancementAccessPInt() : int {
    return EnhancementPInt  
  }
  
  function localEnhancementAccessString() : String {
    return EnhancementString  
  }
  
  function localEnhancementSetBoolean( value : Boolean) { 
    EnhancementBoolean = value  
  }
  
  function localEnhancementSetPBoolean( value : boolean) {
    EnhancementPBoolean = value  
  }
  
  function localEnhancementSetPInt( value : int ) {
    EnhancementPInt = value  
  }
  
  function localEnhancementSetString( value : String ) {
    EnhancementString = value
  }
  
  // Static property get/set methods
  
  static function localStaticEnhancementAccessBoolean() : Boolean {
    return StaticEnhancementBoolean  
  }
  
  static function localStaticEnhancementAccessPBoolean() : boolean {
    return StaticEnhancementPBoolean  
  }
  
  static function localStaticEnhancementAccessPInt() : int {
    return StaticEnhancementPInt  
  }
  
  static function localStaticEnhancementAccessString() : String {
    return StaticEnhancementString  
  }
  
  static function localStaticEnhancementSetBoolean( value : Boolean) { 
    StaticEnhancementBoolean = value  
  }
  
  static function localStaticEnhancementSetPBoolean( value : boolean) {
    StaticEnhancementPBoolean = value  
  }
  
  static function localStaticEnhancementSetPInt( value : int ) {
    StaticEnhancementPInt = value  
  }
  
  static function localStaticEnhancementSetString( value : String ) {
    StaticEnhancementString = value
  }
}
