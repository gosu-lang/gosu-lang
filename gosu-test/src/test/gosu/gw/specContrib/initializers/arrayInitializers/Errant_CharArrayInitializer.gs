package gw.specContrib.initializers.arrayInitializers

uses java.lang.Character
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_CharArrayInitializer {
  var charArray111 : char[] = {'c', 'd','e'}
  var charArray112 : char[] = {1b, 2b, 3b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BYTE>', REQUIRED: 'CHAR[]'
  var charArray113 : char[] = {1s,2s,3s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.SHORT>', REQUIRED: 'CHAR[]'
  var charArray114 : char[] = {1,2,3}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'CHAR[]'
  var charArray115 : char[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'CHAR[]'
  var charArray116 : char[] = {100L,2L,3L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'CHAR[]'
  var charArray117 : char[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'CHAR[]'
  var charArray118 : char[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'CHAR[]'
  var charArray119 : char[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'CHAR[]'
  var charArray120 : char[] = {'c', 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'CHAR[]'
  var charArray121 : char[] = {'c', 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'CHAR[]'
  var charArray122 : char[] = {'c', 1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'CHAR[]'
  var charArray123 : char[] = {'c', "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'CHAR[]'
  var charArray124 : char[] = {"mystring", "mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'CHAR[]'
  var charArray125 : char[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'CHAR[]'
  var charArray126 : char[] = {'c', new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'CHAR[]'
  var charArray127 : char[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'CHAR[]'

  var charArray211 : Character[] = {'c', 'd','e'}
  var charArray212 : Character[] = {1b, 2b, 3b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray213 : Character[] = {1s,2s,3s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray214 : Character[] = {1,2,3}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray215 : Character[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray216 : Character[] = {100L,2L,3L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray217 : Character[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray218 : Character[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray219 : Character[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray220 : Character[] = {'c', 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray221 : Character[] = {'c', 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray222 : Character[] = {'c', 1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray223 : Character[] = {'c', "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray224 : Character[] = {"mystring", "mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray225 : Character[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray226 : Character[] = {'c', new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.LANG.CHARACTER[]'
  var charArray227 : Character[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.LANG.CHARACTER[]'

}