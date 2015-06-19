package gw.specContrib.interfaceMethods.staticMethods

interface Errant_SameNameStaticDefaultInSameInterface {
  function foo(){
  }
  static function foo() {      //## issuekeys: 'FOO()' IS ALREADY DEFINED IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.STATICMETHODS.ERRANT_STATICDEFAULTCONFLICT_2'
  }
  static function foo(s: String) {
  }

}