package gw.specContrib.classes.method_Scoring.block_Types

class Errant_BlocksMS_BlockInterface {

  class JJ{}
  class KK{}
  interface I{
    function hello()
  }

  function foo1(b : block()) : JJ{
    return null
  }
  function foo1(i : I) : KK{
    return null
  }

  function caller() {
    var a111 : JJ = foo1(\ -> {})
    var b111 : KK = foo1(\ -> {})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.BLOCKS_METHODSCORING.BLOCKSINTERFACESTRUCTURESASPARAMS.BLOCKSMS_BLOCKINTERFACE.JJ', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.BLOCKS_METHODSCORING.BLOCKSINTERFACESTRUCTURESASPARAMS.BLOCKSMS_BLOCKINTERFACE.KK'
  }
}