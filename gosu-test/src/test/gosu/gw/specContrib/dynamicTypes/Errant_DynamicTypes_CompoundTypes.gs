package gw.specContrib.dynamicTypes

uses java.io.Serializable

class Errant_DynamicTypes_CompoundTypes {
  interface MyInterface1 extends Serializable{}
  interface MyInterface2{}

  var bb1: java.io.Serializable & dynamic.Dynamic      //## issuekeys: INTERFACE DYNAMIC IS REDUNDANT
  var bb2: java.io.Serializable  & MyInterface1      //## issuekeys: TYPE IS ALREADY USED IN A COMPONENT TYPE SINCE 'GW.SPECCONTRIB.DYNAMICTYPES.ERRANT_DYNAMICTYPES_COMPOUNDTYPES.MYINTERFACE1' EXTENDS/IMPLEMENTS 'JAVA.IO.SERIALIZABLE'
  var bb3: java.io.Serializable  & MyInterface1 & dynamic.Dynamic      //## issuekeys: TYPE IS ALREADY USED IN A COMPONENT TYPE SINCE 'GW.SPECCONTRIB.DYNAMICTYPES.ERRANT_DYNAMICTYPES_COMPOUNDTYPES.MYINTERFACE1' EXTENDS/IMPLEMENTS 'JAVA.IO.SERIALIZABLE'
  var cc : Dynamic & MyInterface1      //## issuekeys: INTERFACE DYNAMIC IS REDUNDANT
  var dd : MyInterface1 & Dynamic & Comparator      //## issuekeys: INTERFACE DYNAMIC IS REDUNDANT
  var ee : MyInterface1 & Comparator & MyInterface2 & Dynamic      //## issuekeys: INTERFACE DYNAMIC IS REDUNDANT
  var ff : MyInterface1 & Comparator & Dynamic & MyInterface2      //## issuekeys: INTERFACE DYNAMIC IS REDUNDANT
  var gg : Dynamic
}