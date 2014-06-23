package gw.internal.gosu.compiler.sample.statement

uses java.util.*
uses java.lang.*
uses java.math.*

class HasSwitchStatement
{
  static var BYTE_1 : byte = 1
  static var BYTE_8 : byte = 8
  static var BYTE_10 : byte = 10

  static var CHAR_1 : char = 1 as char
  static var CHAR_8 : char = 8 as char
  static var CHAR_10 : char = 10 as char
  
  static var SHORT_1 : short = 1
  static var SHORT_8 : short = 8
  static var SHORT_10 : short = 10    

  static var INT_1 : int = 1
  static var INT_8 : int = 8
  static var INT_10 : int = 10
  
  
  //
  // Int types
  //

  static function testByteSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData : byte = 8
    switch( iData )
    {
      case BYTE_1:
        res.add( 1 )
        break
      case BYTE_8:
        res.add( 8 )
        break
      case BYTE_10:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testCharSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8 as char
    switch( iData )
    {
      case CHAR_1:
        res.add( 1 )
        break
      case CHAR_8:
        res.add( 8 )
        break
      case CHAR_10:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testShortSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData : short = 8
    switch( iData )
    {
      case SHORT_1:
        res.add( 1 )
        break
      case SHORT_8:
        res.add( 8 )
        break
      case SHORT_10:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testIntSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData : int = 8
    switch( iData )
    {
      case INT_1:
        res.add( 1 )
        break
      case INT_8:
        res.add( 8 )
        break
      case INT_10:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testIntSwitchByteCase() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData : int = 8
    switch( iData )
    {
      case BYTE_1:
        res.add( 1 )
        break
      case BYTE_8:
        res.add( 8 )
        break
      case BYTE_10:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testByteSwitchIntCase() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData : byte = 8
    switch( iData )
    {
      case INT_1 as byte:
        res.add( 1 )
        break
      case INT_8 as byte:
        res.add( 8 )
        break
      case INT_10 as byte:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testIntSwitchStringCase() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData : int = 8
    switch( iData )
    {
      case "1".toInt():
        res.add( 1 )
        break
      case "8".toInt():
        res.add( 8 )
        break
      case "10".toInt():
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testStringSwitchIntCase() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = "8"
    switch( iData )
    {
      case INT_1 as String:
        res.add( 1 )
        break
      case INT_8 as String:
        res.add( 8 )
        break
      case INT_10 as String:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  //
  // Other types
  //

  static function testEnumSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var data = FooEnum.Barney
    switch( data )
    {
      case FooEnum.Fred:
        res.add( 1 )
        break
      case FooEnum.Barney:
        res.add( 8 )
        break
      case FooEnum.Betty:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testStringSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var strData = "hello"
    switch( strData )
    {
      case "bye":
        res.add( 1 )
        break
      case "hello":
        res.add( 8 )
        break
      case "goober":
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testBooleanSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData : boolean = true
    switch( iData )
    {
      case false:
        res.add( 1 )
        break
      case true:
        res.add( 8 )
        break
      default:
        res.add( -1 )
    }
    return res
  }
  
  static function testIntegerSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var data = Integer.valueOf( 8 )
    switch( data )
    {
      case Integer.valueOf( 1 ):
        res.add( 1 )
        break
      case Integer.valueOf( 8 ):
        res.add( 8 )
        break
      case Integer.valueOf( 10 ):
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testBigDecimalSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var data = BigDecimal.valueOf( 8.1 )
    switch( data )
    {
      case BigDecimal.valueOf( 8.0 ):
        res.add( 1 )
        break
      case BigDecimal.valueOf( 8.1 ):
        res.add( 8 )
        break
      case BigDecimal.valueOf( 10.5 ):
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  //
  // Single case
  //

  static function testMatchSingleCaseWithBreakWithDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 8:
        res.add( 8 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testMatchSingleCaseWithBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 8:
        res.add( 8 )
        break
    }
    return res
  }

  static function testMatchSingleCaseNoBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 8:
        res.add( 8 )
    }
    return res
  }

  static function testNotMatchSingleCaseWithBreakWithDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 1:
        res.add( 1 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testNotMatchSingleCaseWithBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 1:
        res.add( 1 )
        break
    }
    return res
  }

  static function testNotMatchSingleCaseNoBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 1:
        res.add( 1 )
    }
    return res
  }


  //
  // Multi case
  //

  static function testMatchMultiCaseWithBreakWithDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 7:
        res.add( 7 )
        break
      case 8:
        res.add( 8 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testMatchMultiCaseWithBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 7:
        res.add( 7 )
        break
      case 8:
        res.add( 8 )
        break
    }
    return res
  }

  static function testMatchMultiCaseNoBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 7:
        res.add( 7 )
      case 8:
        res.add( 8 )
    }
    return res
  }

  static function testNotMatchMultiCaseWithBreakWithDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 0:
        res.add( 0 )
        break
      case 1:
        res.add( 1 )
        break
      default:
        res.add( -1 )
    }
    return res
  }

  static function testNotMatchMultiCaseWithBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 1:
        res.add( 1 )
        break
      case 0:
        res.add( 0 )
        break
    }
    return res
  }

  static function testNotMatchMultiCaseNoBreakNoDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 0:
        res.add( 0 )
      case 1:
        res.add( 1 )
    }
    return res
  }


  //
  // Fall through
  //

  static function testFallThroughFromTopWithBreakBeforeDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 8:
        res.add( 8 )
      case 9:
        res.add( 9 )
      case 10:
        res.add( 10 )
        break
      case 11:
        res.add( 11 )
      default:
        res.add( -1 )
    }
    return res
  }

  static function testFallThroughFromMiddleWithBreakBeforeDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 7:
        res.add( 7 )
      case 8:
        res.add( 8 )
      case 9:
        res.add( 9 )
      case 10:
        res.add( 10 )
        break
      case 11:
        res.add( 11 )
      default:
        res.add( -1 )
    }
    return res
  }

  static function testFallThroughAndIncludeDefault() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 7:
        res.add( 7 )
      case 8:
        res.add( 8 )
      case 9:
        res.add( 9 )
      case 10:
        res.add( 10 )
      default:
        res.add( -1 )
    }
    return res
  }

  static function testFallThroughNoBody() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 6:
        break
      case 7:
      case 8:
      case 9:
        res.add( 9 )
        break
      case 10:
        res.add( 10 )
      default:
        res.add( -1 )
    }
    return res
  }


  //
  // Misc.
  //

  static function testSwitch() : List<Integer>
  {
    var res = new ArrayList<Integer>()
    var iData = 8
    switch( iData )
    {
      case 1:
        res.add( 1 )
        break
      case 8:
        res.add( 8 )
        break
      case 10:
        res.add( 10 )
        break
      default:
        res.add( -1 )
    }
    return res
  }



  enum FooEnum
  {
    Fred, Barney, Wilma, Betty
  }
}