package gw.specContrib.classes.abstract_Class

class Errant_AbstractOverride {
  interface MyInterface {
    function foo()
    function bar()
  }

  abstract class MyClass implements MyInterface {
    //IDE-1845
    override abstract function foo()  //no error expected

    //IDE-1845
    abstract override function bar()  //no error expected
  }
}