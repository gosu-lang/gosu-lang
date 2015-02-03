package gw.specContrib.expressions.cast

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_TypeCastPrimitiveArrays {

  function testBooleanArray(b : boolean[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testCharArray(b : char[]) {
    var x111 = b as char[]
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'CHAR[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testByteArray(b : byte[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'CHAR[]'
    var x112 = b as byte[]
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BYTE[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testShortArray(b : short[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'BYTE[]'
    var x113 = b as short[]
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'SHORT[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testIntArray(b : int[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'SHORT[]'
    var x114 = b as int[]
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testFloatArray(b : float[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'INT[]'
    var x115 = b as float[]
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'LONG[]'
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testLongArray(b : long[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'FLOAT[]'
    var x116 = b as long[]
    var x117 = b as double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'DOUBLE[]'
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'LONG[]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testDoubleArray(b : double[]) {
    var x111 = b as char[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'CHAR[]'
    var x112 = b as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'BYTE[]'
    var x113 = b as short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'SHORT[]'
    var x114 = b as int[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'INT[]'
    var x115 = b as float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'FLOAT[]'
    var x116 = b as long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'LONG[]'
    var x117 = b as double[]
    var x118 = b as boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'BOOLEAN[]'

    var x211 = b as Character[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.OBJECT[]'
    var x312 = b as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'DOUBLE[]' TO 'JAVA.LANG.OBJECT[][]'
  }
}