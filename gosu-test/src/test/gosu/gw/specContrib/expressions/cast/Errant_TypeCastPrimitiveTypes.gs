package gw.specContrib.expressions.cast

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_TypeCastPrimitiveTypes {
  function testBool(b: boolean) {
    var b111 = b as char
    var b112 = b as byte
    var b113 = b as short
    var b114 = b as int
    var b115 = b as float
    var b116 = b as long
    var b117 = b as double
    var b118 = b as boolean  //## issuekeys: MSG_UNNECESSARY_COERCION

    //IDE-1709
    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger      //## issuekeys: MSG_TYPE_MISMATCH
    var b315 = b as BigDecimal      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testChar(b: char) {
    var b111 = b as char  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b112 = b as byte
    var b113 = b as short
    var b114 = b as int
    var b115 = b as float
    var b116 = b as long
    var b117 = b as double
    var b118 = b as boolean

    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger
    var b315 = b as BigDecimal
  }
  function testByte(b: byte) {
    var b111 = b as char
    var b112 = b as byte  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b113 = b as short
    var b114 = b as int
    var b115 = b as float
    var b116 = b as long
    var b117 = b as double
    var b118 = b as boolean

    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    //IDE-1709
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger
    var b315 = b as BigDecimal
  }
  function testShort(b: short) {
    var b111 = b as char
    var b112 = b as byte
    var b113 = b as short  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b114 = b as int
    var b115 = b as float
    var b116 = b as long
    var b117 = b as double
    var b118 = b as boolean

    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger
    var b315 = b as BigDecimal
  }
  function testInt(b: int) {
    var b111 = b as char
    var b112 = b as byte
    var b113 = b as short
    var b114 = b as int  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b115 = b as float
    var b116 = b as long
    var b117 = b as double
    var b118 = b as boolean

    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger
    var b315 = b as BigDecimal
  }
  function testFloat(b: float) {
    var b111 = b as char
    var b112 = b as byte
    var b113 = b as short
    var b114 = b as int
    var b115 = b as float  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b116 = b as long
    var b117 = b as double
    var b118 = b as boolean

    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger
    var b315 = b as BigDecimal
  }
  function testLong(b: long) {
    var b111 = b as char
    var b112 = b as byte
    var b113 = b as short
    var b114 = b as int
    var b115 = b as float
    var b116 = b as long  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b117 = b as double
    var b118 = b as boolean

    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger
    var b315 = b as BigDecimal
  }
  function testDouble(b: double) {
    var b111 = b as char
    var b112 = b as byte
    var b113 = b as short
    var b114 = b as int
    var b115 = b as float
    var b116 = b as long
    var b117 = b as double  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b118 = b as boolean

    var b211 = b as Character
    var b212 = b as Byte
    var b213 = b as Short
    var b214 = b as Integer
    var b215 = b as Float
    var b216 = b as Long
    var b217 = b as Double
    var b218 = b as Boolean

    var b311 = b as String
    var b312 = b as java.util.Date      //## issuekeys: MSG_TYPE_MISMATCH
    var b313 = b as Object
    var b314 = b as BigInteger
    var b315 = b as BigDecimal
  }
}