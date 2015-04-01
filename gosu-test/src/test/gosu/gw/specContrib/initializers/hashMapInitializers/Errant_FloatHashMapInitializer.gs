package gw.specContrib.initializers.hashMapInitializers

uses java.lang.Float
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_FloatHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var floatHashMap1111 : HashMap<Float, Float> = {42.5f->'c'}
  var floatHashMap1110 : HashMap<Float, Float> = {42.5f->1b}
  var floatHashMap1112 : HashMap<Float, Float> = {42.5f->1s}
  var floatHashMap1113 : HashMap<Float, Float> = {42.5f->42}
  var floatHashMap1114 : HashMap<Float, Float> = {42.5f->42.5f}
  //IDE-494 Fixed
  var floatHashMap1115 : HashMap<Float, Float> = {42.5f->42.5}
  var floatHashMap1116 : HashMap<Float, Float> = {42.5f->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1117 : HashMap<Float, Float> = {42.5f->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1118 : HashMap<Float, Float> = {42.5f->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1119 : HashMap<Float, Float> = {42.5f->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1120 : HashMap<Float, Float> = {42.5f->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.FLOATHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1121 : HashMap<Float, Float> = {42.5f->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1122 : HashMap<Float, Float> = {42.5f->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1123 : HashMap<Float, Float> = {42.5f->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'

  var floatHashMap1211 : HashMap<Float, Float> = {'c'-> 42.5f}
  var floatHashMap1210 : HashMap<Float, Float> = {1b-> 42.5f}
  var floatHashMap1212 : HashMap<Float, Float> = {1s-> 42.5f}
  var floatHashMap1213 : HashMap<Float, Float> = {42-> 42.5f}
  var floatHashMap1214 : HashMap<Float, Float> = {42.5f-> 42.5f}
  //IDE-494 Fixed
  var floatHashMap1215 : HashMap<Float, Float> = {42.5-> 42.5f}
  var floatHashMap1216 : HashMap<Float, Float> = {BigInteger.ONE-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1217 : HashMap<Float, Float> = {BigDecimal.TEN-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1218 : HashMap<Float, Float> = {d1-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1219 : HashMap<Float, Float> = {o-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1220 : HashMap<Float, Float> = {aaa-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.FLOATHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1221 : HashMap<Float, Float> = {"mystring"-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1222 : HashMap<Float, Float> = {arrayList-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1223 : HashMap<Float, Float> = {hashMap-> 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'

  var floatHashMap1311 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->'c'}
  var floatHashMap1310 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->1b}
  var floatHashMap1312 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->1s}
  var floatHashMap1313 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->42}
  var floatHashMap1314 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->42.5f}
  //IDE-494 Fixed
  var floatHashMap1315 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->42.5}
  var floatHashMap1316 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1317 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1318 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1319 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1320 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.FLOATHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1321 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1322 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1323 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'

  var floatHashMap1411 : HashMap<Float, Float> = {42.5f->43.5f, 'c'->43.5f}
  var floatHashMap1410 : HashMap<Float, Float> = {42.5f->43.5f, 1b->43.5f}
  var floatHashMap1412 : HashMap<Float, Float> = {42.5f->43.5f, 1s->43.5f}
  var floatHashMap1413 : HashMap<Float, Float> = {42.5f->43.5f, 42->43.5f}
  var floatHashMap1414 : HashMap<Float, Float> = {42.5f->43.5f, 42.5f->43.5f}
  //IDE-494 Fixed
  var floatHashMap1415 : HashMap<Float, Float> = {42.5f->43.5f, 42.5->43.5f}
  var floatHashMap1416 : HashMap<Float, Float> = {42.5f->43.5f, BigInteger.ONE->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1417 : HashMap<Float, Float> = {42.5f->43.5f, BigDecimal.TEN->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1418 : HashMap<Float, Float> = {42.5f->43.5f, d1->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1419 : HashMap<Float, Float> = {42.5f->43.5f, o->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1420 : HashMap<Float, Float> = {42.5f->43.5f, aaa->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.FLOATHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1421 : HashMap<Float, Float> = {42.5f->43.5f, "mystring"->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1422 : HashMap<Float, Float> = {42.5f->43.5f, arrayList->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'
  var floatHashMap1423 : HashMap<Float, Float> = {42.5f->43.5f, hashMap->43.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.FLOAT'

}