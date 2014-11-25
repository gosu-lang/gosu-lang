package gw.specContrib.initializers.hashMapInitializers

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_BigDecimalHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var bigDecimalHashMap1111 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->'c'}
  var bigDecimalHashMap1110 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->1b}
  var bigDecimalHashMap1112 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->1s}
  var bigDecimalHashMap1113 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->42}
  var bigDecimalHashMap1114 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->42.5f}
  var bigDecimalHashMap1115 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->42.5}
  var bigDecimalHashMap1116 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->BigInteger.ONE}
  var bigDecimalHashMap1117 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->BigDecimal.TEN}
  var bigDecimalHashMap1118 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1119 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1120 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGDECIMALHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1121 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1122 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1123 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'

  var bigDecimalHashMap1211 : HashMap<BigDecimal, BigDecimal> = {'c'-> BigDecimal.TEN}
  var bigDecimalHashMap1210 : HashMap<BigDecimal, BigDecimal> = {1b-> BigDecimal.TEN}
  var bigDecimalHashMap1212 : HashMap<BigDecimal, BigDecimal> = {1s-> BigDecimal.TEN}
  var bigDecimalHashMap1213 : HashMap<BigDecimal, BigDecimal> = {42-> BigDecimal.TEN}
  var bigDecimalHashMap1214 : HashMap<BigDecimal, BigDecimal> = {42.5f-> BigDecimal.TEN}
  var bigDecimalHashMap1215 : HashMap<BigDecimal, BigDecimal> = {42.5-> BigDecimal.TEN}
  var bigDecimalHashMap1216 : HashMap<BigDecimal, BigDecimal> = {BigInteger.ONE-> BigDecimal.TEN}
  var bigDecimalHashMap1217 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.TEN-> BigDecimal.TEN}
  var bigDecimalHashMap1218 : HashMap<BigDecimal, BigDecimal> = {d1-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1219 : HashMap<BigDecimal, BigDecimal> = {o-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1220 : HashMap<BigDecimal, BigDecimal> = {aaa-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGDECIMALHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1221 : HashMap<BigDecimal, BigDecimal> = {"mystring"-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1222 : HashMap<BigDecimal, BigDecimal> = {arrayList-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1223 : HashMap<BigDecimal, BigDecimal> = {hashMap-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'

  var bigDecimalHashMap1311 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->'c'}
  var bigDecimalHashMap1310 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->1b}
  var bigDecimalHashMap1312 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->1s}
  var bigDecimalHashMap1313 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->42}
  var bigDecimalHashMap1314 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->42.5f}
  var bigDecimalHashMap1315 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->42.5}
  var bigDecimalHashMap1316 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->BigInteger.ONE}
  var bigDecimalHashMap1317 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->BigDecimal.TEN}
  var bigDecimalHashMap1318 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1319 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1320 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGDECIMALHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1321 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1322 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1323 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.ONE->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'

  var bigDecimalHashMap1411 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, 'c'-> BigDecimal.TEN}
  var bigDecimalHashMap1410 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, 1b-> BigDecimal.TEN}
  var bigDecimalHashMap1412 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, 1s-> BigDecimal.TEN}
  var bigDecimalHashMap1413 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, 42-> BigDecimal.TEN}
  var bigDecimalHashMap1414 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, 42.5f-> BigDecimal.TEN}
  var bigDecimalHashMap1415 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, 42.5-> BigDecimal.TEN}
  var bigDecimalHashMap1416 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigInteger.ONE-> BigDecimal.TEN}
  var bigDecimalHashMap1417 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, BigDecimal.TEN-> BigDecimal.TEN}
  var bigDecimalHashMap1418 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, d1-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1419 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, o-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1420 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, aaa-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BIGDECIMALHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1421 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, "mystring"-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1422 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, arrayList-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimalHashMap1423 : HashMap<BigDecimal, BigDecimal> = {BigDecimal.ONE->BigDecimal.TEN, hashMap-> BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.MATH.BIGDECIMAL'

}