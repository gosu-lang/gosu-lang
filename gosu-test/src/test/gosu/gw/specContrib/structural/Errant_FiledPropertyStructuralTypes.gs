package gw.specContrib.structural

uses java.lang.Integer

class Errant_FiledPropertyStructuralTypes {

  structure  MyStructure {
    property get Prop1() : String
    property set Prop1(s : String)
  }

  class MyClass5 {
    private var Prop1: String
  }

  function main() {
    var mystructure1 : MyStructure = new MyClass1()
    var mystructure2 : MyStructure = new MyClass2()
    var mystructure3 : MyStructure = new MyClass3()
    var mystructure4 : MyStructure = new MyClass4()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.MYCLASS4', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_FILEDPROPERTYSTRUCTURALTYPES.MYSTRUCTURE'
    var mystructure5 : MyStructure = new MyClass5()
  }
}