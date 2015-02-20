package gw.specContrib.classes.method_Scoring.block_Types

class Errant_BlocksMS_InterfaceClass {

  class JJ{}
  class KK{}
  interface I{
    function hello()
  }
  class C {
    function hello() {}
  }

  function foo1(i : I) : JJ{
    return null
  }
  function foo1(c : C) : KK{
    return null
  }

  function caller() {
    var a111 : JJ = foo1(\ -> {})
    var b111 : KK = foo1(\ -> {})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.BLOCKS_METHODSCORING.BLOCKSINTERFACESTRUCTURESASPARAMS.BLOCKSMS_INTERFACECLASS.JJ', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.BLOCKS_METHODSCORING.BLOCKSINTERFACESTRUCTURESASPARAMS.BLOCKSMS_INTERFACECLASS.KK'
  }
}