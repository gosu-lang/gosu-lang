package gw.specContrib.interfaceMethods.defaultMethods

static class Errant_AbstractAndDefaultMethodsConflict {

  interface IA {
    function sameFunction() {}
  }
  interface IB {
    function sameFunction()
  }

  interface IC extends IA, IB {       //## issuekeys: MSG_INHERITS_ABSTRACT_AND_DEFAULT
  }

  interface IC2 extends IA, IB {
    override function sameFunction() {}
  }

  abstract class C implements IA, IB {   //## issuekeys: MSG_INHERITS_ABSTRACT_AND_DEFAULT

  }

  class MyClassOne implements IA, IB {        //## issuekeys: MSG_UNIMPLEMENTED_METHOD

  }

  class MyClassTwo implements IA, IB {
    override function sameFunction() {}
  }

}