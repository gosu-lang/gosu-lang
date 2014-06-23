package gw.internal.gosu.compiler.instrumentation

class SampleInstrumentedClass {

  static function testMethod() {
    print( "hello world" )
  }

  static function testMethodWithVar() {
    var x = "test"
    var y = 42
    var z = 10.0
  }

  static function throwAnException() {
    throw new java.lang.RuntimeException( "yay" )
  }

  static var _value = ""

  static function withTryFinally() : String {
    try {
      _value += "try_"
    } finally {
      _value += "finally_"
    }
    _value += "end"
    return _value
  }

  static function forLoopWithNumber( i : int ) {
    for( j in 0..|i ) {
      print( j )
    }
  }

  static function forLoopWithList( i : List ) {
    for( j in i ) {
      print( j )
    }
  }

  static function forLoopWithArray( i : Object[] ) {
    for( j in i ) {
      print( j )
    }
  }

  static function whileLoop( i : int ) {
    var j = 0
    while( j < i ) {
      print( j )
      j++
    }
  }

  static function doWhileLoop( i : int ) {
    var j = 0
    do{
      print( j )
      j++
    } while( j < i )
  }

  function i_testMethod() {
    print( "hello world" )
  }

  function _i_testMethodWithVar() {
    var x = "test"
    var y = 42
    var z = 10.0
  }

  function _i_throwAnException() {
    throw new java.lang.RuntimeException( "yay" )
  }

  var _i_value = ""

  function _i_withTryFinally() : String {
    try {
      _i_value += "try_"
    } finally {
      _i_value += "finally_"
    }
    _i_value += "end"
    return _i_value
  }

  function _i_forLoopWithNumber( i : int ) {
    for( j in 0..|i ) {
      print( j )
    }
  }

  function _i_forLoopWithList( i : List ) {
    for( j in i ) {
      print( j )
    }
  }

  function _i_forLoopWithArray( i : Object[] ) {
    for( j in i ) {
      print( j )
    }
  }

  function _i_whileLoop( i : int ) {
    var j = 0
    while( j < i ) {
      print( j )
      j++
    }
  }

  function _i_doWhileLoop( i : int ) {
    var j = 0
    do{
      print( j )
      j++
    } while( j < i )
  }

  construct() {
  }

  construct(s : String) {
    print( "hello" )
  }

  static function testCallChain1() {
    testCallChain2()
  }

  static function testCallChain2() {
    testCallChain3()
  }

  static function testCallChain3() {
    print( "foo" )
  }
}