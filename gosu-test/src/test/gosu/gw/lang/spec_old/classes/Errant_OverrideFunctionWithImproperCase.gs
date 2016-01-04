package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_OverrideFunctionWithImproperCase extends TestClassForOverrideWithImproperCase {

  override function shouldhavehumps() {

  } 
  
  override property get onehump() : String { return "onehump" }
  override property set twoHump( v: String ) {}
}
