package gw.internal.gosu.parser.statements

uses gw.BaseVerifyErrantTest

class UsesStatementListTest extends BaseVerifyErrantTest {

  function testErrant_DuplicateNames() {
    processErrantType( Errant_DuplicateNames )
  }
}