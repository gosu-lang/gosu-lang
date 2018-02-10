package gw.specification.metaDataAnnotations

uses java.lang.CharSequence

class Errant_AnnotationTypes {

  annotation MyAnno_1 {
  }

  annotation MyAnno_1_1<T> {  //## issuekeys: MSG_GENERIC_ANNOTATIONS_NOT_SUPPORTED
  }

  annotation MyAnno_2 extends java.util.ArrayList {  //## issuekeys: MSG_NO_EXTENDS_ALLOWED, MSG_INTERFACE_CANNOT_EXTEND_CLASS
  }

  annotation MyAnno_3 extends java.lang.CharSequence {  //## issuekeys: MSG_NO_EXTENDS_ALLOWED
  }

  annotation MyAnno_4 implements CharSequence {  //## issuekeys: MSG_NO_IMPLEMENTS_ALLOWED
  }

  annotation MyAnno_5 {
   function value() : String
  }

  annotation MyAnno_6 {
   function value() : String = "hi"
  }

  annotation MyAnno_7 {
   function value() : int = String  //## issuekeys: MSG_TYPE_MISMATCH
  }

  annotation MyAnno_8 {
   function value() : String
   function stuff() : String
  }

  annotation MyAnno_9 {
   function value() : String = "hi"
   function stuff() : String
  }

  annotation MyAnno_10 {
   function value() : String = "hi"
   function stuff() : String = "bye"
  }

  annotation MyAnno_11 {
   function value() : String
   function stuff() : String = "bye"
  }

  annotation MyAnno_12 {
    function stuff0() : boolean = true
    function stuff1() : char = 'c'
    function stuff2() : byte = 1
    function stuff3() : short = 1
    function stuff4() : int = 1
    function stuff5() : long = 1
    function stuff6() : float = 1.0
    function stuff7() : double = 1.0
    function stuff8() : String = "hi"
    function stuff9() : manifold.api.type.ClassType = Enhancement
    function stuff10() : java.lang.Class = java.lang.String
    function stuff11() : gw.lang.Deprecated = @gw.lang.Deprecated("hi")

    function stuff_array0() : boolean[] = {true, false}
    function stuff_array1() : char[] = {'a', 'b', 'c'}
    function stuff_array2() : byte[] = {0,1}
    function stuff_array3() : short[] = {0,1}
    function stuff_array4() : int[] = {0,1}
    function stuff_array5() : long[] = {0,1}
    function stuff_array6() : float[] = {1.0, 1.1}
    function stuff_array7() : double[] = {1.0,1.1}
    function stuff_array8() : String[] = {"hi", "bye"}
    function stuff_array9() : manifold.api.type.ClassType[] = {Enhancement, Enhancement}
    function stuff_array10() : java.lang.Class[] = {java.lang.String, java.lang.Byte}
   //## todo: function stuff_array11() : gw.lang.Deprecated[] = {@gw.lang.Deprecated("hi")}

    // Error types
    function stuff_e1() : java.lang.StringBuilder  //## issuekeys: MSG_INVALID_TYPE_FOR_ANNOTATION_MEMBER
    function stuff_e2() : java.lang.Class = java.util.ArrayList<String>  //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED
    function stuff_e3() : Object  //## issuekeys: MSG_INVALID_TYPE_FOR_ANNOTATION_MEMBER
    function stuff_e4() : void  //## issuekeys: MSG_INVALID_TYPE_FOR_ANNOTATION_MEMBER
    function stuff_e5() : String = null //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED
    function stuff_e6()  //## issuekeys: MSG_EXPECTING_RETURN_TYPE_OR_FUN_BODY
  }
}