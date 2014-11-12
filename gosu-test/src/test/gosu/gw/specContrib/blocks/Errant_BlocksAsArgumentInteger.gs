package test.gosu.gw.specContrib.blocks

uses java.lang.Integer
uses java.lang.Short
uses java.math.BigDecimal


class Errant_BlocksAsArgumentInteger {

  //Integer as block argument and return type
  function blockIntegerFun111(block()) {
  }

  function blockIntegerFun112(block1: block()) {
  }

  function blockIntegerFun113(block1: block(Integer)) {
  }

  function blockIntegerFun114(block1: block(s: Integer)) {
  }

  function blockIntegerFun115(block1: block(s: Integer): Integer) {
  }

  function blockIntegerFun116(block1: block(s: Integer): Integer): Integer {
    return null
  }

  function blockIntegerFunCaller() {
    blockIntegerFun111(\-> print("hello"))
    blockIntegerFun112(\-> print("hello"))
    blockIntegerFun113(\_ -> print("hello"))
    blockIntegerFun114(\s -> print("hello"))

    //IDE-1336 & IDE-1347. Should show error
    blockIntegerFun114(\String -> print("hello"))  //## issuekeys:
    blockIntegerFun114(\s: String -> print("hello"))      //## issuekeys: 'BLOCKINTEGERFUN114(GW.LANG.__PSI__.IBLOCK1<VOID,JAVA.LANG.INTEGER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER' CANNOT BE APPLIED TO '(BLOCK(STRING))'
    blockIntegerFun115(\s -> print("hello"))        //## issuekeys: 'BLOCKINTEGERFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER' CANNOT BE APPLIED TO '(BLOCK(INTEGER))'
    blockIntegerFun115(\s -> 42)
    blockIntegerFun115(\s: Integer -> 42)

    //IDE-1336
    blockIntegerFun115(\Integer -> 42)    //## issuekeys:

    //IDE-1336 & IDE-1347. Should show error
    blockIntegerFun115(\String -> 42)   //## issuekeys:

    blockIntegerFun115(\s: String -> 42)    //## issuekeys: 'BLOCKINTEGERFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER' CANNOT BE APPLIED TO '(BLOCK(STRING):INT)'

    blockIntegerFun115(\s -> {
      return 42
    })  //ERROR Expected
    blockIntegerFun115(\s -> {
      print("blockfun5");
      return 42
    })
    var blockIntVar116: Integer = blockIntegerFun116(\s -> {
      print("blockfun5");
      return 42
    })
  }

  class CoContraVarianceTest {
    var num1: java.lang.Number

    function myFun111(block(n: java.lang.Number): Integer) {
    }

    function caller111() {

      myFun111(\c: java.lang.Number -> num1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.NUMBER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(NUMBER):NUMBER)'
      myFun111(\c: java.lang.Number -> 42)
      myFun111(\c: Integer -> 42)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.NUMBER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(INTEGER):INT)'
      myFun111(\c: Integer -> num1)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.NUMBER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(INTEGER):NUMBER)'

      myFun111(\c: java.lang.Number -> 1b)
      myFun111(\c: java.lang.Number -> 1s)
      myFun111(\c: java.lang.Number -> 42.5f)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.NUMBER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(NUMBER):FLOAT)'
      myFun111(\c: java.lang.Number -> 42.5)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.NUMBER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(NUMBER):DOUBLE)'
      myFun111(\c: Short -> 1s)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.INTEGER,JAVA.LANG.NUMBER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(SHORT):SHORT)'
    }

    function myFun222(block(i: Integer): java.lang.Number) {
    }

    function caller222() {
      myFun222(\c: java.lang.Number -> num1)
      myFun222(\c: java.lang.Number -> 42)
      myFun222(\c: Integer -> 42)
      myFun222(\c: Integer -> num1)

      myFun222(\c: Short -> num1)      //## issuekeys: 'MYFUN222(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.NUMBER,JAVA.LANG.INTEGER>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTINTEGER.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(SHORT):NUMBER)'
      myFun222(\c: Integer -> 42s)
      myFun222(\c: Integer -> 42b)
      myFun222(\c: Integer -> 42.5f)
      myFun222(\c: Integer -> 42.5)
      myFun222(\c: Integer -> BigDecimal.ONE)
    }
  }
}