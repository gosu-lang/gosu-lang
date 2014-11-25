package gw.specContrib.structural.enhancement

class Errant_StructuralPropertyEnhancement {

  structure MyEnhStructure1 {
    property get Prop1(): int
    property set Prop1(i: int)
  }

  structure MyEnhStructure2 {
    property get Prop1(): int
    property set Prop2(i: int)
  }

  property get Prop1(): int {return 1}

  function main() {
    var myEnhStructure1: MyEnhStructure1 = new Errant_StructuralPropertyEnhancement()
    var myEnhStructure2: MyEnhStructure2 = new Errant_StructuralPropertyEnhancement()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ENHANCEMENT.ERRANT_STRUCTURALENHANCEMENT', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ENHANCEMENT.ERRANT_STRUCTURALENHANCEMENT.MYENHSTRUCTURE2'
  }
}