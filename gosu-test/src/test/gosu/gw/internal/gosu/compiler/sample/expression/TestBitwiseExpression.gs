package gw.internal.gosu.compiler.sample.expression

uses java.lang.Integer
  
class TestBitwiseExpression
{
  static function testOrInt() : int
  {
    var x : int = 16
    var y : int = 17
    return x | y
  }

  static function testXorInt() : int
  {
    var x : int = 16
    var y : int = 17
    return x ^ y
  }
  
  static function testAndInt() : int
  {
    var x : int = 16
    var y : int = 17
    return x & y
  }


  static function testOrIntLong() : int
  {
    var x : int = 16
    var y : long = 17
    return x | y as int
  }

  static function testXorIntLong() : int
  {
    var x : int = 16
    var y : long = 17
    return x ^ y as int
  }

  static function testAndIntLong() : int
  {
    var x : int = 16
    var y : long = 17
    return x & y as int
  }


  static function testOrLong() : long
  {
    var x : long = 16
    var y : long = 17
    return x | y
  }
  
  static function testXorLong() : long
  {
    var x : long = 16
    var y : long = 17
    return x ^ y
  }
    
  static function testAndLong() : long
  {
    var x : long = 16
    var y : long = 17
    return x & y
  }


  static function testOrLongInt() : long
  {
    var x : long = 16
    var y : int = 17
    return x as int | y
  }

  static function testXorLongInt() : long
  {
    var x : long = 16
    var y : int = 17
    return x as int ^ y
  }

  static function testAndLongInt() : long
  {
    var x : long = 16
    var y : int = 17
    return (x as int) & y
  }
}