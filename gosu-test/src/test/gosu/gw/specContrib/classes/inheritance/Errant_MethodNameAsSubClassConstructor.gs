package gw.specContrib.classes.inheritance

class Errant_MethodNameAsSubClassConstructor {

  //IDE-535
  class AParent {
    construct() {}
    function AChild() {}
  }

  class AChild extends AParent {
    construct () {
      super();
    }
  }
}