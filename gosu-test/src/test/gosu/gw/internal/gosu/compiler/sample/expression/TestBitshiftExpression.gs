package gw.internal.gosu.compiler.sample.expression

uses java.lang.Integer
  
class TestBitshiftExpression
{
  static function testShiftRightInt() : int
  {
    var x = 1024
    return x >> 2
  }

  static function testShiftLeftInt() : int
  {
    var x = 1024
    return x << 2
  }

  static function testShiftURightInt() : int
  {
    var x = 1024
    return x >>> 2
  }

  static function testShiftRightIntNeg() : int
  {
    var x = -1024
    return x >> 2
  }

  static function testShiftLeftIntNeg() : int
  {
    var x = -1024
    return x << 2
  }

  static function testShiftURightIntNeg() : int
  {
    var x = -1024
    return x >>> 2
  }
  
  static function testShiftRightLong() : long
  {
    var x : long = (Integer.MAX_VALUE as long) + 2
    return x >> 2
  }
  
  static function testShiftLeftLong() : long
  {
    var x : long = (Integer.MAX_VALUE as long) + 2
    return x << 2
  }
  
  static function testShiftURightLong() : long
  {
    var x : long = (Integer.MAX_VALUE as long) + 2
    return x >>> 2
  }
  
  static function testShiftRightLongNeg() : long
  {
    var x : long = -((Integer.MAX_VALUE as long) + 2)
    return x >> 2
  }
  
  static function testShiftLeftLongNeg() : long
  {
    var x : long = -((Integer.MAX_VALUE as long) + 2)
    return x << 2
  }
  
  static function testShiftURightLongNeg() : long
  {
    var x : long = -((Integer.MAX_VALUE as long) + 2)
    return x >>> 2
  }
}