package gw.specContrib.initializers.hashMapInitializers

uses java.lang.Byte
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_ByteHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var byteHashMap1111 : HashMap<Byte, Byte> = {1b->'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1110 : HashMap<Byte, Byte> = {1b->1b}
  var byteHashMap1112 : HashMap<Byte, Byte> = {1b->1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1113 : HashMap<Byte, Byte> = {1b->42}
  var byteHashMap1114 : HashMap<Byte, Byte> = {1b->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1115 : HashMap<Byte, Byte> = {1b->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1116 : HashMap<Byte, Byte> = {1b->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1117 : HashMap<Byte, Byte> = {1b->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1118 : HashMap<Byte, Byte> = {1b->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1119 : HashMap<Byte, Byte> = {1b->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1120 : HashMap<Byte, Byte> = {1b->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BYTEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1121 : HashMap<Byte, Byte> = {1b->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1122 : HashMap<Byte, Byte> = {1b->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1123 : HashMap<Byte, Byte> = {1b->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'

  var byteHashMap1211 : HashMap<Byte, Byte> = {'c'-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1210 : HashMap<Byte, Byte> = {1b-> 2b}
  var byteHashMap1212 : HashMap<Byte, Byte> = {1s-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1213 : HashMap<Byte, Byte> = {42-> 2b}
  var byteHashMap1214 : HashMap<Byte, Byte> = {42.5f-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1215 : HashMap<Byte, Byte> = {42.5-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1216 : HashMap<Byte, Byte> = {BigInteger.ONE-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1217 : HashMap<Byte, Byte> = {BigDecimal.TEN-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1218 : HashMap<Byte, Byte> = {d1-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1219 : HashMap<Byte, Byte> = {o-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1220 : HashMap<Byte, Byte> = {aaa-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BYTEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1221 : HashMap<Byte, Byte> = {"mystring"-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1222 : HashMap<Byte, Byte> = {arrayList-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1223 : HashMap<Byte, Byte> = {hashMap-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'

  var byteHashMap1311 : HashMap<Byte, Byte> = {1b->2b, 1b->'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1310 : HashMap<Byte, Byte> = {1b->2b, 1b->1b}
  var byteHashMap1312 : HashMap<Byte, Byte> = {1b->2b, 1b->1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1313 : HashMap<Byte, Byte> = {1b->2b, 1b->42}
  var byteHashMap1314 : HashMap<Byte, Byte> = {1b->2b, 1b->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1315 : HashMap<Byte, Byte> = {1b->2b, 1b->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1316 : HashMap<Byte, Byte> = {1b->2b, 1b->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1317 : HashMap<Byte, Byte> = {1b->2b, 1b->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1318 : HashMap<Byte, Byte> = {1b->2b, 1b->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1319 : HashMap<Byte, Byte> = {1b->2b, 1b->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1320 : HashMap<Byte, Byte> = {1b->2b, 1b->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BYTEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1321 : HashMap<Byte, Byte> = {1b->2b, 1b->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1322 : HashMap<Byte, Byte> = {1b->2b, 1b->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1323 : HashMap<Byte, Byte> = {1b->2b, 1b->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1324 : HashMap<Byte, Byte> = {1b->2b, 1b->300}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.BYTE'

  var byteHashMap1411 : HashMap<Byte, Byte> = {1b->2b, 'c'-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1410 : HashMap<Byte, Byte> = {1b->2b, 1b-> 2b}
  var byteHashMap1412 : HashMap<Byte, Byte> = {1b->2b, 1s-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1413 : HashMap<Byte, Byte> = {1b->2b, 42-> 2b}
  var byteHashMap1414 : HashMap<Byte, Byte> = {1b->2b, 42.5f-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1415 : HashMap<Byte, Byte> = {1b->2b, 42.5-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1416 : HashMap<Byte, Byte> = {1b->2b, BigInteger.ONE-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1417 : HashMap<Byte, Byte> = {1b->2b, BigDecimal.TEN-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1418 : HashMap<Byte, Byte> = {1b->2b, d1-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1419 : HashMap<Byte, Byte> = {1b->2b, o-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1420 : HashMap<Byte, Byte> = {1b->2b, aaa-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.BYTEHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1421 : HashMap<Byte, Byte> = {1b->2b, "mystring"-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1422 : HashMap<Byte, Byte> = {1b->2b, arrayList-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'
  var byteHashMap1423 : HashMap<Byte, Byte> = {1b->2b, hashMap-> 2b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.BYTE'

}