package gw.internal.gosu.parser.annotation

uses gw.BaseVerifyErrantTest

class DeprecationWarningsMaskedByEnclosingDeprecationTest extends BaseVerifyErrantTest {

  function testErrant_DeprecationWarningsMaskedByEnclosingDeprecation() {
    processErrantType( Errant_DeprecationWarningsMaskedByEnclosingDeprecation )
  }

}