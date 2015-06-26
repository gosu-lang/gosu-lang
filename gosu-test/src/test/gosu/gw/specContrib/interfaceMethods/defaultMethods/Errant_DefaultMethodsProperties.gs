package gw.specContrib.interfaceMethods.defaultMethods

uses java.lang.Integer

class Errant_DefaultMethodsProperties {

  //same default property
  interface MyInterface1 {
    property get Age(): Integer {
      return null
    }
  }

  interface MyInterface2 {
    property get Age(): Integer {
      return null
    }
  }

  class MyClass12 implements MyInterface1, MyInterface2 {      //## issuekeys: GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSPROPERTIES.MYCLASS12 INHERITS UNRELATED DEFAULTS FOR GETAGE() FROM TYPES GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSPROPERTIES.MYINTERFACE1 AND GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSPROPERTIES.MYINTERFACE2
  }

  //same default property but overridden in implementing class so no error
  interface MyInterface3 {
    property get Age(): Integer {
      return null
    }
  }

  interface MyInterface4 {
    property get Age(): Integer {
      return null
    }
  }

  class MyClass34 implements MyInterface3, MyInterface4 {
    override property get Age(): Integer {
      return null
    }
  }
}