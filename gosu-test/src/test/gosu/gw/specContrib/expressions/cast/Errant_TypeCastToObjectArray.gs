package gw.specContrib.expressions.cast

class Errant_TypeCastToObjectArray {
  //IDE-1524
  function testCast() {
    var y: Object[] = {1, 2}

    var a101 = {1, 2, 3} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES
    var a102 = {"hello", "world"} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES
    var a103 = {new String("hello"), new String("world")} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES
    var a104 = {new Object(), new Object()} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES

    var z = {1, 2}
    var x = z as Object[]            //## issuekeys: ERROR: INCONVERTIBLE TYPES
  }

  //Tests for IDE-4083
  function foo() {
    bar({"SF", "LA"})  //infer type from context
    bar({"SF", "LA"} as Object[]) //infer type from component type because of typecasting
    bar({new String("hello"), new String("world")} as Object[]) //infer type from component type because of typecasting

    var xx : Object[] = {"sdfds", "sdf"}
    bar(xx)

    var ss : String[] = {"sdfds", "sdf"}
    bar(ss)
    bar(ss as Object[])
  }

  private function bar(strings: Object[]) {

  }

  function foo2() {
    bar({1,2,3} as Object[])

    bar1({1, 2, 3} as ArrayList<Integer>)  //arraylist should work as is. Type inferred from context

    bar2({1, 2, 3} as Integer[])  //inferred type should be boxed

    bar3({1, 2, 3} as int[])

  }
  private function bar1(integers: ArrayList<Object>) {

  }

  private function bar2(integers: Object[]) {

  }

  private function bar3(strings: int[]) {

  }

}
