package gw.specContrib.blocks

uses java.lang.Integer
uses java.lang.Short
uses java.math.BigDecimal


class Errant_BlocksAsArgumentInteger {

  //Integer as block argument and return type
  function blockIntegerFun111(block()) {
  }

  function blockIntegerFun112(block1: block()) {
  }

  function blockIntegerFun113(block1: block(Integer)) {  //## issuekeys: MSG_BLOCK_TYPES_SHOULD_HAVE_ARG_NAMES
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

    //IDE-1336 - Not A Bug. The type is actually a variable name here. No error expected
    blockIntegerFun114(\String -> print("hello"))
    blockIntegerFun114(\s: String -> print("hello"))      //## issuekeys: MSG_TYPE_MISMATCH
    blockIntegerFun115(\s -> print("hello"))        //## issuekeys: MSG_TYPE_MISMATCH, MSG_VOID_RETURN_IN_CTX_EXPECTING_VALUE
    blockIntegerFun115(\s -> 42)
    blockIntegerFun115(\s: Integer -> 42)

    //IDE-1336 - Not A Bug. The type is actually a variable name here. No error expected
    blockIntegerFun115(\Integer -> 42)

    //IDE-1336 - Not A Bug. The type is actually a variable name here. No error expected
    blockIntegerFun115(\String -> 42)

    blockIntegerFun115(\s: String -> 42)    //## issuekeys: MSG_TYPE_MISMATCH

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

      myFun111(\c: java.lang.Number -> num1)      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
      myFun111(\c: java.lang.Number -> 42)
      myFun111(\c: Integer -> 42)      //## issuekeys: MSG_TYPE_MISMATCH
      myFun111(\c: Integer -> num1)      //## issuekeys: MSG_TYPE_MISMATCH, MSG_IMPLICIT_COERCION_ERROR

      myFun111(\c: java.lang.Number -> 1b)
      myFun111(\c: java.lang.Number -> 1s)
      myFun111(\c: java.lang.Number -> 42.5f)      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
      myFun111(\c: java.lang.Number -> 42.5)      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
      myFun111(\c: Short -> 1s)      //## issuekeys: MSG_TYPE_MISMATCH
    }

    function myFun222(block(i: Integer): java.lang.Number) {
    }

    function caller222() {
      myFun222(\c: java.lang.Number -> num1)
      myFun222(\c: java.lang.Number -> 42)
      myFun222(\c: Integer -> 42)
      myFun222(\c: Integer -> num1)

      myFun222(\c: Short -> num1)      //## issuekeys: MSG_TYPE_MISMATCH
      myFun222(\c: Integer -> 42s)
      myFun222(\c: Integer -> 42b)
      myFun222(\c: Integer -> 42.5f)
      myFun222(\c: Integer -> 42.5)
      myFun222(\c: Integer -> BigDecimal.ONE)
    }
  }
}