package gw.specContrib.expressions.cast

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.Date

class Errant_TypeCastInBuiltTypes {
   function testString (s : String ) {
     var b111 = s as char      //## issuekeys: MSG_TYPE_MISMATCH
     var b112 = s as byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b113 = s as short      //## issuekeys: MSG_TYPE_MISMATCH
     var b114 = s as int      //## issuekeys: MSG_TYPE_MISMATCH
     var b115 = s as float      //## issuekeys: MSG_TYPE_MISMATCH
     var b116 = s as long      //## issuekeys: MSG_TYPE_MISMATCH
     var b117 = s as double      //## issuekeys: MSG_TYPE_MISMATCH
     var b118 = s as boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b211 = s as Character      //## issuekeys: MSG_TYPE_MISMATCH
     var b212 = s as Byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b213 = s as Short      //## issuekeys: MSG_TYPE_MISMATCH
     var b214 = s as Integer      //## issuekeys: MSG_TYPE_MISMATCH
     var b215 = s as Float      //## issuekeys: MSG_TYPE_MISMATCH
     var b216 = s as Long      //## issuekeys: MSG_TYPE_MISMATCH
     var b217 = s as Double      //## issuekeys: MSG_TYPE_MISMATCH
     var b218 = s as Boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b311 = s as String  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b312 = s as Date      //## issuekeys: MSG_TYPE_MISMATCH
     var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b314 = s as BigInteger      //## issuekeys: MSG_TYPE_MISMATCH
     var b315 = s as BigDecimal      //## issuekeys: MSG_TYPE_MISMATCH
   }
   function testArray (s : int[] ) {
     var b111 = s as char      //## issuekeys: MSG_TYPE_MISMATCH
     var b112 = s as byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b113 = s as short      //## issuekeys: MSG_TYPE_MISMATCH
     var b114 = s as int      //## issuekeys: MSG_TYPE_MISMATCH
     var b115 = s as float      //## issuekeys: MSG_TYPE_MISMATCH
     var b116 = s as long      //## issuekeys: MSG_TYPE_MISMATCH
     var b117 = s as double      //## issuekeys: MSG_TYPE_MISMATCH
     var b118 = s as boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b211 = s as Character      //## issuekeys: MSG_TYPE_MISMATCH
     var b212 = s as Byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b213 = s as Short      //## issuekeys: MSG_TYPE_MISMATCH
     var b214 = s as Integer      //## issuekeys: MSG_TYPE_MISMATCH
     var b215 = s as Float      //## issuekeys: MSG_TYPE_MISMATCH
     var b216 = s as Long      //## issuekeys: MSG_TYPE_MISMATCH
     var b217 = s as Double      //## issuekeys: MSG_TYPE_MISMATCH
     var b218 = s as Boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b311 = s as String
     var b312 = s as Date      //## issuekeys: MSG_TYPE_MISMATCH
     var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b314 = s as BigInteger      //## issuekeys: MSG_TYPE_MISMATCH
     var b315 = s as BigDecimal      //## issuekeys: MSG_TYPE_MISMATCH


     var b411 = s as int[]  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b412 = s as ArrayList      //## issuekeys: MSG_TYPE_MISMATCH
     var b413 = s as HashMap      //## issuekeys: MSG_TYPE_MISMATCH
   }
   function testArrayList (s : ArrayList<Integer> ) {
     var b111 = s as char      //## issuekeys: MSG_TYPE_MISMATCH
     var b112 = s as byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b113 = s as short      //## issuekeys: MSG_TYPE_MISMATCH
     var b114 = s as int      //## issuekeys: MSG_TYPE_MISMATCH
     var b115 = s as float      //## issuekeys: MSG_TYPE_MISMATCH
     var b116 = s as long      //## issuekeys: MSG_TYPE_MISMATCH
     var b117 = s as double      //## issuekeys: MSG_TYPE_MISMATCH
     var b118 = s as boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b211 = s as Character      //## issuekeys: MSG_TYPE_MISMATCH
     var b212 = s as Byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b213 = s as Short      //## issuekeys: MSG_TYPE_MISMATCH
     var b214 = s as Integer      //## issuekeys: MSG_TYPE_MISMATCH
     var b215 = s as Float      //## issuekeys: MSG_TYPE_MISMATCH
     var b216 = s as Long      //## issuekeys: MSG_TYPE_MISMATCH
     var b217 = s as Double      //## issuekeys: MSG_TYPE_MISMATCH
     var b218 = s as Boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b311 = s as String
     var b312 = s as Date      //## issuekeys: MSG_TYPE_MISMATCH
     var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b314 = s as BigInteger      //## issuekeys: MSG_TYPE_MISMATCH
     var b315 = s as BigDecimal      //## issuekeys: MSG_TYPE_MISMATCH


     var b411 = s as int[]      //## issuekeys: MSG_TYPE_MISMATCH
     var b412 = s as ArrayList  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b413 = s as HashMap      //## issuekeys: MSG_TYPE_MISMATCH
   }
   function testHashMap (s : HashMap ) {
     var b111 = s as char      //## issuekeys: MSG_TYPE_MISMATCH
     var b112 = s as byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b113 = s as short      //## issuekeys: MSG_TYPE_MISMATCH
     var b114 = s as int      //## issuekeys: MSG_TYPE_MISMATCH
     var b115 = s as float      //## issuekeys: MSG_TYPE_MISMATCH
     var b116 = s as long      //## issuekeys: MSG_TYPE_MISMATCH
     var b117 = s as double      //## issuekeys: MSG_TYPE_MISMATCH
     var b118 = s as boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b211 = s as Character      //## issuekeys: MSG_TYPE_MISMATCH
     var b212 = s as Byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b213 = s as Short      //## issuekeys: MSG_TYPE_MISMATCH
     var b214 = s as Integer      //## issuekeys: MSG_TYPE_MISMATCH
     var b215 = s as Float      //## issuekeys: MSG_TYPE_MISMATCH
     var b216 = s as Long      //## issuekeys: MSG_TYPE_MISMATCH
     var b217 = s as Double      //## issuekeys: MSG_TYPE_MISMATCH
     var b218 = s as Boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b311 = s as String
     var b312 = s as Date      //## issuekeys: MSG_TYPE_MISMATCH
     var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b314 = s as BigInteger      //## issuekeys: MSG_TYPE_MISMATCH
     var b315 = s as BigDecimal      //## issuekeys: MSG_TYPE_MISMATCH


     var b411 = s as int[]      //## issuekeys: MSG_TYPE_MISMATCH
     var b412 = s as ArrayList      //## issuekeys: MSG_TYPE_MISMATCH
     var b413 = s as HashMap  //## issuekeys: MSG_UNNECESSARY_COERCION
   }
   function testDateTime (s : Date ) {
     var b111 = s as char      //## issuekeys: MSG_TYPE_MISMATCH
     var b112 = s as byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b113 = s as short      //## issuekeys: MSG_TYPE_MISMATCH
     var b114 = s as int      //## issuekeys: MSG_TYPE_MISMATCH
     var b115 = s as float      //## issuekeys: MSG_TYPE_MISMATCH
     var b116 = s as long      //## issuekeys: MSG_TYPE_MISMATCH
     var b117 = s as double      //## issuekeys: MSG_TYPE_MISMATCH
     var b118 = s as boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b211 = s as Character      //## issuekeys: MSG_TYPE_MISMATCH
     var b212 = s as Byte      //## issuekeys: MSG_TYPE_MISMATCH
     var b213 = s as Short      //## issuekeys: MSG_TYPE_MISMATCH
     var b214 = s as Integer      //## issuekeys: MSG_TYPE_MISMATCH
     var b215 = s as Float      //## issuekeys: MSG_TYPE_MISMATCH
     var b216 = s as Long      //## issuekeys: MSG_TYPE_MISMATCH
     var b217 = s as Double      //## issuekeys: MSG_TYPE_MISMATCH
     var b218 = s as Boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b311 = s as String
     var b312 = s as Date  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b314 = s as BigInteger      //## issuekeys: MSG_TYPE_MISMATCH
     var b315 = s as BigDecimal      //## issuekeys: MSG_TYPE_MISMATCH


     var b411 = s as int[]      //## issuekeys: MSG_TYPE_MISMATCH
     var b412 = s as ArrayList      //## issuekeys: MSG_TYPE_MISMATCH
     var b413 = s as HashMap      //## issuekeys: MSG_TYPE_MISMATCH
   }
   function testBigInteger (s : BigInteger ) {
     var b111 = s as char
     var b112 = s as byte
     var b113 = s as short
     var b114 = s as int
     var b115 = s as float
     var b116 = s as long
     var b117 = s as double
     var b118 = s as boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b211 = s as Character
     var b212 = s as Byte
     var b213 = s as Short
     var b214 = s as Integer
     var b215 = s as Float
     var b216 = s as Long
     var b217 = s as Double
     var b218 = s as Boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b311 = s as String
     var b312 = s as Date      //## issuekeys: MSG_TYPE_MISMATCH
     var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b314 = s as BigInteger  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b315 = s as BigDecimal


     var b411 = s as int[]      //## issuekeys: MSG_TYPE_MISMATCH
     var b412 = s as ArrayList      //## issuekeys: MSG_TYPE_MISMATCH
     var b413 = s as HashMap      //## issuekeys: MSG_TYPE_MISMATCH
   }
   function testBigDecimal (s : BigDecimal ) {
     var b111 = s as char
     var b112 = s as byte
     var b113 = s as short
     var b114 = s as int
     var b115 = s as float
     var b116 = s as long
     var b117 = s as double
     var b118 = s as boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b211 = s as Character
     var b212 = s as Byte
     var b213 = s as Short
     var b214 = s as Integer
     var b215 = s as Float
     var b216 = s as Long
     var b217 = s as Double
     var b218 = s as Boolean      //## issuekeys: MSG_TYPE_MISMATCH

     var b311 = s as String
     var b312 = s as Date      //## issuekeys: MSG_TYPE_MISMATCH
     var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
     var b314 = s as BigInteger
     var b315 = s as BigDecimal  //## issuekeys: MSG_UNNECESSARY_COERCION


     var b411 = s as int[]      //## issuekeys: MSG_TYPE_MISMATCH
     var b412 = s as ArrayList      //## issuekeys: MSG_TYPE_MISMATCH
     var b413 = s as HashMap      //## issuekeys: MSG_TYPE_MISMATCH
   }

  function testObject (s : Object ) {
    var b111 = s as char
    var b112 = s as byte
    var b113 = s as short
    var b114 = s as int
    var b115 = s as float
    var b116 = s as long
    var b117 = s as double
    var b118 = s as boolean

    var b211 = s as Character
    var b212 = s as Byte
    var b213 = s as Short
    var b214 = s as Integer
    var b215 = s as Float
    var b216 = s as Long
    var b217 = s as Double
    var b218 = s as Boolean

    var b311 = s as String
    var b312 = s as Date
    var b313 = s as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var b314 = s as BigInteger
    var b315 = s as BigDecimal


    var b411 = s as int[]
    var b412 = s as ArrayList
    var b413 = s as HashMap
  }

}