package gw.specContrib.expressions.cast

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_TypeCast2DArrays {

  function testBooleanArray(b : boolean[][]) {
    var x111 = b as char[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'CHAR[]'
    var x112 = b as byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'BYTE[]'
    var x113 = b as short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'SHORT[]'
    var x114 = b as int[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'INT[]'
    var x115 = b as float[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'FLOAT[]'
    var x116 = b as long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'LONG[]'
    var x117 = b as double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'DOUBLE[]'
    var x118 = b as boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'BOOLEAN[]'

    var x211 = b as Character[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]                  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[][]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testBooleanArray(b : Boolean[][]) {
    var x111 = b as char[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'CHAR[]'
    var x112 = b as byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'BYTE[]'
    var x113 = b as short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'SHORT[]'
    var x114 = b as int[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'INT[]'
    var x115 = b as float[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'FLOAT[]'
    var x116 = b as long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'LONG[]'
    var x117 = b as double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'DOUBLE[]'
    var x118 = b as boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'BOOLEAN[]'

    var x211 = b as Character[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]                  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[][]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      
  }
  function testIntArray(b : int[][]) {
    var x111 = b as char[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'CHAR[]'
    var x112 = b as byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'BYTE[]'
    var x113 = b as short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'SHORT[]'
    var x114 = b as int[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'INT[]'
    var x115 = b as float[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'FLOAT[]'
    var x116 = b as long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'LONG[]'
    var x117 = b as double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'DOUBLE[]'
    var x118 = b as boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'BOOLEAN[]'

    var x211 = b as Character[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[][]' TO 'JAVA.LANG.OBJECT[][]'
  }
  function testIntArray(b : Integer[][]) {
    var x111 = b as char[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'CHAR[]'
    var x112 = b as byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'BYTE[]'
    var x113 = b as short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'SHORT[]'
    var x114 = b as int[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'INT[]'
    var x115 = b as float[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'FLOAT[]'
    var x116 = b as long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'LONG[]'
    var x117 = b as double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'DOUBLE[]'
    var x118 = b as boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'BOOLEAN[]'

    var x211 = b as Character[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.CHARACTER[]'
    var x212 = b as Byte[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.BYTE[]'
    var x213 = b as Short[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.SHORT[]'
    var x214 = b as Integer[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.INTEGER[]'
    var x215 = b as Float[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.FLOAT[]'
    var x216 = b as Long[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.LONG[]'
    var x217 = b as Double[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.DOUBLE[]'
    var x218 = b as Boolean[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.BOOLEAN[]'

    var x311 = b as Object[]
    var x312 = b as String[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.LANG.STRING[]'
    var x313 = b as DateTime[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.UTIL.DATE[]'
    var x314 = b as BigDecimal[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.MATH.BIGDECIMAL[]'
    var x315 = b as BigInteger[]            //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[][]' TO 'JAVA.MATH.BIGINTEGER[]'
    var x316 = b as Object
    var x317 = b as Object[][]      
  }

  function testBooleanArray(b : boolean[]) {
    var x111 = b as char[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'CHAR[][]'
    var x112 = b as byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'BYTE[][]'
    var x113 = b as short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'SHORT[][]'
    var x114 = b as int[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'INT[][]'
    var x115 = b as float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'FLOAT[][]'
    var x116 = b as long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'LONG[][]'
    var x117 = b as double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'DOUBLE[][]'
    var x118 = b as boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'BOOLEAN[][]'

    var x211 = b as Character[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.CHARACTER[][]'
    var x212 = b as Byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.BYTE[][]'
    var x213 = b as Short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.SHORT[][]'
    var x214 = b as Integer[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.INTEGER[][]'
    var x215 = b as Float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.FLOAT[][]'
    var x216 = b as Long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.LONG[][]'
    var x217 = b as Double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.DOUBLE[][]'
    var x218 = b as Boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.BOOLEAN[][]'

    var x311 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.OBJECT[][]'
    var x312 = b as String[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.STRING[][]'
    var x313 = b as DateTime[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.UTIL.DATE[][]'
    var x314 = b as BigDecimal[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.MATH.BIGDECIMAL[][]'
    var x315 = b as BigInteger[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.MATH.BIGINTEGER[][]'
    var x316 = b as Object
    var x317 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'BOOLEAN[]' TO 'JAVA.LANG.OBJECT[]'
  }
  function testBooleanArray(b : Boolean[]) {
    var x111 = b as char[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'CHAR[][]'
    var x112 = b as byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'BYTE[][]'
    var x113 = b as short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'SHORT[][]'
    var x114 = b as int[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'INT[][]'
    var x115 = b as float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'FLOAT[][]'
    var x116 = b as long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'LONG[][]'
    var x117 = b as double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'DOUBLE[][]'
    var x118 = b as boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'BOOLEAN[][]'

    var x211 = b as Character[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.CHARACTER[][]'
    var x212 = b as Byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.BYTE[][]'
    var x213 = b as Short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.SHORT[][]'
    var x214 = b as Integer[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.INTEGER[][]'
    var x215 = b as Float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.FLOAT[][]'
    var x216 = b as Long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.LONG[][]'
    var x217 = b as Double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.DOUBLE[][]'
    var x218 = b as Boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.BOOLEAN[][]'

    var x311 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.OBJECT[][]'
    var x312 = b as String[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.LANG.STRING[][]'
    var x313 = b as DateTime[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.UTIL.DATE[][]'
    var x314 = b as BigDecimal[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.MATH.BIGDECIMAL[][]'
    var x315 = b as BigInteger[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.BOOLEAN[]' TO 'JAVA.MATH.BIGINTEGER[][]'
    var x316 = b as Object
    var x317 = b as Object[]
  }
  function testIntArray(b : int[]) {
    var x111 = b as char[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'CHAR[][]'
    var x112 = b as byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BYTE[][]'
    var x113 = b as short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'SHORT[][]'
    var x114 = b as int[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'INT[][]'
    var x115 = b as float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'FLOAT[][]'
    var x116 = b as long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'LONG[][]'
    var x117 = b as double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'DOUBLE[][]'
    var x118 = b as boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BOOLEAN[][]'

    var x211 = b as Character[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.CHARACTER[][]'
    var x212 = b as Byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.BYTE[][]'
    var x213 = b as Short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.SHORT[][]'
    var x214 = b as Integer[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.INTEGER[][]'
    var x215 = b as Float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.FLOAT[][]'
    var x216 = b as Long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.LONG[][]'
    var x217 = b as Double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.DOUBLE[][]'
    var x218 = b as Boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.BOOLEAN[][]'

    var x311 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.OBJECT[][]'
    var x312 = b as String[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.STRING[][]'
    var x313 = b as DateTime[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.UTIL.DATE[][]'
    var x314 = b as BigDecimal[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.MATH.BIGDECIMAL[][]'
    var x315 = b as BigInteger[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.MATH.BIGINTEGER[][]'
    var x316 = b as Object
    var x317 = b as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'JAVA.LANG.OBJECT[]'
  }
  function testIntArray(b : Integer[]) {
    var x111 = b as char[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'CHAR[][]'
    var x112 = b as byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'BYTE[][]'
    var x113 = b as short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'SHORT[][]'
    var x114 = b as int[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'INT[][]'
    var x115 = b as float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'FLOAT[][]'
    var x116 = b as long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'LONG[][]'
    var x117 = b as double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'DOUBLE[][]'
    var x118 = b as boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'BOOLEAN[][]'

    var x211 = b as Character[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.CHARACTER[][]'
    var x212 = b as Byte[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BYTE[][]'
    var x213 = b as Short[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.SHORT[][]'
    var x214 = b as Integer[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.INTEGER[][]'
    var x215 = b as Float[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.FLOAT[][]'
    var x216 = b as Long[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.LONG[][]'
    var x217 = b as Double[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.DOUBLE[][]'
    var x218 = b as Boolean[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BOOLEAN[][]'

    var x311 = b as Object[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.OBJECT[][]'
    var x312 = b as String[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.STRING[][]'
    var x313 = b as DateTime[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.UTIL.DATE[][]'
    var x314 = b as BigDecimal[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.MATH.BIGDECIMAL[][]'
    var x315 = b as BigInteger[][]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.MATH.BIGINTEGER[][]'
    var x316 = b as Object
    var x317 = b as Object[]
  }
}