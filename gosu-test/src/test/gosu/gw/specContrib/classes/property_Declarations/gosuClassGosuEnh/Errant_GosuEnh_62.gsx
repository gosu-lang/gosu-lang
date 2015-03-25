package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

uses java.lang.Integer
uses java.util.ArrayList

enhancement Errant_GosuEnh_62: Errant_GosuClass_62 {

  //This should be error - IDE-1817
  function setMyProperty1(a: ArrayList<Integer>) {}                 //## issuekeys: CONFLICT

  //This should be error - IDE-1817
  function setMyProperty2(a: ArrayList<Integer>) {}                //## issuekeys: CONFLICT

  function getMyProperty3() : ArrayList<Integer> {return null}      //## issuekeys: THE METHOD 'GETMYPROPERTY3()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_62'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

  function getMyProperty4() : ArrayList<Integer> {return null}      //## issuekeys: THE METHOD 'GETMYPROPERTY4()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_62'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

}
