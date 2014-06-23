package gw.lang.spec_old.bugs
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
abstract class Errant_DuplicateAbstractPropertiesDefined {
  
 abstract property get Prop1() : String
 abstract property get Prop1() : String
 
 abstract property set Prop2( s : String ) 
 abstract property set Prop2( s : String ) 

 abstract property get Prop3() : String 
 abstract property get Prop3() : String 
 abstract property set Prop3( s : String ) 

 abstract property get Prop4() : String 
 abstract property set Prop4( s : String ) 
 abstract property set Prop4( s : String ) 

}
