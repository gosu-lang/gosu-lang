package gw.specContrib.structural

class Errant_SimpleStructuralTypes {
  class A {}
  class B extends A {}
  class C extends B {}

  structure Transformer {
    function transform(o: B) : B
  }

  class Transformer1 {
    function transform(s: A): A {return null}
  }
  class Transformer2 {
    function transform(s: A): B {return null}
  }
  class Transformer3 {
    function transform(s: A): C {return null}
  }
  class Transformer4 {
    function transform(s: B): A {return null}
  }
  class Transformer5 {
    function transform(s: B): B {return null}
  }
  class Transformer6 {
    function transform(s: B): C {return null}
  }
  class Transformer7 {
    function transform(s: C): A {return null}
  }
  class Transformer8 {
    function transform(s: C): B {return null}
  }
  class Transformer9 {
    function transform(s: C): C {return null}
  }

 function main() {
    var tr1 : Transformer =  new Transformer1()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.TRANSFORMERUTIL.TRANSFORMER1', REQUIRED: 'GW.TRANSFORMERUTIL.TRANSFORMER'
    var tr2 : Transformer =  new Transformer2()
    var tr3 : Transformer =  new Transformer3()
    var tr4 : Transformer =  new Transformer4()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.TRANSFORMERUTIL.TRANSFORMER4', REQUIRED: 'GW.TRANSFORMERUTIL.TRANSFORMER'
    var tr5 : Transformer =  new Transformer5()
    var tr6 : Transformer =  new Transformer6()
    var tr7 : Transformer =  new Transformer7()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.TRANSFORMERUTIL.TRANSFORMER7', REQUIRED: 'GW.TRANSFORMERUTIL.TRANSFORMER'
    var tr8 : Transformer =  new Transformer8()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.TRANSFORMERUTIL.TRANSFORMER8', REQUIRED: 'GW.TRANSFORMERUTIL.TRANSFORMER'
    var tr9 : Transformer =  new Transformer9()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.TRANSFORMERUTIL.TRANSFORMER9', REQUIRED: 'GW.TRANSFORMERUTIL.TRANSFORMER'
  }
}