package gw.specification.types.primitiveTypes

uses gw.BaseVerifyErrantTest

class NumericLiteralBasicTest extends BaseVerifyErrantTest {

  function testNumberLiteral() : void {
    var x0 = 123
    assertTrue(typeof x0 == int)
    assertEquals(x0, 123)
    var x1 = 123l
    assertTrue(typeof x1 == long)
    assertEquals(x1, 123 as long)
    var x2 : long = 123
    assertTrue(typeof x2 == long)
    assertEquals(x2, 123 as long)
    var x3 : long = 9223372036854775807l
    assertTrue(typeof x3 == long)
    assertEquals(x3, 9223372036854775807L)
    var x4: byte = 0b1
    assertTrue(typeof x4 == byte)
    assertEquals(x4, 1 as byte)
  }

  function testCharLiteral() {
    var c1 = 'c'
    assertEquals( typeof c1, char )
    var c2 = -'c'
    assertEquals( typeof c2, int )
    assertTrue( c1 != c2 )
    assertTrue( -c1 == c2 )
  }

  function testErrant_NumericLiteralBasicTest() {
    processErrantType(Errant_NumericLiteralBasicTest)
  }

}