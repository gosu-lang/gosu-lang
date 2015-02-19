package gw.specContrib.classes.property_Declarations.gosuClassGosuSubClass

uses java.lang.Integer
uses java.util.ArrayList

class Errant_GosuSuperGosuSub_6 {
  //Case 6 : Conflict check with disagreement on parameter type
  class Case61Super {

    function setMyProperty1(a: ArrayList<Integer>) {}

    function setMyProperty2(a: ArrayList<Integer>) {}

    function getMyProperty3() : ArrayList<Integer> {return null}

    function getMyProperty4() : ArrayList<Integer> {return null}

    var myProp51 : ArrayList<Integer> as MyProperty5
  }
  class Case61Sub extends Case61Super {
    //IDE-1817 Parser issue. Should show error here. shows at the package line on top
    var list1 : ArrayList<String> as MyProperty1           //## issuekeys: CONFLICT

    property set MyProperty2(a : ArrayList<String>){}      //## issuekeys: CONFLICT

    var list3 : ArrayList<String> as MyProperty3       //## issuekeys: CONFLICT

    property get MyProperty4() : ArrayList<String> { return null }     //## issuekeys: CONFLICT

    var myProp52 : ArrayList<String> as MyProperty5          //## issuekeys: ERROR
  }

  //Case-62 is reverse of Case-61. Super class and sub class contents swapped
  class Case62Super {

    var list1 : ArrayList<String> as MyProperty1

    property set MyProperty2(a : ArrayList<String>){}

    var list3 : ArrayList<String> as MyProperty3

    property get MyProperty4() : ArrayList<String> { return null }

    var myProp52 : ArrayList<String> as MyProperty5
  }
  class Case62Sub extends Case62Super {

    function setMyProperty1(a: ArrayList<Integer>) {}            //## issuekeys: CONFLICT

    function setMyProperty2(a: ArrayList<Integer>) {}           //## issuekeys: CONFLICT

    function getMyProperty3() : ArrayList<Integer> {return null}      //## issuekeys: THE METHOD 'GETMYPROPERTY3()' CONFLICTS WITH THE IMPLICIT METHOD GENERATED BY THE PROPERTY 'MYPROPERTY3'

    function getMyProperty4() : ArrayList<Integer> {return null}      //## issuekeys: THE METHOD 'GETMYPROPERTY4()' CONFLICTS WITH THE IMPLICIT METHOD GENERATED BY THE PROPERTY 'MYPROPERTY4'

    var myProp52 : ArrayList<Integer> as MyProperty5      //## issuekeys: VARIABLE 'MYPROP52' IS ALREADY DEFINED IN THE SCOPE
  }

}