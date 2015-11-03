package gw.specContrib.types

uses java.io.Serializable
uses java.lang.Cloneable

class Errant_CompoundType {

  var bad1 : int & java.lang.Runnable //## issuekeys: MSG_NO_PRIMITIVE_IN_COMPONENT_TYPE
  var bad2 : String[] & java.lang.Runnable //## issuekeys: MSG_NO_ARRAY_IN_COMPONENT_TYPE

  class Type1 {}
  class Type2 {}

  interface Intf {}
  class IntfImpl implements Intf {}

  var correctOne: Type1 & Intf
  var withPrimitive: Type1 & int                  //## issuekeys: MSG_
  var withArray: Type1 & Intf[]                   //## issuekeys: MSG_
  var withUnresolved: Type1 & Type666             //## issuekeys: MSG_
  var withTwoClasses: Type1 & Intf & Type2        //## issuekeys: MSG_
  var withDuplicateType: Intf & Type1 & Intf      //## issuekeys: MSG_
  var withRedundantType: Intf & IntfImpl          //## issuekeys: MSG_

  function test() {
    // IDE-1942
    var sc: Serializable & Cloneable
    var i = sc.hashCode()
  }

  function testCompoundAndDynamic() {
    var v1: java.io.Serializable & dynamic.Dynamic  //## issuekeys: MSG_INTERFACE_REDUNDANT
    var v2: java.io.Serializable = v1
  }

  interface foo {}
  function testCompoundTypes() {
    var param: java.lang.Comparable<java.lang.Integer> & foo
    var q1 = param < 11000  //## issuekeys: MSG_
  }

}