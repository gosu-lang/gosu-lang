package gw.specContrib.blocks

uses java.lang.CharSequence
uses java.lang.Integer

class Errant_BlocksAsArgumentString {
  //String as block argument and return type
  function blockStringFun111(block()) {
  }

  function blockStringFun112(block1: block()) {
  }

  function blockStringFun113(block1: block(String)) {  //## issuekeys: MSG_BLOCK_TYPES_SHOULD_HAVE_ARG_NAMES
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
      return s                  //## issuekeys: MSG_NOT_A_STATEMENT
    })

    blockStringFun115(\s -> print("hello"))   //## issuekeys: MSG_TYPE_MISMATCH, MSG_VOID_RETURN_IN_CTX_EXPECTING_VALUE

    blockStringFun115(\s -> "hello")

    blockStringFun115(\s: String -> "hello")

    //IDE-1336 - Not A Bug. The type of the block argument is actually a variable name here. No error expected
    blockStringFun115(\String -> "hello")

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
    blockStringFun112(\s: String -> print(""))      //## issuekeys: MSG_TYPE_MISMATCH
    blockStringFun113(\s: CharSequence -> print(""))
    blockStringFun114(\s: CharSequence -> print(""))
    blockStringFun113(\s: Integer -> print(""))      //## issuekeys: MSG_TYPE_MISMATCH
    blockStringFun115(\s: CharSequence -> "hello")
    blockStringFun115(\s: String -> new Object())      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    blockStringFun115(\s: String -> new String("mystring"))
    blockStringFun115(\s: String -> new Object().toString())
  }

  class CoContraVarianceTest {
    var cseq2: CharSequence

    function myFun111(block(cs : CharSequence): String) {
    }

    function caller111() {

      myFun111(\c: CharSequence -> cseq2)      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
      myFun111(\c: CharSequence -> "mystring")
      myFun111(\c: String -> "mystring")      //## issuekeys: MSG_TYPE_MISMATCH
      myFun111(\c: String -> cseq2)      //## issuekeys: MSG_TYPE_MISMATCH, MSG_IMPLICIT_COERCION_ERROR

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
      myFun222(\c: CharSequence -> 'c')      //## issuekeys: MSG_TYPE_MISMATCH, MSG_TYPE_MISMATCH
      myFun222(\c: char -> 'ccc')      //## issuekeys: MSG_TYPE_MISMATCH
    }
  }
}