package gw.internal.gosu.parser.classTests

uses gw.BaseVerifyErrantTest

class ImproperUseOfKeywordTest extends BaseVerifyErrantTest {

  function testErrant_ImproperUseOfKeyword() {
    processErrantType( Errant_ImproperUseOfKeyword )
  }
}