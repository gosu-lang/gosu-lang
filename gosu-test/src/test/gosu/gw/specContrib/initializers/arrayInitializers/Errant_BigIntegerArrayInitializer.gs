package gw.specContrib.initializers.arrayInitializers

uses java.lang.Short
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_BigIntegerArrayInitializer {
  var bigIntegerArray111 : BigInteger[] = {'c', 'd', 'e'}
  var bigIntegerArray112 : BigInteger[] = {1b, 2b, 3b}
  var bigIntegerArray113 : BigInteger[] = {1s,2s,3s}
  var bigIntegerArray114 : BigInteger[] = {1,2,3}
  var bigIntegerArray115 : BigInteger[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray116 : BigInteger[] = {100L,2L,3L}
  var bigIntegerArray117 : BigInteger[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray118 : BigInteger[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray119 : BigInteger[] = {BigInteger.ONE, BigInteger.TEN}
  //IDE-1284
  var bigIntegerArray120 : BigInteger[] = {BigInteger.ONE, 1b, 'c'}
  var bigIntegerArray121 : BigInteger[] = {BigInteger.ONE, 1b, 1s}
  var bigIntegerArray122 : BigInteger[] = {BigInteger.ONE, 42}
  var bigIntegerArray123 : BigInteger[] = {BigInteger.ONE, 45.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.NUMBER & JAVA.LANG.COMPARABLE<JAVA.LANG.NUMBER & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray124 : BigInteger[] = {BigInteger.ONE, 100L}
  var bigIntegerArray125 : BigInteger[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray126 : BigInteger[] = {BigInteger.ONE, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray127 : BigInteger[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray128 : BigInteger[] = {BigInteger.ONE, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'
  var bigIntegerArray129 : BigInteger[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.MATH.BIGINTEGER[]'

}