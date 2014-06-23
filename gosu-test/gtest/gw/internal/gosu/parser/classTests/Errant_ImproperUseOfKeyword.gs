package gw.internal.gosu.parser.classTests
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ImproperUseOfKeyword {
  var yy : int as internal  //## issuekeys: MSG_IMPROPER_USE_OF_KEYWORD

  function fo() {
    var get : int  //## issuekeys: MSG_IMPROPER_USE_OF_KEYWORD

    for( set in 1..2 index index iterator iterator ) {}  //## issuekeys: MSG_IMPROPER_USE_OF_KEYWORD, MSG_IMPROPER_USE_OF_KEYWORD, MSG_IMPROPER_USE_OF_KEYWORD
  }
}
