package gw.specContrib.initializers.arrayInitializers

uses java.lang.Long
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_LongArrayInitializer {

  var longArray111 : long[] = {'c', 'd', 'e'}
  var longArray112 : long[] = {1b, 2b, 3b}
  var longArray113 : long[] = {1s,2s,3s}
  var longArray114 : long[] = {1,2,3}
  var longArray115 : long[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'LONG[]'
  var longArray116 : long[] = {100L,2L,3L}
  var longArray117 : long[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'LONG[]'
  var longArray118 : long[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'LONG[]'
  var longArray119 : long[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'LONG[]'
  var longArray120 : long[] = {100L, 1b}
  var longArray121 : long[] = {100L, 1b, 1s}
  var longArray122 : long[] = {100L, 42}
  var longArray123 : long[] = {100L, 45.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'LONG[]'
  var longArray124 : long[] = {100L, 45.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'LONG[]'
  var longArray125 : long[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'LONG[]'
  var longArray126 : long[] = {100L, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'LONG[]'
  var longArray127 : long[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'LONG[]'
  var longArray128 : long[] = {100L, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'LONG[]'
  var longArray129 : long[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'LONG[]'

  var longArray211 : Long[] = {'c', 'd', 'e'}
  var longArray212 : Long[] = {1b, 2b, 3b}
  var longArray213 : Long[] = {1s,2s,3s}
  var longArray214 : Long[] = {1,2,3}
  var longArray215 : Long[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray216 : Long[] = {100L,2L,3L}
  var longArray217 : Long[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray218 : Long[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray219 : Long[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray220 : Long[] = {100L, 1b}
  var longArray221 : Long[] = {100L, 1b, 1s}
  var longArray222 : Long[] = {100L, 42}
  var longArray223 : Long[] = {100L, 45.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray224 : Long[] = {100L, 45.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray225 : Long[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray226 : Long[] = {100L, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray227 : Long[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray228 : Long[] = {100L, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.LANG.LONG[]'
  var longArray229 : Long[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.LANG.LONG[]'
}