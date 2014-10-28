package gw.specContrib.initializers.arrayInitializers

uses java.lang.Double
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_DoubleArrayInitializer {

  var doubleArray111 : double[] = {'c', 'd', 'e'}
  var doubleArray112 : double[] = {1b, 2b, 3b}
  var doubleArray113 : double[] = {1s,2s,3s}
  var doubleArray114 : double[] = {1,2,3}
  var doubleArray115 : double[] = {1000.5f, 42.5f,2.5f}
  var doubleArray116 : double[] = {100L,2L,3L}
  var doubleArray117 : double[] = {42.5,2.5,3.6}
  var doubleArray118 : double[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'DOUBLE[]'
  var doubleArray119 : double[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'DOUBLE[]'
  var doubleArray120 : double[] = {100.5, 1b, 'c'}
  var doubleArray121 : double[] = {100.5, 1b, 1s}
  var doubleArray122 : double[] = {100.5, 42}
  var doubleArray123 : double[] = {100.5, 45.5f}
  var doubleArray124 : double[] = {100.5, 100L}
  var doubleArray125 : double[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'DOUBLE[]'
  var doubleArray126 : double[] = {100.5, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'DOUBLE[]'
  var doubleArray127 : double[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'DOUBLE[]'
  var doubleArray128 : double[] = {100.5, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'DOUBLE[]'
  var doubleArray129 : double[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}

  var doubleArray211 : Double[] = {'c', 'd', 'e'}
  var doubleArray212 : Double[] = {1b, 2b, 3b}
  var doubleArray213 : Double[] = {1s,2s,3s}
  var doubleArray214 : Double[] = {1,2,3}
  var doubleArray215 : Double[] = {1000.5f, 42.5f,2.5f}
  var doubleArray216 : Double[] = {100L,2L,3L}
  var doubleArray217 : Double[] = {42.5,2.5,3.6}
  var doubleArray218 : Double[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'DOUBLE[]'
  var doubleArray219 : Double[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'DOUBLE[]'
  var doubleArray220 : Double[] = {100.5, 1b, 'c'}
  var doubleArray221 : Double[] = {100.5, 1b, 1s}
  var doubleArray222 : Double[] = {100.5, 42}
  var doubleArray223 : Double[] = {100.5, 45.5f}
  var doubleArray224 : Double[] = {100.5, 100L}
  var doubleArray225 : Double[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'DOUBLE[]'
  var doubleArray226 : Double[] = {100.5, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'DOUBLE[]'
  var doubleArray227 : Double[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'DOUBLE[]'
  var doubleArray228 : Double[] = {100.5, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'DOUBLE[]'
  var doubleArray229 : Double[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}
}