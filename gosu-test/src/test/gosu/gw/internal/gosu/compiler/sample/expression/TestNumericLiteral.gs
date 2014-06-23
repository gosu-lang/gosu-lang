package gw.internal.gosu.compiler.sample.expression
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.math.BigDecimal

class TestNumericLiteral
{
  function testTypeWithContextPbyte() : Object
  {
    var n : byte = 8
    return n
  }
  function testTypeWithContextByte() : Object
  {
    var n : Byte = 8
    return n
  }

  function testTypeWithContextPshort() : Object
  {
    var n : short = 8
    return n
  }
  function testTypeWithContextShort() : Object
  {
    var n : Short = 8
    return n
  }

  function testTypeWithContextPint() : Object
  {
    var n : int = 8
    return n
  }
  function testTypeWithContextInt() : Object
  {
    var n : Integer = 8
    return n
  }

  function testTypeWithContextPlong() : Object
  {
    var n : long = 8
    return n
  }
  function testTypeWithContextLong() : Object
  {
    var n : Long = 8
    return n
  }

  function testTypeWithContextPfloat() : Object
  {
    var n : float = 8
    return n
  }
  function testTypeWithContextFloat() : Object
  {
    var n : Float = 8
    return n
  }

  function testTypeWithContextPdouble() : Object
  {
    var n : double = 8
    return n
  }
  function testTypeWithContextDouble() : Object
  {
    var n : Double = 8
    return n
  }

  function testTypeWithContextBigInteger() : Object
  {
    var n : BigInteger = 8
    return n
  }

  function testTypeWithContextBigDecimal() : Object
  {
    var n : BigDecimal = 8.500
    return n
  }
}