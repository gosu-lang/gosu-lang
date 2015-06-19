package gw.specContrib.interfaceMethods.staticMethods

class Errant_ParentWithDefaultChildWithStaticMethod {
  interface IA {
    function bar() {
    }
  }

  interface IB extends IA {
    //static method cannot override instance method
    static function bar() {            //## issuekeys: STATIC METHOD 'BAR()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.ERRANT_STATICDEFAULTCONFLICT_1.IB' CANNOT OVERRIDE INSTANCE METHOD 'BAR()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.ERRANT_STATICDEFAULTCONFLICT_1.IA'
    }
  }
}