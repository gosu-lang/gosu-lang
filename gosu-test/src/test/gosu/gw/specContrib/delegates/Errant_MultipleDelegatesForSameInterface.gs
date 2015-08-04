package gw.specContrib.delegates

class Errant_MultipleDelegatesForSameInterface {
  interface IA {
    function ifun()
  }

  class A implements IA {
    override function ifun() {
      print("ifun by class A's object...")
    }
  }

  class B implements IA {
    override function ifun() {
      print("ifun by class B's object...")
    }
  }

  class D implements IA {
    override function ifun() {
      print("ifun by class D's object...")
    }
  }

  class C implements IA {
    //IDE-1248
    delegate a1 represents IA
    delegate b1 represents IA            //## issuekeys: DELEGATION FOR METHOD 'IFUN()' IS AMBIGUOUS: DELEGATE 'C.A1' ALREADY PROVIDES THIS METHOD
    delegate d1 represents IA            //## issuekeys: DELEGATION FOR METHOD 'IFUN()' IS AMBIGUOUS: DELEGATE 'C.B1' ALREADY PROVIDES THIS METHOD

    construct() {
      a1 = new A()
      b1 = new B()
      d1 = new D()
    }
  }
}