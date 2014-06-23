package gw.internal.gosu.parser.expressions

uses gw.BaseVerifyErrantTest

class InferredParamTest extends BaseVerifyErrantTest {

  function testErrant_InferredParamType() {
    processErrantType( Errant_InferredParamType )
  }
}