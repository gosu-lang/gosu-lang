package gw.specContrib.classes.method_Scoring.block_Types

class Errant_BlocksMS_InterfaceObject {

  class JJ{}
  class KK{}
  interface I{
    function hello()
  }

  function foo1(i : I) : JJ{
    return null
  }
  function foo1(o : Object) : KK{
    return null
  }

  function caller() {
    //IDE-1781
    var a111 : JJ = foo1(\ -> {})
    var b111 : KK = foo1(\ -> {})      //## issuekeys: INCOMPATIBLE TYPES.
  }
}