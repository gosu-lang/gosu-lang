package gw.internal.gosu.compiler.sample.expression
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double

class TestUnaryNotPlusMinusExpression
{
    function testNotPbyte() : boolean
    {
      var n : byte = 8 as byte
      return !(n as boolean)
    }
    function testNotByte() : boolean
    {
      var n : Byte = 8 as byte
      return !(n as boolean)
    }

    function testNotPshort() : boolean
    {
      var n : short = 8
      return !(n as boolean)
    }
    function testNotShort() : boolean
    {
      var n : Short = 8
      return !(n as boolean)
    }

    function testNotPint() : boolean
    {
      var n : int = 8
      return !(n as boolean)
    }
    function testNotInt() : boolean
    {
      var n : Integer = 8
      return !(n as boolean)
    }

    function testNotPlong() : boolean
    {
      var n : long = 8
      return !(n as boolean)
    }
    function testNotLong() : boolean
    {
      var n : Long = 8
      return !(n as boolean)
    }

    function testNotPfloat() : boolean
    {
      var n : float = 8
      return !(n as boolean)
    }
    function testNotFloat() : boolean
    {
      var n : Float = 8
      return !(n as boolean)
    }

    function testNotPdouble() : boolean
    {
      var n : double = 8
      return !(n as boolean)
    }
    function testNotDouble() : boolean
    {
      var n : Double = 8
      return !(n as boolean)
    }

    function testBitNotPbyte() : byte
    {
      var n : byte = 8 as byte
      return (~n) as byte
    }
    function testBitNotByte() : Byte
    {
      var n : Byte = 8 as byte
      return (~n) as byte
    }

    function testBitNotPshort() : short
    {
      var n : short = 8
      return (~n) as short
    }
    function testBitNotShort() : Short
    {
      var n : Short = 8
      return (~n) as short
    }

    function testBitNotPint() : int
    {
      var n : int = 8
      return ~n
    }
    function testBitNotInt() : Integer
    {
      var n : Integer = 8
      return ~n
    }

    function testBitNotPlong() : long
    {
      var n : long = 8
      return ~n
    }
    function testBitNotLong() : Long
    {
      var n : Long = 8
      return ~n
    }
}
