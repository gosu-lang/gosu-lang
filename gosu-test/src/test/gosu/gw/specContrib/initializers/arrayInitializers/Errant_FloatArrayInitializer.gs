package gw.specContrib.initializers.arrayInitializers

uses java.lang.Float
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_FloatArrayInitializer {

  var floatArray111 : float[] = {'c', 'd', 'e'}
  var floatArray112 : float[] = {1b, 2b, 3b}
  var floatArray113 : float[] = {1s,2s,3s}
  var floatArray114 : float[] = {1,2,3}
  var floatArray115 : float[] = {1000.5f, 42.5f,2.5f}
  var floatArray116 : float[] = {100L,2L,3L}
  //TODO IDE-1284  and affected by IDE-494 too
  //var floatArray117 : float[] = {42.5,2.5,3.6}
  var floatArray118 : float[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'FLOAT[]'
  var floatArray119 : float[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'FLOAT[]'
  var floatArray120 : float[] = {42.5f, 1b, 'c'}
  var floatArray121 : float[] = {42.5f, 1b, 1s}
  var floatArray122 : float[] = {42.5f, 42}
  var floatArray123 : float[] = {42.5f, 45L}
  var floatArray124 : float[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'FLOAT[]'
  var floatArray125 : float[] = {42.5f, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'FLOAT[]'
  var floatArray126 : float[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'FLOAT[]'
  var floatArray127 : float[] = {42.5f, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'FLOAT[]'
  //TODO IDE-494. Parser shows error, OS Gosu does not. Waiting for IDE-494 resolution
  //var floatArray128 : float[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}

  var floatArray211 : Float[] = {'c', 'd', 'e'}
  var floatArray212 : Float[] = {1b, 2b, 3b}
  var floatArray213 : Float[] = {1s,2s,3s}
  var floatArray214 : Float[] = {1,2,3}
  var floatArray215 : Float[] = {1000.5f, 42.5f,2.5f}
  var floatArray216 : Float[] = {100L,2L,3L}
  //TODO IDE-1284  and affected by IDE-494 too
  //var floatArray217 : Float[] = {42.5,2.5,3.6}
  var floatArray218 : Float[] = {BigDecimal.ONE, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'FLOAT[]'
  var floatArray219 : Float[] = {BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'FLOAT[]'
  var floatArray220 : Float[] = {42.5f, 1b, 'c'}
  var floatArray221 : Float[] = {42.5f, 1b, 1s}
  var floatArray222 : Float[] = {42.5f, 42}
  var floatArray223 : Float[] = {42.5f, 45L}
  var floatArray224 : Float[] = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'FLOAT[]'
  var floatArray225 : Float[] = {42.5f, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'FLOAT[]'
  var floatArray226 : Float[] = {new Date(), new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'FLOAT[]'
  var floatArray227 : Float[] = {42.5f, new Date()}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'FLOAT[]'
  //TODO IDE-494. Parser shows error, OS Gosu does not. Waiting for IDE-494 resolution
  //var floatArray228 : Float[] = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}

}