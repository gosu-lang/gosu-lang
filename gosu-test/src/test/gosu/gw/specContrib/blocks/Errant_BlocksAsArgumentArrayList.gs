package gw.specContrib.blocks

uses java.lang.Integer
uses java.lang.Short
uses java.util.ArrayList

class Errant_BlocksAsArgumentArrayList {

  //ArrayList as block argument and return type
  function blockArrayListFun111(block()) {
  }

  function blockArrayListFun112(block1: block()) {
  }

  function blockArrayListFun113(block1: block(ArrayList)) {
  }

  function blockArrayListFun114(block1: block(s: ArrayList)) {
  }

  function blockArrayListFun115(block1: block(s: ArrayList): ArrayList) {
  }

  function blockArrayListFun116(block1: block(s: ArrayList): ArrayList): ArrayList {
    return null
  }

  function blockArrayListFunCaller() {
    blockArrayListFun111(\-> print("hello"))
    blockArrayListFun112(\-> print("hello"))
    blockArrayListFun113(\_ -> print("hello"))
    blockArrayListFun114(\s -> print("hello"))
    blockArrayListFun114(\s: ArrayList -> print("hello"))

    //IDE-1336 - Not A Bug. The type is actually a variable name here. No error expected
    blockArrayListFun114(\ArrayList -> print("hello"))

    blockArrayListFun115(\s -> print("hello"))      //## issuekeys: 'BLOCKARRAYLISTFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST,JAVA.UTIL.ARRAYLIST>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<OBJECT>))'

    blockArrayListFun115(\s -> {
      1,2,3         //## issuekeys: NOT A STATEMENT
    })
    blockArrayListFun115(\s -> {
      return {1, 'c', "mystring"}
    })
    blockArrayListFun115(\s: ArrayList -> {
      return {1, 'c', "mystring"}
    })

    //IDE-1336 - Not A Bug. The type is actually a variable name here. No error expected
    blockArrayListFun115(\ArrayList -> {
      return {1, 'c', "mystring"}
    })
    blockArrayListFun115(\s -> {
      print("blockfun5");
      return {1, 'c', "mystring"}
    })
    var blockArrayListVar116: ArrayList = blockArrayListFun116(\s -> {
      print("blockfun5");
      return {1, 'c', "mystring"}
    })
  }


  //ArrayList<Integer> as block argument and return type
  function blockArrayListIntegerFun111(block()) {
  }

  function blockArrayListIntegerFun112(block1: block()) {
  }

  function blockArrayListIntegerFun113(block1: block(ArrayList<Integer>)) {
  }

  function blockArrayListIntegerFun114(block1: block(s: ArrayList<Integer>)) {
  }

  function blockArrayListIntegerFun115(block1: block(s: ArrayList<Integer>): ArrayList<Integer>) {
  }

  function blockArrayListIntegerFun116(block1: block(s: ArrayList<Integer>): ArrayList<Integer>): ArrayList<Integer> {
    return null
  }

  function blockArrayListIntegerFun117(block1: block(): ArrayList<Integer>): ArrayList<Integer> {
    return null
  }


  function blockArrayListIntegerFunCaller() {

    blockArrayListIntegerFun111(\-> print("hello"))
    blockArrayListIntegerFun112(\-> print("hello"))
    blockArrayListIntegerFun113(\_ -> print("hello"))
    blockArrayListIntegerFun114(\s -> print("hello"))
    blockArrayListIntegerFun114(\s: ArrayList -> print("hello"))
    blockArrayListIntegerFun114(\s: ArrayList<Integer> -> print("hello"))

    //IDE-1336 - argument name missing. Good in this case
    blockArrayListIntegerFun114(\ArrayList<Integer> -> print("hello"))      //## issuekeys: UNEXPECTED TOKEN: <
    blockArrayListIntegerFun115(\s -> print("hello"))        //## issuekeys: 'BLOCKARRAYLISTINTEGERFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>))'
    blockArrayListIntegerFun115(\s -> {
      1,2,3    //ERROR Expected      //## issuekeys: NOT A STATEMENT
    })
    blockArrayListIntegerFun115(\s -> {
      return {1, 2, 3}
    })
    blockArrayListIntegerFun115(\s: ArrayList<Integer> -> {
      return {1, 2, 3}
    })

    //IDE-1336 - Error expected. Good in this case (the argument name is not specified)
    blockArrayListIntegerFun115(\ArrayList<Integer>->{      //## issuekeys: UNEXPECTED TOKEN: <
      return {1, 2, 3}      //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
    })      //## issuekeys: UNEXPECTED TOKEN: <

    blockArrayListIntegerFun115(\s -> {
      print("blockfun5");
      return {1, 2, 3}
    })

    var blockArrayListIntVar116: ArrayList<Integer> = blockArrayListIntegerFun116(\s -> {
      print("blockfun5");
      return {1, 2, 3}
    })
    var blockArrayListIntVar117: ArrayList<Integer> = blockArrayListIntegerFun117(\-> {
      return {1, 2, 3}
    })
  }

  class CoContraVarianceTest {
    var alist1: ArrayList
    var alistInteger1: ArrayList<Integer>

    function myFun111(block(a: ArrayList): ArrayList<Integer>) {
    }

    function caller111() {

      myFun111(\c: ArrayList -> alist1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST):ARRAYLIST<OBJECT>)'
      myFun111(\c: ArrayList -> alistInteger1)
      myFun111(\c: ArrayList<Integer> -> alistInteger1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST<INTEGER>)'
      myFun111(\c: ArrayList<Integer> -> alist1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST<OBJECT>)'

      myFun111(\c: ArrayList -> {
        return {1s, 2s, 3s}
      })
      myFun111(\c: ArrayList<Number> -> {      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<NUMBER>):ARRAYLIST<INTEGER>)'
        return {1s, 2s, 3s}
      })
      myFun111(\c: ArrayList -> {      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST):ARRAYLIST<SHORT>)'
        return new ArrayList<Short>()
      })
    }

    function myFun222(block(a: ArrayList<Integer>): ArrayList) {
    }

    function caller222() {
      myFun222(\c: ArrayList -> alist1)
      myFun222(\c: ArrayList -> alistInteger1)
      myFun222(\c: ArrayList<Integer> -> alistInteger1)
      myFun222(\c: ArrayList<Integer> -> alist1)

      myFun222(\c: ArrayList<Number> -> {      //## issuekeys: 'MYFUN222(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTARRAYLIST.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<NUMBER>):ARRAYLIST<SHORT>)'
        return new ArrayList<Short>()
      })
    }
  }
}