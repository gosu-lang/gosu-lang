package gw.specContrib.initializers.hashMapInitializers

uses java.lang.Integer
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_IntegerHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var integerHashMap1111 : HashMap<Integer, Integer> = {42->'c'}
  var integerHashMap1110 : HashMap<Integer, Integer> = {42->1b}
  var integerHashMap1112 : HashMap<Integer, Integer> = {42->1s}
  var integerHashMap1113 : HashMap<Integer, Integer> = {42->42}
  var integerHashMap1114 : HashMap<Integer, Integer> = {42->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1115 : HashMap<Integer, Integer> = {42->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1116 : HashMap<Integer, Integer> = {42->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1117 : HashMap<Integer, Integer> = {42->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1118 : HashMap<Integer, Integer> = {42->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1119 : HashMap<Integer, Integer> = {42->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1120 : HashMap<Integer, Integer> = {42->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.INTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1121 : HashMap<Integer, Integer> = {42->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1122 : HashMap<Integer, Integer> = {42->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1123 : HashMap<Integer, Integer> = {42->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'

  var integerHashMap1211 : HashMap<Integer, Integer> = {'c'-> 42}
  var integerHashMap1210 : HashMap<Integer, Integer> = {1b-> 42}
  var integerHashMap1212 : HashMap<Integer, Integer> = {1s-> 42}
  var integerHashMap1213 : HashMap<Integer, Integer> = {42-> 42}
  var integerHashMap1214 : HashMap<Integer, Integer> = {42.5f-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1215 : HashMap<Integer, Integer> = {42.5-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1216 : HashMap<Integer, Integer> = {BigInteger.ONE-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1217 : HashMap<Integer, Integer> = {BigDecimal.TEN-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1218 : HashMap<Integer, Integer> = {d1-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1219 : HashMap<Integer, Integer> = {o-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1220 : HashMap<Integer, Integer> = {aaa-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.INTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1221 : HashMap<Integer, Integer> = {"mystring"-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1222 : HashMap<Integer, Integer> = {arrayList-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1223 : HashMap<Integer, Integer> = {hashMap-> 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'

  var integerHashMap1311 : HashMap<Integer, Integer> = {42->43, 42->'c'}
  var integerHashMap1310 : HashMap<Integer, Integer> = {42->43, 42->1b}
  var integerHashMap1312 : HashMap<Integer, Integer> = {42->43, 42->1s}
  var integerHashMap1313 : HashMap<Integer, Integer> = {42->43, 42->42}
  var integerHashMap1314 : HashMap<Integer, Integer> = {42->43, 42->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1315 : HashMap<Integer, Integer> = {42->43, 42->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1316 : HashMap<Integer, Integer> = {42->43, 42->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1317 : HashMap<Integer, Integer> = {42->43, 42->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1318 : HashMap<Integer, Integer> = {42->43, 42->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1319 : HashMap<Integer, Integer> = {42->43, 42->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1320 : HashMap<Integer, Integer> = {42->43, 42->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.INTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1321 : HashMap<Integer, Integer> = {42->43, 42->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1322 : HashMap<Integer, Integer> = {42->43, 42->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1323 : HashMap<Integer, Integer> = {42->43, 42->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'

  var integerHashMap1411 : HashMap<Integer, Integer> = {42->43, 'c'->43}
  var integerHashMap1410 : HashMap<Integer, Integer> = {42->43, 1b->43}
  var integerHashMap1412 : HashMap<Integer, Integer> = {42->43, 1s->43}
  var integerHashMap1413 : HashMap<Integer, Integer> = {42->43, 42->43}
  var integerHashMap1414 : HashMap<Integer, Integer> = {42->43, 42.5f->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1415 : HashMap<Integer, Integer> = {42->43, 42.5->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1416 : HashMap<Integer, Integer> = {42->43, BigInteger.ONE->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1417 : HashMap<Integer, Integer> = {42->43, BigDecimal.TEN->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1418 : HashMap<Integer, Integer> = {42->43, d1->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1419 : HashMap<Integer, Integer> = {42->43, o->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1420 : HashMap<Integer, Integer> = {42->43, aaa->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.INTEGERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1421 : HashMap<Integer, Integer> = {42->43, "mystring"->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1422 : HashMap<Integer, Integer> = {42->43, arrayList->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'
  var integerHashMap1423 : HashMap<Integer, Integer> = {42->43, hashMap->43}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.INTEGER'

}