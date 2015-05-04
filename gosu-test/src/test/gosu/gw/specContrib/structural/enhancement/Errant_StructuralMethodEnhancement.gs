package gw.specContrib.structural.enhancement

class Errant_StructuralMethodEnhancement {

  structure MyEnhStructure1 {
    function structFunOne()
    function structFunTwo()
  }

  structure MyEnhStructure2 {
    function structFunOne()
    function structFunThree()
  }

  function structFunOne() {}

  function main() {
    var myEnhStructure1: MyEnhStructure1 = new Errant_StructuralMethodEnhancement()
    var myEnhStructure2: MyEnhStructure2 = new Errant_StructuralMethodEnhancement()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ENHANCEMENT.ERRANT_STRUCTURALENHANCEMENT', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ENHANCEMENT.ERRANT_STRUCTURALENHANCEMENT.MYENHSTRUCTURE2'
  }
}