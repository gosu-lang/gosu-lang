package gw.specContrib.classes.method_Scoring.block_Types

class Errant_BlocksMS_StructureObject {

  class JJ{}
  class KK{}
  structure S{
    function hello()
  }

  function foo1(s : S) : JJ{
    return null
  }
  //IDE-1787 - OS Gosu issue logged
  function foo1(O : Object) : KK {   //## issuekeys: ERROR
    return null
  }

  function caller() {
    //IDE-1781 - Parser issue
    var a111 : JJ = foo1(\ -> {})
    var b111 : KK = foo1(\ -> {})      //## issuekeys: INCOMPATIBLE TYPES.
  }
}