package gw.internal.gosu.compiler.sample.expression

class TestMemberAccess
{
  static var INSTANCE : TestMemberAccess = new TestMemberAccess()

  var _boolean : boolean as boolean_prop = true
  var _byte : byte as byte_prop = 3
  var _short : short as short_prop = 4
  var _int : int as int_prop = 5
  var _long : long as long_prop = 6
  var _float : float as float_prop = 7
  var _double : double as double_prop = 8
  var _char : char as char_prop = '9'
  var _string : String as string_prop = "10"
  var _intArray : int[] as intArray_prop = new int[] {1, 2, 3}

  static function booleanFieldAccess() : boolean
  {
    return INSTANCE._boolean
  }

  static function byteFieldAccess() : byte
  {
    return INSTANCE._byte
  }

  static function shortFieldAccess() : short
  {
    return INSTANCE._short
  }

  static function intFieldAccess() : int
  {
    return INSTANCE._int
  }

  static function longFieldAccess() : long
  {
    return INSTANCE._long
  }

  static function floatFieldAccess() : float
  {
    return INSTANCE._float
  }

  static function doubleFieldAccess() : double
  {
    return INSTANCE._double
  }

  static function charFieldAccess() : char
  {
    return INSTANCE._char
  }

  static function stringFieldAccess() : String
  {
    return INSTANCE._string
  }

  static function intArrayFieldAccess() : int[]
  {
    return INSTANCE._intArray
  }

  static function booleanPropertyAccess() : boolean
  {
    return INSTANCE.boolean_prop
  }

  static function bytePropertyAccess() : byte
  {
    return INSTANCE.byte_prop
  }

  static function shortPropertyAccess() : short
  {
    return INSTANCE.short_prop
  }

  static function intPropertyAccess() : int
  {
    return INSTANCE.int_prop
  }

  static function longPropertyAccess() : long
  {
    return INSTANCE.long_prop
  }

  static function floatPropertyAccess() : float
  {
    return INSTANCE.float_prop
  }

  static function doublePropertyAccess() : double
  {
    return INSTANCE.double_prop
  }

  static function charPropertyAccess() : char
  {
    return INSTANCE.char_prop
  }

  static function stringPropertyAccess() : String
  {
    return INSTANCE.string_prop
  }

  static function intArrayPropertyAccess() : int[]
  {
    return INSTANCE.intArray_prop
  }

  static function intArrayPropertyLengthAccess() : int
  {
    return INSTANCE.intArray_prop.length
  }

  static function shortCircuitFieldAccess() : String
  {
    var nullRef : TestMemberAccess
    return nullRef?._string
  }

  static function shortCircuitPropertyAccess() : String
  {
    var nullRef : TestMemberAccess
    return nullRef?.string_prop
  }

  static function npePrimitiveFieldAccess() : int
  {
    var nullRef : TestMemberAccess
    return nullRef._int
  }

  static function npePrimitivePropertyAccess() : int
  {
    var nullRef : TestMemberAccess
    return nullRef.int_prop
  }

  static function associativeArrayAccesStatic() : int
  {
    var n = new java.lang.Integer( "9" )
    return n["MIN_VALUE"] as java.lang.Integer
  }

  static function associativeArrayAccesStaticPolymorphism() : int
  {
    var n : Object = new java.lang.Integer( "9" )
    return n["MIN_VALUE"] as java.lang.Integer
  }

  static function associativeArrayAccesNonStatic() : int
  {
    var x = new java.awt.Rectangle( 4, 4, 4, 4 )
    return x["Height"] as java.lang.Integer
  }

  static function associativeArrayAccesNonStaticPolymorphism() : int
  {
    var x : Object = new java.awt.Rectangle( 4, 4, 4, 4 )
    return x["Height"] as java.lang.Integer
  }
}