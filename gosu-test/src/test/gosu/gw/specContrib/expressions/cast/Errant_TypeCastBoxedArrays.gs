package gw.specContrib.expressions.cast

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_TypeCastBoxedArrays {

  function testBooleanArray(b : Boolean[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]

    var x311 = b as Object[]
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testCharArray(b : Character[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]    //## issuekeys: INCONVERTIBLE TYPES;
    var x215 = b as Float[]       //## issuekeys: INCONVERTIBLE TYPES;
    var x216 = b as Long[]        //## issuekeys: INCONVERTIBLE TYPES;
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES;
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]        //## issuekeys: INCONVERTIBLE TYPES;
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]    //## issuekeys: INCONVERTIBLE TYPES;
    var x315 = b as BigInteger[]    //## issuekeys: INCONVERTIBLE TYPES;
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.CHARACTER[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testByteArray(b : Byte[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]
    //IDE-1713 - Parser Issue
    var x213 = b as Short[]         //## issuekeys: INCONVERTIBLE TYPES;
    var x214 = b as Integer[]       //## issuekeys: INCONVERTIBLE TYPES;
    var x215 = b as Float[]         //## issuekeys: INCONVERTIBLE TYPES;
    var x216 = b as Long[]          //## issuekeys: INCONVERTIBLE TYPES;
    var x217 = b as Double[]        //## issuekeys: INCONVERTIBLE TYPES;
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]     //## issuekeys: INCONVERTIBLE TYPES;
    var x315 = b as BigInteger[]     //## issuekeys: INCONVERTIBLE TYPES;
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BYTE[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testShortArray(b : Short[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES;
    var x215 = b as Float[]        //## issuekeys: INCONVERTIBLE TYPES;
    var x216 = b as Long[]         //## issuekeys: INCONVERTIBLE TYPES;
    var x217 = b as Double[]       //## issuekeys: INCONVERTIBLE TYPES;
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]     //## issuekeys: INCONVERTIBLE TYPES;
    var x315 = b as BigInteger[]     //## issuekeys: INCONVERTIBLE TYPES;
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.SHORT[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testIntArray(b : Integer[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]
    var x215 = b as Float[]        //## issuekeys: INCONVERTIBLE TYPES;
    var x216 = b as Long[]         //## issuekeys: INCONVERTIBLE TYPES;
    var x217 = b as Double[]       //## issuekeys: INCONVERTIBLE TYPES;
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]    //## issuekeys: INCONVERTIBLE TYPES;
    var x315 = b as BigInteger[]    //## issuekeys: INCONVERTIBLE TYPES;
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testFloatArray(b : Float[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES;
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]     //## issuekeys: INCONVERTIBLE TYPES;
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testLongArray(b : Long[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]        //## issuekeys: INCONVERTIBLE TYPES;
    var x216 = b as Long[]
    var x217 = b as Double[]       //## issuekeys: INCONVERTIBLE TYPES;
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]     //## issuekeys: INCONVERTIBLE TYPES;
    var x315 = b as BigInteger[]    //## issuekeys: INCONVERTIBLE TYPES;
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.LONG[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testDoubleArray(b : Double[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]     //## issuekeys: INCONVERTIBLE TYPES;
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.DOUBLE[]' TO 'JAVA.LANG.OBJECT[][]'
  }
}