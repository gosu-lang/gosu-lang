package gw.specContrib.classes.method_Scoring.block_Types

uses java.lang.Integer
uses java.util.ArrayList

class Errant_BlocksMS_BlocksReturnTypeDiff {

  class BB {}
  class CC {}


  class A {}
  class B extends A {}
  class C extends B {}

  function foo1(b : block(b : B), alist : ArrayList<Integer>) : BB {
    return null
  }
  function foo1(b : block(c : C), i : int[]) : CC {
    return null
  }


  function caller() {
    var a111 : BB = foo1(\ a : A -> {}, {1,2,3})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'BLOCKSMS_BLOCKSRETURNTYPEDIFF.FOO1(BLOCK(B:B), ARRAYLIST<INTEGER>)' AND 'BLOCKSMS_BLOCKSRETURNTYPEDIFF.FOO1(BLOCK(C:C), INT[])' MATCH
    var b111 : BB = foo1(\ b : B -> {}, {1,2,3})
    var c111 : BB = foo1(\ c : C -> {}, {1,2,3})      //## issuekeys: AMBIGUOUS METHOD CALL

    var a112 : CC = foo1(\ a : A -> {}, {1,2,3})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'BLOCKSMS_BLOCKSRETURNTYPEDIFF.FOO1(BLOCK(B:B), ARRAYLIST<INTEGER>)' AND 'BLOCKSMS_BLOCKSRETURNTYPEDIFF.FOO1(BLOCK(C:C), INT[])' MATCH
    var b112 : CC = foo1(\ b : B -> {}, {1,2,3})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.BLOCKS_METHODSCORING.DIFFBLOCKSASPARAMS.BLOCKSMS_BLOCKSRETURNTYPEDIFF.JJ', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.BLOCKS_METHODSCORING.DIFFBLOCKSASPARAMS.BLOCKSMS_BLOCKSRETURNTYPEDIFF.KK'
    //IDE-1803 - OS Gosu issue - waiting for decision
    var c112 : CC = foo1(\ c : C -> {}, {1,2,3})      //## issuekeys: AMBIGUOUS METHOD CALL:

    var a113 = foo1(\ a : A -> {}, {1,2,3})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'BLOCKSMS_BLOCKSRETURNTYPEDIFF.FOO1(BLOCK(B:B), ARRAYLIST<INTEGER>)' AND 'BLOCKSMS_BLOCKSRETURNTYPEDIFF.FOO1(BLOCK(C:C), INT[])' MATCH
    var b113 = foo1(\ b : B -> {}, {1,2,3})
    //IDE-1803 - OS Gosu issue - waiting for decision
    var c113 = foo1(\ c : C -> {}, {1,2,3})       //## issuekeys: AMBIGUOUS METHOD CALL:
  }

}