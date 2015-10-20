package gw.specification.types.primitiveTypes

uses gw.BaseVerifyErrantTest
uses java.math.BigInteger
uses java.math.BigDecimal
uses java.lang.*

class NumericLiteralBoundaryTest extends BaseVerifyErrantTest {

  function testByteSyntax() {
    // boundary cases
    assertEquals( new Byte( "0" ), 0b )
    assertEquals( new Byte("-0"), -0b )
  }

  function testShortSyntax() {
    // boundary cases
    assertEquals( new Short( (Short.MIN_VALUE + 1) as short ), eval( "${(Short.MIN_VALUE + 1) as short}s") )
    assertEquals( new Short( (Short.MIN_VALUE + 1) as short ), eval( "${(Short.MIN_VALUE + 1) as short}S") )
    assertEquals( new Short( Short.MAX_VALUE ), eval( "${Short.MAX_VALUE}s" ) )
    assertEquals( new Short( Short.MAX_VALUE ), eval( "${Short.MAX_VALUE}S") )
    assertEquals( new Short( "-0" ), -0s )
    assertEquals( new Short( "-0" ), -0S )
  }

  function testLongSyntax() {
    // boundary cases
    assertEquals( new Long( Long.MIN_VALUE + 1 ), eval( "${Long.MIN_VALUE + 1}l") )
    assertEquals( new Long( Long.MIN_VALUE + 1 ), eval( "${Long.MIN_VALUE + 1}L") )
    assertEquals( new Long( Long.MAX_VALUE ), eval( "${Long.MAX_VALUE}l" ) )
    assertEquals( new Long( Long.MAX_VALUE ), eval( "${Long.MAX_VALUE}L") )
    assertEquals( new Long( "-0" ), -0l )
    assertEquals( new Long( "-0" ), -0L )
  }

  function testFloatSyntax() {
    // boundary cases
    assertEquals( new Float( "-0" ), -0f )
    assertEquals( new Float( "-0" ), -0F )
  }

  function testDoubleSyntax() {
    // boundary cases
    assertEquals( new Double( "-0" ), -0d )
    assertEquals( new Double( "-0" ), -0D )
  }

  function testBigIntegerSyntax() {
    // boundary cases
    assertEquals( new BigInteger( "-0" ), -0bi )
    assertEquals( new BigInteger( "-0" ), -0BI )
  }

  function testBigDecimalSyntax() {
    // boundary cases
    assertEquals( new BigDecimal( "-0" ), -0bd )
    assertEquals( new BigDecimal( "-0" ), -0BD )
  }

  function testBinarySyntax() {
    // boundary cases
    assertEquals( Integer.MAX_VALUE,  eval("0b" + Integer.toString( Integer.MAX_VALUE, 2) ) )
    assertEquals( Integer.MAX_VALUE,  eval("0B" + Integer.toString( Integer.MAX_VALUE, 2) ) )
  }

  function testHexSyntax() {
    // boundary cases
    assertEquals( Integer.MAX_VALUE,  eval("0x" + Integer.toString( Integer.MAX_VALUE, 16) ) )
    assertEquals( Integer.MAX_VALUE,  eval("0X" + Integer.toString( Integer.MAX_VALUE, 16) ) )
  }
}