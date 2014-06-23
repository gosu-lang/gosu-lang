package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantOverridesEnhancementMethod extends BaseAbstractClass {

  override function methodFromEnhancement() {
  }
  
  override function foo() : String {
    return null
  }

  override property get Bar() : int {
    return 1
  } 

}
