package gw.internal.gosu.parser.classTests.gwtest.ctor


uses gw.BaseVerifyErrantTest

class BadJavaConstructorsTest extends BaseVerifyErrantTest {
  function testErrant_BadJavaConstructors() {
    processErrantType( Errant_BadJavaConstructors )
  }
}