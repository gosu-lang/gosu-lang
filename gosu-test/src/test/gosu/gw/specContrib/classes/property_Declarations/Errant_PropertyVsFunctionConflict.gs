package gw.specContrib.classes.property_Declarations

// IDE-1138
class Errant_PropertyVsFunctionConflict {

  class Class1 {
    function getMyProp1() : String { return null }
    function setMyProp2(s: String) {}
  }

  class SubClass1 extends Class1 {
    property get MyProp1() : String { return null }   //## issuekeys: IMPLICITLY OVERRIDES PARENT CLASS FUNCTION
    property set MyProp2(s: String) { }               //## issuekeys: IMPLICITLY OVERRIDES PARENT CLASS FUNCTION
  }


  class Class2 {
    function getMyProp1() : String { return null }
    function setMyProp2(s: String) {}
  }

  class SubClass2 extends Class2 {
    var myProp1 : String as MyProp1    //## issuekeys: IMPLICITLY OVERRIDES PARENT CLASS FUNCTION
    var myProp2 : String as MyProp2    //## issuekeys: IMPLICITLY OVERRIDES PARENT CLASS FUNCTION
  }

  class Class3 {
    var myProp : String as MyProp
  }

  class SubClass3 extends Class3 {
    function getMyProp() : String { return null }  //## issuekeys: OVERRIDES PARENT CLASS IMPLICIT FUNCTION
    function setMyProp(s: String) {}               //## issuekeys: OVERRIDES PARENT CLASS IMPLICIT FUNCTION
  }

  class SubClass4 extends JavaClass2 {
    function getText1(): String { return null } //## issuekeys: OVERRIDES PARENT CLASS IMPLICIT FUNCTION
  }

  class Class5 {
    property get prop () : String {  return null }
  }

  class SubClass5 extends Class5 {
    function getProp() :  int { return 0 }
  }

  class Class6 {
    function getProp(): String { return null }
  }

  class SubClass6 extends Class6 {
    property get prop(): String { return null }
  }
}
