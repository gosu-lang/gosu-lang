package gw.specContrib.initializers.hashMapInitializers

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_BigIntegerHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var bigIntegerHashMap1111 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->'c'}
  var bigIntegerHashMap1110 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->1b}
  var bigIntegerHashMap1112 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->1s}
  var bigIntegerHashMap1113 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->42}
  var bigIntegerHashMap1114 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1115 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1116 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->BigInteger.ONE}
  var bigIntegerHashMap1117 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1118 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1119 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1120 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGINTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1121 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1122 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1123 : HashMap<BigInteger, BigInteger> = {BigInteger.TEN->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'

  var bigIntegerHashMap1211 : HashMap<BigInteger, BigInteger> = {'c'-> BigInteger.TEN}
  var bigIntegerHashMap1210 : HashMap<BigInteger, BigInteger> = {1b-> BigInteger.TEN}
  var bigIntegerHashMap1212 : HashMap<BigInteger, BigInteger> = {1s-> BigInteger.TEN}
  var bigIntegerHashMap1213 : HashMap<BigInteger, BigInteger> = {42-> BigInteger.TEN}
  var bigIntegerHashMap1214 : HashMap<BigInteger, BigInteger> = {42.5f-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1215 : HashMap<BigInteger, BigInteger> = {42.5-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1216 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE-> BigInteger.TEN}
  var bigIntegerHashMap1217 : HashMap<BigInteger, BigInteger> = {BigDecimal.TEN-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1218 : HashMap<BigInteger, BigInteger> = {d1-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1219 : HashMap<BigInteger, BigInteger> = {o-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1220 : HashMap<BigInteger, BigInteger> = {aaa-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGINTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1221 : HashMap<BigInteger, BigInteger> = {"mystring"-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1222 : HashMap<BigInteger, BigInteger> = {arrayList-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1223 : HashMap<BigInteger, BigInteger> = {hashMap-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'

  var bigIntegerHashMap1311 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->'c'}
  var bigIntegerHashMap1310 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->1b}
  var bigIntegerHashMap1312 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->1s}
  var bigIntegerHashMap1313 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->42}
  var bigIntegerHashMap1314 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1315 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1316 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->BigInteger.ONE}
  var bigIntegerHashMap1317 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1318 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1319 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1320 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGINTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1321 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1322 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1323 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'

  var bigIntegerHashMap1411 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, 'c'-> BigInteger.TEN}
  var bigIntegerHashMap1410 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, 1b-> BigInteger.TEN}
  var bigIntegerHashMap1412 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, 1s-> BigInteger.TEN}
  var bigIntegerHashMap1413 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, 42-> BigInteger.TEN}
  var bigIntegerHashMap1414 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, 42.5f-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1415 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, 42.5-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1416 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigInteger.ONE-> BigInteger.TEN}
  var bigIntegerHashMap1417 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, BigDecimal.TEN-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1418 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, d1-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1419 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, o-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1420 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, aaa-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGINTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1421 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, "mystring"-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1422 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, arrayList-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigIntegerHashMap1423 : HashMap<BigInteger, BigInteger> = {BigInteger.ONE->BigInteger.TEN, hashMap-> BigInteger.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGINTEGER'

}