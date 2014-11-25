package gw.specContrib.initializers.arrayInitializers

uses java.lang.Integer
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_IntegerArrayInitializer {

  var intArray111 : int[] = {'c', 'd','e'}
  var intArray112 : int[] = {1b, 2b, 3b}
  var intArray113 : int[] = {1s,2s,3s}
  var intArray114 : int[] = {1,2,3}
  var intArray115 : int[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'INT[]'
  var intArray116 : int[] = {100L,2L,3L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'INT[]'
  var intArray117 : int[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'INT[]'
  var intArray118 : int[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'INT[]'
  var intArray119 : int[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'INT[]'
  var intArray120 : int[] = {12, 'c'}
  var intArray121 : int[] = {12, 1b, 1s}
  var intArray122 : int[] = {12, 45.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'INT[]'
  var intArray123 : int[] = {12, 45L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'INT[]'
  var intArray124 : int[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'INT[]'
  var intArray125 : int[] = {12, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'INT[]'
  var intArray126 : int[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'INT[]'
  var intArray127 : int[] = {12, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'INT[]'
  var intArray128 : int[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'INT[]'

  var intArray211 : Integer[] = {'c', 'd','e'}
  var intArray212 : Integer[] = {1b, 2b, 3b}
  var intArray213 : Integer[] = {1s,2s,3s}
  var intArray214 : Integer[] = {1,2,3}
  var intArray215 : Integer[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'INT[]'
  var intArray216 : Integer[] = {100L,2L,3L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'INT[]'
  var intArray217 : Integer[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'INT[]'
  var intArray218 : Integer[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'INT[]'
  var intArray219 : Integer[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'INT[]'
  var intArray220 : Integer[] = {12, 'c'}
  var intArray221 : Integer[] = {12, 1b, 1s}
  var intArray222 : Integer[] = {12, 45.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'INT[]'
  var intArray223 : Integer[] = {12, 45L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'INT[]'
  var intArray224 : Integer[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'INT[]'
  var intArray225 : Integer[] = {12, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'INT[]'
  var intArray226 : Integer[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'INT[]'
  var intArray227 : Integer[] = {12, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'INT[]'
  var intArray228 : Integer[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'INT[]'

}