package gw.specContrib.expressions.cast

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_TypeCastBoxedArrays {

  function testBooleanArray(b : Boolean[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x212 = b as Byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as Short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as Integer[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as Float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as Long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as Double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x218 = b as Boolean[]  //## issuekeys: MSG_UNNECESSARY_COERCION

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testCharArray(b : Character[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x212 = b as Byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as Short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as Integer[]    //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as Float[]       //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as Long[]        //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as Double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x218 = b as Boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]        //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]    //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]    //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testByteArray(b : Byte[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x212 = b as Byte[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    //IDE-1713 - Parser Issue
    var x213 = b as Short[]         //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as Integer[]       //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as Float[]         //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as Long[]          //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as Double[]        //## issuekeys: MSG_TYPE_MISMATCH
    var x218 = b as Boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]     //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]     //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testShortArray(b : Short[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x212 = b as Byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as Short[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x214 = b as Integer[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as Float[]        //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as Long[]         //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as Double[]       //## issuekeys: MSG_TYPE_MISMATCH
    var x218 = b as Boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]     //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]     //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testIntArray(b : Integer[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x212 = b as Byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as Short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as Integer[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x215 = b as Float[]        //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as Long[]         //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as Double[]       //## issuekeys: MSG_TYPE_MISMATCH
    var x218 = b as Boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]    //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]    //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testFloatArray(b : Float[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x212 = b as Byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as Short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as Integer[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as Float[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x216 = b as Long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as Double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x218 = b as Boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]     //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testLongArray(b : Long[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x212 = b as Byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as Short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as Integer[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as Float[]        //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as Long[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x217 = b as Double[]       //## issuekeys: MSG_TYPE_MISMATCH
    var x218 = b as Boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]     //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]    //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testDoubleArray(b : Double[]) {
    var x111 = b as char[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as int[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as double[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as Character[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x212 = b as Byte[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as Short[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as Integer[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as Float[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as Long[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as Double[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x218 = b as Boolean[]      //## issuekeys: MSG_TYPE_MISMATCH

    var x311 = b as Object[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x312 = b as String[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x313 = b as DateTime[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x314 = b as BigDecimal[]     //## issuekeys: MSG_TYPE_MISMATCH
    var x315 = b as BigInteger[]      //## issuekeys: MSG_TYPE_MISMATCH
    var x316 = b as Object  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x317 = b as Object[][]      //## issuekeys: MSG_TYPE_MISMATCH
  }
}