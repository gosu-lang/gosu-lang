package gw.specContrib.expressions.binding
uses gw.test.TestClass

class RationalTest extends TestClass {
  function testSimple() {
    assertEquals( "0 / 1", 0r.toFractionString() )
    assertEquals( "1 / 1", 1r.toFractionString() )
    assertEquals( "2 / 1", 2r.toFractionString() )
    assertEquals( "11 / 10", 1.1r.toFractionString() )
    assertEquals( "123456789012345678901234567890 / 1", 123456789012345678901234567890r.toFractionString() )
    assertEquals( "-123456789012345678901234567890 / 1", (-123456789012345678901234567890r).toFractionString() )
  }
}