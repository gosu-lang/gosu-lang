package gw.specContrib.expressions.cast

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_TypeCastInBuiltTypes {
   function testString (s : String ) {
     var b111 = s as char      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'CHAR'
     var b112 = s as byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'BYTE'
     var b113 = s as short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'SHORT'
     var b114 = s as int      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'INT'
     var b115 = s as float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'FLOAT'
     var b116 = s as long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'LONG'
     var b117 = s as double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'DOUBLE'
     var b118 = s as boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'BOOLEAN'

     var b211 = s as Character      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.CHARACTER'
     var b212 = s as Byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.BYTE'
     var b213 = s as Short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.SHORT'
     var b214 = s as Integer      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.INTEGER'
     var b215 = s as Float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.FLOAT'
     var b216 = s as Long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.LONG'
     var b217 = s as Double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.DOUBLE'
     var b218 = s as Boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.LANG.BOOLEAN'

     var b311 = s as String
     var b312 = s as DateTime      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.UTIL.DATE'
     var b313 = s as Object
     var b314 = s as BigInteger      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.MATH.BIGINTEGER'
     var b315 = s as BigDecimal      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING' TO 'JAVA.MATH.BIGDECIMAL'
   }
   function testArray (s : int[] ) {
     var b111 = s as char      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'CHAR'
     var b112 = s as byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BYTE'
     var b113 = s as short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'SHORT'
     var b114 = s as int      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'INT'
     var b115 = s as float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'FLOAT'
     var b116 = s as long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'LONG'
     var b117 = s as double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'DOUBLE'
     var b118 = s as boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BOOLEAN'

     var b211 = s as Character      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.CHARACTER'
     var b212 = s as Byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.BYTE'
     var b213 = s as Short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.SHORT'
     var b214 = s as Integer      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.INTEGER'
     var b215 = s as Float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.FLOAT'
     var b216 = s as Long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.LONG'
     var b217 = s as Double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.DOUBLE'
     var b218 = s as Boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.BOOLEAN'

     var b311 = s as String
     var b312 = s as DateTime      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.UTIL.DATE'
     var b313 = s as Object
     var b314 = s as BigInteger      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.MATH.BIGINTEGER'
     var b315 = s as BigDecimal      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.MATH.BIGDECIMAL'


     var b411 = s as int[]
     var b412 = s as ArrayList      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.UTIL.ARRAYLIST'
     var b413 = s as HashMap      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.UTIL.HASHMAP'
   }
   function testArrayList (s : ArrayList<Integer> ) {
     var b111 = s as char      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'CHAR'
     var b112 = s as byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'BYTE'
     var b113 = s as short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'SHORT'
     var b114 = s as int      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'INT'
     var b115 = s as float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'FLOAT'
     var b116 = s as long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'LONG'
     var b117 = s as double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'DOUBLE'
     var b118 = s as boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'BOOLEAN'

     var b211 = s as Character      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.CHARACTER'
     var b212 = s as Byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.BYTE'
     var b213 = s as Short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.SHORT'
     var b214 = s as Integer      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.INTEGER'
     var b215 = s as Float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.FLOAT'
     var b216 = s as Long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.LONG'
     var b217 = s as Double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.DOUBLE'
     var b218 = s as Boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.BOOLEAN'

     var b311 = s as String
     var b312 = s as DateTime      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.DATE'
     var b313 = s as Object
     var b314 = s as BigInteger      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.MATH.BIGINTEGER'
     var b315 = s as BigDecimal      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.MATH.BIGDECIMAL'


     var b411 = s as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'INT[]'
     var b412 = s as ArrayList
     var b413 = s as HashMap      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.HASHMAP'
   }
   function testHashMap (s : HashMap ) {
     var b111 = s as char      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'CHAR'
     var b112 = s as byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'BYTE'
     var b113 = s as short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'SHORT'
     var b114 = s as int      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'INT'
     var b115 = s as float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'FLOAT'
     var b116 = s as long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'LONG'
     var b117 = s as double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'DOUBLE'
     var b118 = s as boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'BOOLEAN'

     var b211 = s as Character      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.CHARACTER'
     var b212 = s as Byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.BYTE'
     var b213 = s as Short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.SHORT'
     var b214 = s as Integer      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.INTEGER'
     var b215 = s as Float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.FLOAT'
     var b216 = s as Long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.LONG'
     var b217 = s as Double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.DOUBLE'
     var b218 = s as Boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.LANG.BOOLEAN'

     var b311 = s as String
     var b312 = s as DateTime      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.UTIL.DATE'
     var b313 = s as Object
     var b314 = s as BigInteger      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.MATH.BIGINTEGER'
     var b315 = s as BigDecimal      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.MATH.BIGDECIMAL'


     var b411 = s as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'INT[]'
     var b412 = s as ArrayList      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>' TO 'JAVA.UTIL.ARRAYLIST'
     var b413 = s as HashMap
   }
   function testDateTime (s : DateTime ) {
     var b111 = s as char      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'CHAR'
     var b112 = s as byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'BYTE'
     var b113 = s as short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'SHORT'
     var b114 = s as int      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'INT'
     var b115 = s as float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'FLOAT'
     var b116 = s as long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'LONG'
     var b117 = s as double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'DOUBLE'
     var b118 = s as boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'BOOLEAN'

     var b211 = s as Character      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.CHARACTER'
     var b212 = s as Byte      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.BYTE'
     var b213 = s as Short      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.SHORT'
     var b214 = s as Integer      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.INTEGER'
     var b215 = s as Float      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.FLOAT'
     var b216 = s as Long      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.LONG'
     var b217 = s as Double      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.DOUBLE'
     var b218 = s as Boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.LANG.BOOLEAN'

     var b311 = s as String
     var b312 = s as DateTime
     var b313 = s as Object
     var b314 = s as BigInteger      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.MATH.BIGINTEGER'
     var b315 = s as BigDecimal      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.MATH.BIGDECIMAL'


     var b411 = s as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'INT[]'
     var b412 = s as ArrayList      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.UTIL.ARRAYLIST'
     var b413 = s as HashMap      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.DATE' TO 'JAVA.UTIL.HASHMAP'
   }
   function testBigInteger (s : BigInteger ) {
     var b111 = s as char
     var b112 = s as byte
     var b113 = s as short
     var b114 = s as int
     var b115 = s as float
     var b116 = s as long
     var b117 = s as double
     var b118 = s as boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGINTEGER' TO 'BOOLEAN'

     var b211 = s as Character
     var b212 = s as Byte
     var b213 = s as Short
     var b214 = s as Integer
     var b215 = s as Float
     var b216 = s as Long
     var b217 = s as Double
     var b218 = s as Boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGINTEGER' TO 'JAVA.LANG.BOOLEAN'

     var b311 = s as String
     var b312 = s as DateTime      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGINTEGER' TO 'JAVA.UTIL.DATE'
     var b313 = s as Object
     var b314 = s as BigInteger
     var b315 = s as BigDecimal


     var b411 = s as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGINTEGER' TO 'INT[]'
     var b412 = s as ArrayList      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGINTEGER' TO 'JAVA.UTIL.ARRAYLIST'
     var b413 = s as HashMap      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGINTEGER' TO 'JAVA.UTIL.HASHMAP'
   }
   function testBigDecimal (s : BigDecimal ) {
     var b111 = s as char
     var b112 = s as byte
     var b113 = s as short
     var b114 = s as int
     var b115 = s as float
     var b116 = s as long
     var b117 = s as double
     var b118 = s as boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGDECIMAL' TO 'BOOLEAN'

     var b211 = s as Character
     var b212 = s as Byte
     var b213 = s as Short
     var b214 = s as Integer
     var b215 = s as Float
     var b216 = s as Long
     var b217 = s as Double
     var b218 = s as Boolean      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGDECIMAL' TO 'JAVA.LANG.BOOLEAN'

     var b311 = s as String
     var b312 = s as DateTime      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGDECIMAL' TO 'JAVA.UTIL.DATE'
     var b313 = s as Object
     var b314 = s as BigInteger
     var b315 = s as BigDecimal


     var b411 = s as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGDECIMAL' TO 'INT[]'
     var b412 = s as ArrayList      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGDECIMAL' TO 'JAVA.UTIL.ARRAYLIST'
     var b413 = s as HashMap      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.MATH.BIGDECIMAL' TO 'JAVA.UTIL.HASHMAP'
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
    var b312 = s as DateTime
    var b313 = s as Object
    var b314 = s as BigInteger
    var b315 = s as BigDecimal


    var b411 = s as int[]
    var b412 = s as ArrayList
    var b413 = s as HashMap
  }

}