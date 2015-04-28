package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

uses java.lang.Integer

class Errant_MethodOverrideInGenericsTest {
  static interface I10<T>{
    function id(x : T) : T
  }

  static class C10<T>{
    function id(x : T) : T {
      return x
    }
  }

  static class D10 extends C10<String> implements I10<Integer> {
    override function id(x: Integer) : Integer {
      return x
    }
    override function id(x: String) : String {
      return x
    }
  }

  static class D20 extends C10<String> implements I10<Integer> {
    override function id(x: Integer) : Integer {
      return x
    }
  }

  function testMethodOverrideInGenerics(){
    var obj1 = new D10()
    obj1.id(new Integer(1))
    obj1.id("first")

    var obj2 = new D20()
    obj2.id(new Integer(2))
    obj2.id("second")
  }

}