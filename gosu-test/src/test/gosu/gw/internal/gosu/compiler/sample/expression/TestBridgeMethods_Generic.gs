package gw.internal.gosu.compiler.sample.expression
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.math.BigDecimal

class TestBridgeMethods_Generic
{
    function testNonBridgeOnJustArg() : Object
    {
        var a = new Bar()
        return a.blah( new Integer( 2 ) )
    }
    function testBridgeOnJustArg() : Object
    {
        var a = new Bar()
        var b = a as Foo
        return b.blah( new Integer( 2 ) )
    }

    function testNonBridgeOnJustArgWithGenericMethod() : Object
    {
        var a = new Bar()
        return a.genmeth( new java.lang.Float( 9.2 ), new Integer( 2 ) )
    }
    function testBridgeOnJustArgWithGenericMethod() : Object
    {
        var a = new Bar()
        var b = a as Foo
        return b.genmeth( new java.lang.Float( 9.3 ), new Integer( 2 ) )
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
          return (v.floatValue() + v2) + (E as String)
        }
    }
}
