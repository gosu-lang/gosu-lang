package gw.internal.gosu.compiler.sample.expression

uses java.util.ArrayList
uses java.awt.Point

class TestBeanMethodCallExpansion
{
  var _propString : String as PropString
  var _propByte : byte as PropByte
  var _propBoolean : boolean as PropBoolean
  var _propChar : char as PropChar
  var _propShort : short as PropShort
  var _propInt : int as PropInt
  var _propLong : long as PropLong
  var _propFloat : float as PropFloat
  var _propDouble : double as PropDouble

  function callString() : String
  {
    return _propString
  }

  function callByte() : byte
  {
    return _propByte
  }

  function callBoolean() : boolean
  {
    return _propBoolean
  }

  function callChar() : char
  {
    return _propChar
  }

  function callShort() : short
  {
    return _propShort
  }
  
  function callInt() : int
  {
    return _propInt
  }

  function callLong() : long
  {
    return _propLong
  }

  function callFloat() : float
  {
    return _propFloat
  }

  function callDouble() : double
  {
    return _propDouble
  }

  function callVoid( l : ArrayList<java.lang.Double> ) : void
  {
    l.add( _propDouble )
  }

  static function testArray() : Point[]
  {
    var arr = new Point[] {new Point( 1, 2 ), new Point( 3, 4 ), new Point( 5, 6 )}
    return arr*.getLocation()
  }

  static function testStringArray() : String[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropString="abc"}, new TestBeanMethodCallExpansion() {:PropString="def"}, new TestBeanMethodCallExpansion() {:PropString="ghi"} }
    return l*.callString()
  }

  static function testByteArray() : byte[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropByte=1}, new TestBeanMethodCallExpansion() {:PropByte=2}, new TestBeanMethodCallExpansion() {:PropByte=3} }
    return l*.callByte()
  }
  
  static function testBooleanArray() : boolean[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropBoolean=true}, new TestBeanMethodCallExpansion() {:PropBoolean=false}, new TestBeanMethodCallExpansion() {:PropBoolean=true} }
    return l*.callBoolean()
  }
  
  static function testCharArray() : char[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropChar='a'}, new TestBeanMethodCallExpansion() {:PropChar='b'}, new TestBeanMethodCallExpansion() {:PropChar='c'} }
    return l*.callChar()
  }
  
  static function testShortArray() : short[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropShort=1}, new TestBeanMethodCallExpansion() {:PropShort=2}, new TestBeanMethodCallExpansion() {:PropShort=3} }
    return l*.callShort()
  }
  
  static function testIntArray() : int[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropInt=1}, new TestBeanMethodCallExpansion() {:PropInt=2}, new TestBeanMethodCallExpansion() {:PropInt=3} }
    return l*.callInt()
  }
  
  static function testLongArray() : long[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropLong=1}, new TestBeanMethodCallExpansion() {:PropLong=2}, new TestBeanMethodCallExpansion() {:PropLong=3} }
    return l*.callLong()
  }
  
  static function testFloatArray() : float[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropFloat=1}, new TestBeanMethodCallExpansion() {:PropFloat=2}, new TestBeanMethodCallExpansion() {:PropFloat=3} }
    return l*.callFloat()
  }
  
  static function testDoubleArray() : double[]
  {
    var l = {new TestBeanMethodCallExpansion() {:PropDouble=1}, new TestBeanMethodCallExpansion() {:PropDouble=2}, new TestBeanMethodCallExpansion() {:PropDouble=3} }
    return l*.callDouble()
  }

  static function testVoid() : ArrayList<java.lang.Double>
  {
    var l = {new TestBeanMethodCallExpansion() {:PropDouble=1}, new TestBeanMethodCallExpansion() {:PropDouble=2}, new TestBeanMethodCallExpansion() {:PropDouble=3} }
    var al = new ArrayList<java.lang.Double>()
    l*.callVoid( al )
    return al
  }
}