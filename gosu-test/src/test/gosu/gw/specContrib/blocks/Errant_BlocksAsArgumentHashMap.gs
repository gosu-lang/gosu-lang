package test.gosu.gw.specContrib.blocks

uses java.lang.Integer
uses java.util.HashMap

class Errant_BlocksAsArgumentHashMap {

  //HashMap as block argument and return type
  function blockHashMapFun111(block()) {
  }

  function blockHashMapFun112(block1: block()) {
  }

  function blockHashMapFun113(block1: block(HashMap)) {
  }

  function blockHashMapFun114(block1: block(s: HashMap)) {
  }

  function blockHashMapFun115(block1: block(s: HashMap): HashMap) {
  }

  function blockHashMapFun116(block1: block(s: HashMap): HashMap): HashMap {
    return null
  }

  function blockHashMapFunCaller() {
    blockHashMapFun111(\-> print("hello"))
    blockHashMapFun112(\-> print("hello"))
    blockHashMapFun113(\_ -> print("hello"))
    blockHashMapFun114(\s -> print("hello"))
    blockHashMapFun114(\s: HashMap -> print("hello"))
    blockHashMapFun114(\s: HashMap -> print("hello"))

    //IDE-1336 - argument name missing
    blockHashMapFun114(\HashMap -> print("hello"))  //## issuekeys:

    blockHashMapFun115(\s -> print("hello"))     //## issuekeys: 'BLOCKHASHMAPFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.HASHMAP,JAVA.UTIL.HASHMAP>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTHASHMAP' CANNOT BE APPLIED TO '(BLOCK(HASHMAP<OBJECT, OBJECT>))'
    blockHashMapFun115(\s -> {
      1->2,3->4    //ERROR Expected      //## issuekeys: NOT A STATEMENT
    })
    blockHashMapFun115(\s -> {
      return {1->2, 3->4}
    })
    blockHashMapFun115(\s: HashMap -> {
      return {1->2, 3->4}
    })

    //IDE-1336 Error expected. as the argument name is not specified
    blockHashMapFun115(\HashMap -> { //## issuekeys:
      return {1->2, 3->4}
    })
    blockHashMapFun115(\s -> {
      print("blockfun5");
      return {1->2, 3->4}
    })
    var blockHashMapVar116 = blockHashMapFun116(\s -> {
      print("blockfun5");
      return {1->2, 3->4}
    })
  }


  //HashMap<Integer, Integer> as block argument and return type
  function blockHashMapIntegerFun111(block()) {
  }

  function blockHashMapIntegerFun112(block1: block()) {
  }

  function blockHashMapIntegerFun113(block1: block(HashMap<Integer, Integer>)) {
  }

  function blockHashMapIntegerFun114(block1: block(s: HashMap<Integer, Integer>)) {
  }

  function blockHashMapIntegerFun115(block1: block(s: HashMap<Integer, Integer>): HashMap<Integer, Integer>) {
  }

  function blockHashMapIntegerFun116(block1: block(s: HashMap<Integer, Integer>): HashMap<Integer, Integer>): HashMap<Integer, Integer> {
    return null
  }

  function blockHashMapIntegerFun117(block1: block(): HashMap<Integer, Integer>): HashMap<Integer, Integer> {
    return null
  }

  function blockHashMapIntegerFunCaller() {
    blockHashMapIntegerFun111(\-> print("hello"))
    blockHashMapIntegerFun112(\-> print("hello"))
    blockHashMapIntegerFun113(\_ -> print("hello"))
    blockHashMapIntegerFun114(\s -> print("hello"))
    blockHashMapIntegerFun114(\s: HashMap -> print("hello"))
    blockHashMapIntegerFun114(\s: HashMap<Integer, Integer> -> print("hello"))

    //IDE-1336 Error expected. as the argument name is not specified
    blockHashMapIntegerFun114(\HashMap<Integer,Integer>->print("hello"))      //## issuekeys: UNEXPECTED TOKEN: <

    blockHashMapIntegerFun115(\s -> print("hello"))        //## issuekeys: 'BLOCKHASHMAPINTEGERFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>,JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTHASHMAP' CANNOT BE APPLIED TO '(BLOCK(HASHMAP<INTEGER, INTEGER>))'

    //ERROR Expected. Need to have return statement. Cannot return a HashMap directly
    blockHashMapIntegerFun115(\s -> {
      1->2,3->4      //## issuekeys: NOT A STATEMENT
    })
    blockHashMapIntegerFun115(\s -> {
      return {1->2, 3->4}
    })
    blockHashMapIntegerFun115(\s: HashMap<Integer, Integer> -> {
      return {1->2, 3->4}
    })

    //IDE-1336 : Error expected. as the argument name is not specified
    blockHashMapIntegerFun115(\HashMap<Integer,Integer>->{      //## issuekeys: UNEXPECTED TOKEN: <
      return {1->2, 3->4}      //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
    })      //## issuekeys: UNEXPECTED TOKEN: <

    blockHashMapIntegerFun115(\s -> {
      print("blockfun5");
      return {1->2, 3->4}
    })

    var blockHashMapIntVar116: HashMap<Integer, Integer> = blockHashMapIntegerFun116(\s -> {
      print("blockfun5");
      return {1->2, 3->4}
    })
    var blockHashMapListIntVar117: HashMap<Integer, Integer> = blockHashMapIntegerFun117(\-> {
      return {1->2, 3->4}
    })
  }

  class CoContraVarianceTest {
    var hMap1: HashMap
    var hMapInteger1: HashMap<Integer, Integer>

    function myFun111(block(a: HashMap): HashMap<Integer, Integer>) {
    }

    function caller() {

      myFun111(\c: HashMap -> hMap1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>,JAVA.UTIL.HASHMAP>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTHASHMAP.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(HASHMAP):HASHMAP<OBJECT, OBJECT>)'
      myFun111(\c: HashMap -> hMapInteger1)
      myFun111(\c: HashMap<Integer, Integer> -> hMapInteger1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>,JAVA.UTIL.HASHMAP>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTHASHMAP.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(HASHMAP<INTEGER, INTEGER>):HASHMAP<INTEGER, INTEGER>)'
      myFun111(\c: HashMap<Integer, Integer> -> hMap1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>,JAVA.UTIL.HASHMAP>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTHASHMAP.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(HASHMAP<INTEGER, INTEGER>):HASHMAP<OBJECT, OBJECT>)'
    }

    function myFun222(block(a: HashMap<Integer, Integer>): HashMap) {
    }

    function caller222() {
      myFun222(\c: HashMap -> hMap1)
      myFun222(\c: HashMap -> hMapInteger1)
      myFun222(\c: HashMap<Integer, Integer> -> hMapInteger1)
      myFun222(\c: HashMap<Integer, Integer> -> hMap1)
    }
  }

}