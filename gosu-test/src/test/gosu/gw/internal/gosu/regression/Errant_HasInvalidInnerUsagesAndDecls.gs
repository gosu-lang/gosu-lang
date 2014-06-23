package gw.internal.gosu.regression
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_HasInvalidInnerUsagesAndDecls {

  interface InnerInterface1 {}
  
  class NonStaticInnerClass1 {
    interface InnerInnerInterface1{}
    static class InnerInnerClass1{}
    static function StaticInnerInnerFunction(){}
    static property get StaticInnerInnerProp() : String { return null }
    public static var _staticInnerInnerVar : String
  }

  static class StaticInnerClass1 {
    interface InnerInnerInterface1{}
  }
  
  static function staticFunc() {
    var nono = new NonStaticInnerClass1()
    var nono2 = new NonStaticInnerClass1.InnerInnerClass1()
  }
   
  interface ExampleIFace {
  }
    
  static function staticFunc2() {
    var x = new ExampleIFace(){}
    var y = new StaticInnerClass1(){}
  }
}
