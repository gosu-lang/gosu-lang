package gw.specification.types.primitiveTypes

uses gw.BaseVerifyErrantTest
uses java.math.BigInteger

class NumericLiteralPrefixTest extends BaseVerifyErrantTest {

  function testBinaryLiteral() {
    var x0 = 0b1010
    assertTrue(typeof x0 == int)
    assertEquals(x0, 10)

    var x1 = 0b1010s
    assertTrue(typeof x1 == short)
    assertEquals(x1, 10 as short)

    var x2 = 0b1010S
    assertTrue(typeof x2 == short)
    assertEquals(x2, 10 as short)
    
    var x3 = 0b1010l
    assertTrue(typeof x3 == long)
    assertEquals(x3, 10 as long)

    var x4 = 0b1010L
    assertTrue(typeof x4 == long)
    assertEquals(x4, 10 as long)
    
    var x5 = 0b1010b
    assertTrue(typeof x5 == byte)
    assertEquals(x5, 10 as byte)
    
    var x6 = 0b1010B
    assertTrue(typeof x6 == byte)
    assertEquals(x6, 10 as byte)

    var x7 = 0b1010bi
    assertTrue(typeof x7 == BigInteger)
    var ten = new BigInteger("10")
    assertEquals(x7, ten)

    var x8 = 0b1010BI
    assertTrue(typeof x8 == BigInteger)
    assertEquals(x8, ten)
    
    var x9 : float = 0b1010
    assertTrue(typeof x9 == float)
    assertEquals(x9, 10.0 as float, 0)

    var x10 : int = 0b1010
    assertTrue(typeof x10 == int)
    assertEquals(x10, 10)

    var x11 : short = 0b1010
    assertTrue(typeof x11 == short)
    assertEquals(x11, 10 as short)

    var x12 : short = 0b1010S
    assertTrue(typeof x12 == short)
    assertEquals(x12, 10 as short)

    var x13 : long = 0b1010
    assertTrue(typeof x13 == long)
    assertEquals(x13, 10 as long)

    var x14 : long = 0b1010L
    assertTrue(typeof x14 == long)
    assertEquals(x14, 10 as long)

    var x15 : byte = 0b1010
    assertTrue(typeof x15 == byte)
    assertEquals(x15, 10 as byte)

    var x16 : byte = 0b1010B
    assertTrue(typeof x16 == byte)
    assertEquals(x16, 10 as byte)

  }

  public function testHexLiteral() : void {
    var x0 = 0xCAFE
    assertTrue(typeof x0 == int)
    assertEquals(x0, 51966)

    var x1 = 0xCAFEBABEl
    assertTrue(typeof x1 == long)
    assertEquals(x1, 3405691582L)

    var x2 : long = 0xCAFEBABEl
    assertTrue(typeof x2 == long)
    assertEquals(x2, 3405691582L)

    var x3 : long = 0xCAFEBABECAFEL
    assertTrue(typeof x3 == long)
    assertEquals(x3, 223195403569918L)

    var x4 = 0xCAFEBABECAFEL
    assertTrue(typeof x4 == long)
    assertEquals(x4, 223195403569918L)

    var x5 = 0x0CAFs
    assertTrue(typeof x5 == short)
    assertEquals(x5, 3247 as short)

    var x6 : short = 0x0CAF
    assertTrue(typeof x6 == short)
    assertEquals(x6, 3247 as short)

    var x7 : short = 0x0CAFS
    assertTrue(typeof x7 == short)
    assertEquals(x7, 3247 as short)

    var x8 = -0xCAFE
    assertTrue(typeof x8 == int)
    assertEquals(x8, -51966)

    var x9 = -0xCAFEBABEl
    assertTrue(typeof x9 == long)
    assertEquals(x9, -3405691582L)

    var x10 : long = +0xCAFEBABEl
    assertTrue(typeof x10 == long)
    assertEquals(x10, +3405691582L)

    var x11 : long = -0xCAFEBABECAFEL
    assertTrue(typeof x11 == long)
    assertEquals(x11, -223195403569918L)

    var x12 = -0xCAFEBABECAFEL
    assertTrue(typeof x12 == long)
    assertEquals(x12, -223195403569918L)

    var x13 = 0x7F
    assertTrue(typeof x13 == int)
    assertEquals(x13, 127)

    var x14 = -0x7F
    assertTrue(typeof x14 == int)
    assertEquals(x14, -127)

    var x15 = +0x7F
    assertTrue(typeof x15 == int)
    assertEquals(x15, +127)

    var x16 : byte  = 0x7F
    assertTrue(typeof x16 == byte)
    assertEquals(x16, +127 as byte)
  }

  function testErrant_NumericLiteralPrefixTest() {
    processErrantType(Errant_NumericLiteralPrefixTest)
  }
}
