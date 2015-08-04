package gw.specContrib.interfaceMethods.defaultMethods.javaInteraction

uses java.util.HashMap

class Errant_GosuTestClass1 {

  //Gosu class implementing JavaInterface
  class ClassA1 implements JavaInterface1 {
    function test() {
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      var x1 : List = foo()
      var x2 : List = super[JavaInterface1].foo()
    }
  }

  class ClassA2 implements JavaInterface1 {
    override function foo() : List { return null}
    function test() {
      var x1 : List = foo()
      var x2 : List = super[JavaInterface1].foo()  //IDE-2594 - OS Gosu
    }
  }

  class ClassA3 implements JavaInterface1 {
    function foo(s : String) : HashMap { return null}
    function test() {
      var x1 : List = foo()
      var x2 : HashMap = foo("xyz")
      var x3 : List = super[JavaInterface1].foo()
    }
  }


  //Gosu Interface extending Java Interface
  interface GosuInterface1a extends JavaInterface1 {
    function bar() {}
  }
  class TestClassA1 implements GosuInterface1a {
    function test () {
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      foo()
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      super[GosuInterface1a].foo()
      bar()
      super[GosuInterface1a].bar()

    }
  }




  interface GosuInterface1b extends JavaInterface1 {
    override function foo() : List { return null}
    function bar(){}
  }
  class TestClassA2 implements GosuInterface1b {
    function test () {
      foo()
      super[GosuInterface1b].foo()
      bar()
      super[GosuInterface1b].bar()

      hashCode()
      super[Object].hashCode()
      super.hashCode()
    }
  }
  interface GosuInterface1c extends JavaInterface1 {
    function foo(s : String): String { return null}
  }
  class TestClassA3 implements GosuInterface1c {
    function test () {
      var x1 : List = foo()
      var x2 : String = foo("xyz")
      var x3 : List = super[GosuInterface1c].foo()
      var x4 : String = super[GosuInterface1c].foo("xyz")
    }
  }
}