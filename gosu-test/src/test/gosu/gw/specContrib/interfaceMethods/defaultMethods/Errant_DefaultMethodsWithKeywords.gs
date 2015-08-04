package gw.specContrib.interfaceMethods.defaultMethods

class Errant_DefaultMethodsWithKeywords {
  interface MyInterface {
    function foo()
    function bar() {}

    abstract function hello() {}      //## issuekeys: ILLEGAL COMBINATION OF MODIFIERS: 'ABSTRACT' AND 'DEFAULT'

    final function somefun() {}

  }

}