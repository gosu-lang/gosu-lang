package gw.specContrib.interfaceMethods.staticMethods

class Errant_StaticMethodsWithKeywords {

  interface AbstractFinalStatic {
    static function NoBodyFunction()      //## issuekeys: STATIC METHODS NOT ALLOWED IN INTERFACES

    abstract static function hello() {      //## issuekeys: ILLEGAL COMBINATION OF MODIFIERS: 'ABSTRACT' AND 'STATIC'
    }

    final static function hello2() {      //## issuekeys: ILLEGAL COMBINATION OF MODIFIERS: 'FINAL' AND 'STATIC'
    }
  }

}