package gw.specContrib.interfaceMethods.defaultMethods

class Errant_DefaultMethodsConflict {

  //2 interfaces having the same default method and implemented by the same class
  interface InterfaceOne {
    function sameFunction(){}
  }
  interface InterfaceTwo {
    function sameFunction(){}
  }
  class MyClassOne implements InterfaceOne, InterfaceTwo {        //## issuekeys: GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSCONFLICT.MYCLASSONE INHERITS UNRELATED DEFAULTS FOR SAMEFUNCTION() FROM TYPES GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSCONFLICT.INTERFACEONE AND GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSCONFLICT.INTERFACETWO

  }

  class MyClassTwo implements InterfaceOne, InterfaceTwo {
    override function sameFunction() {}
  }

}