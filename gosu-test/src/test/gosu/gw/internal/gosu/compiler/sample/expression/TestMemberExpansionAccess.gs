package gw.internal.gosu.compiler.sample.expression

uses java.util.ArrayList
uses java.awt.Point

class TestMemberExpansionAccess
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
  var _propDoubleArray : double[] as PropDoubleArray


  static function testArray() : double[]
  {
    var arr = new Point[] {new Point( 1, 2 ), new Point( 3, 4 ), new Point( 5, 6 )}
    return arr*.X
  }

  static function testStringArray() : String[]
  {
    var l = {new TestMemberExpansionAccess() {:PropString="abc"}, new TestMemberExpansionAccess() {:PropString="def"}, new TestMemberExpansionAccess() {:PropString="ghi"} }
    return l*.PropString
  }

  static function testByteArray() : byte[]
  {
    var l = {new TestMemberExpansionAccess() {:PropByte=1}, new TestMemberExpansionAccess() {:PropByte=2}, new TestMemberExpansionAccess() {:PropByte=3} }
    return l*.PropByte
  }

  static function testBooleanArray() : boolean[]
  {
    var l = {new TestMemberExpansionAccess() {:PropBoolean=true}, new TestMemberExpansionAccess() {:PropBoolean=false}, new TestMemberExpansionAccess() {:PropBoolean=true} }
    return l*.PropBoolean
  }

  static function testCharArray() : char[]
  {
    var l = {new TestMemberExpansionAccess() {:PropChar='a'}, new TestMemberExpansionAccess() {:PropChar='b'}, new TestMemberExpansionAccess() {:PropChar='c'} }
    return l*.PropChar
  }

  static function testShortArray() : short[]
  {
    var l = {new TestMemberExpansionAccess() {:PropShort=1}, new TestMemberExpansionAccess() {:PropShort=2}, new TestMemberExpansionAccess() {:PropShort=3} }
    return l*.PropShort
  }

  static function testIntArray() : int[]
  {
    var l = {new TestMemberExpansionAccess() {:PropInt=1}, new TestMemberExpansionAccess() {:PropInt=2}, new TestMemberExpansionAccess() {:PropInt=3} }
    return l*.PropInt
  }

  static function testLongArray() : long[]
  {
    var l = {new TestMemberExpansionAccess() {:PropLong=1}, new TestMemberExpansionAccess() {:PropLong=2}, new TestMemberExpansionAccess() {:PropLong=3} }
    return l*.PropLong
  }

  static function testFloatArray() : float[]
  {
    var l = {new TestMemberExpansionAccess() {:PropFloat=1}, new TestMemberExpansionAccess() {:PropFloat=2}, new TestMemberExpansionAccess() {:PropFloat=3} }
    return l*.PropFloat
  }

  static function testDoubleArray() : double[]
  {
    var l = {new TestMemberExpansionAccess() {:PropDouble=1}, new TestMemberExpansionAccess() {:PropDouble=2}, new TestMemberExpansionAccess() {:PropDouble=3} }
    return l*.PropDouble
  }

  static function testDoubleIterator() : double[]
  {
    var l = {new TestMemberExpansionAccess() {:PropDouble=1}, new TestMemberExpansionAccess() {:PropDouble=2}, new TestMemberExpansionAccess() {:PropDouble=3} }
    return l.iterator()*.PropDouble
  }

  static function testDoubleIterable() : double[]
  {
    var l = {new TestMemberExpansionAccess() {:PropDouble=1}, new TestMemberExpansionAccess() {:PropDouble=2}, new TestMemberExpansionAccess() {:PropDouble=3} }
    return (l as java.lang.Iterable<TestMemberExpansionAccess>)*.PropDouble
  }

  static function testDoubleArrayArray() : double[]
  {
    var l = {new TestMemberExpansionAccess() {:PropDoubleArray={1, 2}}, new TestMemberExpansionAccess() {:PropDoubleArray={}}, new TestMemberExpansionAccess() {:PropDoubleArray={3, 4, 5}} }
    return l*.PropDoubleArray
  }

  static function testDoubleNestedList() : double[]
  {
    var l = {{new TestMemberExpansionAccess() {:PropDouble=1}, new TestMemberExpansionAccess() {:PropDouble=2} },
             {new TestMemberExpansionAccess() {:PropDouble=3}, new TestMemberExpansionAccess() {:PropDouble=4} },
             {new TestMemberExpansionAccess() {:PropDouble=5}, new TestMemberExpansionAccess() {:PropDouble=6}, new TestMemberExpansionAccess() {:PropDouble=7} }}
    l.add( 1, {} )
    return l*.PropDouble
  }

  static function testNotNullList() : String[]
  {
    var l : List<TestMemberExpansionAccess>
    var x : TestMemberExpansionAccess
    return l*.PropString
  }
}