package gw.specContrib.interfaceMethods.staticMethods.javaInteraction

class Errant_GosuTestClass1 {
  //Case#1
  interface GosuInterface1A extends JavaInterfaces.JavaInterfaceWithStaticMethod {
    static function foo(){
    }
  }
  interface GosuInterface1B<T> extends JavaInterfaces.JavaInterfaceWithStaticMethod {
    function foo(){
    }
  }
  class GosuClass1 implements JavaInterfaces.JavaInterfaceWithStaticMethod {
    function test() {
      foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      this.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      GosuClass1.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      JavaInterfaces.JavaInterfaceWithStaticMethod.foo()
      super.foo()      //## issuekeys: CANNOT RESOLVE METHOD 'FOO()'
      super[JavaInterfaces.JavaInterfaceWithStaticMethod].foo()

    }
  }
  class GosuClass1A implements GosuInterface1A {
    function test() {
      foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      this.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      GosuClass1A.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      GosuInterface1A.foo()
      super.foo()      //## issuekeys: CANNOT RESOLVE METHOD 'FOO()'
      //IDE-2618 - OS Gosu issue. Should not be able to resolve static method with super map keyword
      super[GosuInterface1A].foo()
    }
  }
  class GosuClass1B implements GosuInterface1B {
    function test() {
      foo()
      this.foo()
      GosuClass1B.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      GosuInterface1B.foo()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      super.foo()      //## issuekeys: CANNOT RESOLVE METHOD 'FOO()'
      super[GosuInterface1B].foo()
      super[GosuInterface1B<Object>].foo() //## issuekeys: MSG_PARAMETERIZED_TYPE_NOT_ALLOWED_HERE

    }
  }
  //Case#2
  interface GosuInterface2A extends JavaInterfaces.JavaInterfaceWithDefaultMethod {
    //IDE-2594 - OS Gosu Issue.
    static function bar(){      //## issuekeys: STATIC METHOD 'BAR()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.JAVAINTERACTION.ERRANT_GOSUTESTCLASS1.GOSUINTERFACE2A' CANNOT OVERRIDE INSTANCE METHOD 'BAR()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.JAVAINTERACTION.JAVAINTERFACES.JAVAINTERFACEWITHDEFAULTMETHOD'
    }
  }
  interface GosuInterface2B extends JavaInterfaces.JavaInterfaceWithDefaultMethod {
    function bar(){
    }
  }
  class GosuClass2 implements JavaInterfaces.JavaInterfaceWithDefaultMethod {
    function test() {
      //IDE-2594 - OS Gosu Issue.
      bar()
      this.bar()
      GosuClass2.bar()      //## issuekeys: NON-STATIC METHOD 'BAR()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      JavaInterfaces.JavaInterfaceWithDefaultMethod.bar()      //## issuekeys: NON-STATIC METHOD 'BAR()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      super.bar()      //## issuekeys: CANNOT RESOLVE METHOD 'BAR()'
      super[JavaInterfaces.JavaInterfaceWithDefaultMethod].bar()

    }
  }
  class GosuClass2A implements GosuInterface2A {      //## issuekeys: STATIC METHOD 'BAR()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.JAVAINTERACTION.ERRANT_GOSUTESTCLASS1.GOSUINTERFACE2A' CANNOT OVERRIDE INSTANCE METHOD 'BAR()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.JAVAINTERACTION.JAVAINTERFACES.JAVAINTERFACEWITHDEFAULTMETHOD'
    function test() {
      bar()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      this.bar()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      GosuClass2A.bar()      //## issuekeys: STATIC METHOD MAY BE INVOKED ON CONTAINING INTERFACE CLASS ONLY
      GosuInterface2A.bar()
      super.bar()      //## issuekeys: CANNOT RESOLVE METHOD 'BAR()'
      super[GosuInterface2A].bar()
    }
  }
  class GosuClass2B implements GosuInterface2B {
    function test() {
      bar()
      this.bar()
      GosuClass2B.bar()      //## issuekeys: NON-STATIC METHOD 'BAR()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      GosuInterface2B.bar()      //## issuekeys: NON-STATIC METHOD 'BAR()' CANNOT BE REFERENCED FROM A STATIC CONTEXT
      super.bar()      //## issuekeys: CANNOT RESOLVE METHOD 'BAR()'
      super[GosuInterface2B].bar()

    }
  }
}