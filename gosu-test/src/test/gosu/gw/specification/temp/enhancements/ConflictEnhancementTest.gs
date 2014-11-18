package gw.specification.temp.enhancements

uses gw.BaseVerifyErrantTest

class ConflictEnhancementTest extends BaseVerifyErrantTest {
  function testErrant_StringTest() {
    processErrantType(Errant_FooEnh)
  }
}