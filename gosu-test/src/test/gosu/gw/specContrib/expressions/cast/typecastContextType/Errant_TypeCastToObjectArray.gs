package gw.specContrib.expressions.cast.typecastContextType

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
    bar({"SF", "LA"} as Object[]) //infer type from component type because of typecasting   //## issuekeys: MSG_UNNECCESARY_COERCION
    bar({new String("hello"), new String("world")} as Object[]) //infer type from component type because of typecasting    //## issuekeys: MSG_UNNECCESARY_COERCION

    var xx : Object[] = {"sdfds", "sdf"}
    bar(xx)

    var ss : String[] = {"sdfds", "sdf"}
    bar(ss)
    bar(ss as Object[])    //## issuekeys: MSG_UNNECCESARY_COERCION
  }

  private function bar(strings: Object[]) {

  }

  function foo2() {
    bar({1,2,3} as Object[])    //## issuekeys: MSG_UNNECCESARY_COERCION

    bar1({1, 2, 3} as ArrayList<Integer>)  //arraylist should work as is. Type inferred from context    //## issuekeys: MSG_UNNECCESARY_COERCION

    bar2({1, 2, 3} as Integer[])  //inferred type should be boxed    //## issuekeys: MSG_UNNECCESARY_COERCION

    bar3({1, 2, 3} as int[])    //## issuekeys: MSG_UNNECCESARY_COERCION

    bar1({1.0, 2.0, 3.0} as ArrayList<Integer>)      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  }
  private function bar1(integers: ArrayList<Object>) {

  }

  private function bar2(integers: Object[]) {

  }

  private function bar3(strings: int[]) {

  }

}
