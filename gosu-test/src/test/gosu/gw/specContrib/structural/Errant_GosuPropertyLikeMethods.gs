package gw.specContrib.structural

class Errant_GosuPropertyLikeMethods {

  structure MyStructure {
    function getMyProperty() : String
    function setMyProperty(s : String)
  }

  class MyClass1 {
    var myProperty : String as MyProperty
  }

  class MyClass2 {
    property get MyProperty() : String {return null}
    property set MyProperty(ss : String) {}
  }

  class MyClass3 {
    function getMyProperty() : String {return null}
    function setMyProperty(ss : String) {}
  }

  function main() {
    var myStructure1 : MyStructure = new MyClass1()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_GOSUPROPERTYLIKEMETHODS.MYCLASS1', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_GOSUPROPERTYLIKEMETHODS.MYSTRUCTURE'
    var myStructure2 : MyStructure = new MyClass2()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_GOSUPROPERTYLIKEMETHODS.MYCLASS2', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_GOSUPROPERTYLIKEMETHODS.MYSTRUCTURE'
    var myStructure3 : MyStructure = new MyClass3()
  }

}