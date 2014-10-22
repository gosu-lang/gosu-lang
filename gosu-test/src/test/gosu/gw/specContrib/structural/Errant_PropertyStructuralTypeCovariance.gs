package gw.specContrib.structural

class Errant_PropertyStructuralTypeCovariance {

  structure MyStructure {
    property get Prop() : Object
    property set Prop(s : Object)
  }

  class Class1 {
    property get Prop(): String {return null}
    property set Prop(s: String) {}
  }

  class Class2 {
    property get Prop(): Object {return null}
    property set Prop(s: Object) {}
  }

  function main() {
     var myStructure1: MyStructure = new Class1()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_PROPERTYSTRUCTURALTYPECOVARIANCE.CLASS1', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_PROPERTYSTRUCTURALTYPECOVARIANCE.MYSTRUCTURE'
     var myStructure2: MyStructure = new Class2()
  }
}