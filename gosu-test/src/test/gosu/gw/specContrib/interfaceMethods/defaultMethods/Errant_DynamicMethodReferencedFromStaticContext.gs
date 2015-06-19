package gw.specContrib.interfaceMethods.defaultMethods

class Errant_DynamicMethodReferencedFromStaticContext {
  interface Super {
    function foo() {
    }
  }

  interface Sub extends Super {
    //cannot be referenced from static context
    static function test() {
      foo()      //## issuekeys: NON-STATIC METHOD 'FOO()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      this.foo()      //## issuekeys: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DYNAMICMETHODREFERENCEDFROMSTATICCONTEXT.SUB.THIS' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      super[Super].foo()      //## issuekeys: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DYNAMICMETHODREFERENCEDFROMSTATICCONTEXT.SUB.SUPER' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      super.foo()      //## issuekeys: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DYNAMICMETHODREFERENCEDFROMSTATICCONTEXT.SUB.SUPER' CANNOT BE REFERENCED FROM A STATIC CONTEXT
    }
  }


}