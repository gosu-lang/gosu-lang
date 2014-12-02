package gw.specContrib.initializers.hashMapInitializers

uses java.lang.Short
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_ShortHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var shortHashMap1111 : HashMap<Short, Short> = {1s->'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1110 : HashMap<Short, Short> = {1s->1b}
  var shortHashMap1112 : HashMap<Short, Short> = {1s->1s}
  var shortHashMap1113 : HashMap<Short, Short> = {1s->42}
  var shortHashMap1114 : HashMap<Short, Short> = {1s->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1115 : HashMap<Short, Short> = {1s->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1116 : HashMap<Short, Short> = {1s->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1117 : HashMap<Short, Short> = {1s->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1118 : HashMap<Short, Short> = {1s->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1119 : HashMap<Short, Short> = {1s->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1120 : HashMap<Short, Short> = {1s->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.SHORTHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1121 : HashMap<Short, Short> = {1s->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1122 : HashMap<Short, Short> = {1s->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1123 : HashMap<Short, Short> = {1s->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'

  var shortHashMap1211 : HashMap<Short, Short> = {'c'-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1210 : HashMap<Short, Short> = {1b-> 2s}
  var shortHashMap1212 : HashMap<Short, Short> = {1s-> 2s}
  var shortHashMap1213 : HashMap<Short, Short> = {42-> 2s}
  var shortHashMap1214 : HashMap<Short, Short> = {42.5f-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1215 : HashMap<Short, Short> = {42.5-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1216 : HashMap<Short, Short> = {BigInteger.ONE-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1217 : HashMap<Short, Short> = {BigDecimal.TEN-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1218 : HashMap<Short, Short> = {d1-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1219 : HashMap<Short, Short> = {o-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1220 : HashMap<Short, Short> = {aaa-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.SHORTHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1221 : HashMap<Short, Short> = {"mystring"-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1222 : HashMap<Short, Short> = {arrayList-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1223 : HashMap<Short, Short> = {hashMap-> 2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'

  var shortHashMap1311 : HashMap<Short, Short> = {1s->2s, 1s->'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1310 : HashMap<Short, Short> = {1s->2s, 1s->1b}
  var shortHashMap1312 : HashMap<Short, Short> = {1s->2s, 1s->1s}
  var shortHashMap1313 : HashMap<Short, Short> = {1s->2s, 1s->42}
  var shortHashMap1314 : HashMap<Short, Short> = {1s->2s, 1s->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1315 : HashMap<Short, Short> = {1s->2s, 1s->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1316 : HashMap<Short, Short> = {1s->2s, 1s->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1317 : HashMap<Short, Short> = {1s->2s, 1s->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1318 : HashMap<Short, Short> = {1s->2s, 1s->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1319 : HashMap<Short, Short> = {1s->2s, 1s->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1320 : HashMap<Short, Short> = {1s->2s, 1s->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.SHORTHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1321 : HashMap<Short, Short> = {1s->2s, 1s->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1322 : HashMap<Short, Short> = {1s->2s, 1s->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1323 : HashMap<Short, Short> = {1s->2s, 1s->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'

  var shortHashMap1411 : HashMap<Short, Short> = {1s->2s, 'c'->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1410 : HashMap<Short, Short> = {1s->2s, 1b->2s}
  var shortHashMap1412 : HashMap<Short, Short> = {1s->2s, 1s->2s}
  var shortHashMap1413 : HashMap<Short, Short> = {1s->2s, 42->2s}
  var shortHashMap1414 : HashMap<Short, Short> = {1s->2s, 42.5f->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1415 : HashMap<Short, Short> = {1s->2s, 42.5->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1416 : HashMap<Short, Short> = {1s->2s, BigInteger.ONE->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1417 : HashMap<Short, Short> = {1s->2s, BigDecimal.TEN->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1418 : HashMap<Short, Short> = {1s->2s, d1->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1419 : HashMap<Short, Short> = {1s->2s, o->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1420 : HashMap<Short, Short> = {1s->2s, aaa->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.SHORTHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1421 : HashMap<Short, Short> = {1s->2s, "mystring"->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1422 : HashMap<Short, Short> = {1s->2s, arrayList->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'
  var shortHashMap1423 : HashMap<Short, Short> = {1s->2s, hashMap->2s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.SHORT'

}