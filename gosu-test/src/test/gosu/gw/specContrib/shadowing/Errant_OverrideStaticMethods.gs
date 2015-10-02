package gw.specContrib.shadowing

class Errant_OverrideStaticMethods {

  static class A {
    static function staticFn() {}
    function fn() {}
  }

  static class B extends A {
    //IDE-2148
    function staticFn() {}      //## issuekeys: INSTANCE METHOD 'STATICFN()' IN 'GW.SPECCONTRIB.SHADOWING.ERRANT_OVERRIDESTATICMETHODS.B' CANNOT OVERRIDE STATIC METHOD 'STATICFN()' IN 'GW.SPECCONTRIB.SHADOWING.ERRANT_OVERRIDESTATICMETHODS.A'
    static function fn() {}      //## issuekeys: STATIC METHOD 'FN()' IN 'GW.SPECCONTRIB.SHADOWING.ERRANT_OVERRIDESTATICMETHODS.B' CANNOT OVERRIDE INSTANCE METHOD 'FN()' IN 'GW.SPECCONTRIB.SHADOWING.ERRANT_OVERRIDESTATICMETHODS.A'
  }
}