package gw.specContrib.classes

interface Errant_StaticMethodsInInterfaces {
    static function bar() {}     //## issuekeys: INTERFACE METHODS CANNOT HAVE BODY
}