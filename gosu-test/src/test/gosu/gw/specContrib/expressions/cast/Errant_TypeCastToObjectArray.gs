package gw.specContrib.expressions.cast

class Errant_TypeCastToObjectArray {
    //IDE-1524
    function testCast{
          var y : Object[] = {1,2}

          var a101 = {1, 2, 3} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES
          var a102 = {"hello", "world"} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES
          var a103 = {new String("hello"), new String("world")} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES
          var a104 = {new Object(), new Object()} as Object[]   //## issuekeys: ERROR: INCONVERTIBLE TYPES

          var z = {1,2}
          var x = z as Object[]            //## issuekeys: ERROR: INCONVERTIBLE TYPES
    }
}
