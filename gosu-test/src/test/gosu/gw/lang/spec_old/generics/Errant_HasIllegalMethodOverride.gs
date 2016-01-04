package gw.lang.spec_old.generics
uses java.util.ArrayList
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_HasIllegalMethodOverride<T> extends ArrayList<T>
{ 
  override function add( t : T )  
  {
  }
}
 