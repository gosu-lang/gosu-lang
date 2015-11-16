package gw.specContrib.structural

class Errant_DifferentReturnTypes {
  structure  MyStructure {
    function hello() : Integer
  }

  class MyClass  {
    function hello() : String { return null }
  }

  structure MyStructure2 {
    property get hello2() : Integer
  }

  class MyClass2 {
    property get hello2() : String { return null }
  }

  function main() {
    var mystructure1 : MyStructure = new MyClass()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_DIFFERENTRETURNTYPES.MYCLASS', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_DIFFERENTRETURNTYPES.MYSTRUCTURE'
    var mystructure2 : MyStructure2 = new MyClass2()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_DIFFERENTRETURNTYPES.MYCLASS2', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_DIFFERENTRETURNTYPES.MYSTRUCTURE2'
  }

}