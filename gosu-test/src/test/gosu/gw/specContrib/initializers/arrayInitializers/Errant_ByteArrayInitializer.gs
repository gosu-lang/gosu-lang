package gw.specContrib.initializers.arrayInitializers

uses java.lang.Byte
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_ByteArrayInitializer {
  var byteArray111 : byte[] = {'c', 'd', 'e'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.CHARACTER>', REQUIRED: 'BYTE[]'
  var byteArray112 : byte[] = {1b, 2b, 3b}
  var byteArray113 : byte[] = {1s,2s,3s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.SHORT>', REQUIRED: 'BYTE[]'
  //IDE-1284
  var byteArray114 : byte[] = {1,2,3}
  var byteArray115 : byte[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'BYTE[]'
  var byteArray116 : byte[] = {100L,2L,3L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'BYTE[]'
  var byteArray117 : byte[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'BYTE[]'
  var byteArray118 : byte[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'BYTE[]'
  var byteArray119 : byte[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'BYTE[]'
  var byteArray120 : byte[] = {1b, 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'BYTE[]'
  var byteArray121 : byte[] = {1b, 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'BYTE[]'
  //IDE-1284
  var byteArray122 : byte[] = {1b, 42}
  var byteArray123 : byte[] = {1b, 45l}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'BYTE[]'
  var byteArray124 : byte[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'BYTE[]'
  var byteArray125 : byte[] = {1b, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'BYTE[]'
  var byteArray126 : byte[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'BYTE[]'
  var byteArray127 : byte[] = {1b, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'BYTE[]'
  var byteArray128 : byte[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'BYTE[]'


  var byteArray211 : Byte[] = {'c', 'd', 'e'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.CHARACTER>', REQUIRED: 'BYTE[]'
  var byteArray212 : Byte[] = {1b, 2b, 3b}
  var byteArray213 : Byte[] = {1s,2s,3s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.SHORT>', REQUIRED: 'BYTE[]'
  //IDE-1284
  var byteArray214 : Byte[] = {1,2,3}
  var byteArray215 : Byte[] = {1000.5f, 42.5f,2.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>', REQUIRED: 'BYTE[]'
  var byteArray216 : Byte[] = {100L,2L,3L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'BYTE[]'
  var byteArray217 : Byte[] = {42.5,2.5,3.6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'BYTE[]'
  var byteArray218 : Byte[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'BYTE[]'
  var byteArray219 : Byte[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'BYTE[]'
  var byteArray220 : Byte[] = {1b, 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'BYTE[]'
  var byteArray221 : Byte[] = {1b, 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'BYTE[]'
  //IDE-1284
  var byteArray222 : Byte[] = {1b, 42}
  var byteArray223 : Byte[] = {1b, 45L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>', REQUIRED: 'BYTE[]'
  var byteArray224 : Byte[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'BYTE[]'
  var byteArray225 : Byte[] = {1b, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'BYTE[]'
  var byteArray226 : Byte[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'BYTE[]'
  var byteArray227 : Byte[] = {1b, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'BYTE[]'
  var byteArray228 : Byte[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>', REQUIRED: 'BYTE[]'

}