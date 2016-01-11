package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_IllegalEscapeChars {

  function illegalEscapeLetter() :char {
    return '\q' // see http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089
  }

  function illegalEscapeNumber() :char {
    return '\8' // only \0 - \7 are legal
  }

  function illegalEscapeNonTerminated() :char {
    return '\'
  }   
  
  function illegalEscapeUnicode0() :char {
    return '\u'
  }  
  function illegalEscapeUnicode1() :char {
    return '\u1'
  }
  function illegalEscapeUnicode2() :char {
    return '\u11'
  }
  function illegalEscapeUnicode3() :char {
    return '\u111'
  }
  
}
