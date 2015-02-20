package gw.specContrib.classes.method_Scoring.block_Types

class Errant_BlocksMS_InterfaceStructure {

  class JJ{}
  class KK{}

  interface I{
    function hello()
  }
  structure S {
    function hello()
  }

  function foo1(i : I) : JJ{
    return null
  }
  function foo1(s : S) : KK{
    return null
  }

  function caller() {
    var a111 : JJ = foo1(\ -> {})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'BLOCKSMS_INTERFACESTRUCTURE.FOO1(I)' AND 'BLOCKSMS_INTERFACESTRUCTURE.FOO1(S)' MATCH
    var b111 : KK = foo1(\ -> {})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'BLOCKSMS_INTERFACESTRUCTURE.FOO1(I)' AND 'BLOCKSMS_INTERFACESTRUCTURE.FOO1(S)' MATCH
  }
}