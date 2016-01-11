package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_CallsAbstractMethodFromSuper extends AbstractClassWithAbstractMethod
{
  override function abstr() : void 
  {
    super.abstr() // err
  }
  
  function canCallAbstractMethodIndirectly()
  {
    var x : AbstractClassWithAbstractMethod
    x.abstr() // ok 
  }
}
