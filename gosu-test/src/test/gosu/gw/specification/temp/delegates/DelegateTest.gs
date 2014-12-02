package gw.specification.temp.delegates

uses gw.BaseVerifyErrantTest

class DelegateTest extends BaseVerifyErrantTest {
  function testErrant_DelegateMemberConflict() {
    processErrantType( Errant_DelegateMemberConflict )
  }
}