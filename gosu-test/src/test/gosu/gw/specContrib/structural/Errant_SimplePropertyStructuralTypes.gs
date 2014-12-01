package gw.specContrib.structural

class Errant_SimplePropertyStructuralTypes {
  class A {}
  class B extends A {}
  class C extends B {}

  structure Transformer {
    property get Result() : B
  }

  class Transformer1 {
    property get Result(): A {return null}
  }
  class Transformer2 {
    property get Result(): B {return null}
  }
  class Transformer3 {
    property get Result(): C {return null}
  }

 function main() {
    var tr1 : Transformer =  new Transformer1()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_PROPERTYSTRUCTURALTYPES.TRANSFORMER1', REQUIRED: 'GW.SPECCONTRIB.STRUCTURAL.ERRANT_PROPERTYSTRUCTURALTYPES.TRANSFORMER'
    var tr2 : Transformer =  new Transformer2()
    var tr3 : Transformer =  new Transformer3()
  }
}