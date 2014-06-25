package gw.internal.gosu.compiler.sample.expression
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.math.BigDecimal

class TestUnaryExpression
{
    function testNegatePbyte() : byte
    {
      var n : byte = 8
      return -n
    }
    function testNegateByte() : Byte
    {
      var n : Byte = 8
      return -n
    }

    function testNegatePshort() : short
    {
      var n : short = 8
      return -n
    }
    function testNegateShort() : Short
    {
      var n : Short = 8
      return -n
    }

    function testNegatePint() : int
    {
      var n : int = 8
      return -n
    }
    function testNegateInt() : Integer
    {
      var n : Integer = 8
      return -n
    }

    function testNegatePlong() : long
    {
      var n : long = 8
      return -n
    }
    function testNegateLong() : Long
    {
      var n : Long = 8
      return -n
    }

    function testNegatePfloat() : float
    {
      var n : float = 8
      return -n
    }
    function testNegateFloat() : Float
    {
      var n : Float = 8
      return -n
    }

    function testNegatePdouble() : double
    {
      var n : double = 8
      return -n
    }
    function testNegateDouble() : Double
    {
      var n : Double = 8
      return -n
    }

    function testNegateBigInteger() : BigInteger
    {
      var n : BigInteger = 8
      return -n
    }

    function testNegateBigDecimal() : BigDecimal
    {
      var n : BigDecimal = 8.500
      return -n
    }

    function testNegateDimension() : TestDim
    {
      print( TestDim )
      var n = new TestDim( 9 )
      return -n
    }

    function testBridgeOnJustArg()
    {
        var a = new Bar()
        print( a.blah( new Integer( 2 ) ) )
        var b = a as Foo
        print( b.blah( new Integer( 2 ) ) )
    }
    function testBridgeOnJustArgWithGenericMethod()
    {
        var a = new Bar()
        print( a.genmeth( new java.lang.Float( 9.2 ), new Integer( 2 ) ) )
        var b = a as Foo
        print( b.genmeth( new java.lang.Float( 9.3 ), new Integer( 2 ) ) )
    }

    private static class TestDim implements IDimension<TestDim, Integer>
    {
      var _value : Integer

      construct( value : Integer )
      {
        _value = value
      }

      override function toNumber() : Integer {
        return _value
      }

      override function fromNumber( p0: Integer ) : TestDim {
        return new TestDim( p0 )
      }

      override function numberType() : java.lang.Class<Integer> {
        return Integer 
      }

      override function compareTo( o: TestDim ) : int {
        return _value.compareTo( o._value )
      }
    }

    private static class Foo<T>
    {
        function blah( v: T ) : Object
        {
          return v
        }

        function genmeth<E extends java.lang.Number>( v: E, v2: T ) : Object
        {
          return v
        }
    }

    private static class Bar extends Foo<Integer>
    {
        function blah( i: Integer ) : Object
        {
          return i + 3
        }

        function genmeth<E extends java.lang.Number>( v: E, v2: Integer ) : Object
        {
          return (v.intValue() + v2) + (E as String)
        }
    }
}
