package gw.specContrib.initializers.hashMapInitializers

uses java.lang.Double
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_DoubleHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var doubleHashMap1111 : HashMap<Double, Double> = {42.5->'c'}
  var doubleHashMap1110 : HashMap<Double, Double> = {42.5->1b}
  var doubleHashMap1112 : HashMap<Double, Double> = {42.5->1s}
  var doubleHashMap1113 : HashMap<Double, Double> = {42.5->42}
  var doubleHashMap1114 : HashMap<Double, Double> = {42.5->42.5f}
  var doubleHashMap1115 : HashMap<Double, Double> = {42.5->42.5}
  var doubleHashMap1116 : HashMap<Double, Double> = {42.5->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1117 : HashMap<Double, Double> = {42.5->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1118 : HashMap<Double, Double> = {42.5->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1119 : HashMap<Double, Double> = {42.5->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1120 : HashMap<Double, Double> = {42.5->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.DOUBLEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1121 : HashMap<Double, Double> = {42.5->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1122 : HashMap<Double, Double> = {42.5->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1123 : HashMap<Double, Double> = {42.5->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'

  var doubleHashMap1211 : HashMap<Double, Double> = {'c'-> 42.5}
  var doubleHashMap1210 : HashMap<Double, Double> = {1b-> 42.5}
  var doubleHashMap1212 : HashMap<Double, Double> = {1s-> 42.5}
  var doubleHashMap1213 : HashMap<Double, Double> = {42-> 42.5}
  var doubleHashMap1214 : HashMap<Double, Double> = {42.5f-> 42.5}
  var doubleHashMap1215 : HashMap<Double, Double> = {42.5-> 42.5}
  var doubleHashMap1216 : HashMap<Double, Double> = {BigInteger.ONE-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1217 : HashMap<Double, Double> = {BigDecimal.TEN-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1218 : HashMap<Double, Double> = {d1-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1219 : HashMap<Double, Double> = {o-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1220 : HashMap<Double, Double> = {aaa-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.DOUBLEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1221 : HashMap<Double, Double> = {"mystring"-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1222 : HashMap<Double, Double> = {arrayList-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1223 : HashMap<Double, Double> = {hashMap-> 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'

  var doubleHashMap1311 : HashMap<Double, Double> = {42.5->43.5, 42.5->'c'}
  var doubleHashMap1310 : HashMap<Double, Double> = {42.5->43.5, 42.5->1b}
  var doubleHashMap1312 : HashMap<Double, Double> = {42.5->43.5, 42.5->1s}
  var doubleHashMap1313 : HashMap<Double, Double> = {42.5->43.5, 42.5->42}
  var doubleHashMap1314 : HashMap<Double, Double> = {42.5->43.5, 42.5->42.5f}
  var doubleHashMap1315 : HashMap<Double, Double> = {42.5->43.5, 42.5->42.5}
  var doubleHashMap1316 : HashMap<Double, Double> = {42.5->43.5, 42.5->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1317 : HashMap<Double, Double> = {42.5->43.5, 42.5->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1318 : HashMap<Double, Double> = {42.5->43.5, 42.5->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1319 : HashMap<Double, Double> = {42.5->43.5, 42.5->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1320 : HashMap<Double, Double> = {42.5->43.5, 42.5->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.DOUBLEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1321 : HashMap<Double, Double> = {42.5->43.5, 42.5->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1322 : HashMap<Double, Double> = {42.5->43.5, 42.5->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1323 : HashMap<Double, Double> = {42.5->43.5, 42.5->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'

  var doubleHashMap1411 : HashMap<Double, Double> = {42.5->43.5, 'c'->43.5}
  var doubleHashMap1410 : HashMap<Double, Double> = {42.5->43.5, 1b->43.5}
  var doubleHashMap1412 : HashMap<Double, Double> = {42.5->43.5, 1s->43.5}
  var doubleHashMap1413 : HashMap<Double, Double> = {42.5->43.5, 42->43.5}
  var doubleHashMap1414 : HashMap<Double, Double> = {42.5->43.5, 42.5f->43.5}
  var doubleHashMap1415 : HashMap<Double, Double> = {42.5->43.5, 42.5->43.5}
  var doubleHashMap1416 : HashMap<Double, Double> = {42.5->43.5, BigInteger.ONE->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1417 : HashMap<Double, Double> = {42.5->43.5, BigDecimal.TEN->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1418 : HashMap<Double, Double> = {42.5->43.5, d1->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1419 : HashMap<Double, Double> = {42.5->43.5, o->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1420 : HashMap<Double, Double> = {42.5->43.5, aaa->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.DOUBLEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1421 : HashMap<Double, Double> = {42.5->43.5, "mystring"->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1422 : HashMap<Double, Double> = {42.5->43.5, arrayList->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'
  var doubleHashMap1423 : HashMap<Double, Double> = {42.5->43.5, hashMap->43.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.DOUBLE'

}