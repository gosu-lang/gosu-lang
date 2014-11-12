package test.gosu.gw.specContrib.blocks

uses java.lang.CharSequence
uses java.lang.Integer

class Errant_BlocksAsArgumentString {
  //String as block argument and return type
  function blockStringFun111(block()) {
  }

  function blockStringFun112(block1: block()) {
  }

  function blockStringFun113(block1: block(String)) {
  }

  function blockStringFun114(block1: block(s: String)) {
  }

  function blockStringFun115(block1: block(s: String): String) {
  }

  function blockStringFun116(block1: block(s: String): String): String {
    return null
  }

  function blockfunCaller1111() {
    blockStringFun111(\-> print("hello"))

    blockStringFun112(\-> print("hello"))

    blockStringFun113(\_ -> print("hello"))

    blockStringFun114(\s -> print("hello"))
    blockStringFun114(\s -> print(s))
    blockStringFun114(\s: String -> print(s))

    //IDE-1321 - should show error. Block has return statement but is not supposed to return anything
    blockStringFun114(\s: String -> {
      return s                  //## issuekeys:
    })

    blockStringFun115(\s -> print("hello"))   //## issuekeys: 'BLOCKSTRINGFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING' CANNOT BE APPLIED TO '(BLOCK(STRING))'

    blockStringFun115(\s -> "hello")

    blockStringFun115(\s: String -> "hello")

    //IDE-1336 - Error expected, as the argument name is not specified
    blockStringFun115(\String -> "hello")        //## issuekeys:

    blockStringFun115(\s -> {
      return "hello"
    })
    blockStringFun115(\s -> {
      print("hello");
      return "hello"
    })
    var blockStrVar116: String = blockStringFun116(\s -> {
      print("hello");
      return "hello"
    })
  }

  function mixedCases() {
    blockStringFun112(\s: String -> print(""))      //## issuekeys: 'BLOCKSTRINGFUN112(GW.LANG.__PSI__.IBLOCK0<VOID>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING' CANNOT BE APPLIED TO '(BLOCK(STRING))'
    blockStringFun113(\s: CharSequence -> print(""))
    blockStringFun114(\s: CharSequence -> print(""))
    blockStringFun113(\s: Integer -> print(""))      //## issuekeys: 'BLOCKSTRINGFUN113(GW.LANG.__PSI__.IBLOCK1<VOID,JAVA.LANG.STRING>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING' CANNOT BE APPLIED TO '(BLOCK(INTEGER))'
    blockStringFun115(\s: CharSequence -> "hello")
    blockStringFun115(\s: String -> new Object())      //## issuekeys: 'BLOCKSTRINGFUN115(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING' CANNOT BE APPLIED TO '(BLOCK(STRING):OBJECT)'
    blockStringFun115(\s: String -> new String("mystring"))
    blockStringFun115(\s: String -> new Object().toString())
  }

  class CoContraVarianceTest {
    var cseq2: CharSequence

    function myFun111(block(cs : CharSequence): String) {
    }

    function caller111() {

      myFun111(\c: CharSequence -> cseq2)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.STRING,JAVA.LANG.CHARSEQUENCE>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(CHARSEQUENCE):CHARSEQUENCE)'
      myFun111(\c: CharSequence -> "mystring")
      myFun111(\c: String -> "mystring")      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.STRING,JAVA.LANG.CHARSEQUENCE>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(STRING):STRING)'
      myFun111(\c: String -> cseq2)      //## issuekeys: 'MYFUN111(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.STRING,JAVA.LANG.CHARSEQUENCE>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(STRING):CHARSEQUENCE)'

      myFun111(\c: CharSequence -> 'ccc')
      myFun111(\c: CharSequence -> 'c')
    }

    function myFun222(block(s : String): CharSequence) {
    }

    function caller222() {
      myFun222(\c: CharSequence -> cseq2)
      myFun222(\c: CharSequence -> "mystring")
      myFun222(\c: String -> "mystring")
      myFun222(\c: String -> cseq2)

      myFun222(\c: CharSequence -> 'ccc')
      myFun222(\c: CharSequence -> 'c')      //## issuekeys: 'MYFUN222(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.CHARSEQUENCE,JAVA.LANG.STRING>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(CHARSEQUENCE):CHAR)'
      myFun222(\c: char -> 'ccc')      //## issuekeys: 'MYFUN222(GW.LANG.__PSI__.IBLOCK1<JAVA.LANG.CHARSEQUENCE,JAVA.LANG.STRING>)' IN 'AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSASARGUMENTSSTRING.COCONTRAVARIANCETEST' CANNOT BE APPLIED TO '(BLOCK(CHAR):STRING)'
    }
  }
}