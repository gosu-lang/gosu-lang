package gw.specContrib.initializers.arrayInitializers

uses java.lang.Short
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_BigDecimalArrayInitializer {


  var bigDecimalArray111 : BigDecimal[] = {'c', 'd', 'e'}
  var bigDecimalArray112 : BigDecimal[] = {1b, 2b, 3b}
  var bigDecimalArray113 : BigDecimal[] = {1s,2s,3s}
  var bigDecimalArray114 : BigDecimal[] = {1,2,3}
  var bigDecimalArray115 : BigDecimal[] = {1000.5f, 42.5f,2.5f}
  var bigDecimalArray116 : BigDecimal[] = {100L,2L,3L}
  var bigDecimalArray117 : BigDecimal[] = {42.5,2.5,3.6}
  var bigDecimalArray118 : BigDecimal[] = {BigDecimal.ONE, BigDecimal.TEN}
  var bigDecimalArray119 : BigDecimal[] = {BigInteger.ONE}
  //IDE-1284
  var bigDecimalArray120 : BigDecimal[] = {BigDecimal.ONE, 1b, 'c'}
  var bigDecimalArray121 : BigDecimal[] = {BigDecimal.ONE, 1b, 1s}
  var bigDecimalArray122 : BigDecimal[] = {BigDecimal.ONE, 42}
  var bigDecimalArray123 : BigDecimal[] = {BigDecimal.ONE, 45.5f, 43.5}
  var bigDecimalArray124 : BigDecimal[] = {BigDecimal.ONE, 100L, 42.5f}
  var bigDecimalArray125 : BigDecimal[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.MATH.BIGDECIMAL[]'
  var bigDecimalArray126 : BigDecimal[] = {BigDecimal.ONE, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.MATH.BIGDECIMAL[]'
  var bigDecimalArray127 : BigDecimal[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.MATH.BIGDECIMAL[]'
  var bigDecimalArray128 : BigDecimal[] = {BigDecimal.ONE, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.MATH.BIGDECIMAL[]'
  var bigDecimalArray129 : BigDecimal[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}

}