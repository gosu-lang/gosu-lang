package gw.specContrib.classes.method_Scoring.objects_And_Primitive_Types

uses java.lang.Integer
uses java.lang.Long

class Errant_ObjectsPrimitives_longAndObject {
  class A{}
  class B{}

  //7. (int, int), (long, long) - (long, Long)
  function foo7(i : long, j : long) : A { return null }
  function foo7(i : Object, j : Object) : B { return null }

  function caller() {
    var a1111 : A =  foo7(42, 42)
    var a1112 : A =  foo7(100L , 100L)
    var a1113 : A =  foo7(42, 100L)
    var a1114 : A =  foo7(100L, 42)
    //IDE-1515
    var a1115 : A =  foo7(42, new Integer(42))
    var a1116 : A =  foo7(new Integer(42), 42)
    var a1117 : A =  foo7(100L, new Long(100))
    var a1118 : A =  foo7(new Long(100), 100L)
    //IDE-1515
    var a1119 : A =  foo7(42, new Long(100))
    var a1120 : A =  foo7(new Integer(42), 100L)
    var a1121 : A =  foo7(new Integer(42), new Long(100))                        //## issuekeys: MSG_TYPE_MISMATCH
    var a1122 : A =  foo7(new Long(100), new Integer(42))                        //## issuekeys: MSG_TYPE_MISMATCH

    var b1111 : B =  foo7(42, 42)                        //## issuekeys: MSG_TYPE_MISMATCH
    var b1112 : B =  foo7(100L , 100L)                        //## issuekeys: MSG_TYPE_MISMATCH
    var b1113 : B =  foo7(42, 100L)                        //## issuekeys: MSG_TYPE_MISMATCH
    var b1114 : B =  foo7(100L, 42)                        //## issuekeys: MSG_TYPE_MISMATCH
    //IDE-1515
    var b1115 : B =  foo7(42, new Integer(42))  //## issuekeys: MSG_TYPE_MISMATCH
    var b1116 : B =  foo7(new Integer(42), 42)  //## issuekeys: MSG_TYPE_MISMATCH
    var b1117 : B =  foo7(100L, new Long(100))                        //## issuekeys: MSG_TYPE_MISMATCH
    var b1118 : B =  foo7(new Long(100), 100L)                        //## issuekeys: MSG_TYPE_MISMATCH
    var b1119 : B =  foo7(42, new Long(100))                        //## issuekeys: MSG_TYPE_MISMATCH
    var b1120 : B =  foo7(new Integer(42), 100L)  //## issuekeys: MSG_TYPE_MISMATCH
    var b1121 : B =  foo7(new Integer(42), new Long(100))
    var b1122 : B =  foo7(new Long(100), new Integer(42))

    var c1111 =  foo7(42, 42)
    var c1112 =  foo7(100L , 100L)
    var c1113 =  foo7(42, 100L)
    var c1114 =  foo7(100L, 42)
    var c1115 =  foo7(42, new Integer(42))
    var c1116 =  foo7(new Integer(42), 42)
    var c1117 =  foo7(100L, new Long(100))
    var c1118 =  foo7(new Long(100), 100L)
    //IDE-1515
    var c1119 =  foo7(42, new Long(100))
    var c1120 =  foo7(new Integer(42), 100L)
    var c1121 =  foo7(new Integer(42), new Long(100))
    var c1122 =  foo7(new Long(100), new Integer(42))

  }
}