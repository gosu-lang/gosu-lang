package gw.specification.types.primitiveTypes

class Errant_NumericLiteralBasicTest {
  function testNumberLiteral() : void {
    var x0 = 123
    var x1 = 123l
    var x2 : long = 123
    var x3 : long = 9223372036854775807l
    var x4: byte = 0b1
  }
}
