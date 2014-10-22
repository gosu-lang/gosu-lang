package gw.specContrib.types

class Errant_CompoundType {

  var bad1 : int & java.lang.Runnable //## issuekeys: MSG_NO_PRIMITIVE_IN_COMPONENT_TYPE
  var bad2 : String[] & java.lang.Runnable //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE

}