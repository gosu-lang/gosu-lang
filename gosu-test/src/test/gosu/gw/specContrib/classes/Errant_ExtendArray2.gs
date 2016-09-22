package gw.specContrib.classes

class Errant_ExtendArray2 {
  var x: Object[] & Runnable         //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE
  class B<T extends Object[] & Runnable> {}  //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE

  interface MyInterface{}
  class A<T extends int & MyInterface> {}      //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE

  class BB<T extends Object[]> {}
}