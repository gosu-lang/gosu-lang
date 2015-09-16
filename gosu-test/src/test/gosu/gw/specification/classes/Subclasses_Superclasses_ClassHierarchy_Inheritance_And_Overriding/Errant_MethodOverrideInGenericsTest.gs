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

  static class D10 extends C10<String> implements I10<Integer> {  //## issuekeys: MSG_FUNCTION_CLASH_PARAMS
    override function id(x: Integer) : Integer {
      return x
    }
    override function id(x: String) : String {
      return x
    }
  }

  static class D20 extends C10<String> implements I10<Integer> {  //## issuekeys: MSG_FUNCTION_CLASH_PARAMS
    override function id(x: Integer) : Integer {
      return x
    }
  }

  interface Fred<T> { function foo( t: T ) }
  interface Barney<T> { function foo( t: T ) }
  abstract class TestMe implements Fred<Integer>, Barney<String>  //## issuekeys: MSG_FUNCTION_CLASH_PARAMS
  {
  }
}