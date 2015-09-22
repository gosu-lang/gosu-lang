package gw.specContrib.classes

class Errant_ExtendArray2 {
  var x: Object[] & Runnable         //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE
  class B<T extends Object[] & Runnable> {}  //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE
}