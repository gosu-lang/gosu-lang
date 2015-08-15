package gw.specContrib.classes

class Errant_ExtendArray extends Object[] {  //## issuekeys: MSG_CANNOT_EXTEND_ARRAY
  var x: Object[] & Runnable         //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE
  class B<T extends Object[] & Runnable> {}  //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE
}