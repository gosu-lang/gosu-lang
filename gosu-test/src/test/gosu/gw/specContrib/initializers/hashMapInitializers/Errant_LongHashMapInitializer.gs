package gw.specContrib.initializers.hashMapInitializers

uses java.lang.Long
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_LongHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var longHashMap1111 : HashMap<Long, Long> = {42L->'c'}
  var longHashMap1110 : HashMap<Long, Long> = {42L->1b}
  var longHashMap1112 : HashMap<Long, Long> = {42L->1s}
  var longHashMap1113 : HashMap<Long, Long> = {42L->42}
  var longHashMap1114 : HashMap<Long, Long> = {42L->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1115 : HashMap<Long, Long> = {42L->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1116 : HashMap<Long, Long> = {42L->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1117 : HashMap<Long, Long> = {42L->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1118 : HashMap<Long, Long> = {42L->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1119 : HashMap<Long, Long> = {42L->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1120 : HashMap<Long, Long> = {42L->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.LONGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1121 : HashMap<Long, Long> = {42L->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1122 : HashMap<Long, Long> = {42L->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1123 : HashMap<Long, Long> = {42L->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'

  var longHashMap1211 : HashMap<Long, Long> = {'c'-> 42L}
  var longHashMap1210 : HashMap<Long, Long> = {1b-> 42L}
  var longHashMap1212 : HashMap<Long, Long> = {1s-> 42L}
  var longHashMap1213 : HashMap<Long, Long> = {42-> 42L}
  var longHashMap1214 : HashMap<Long, Long> = {42.5f-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1215 : HashMap<Long, Long> = {42.5-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1216 : HashMap<Long, Long> = {BigInteger.ONE-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1217 : HashMap<Long, Long> = {BigDecimal.TEN-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1218 : HashMap<Long, Long> = {d1-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1219 : HashMap<Long, Long> = {o-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1220 : HashMap<Long, Long> = {aaa-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.LONGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1221 : HashMap<Long, Long> = {"mystring"-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1222 : HashMap<Long, Long> = {arrayList-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1223 : HashMap<Long, Long> = {hashMap-> 42L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'

  var longHashMap1311 : HashMap<Long, Long> = {42L->43L, 42L->'c'}
  var longHashMap1310 : HashMap<Long, Long> = {42L->43L, 42L->1b}
  var longHashMap1312 : HashMap<Long, Long> = {42L->43L, 42L->1s}
  var longHashMap1313 : HashMap<Long, Long> = {42L->43L, 42L->42}
  var longHashMap1314 : HashMap<Long, Long> = {42L->43L, 42L->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1315 : HashMap<Long, Long> = {42L->43L, 42L->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1316 : HashMap<Long, Long> = {42L->43L, 42L->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1317 : HashMap<Long, Long> = {42L->43L, 42L->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1318 : HashMap<Long, Long> = {42L->43L, 42L->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1319 : HashMap<Long, Long> = {42L->43L, 42L->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1320 : HashMap<Long, Long> = {42L->43L, 42L->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.LONGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1321 : HashMap<Long, Long> = {42L->43L, 42L->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1322 : HashMap<Long, Long> = {42L->43L, 42L->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1323 : HashMap<Long, Long> = {42L->43L, 42L->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'

  var longHashMap1411 : HashMap<Long, Long> = {42L->43L, 'c'->43L}
  var longHashMap1410 : HashMap<Long, Long> = {42L->43L, 1b->43L}
  var longHashMap1412 : HashMap<Long, Long> = {42L->43L, 1s->43L}
  var longHashMap1413 : HashMap<Long, Long> = {42L->43L, 42->43L}
  var longHashMap1414 : HashMap<Long, Long> = {42L->43L, 42.5f->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1415 : HashMap<Long, Long> = {42L->43L, 42.5->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1416 : HashMap<Long, Long> = {42L->43L, BigInteger.ONE->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1417 : HashMap<Long, Long> = {42L->43L, BigDecimal.TEN->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1418 : HashMap<Long, Long> = {42L->43L, d1->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1419 : HashMap<Long, Long> = {42L->43L, o->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1420 : HashMap<Long, Long> = {42L->43L, aaa->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.LONGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1421 : HashMap<Long, Long> = {42L->43L, "mystring"->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1422 : HashMap<Long, Long> = {42L->43L, arrayList->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'
  var longHashMap1423 : HashMap<Long, Long> = {42L->43L, hashMap->43L}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.LONG'

}