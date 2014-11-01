package gw.specContrib.initializers.hashMapInitializers

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_StringHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var stringHashMap1111 : HashMap<String, String> = {"mystring"->'c'}
  var stringHashMap1110 : HashMap<String, String> = {"mystring"->1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1112 : HashMap<String, String> = {"mystring"->1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1113 : HashMap<String, String> = {"mystring"->42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1114 : HashMap<String, String> = {"mystring"->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1115 : HashMap<String, String> = {"mystring"->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1116 : HashMap<String, String> = {"mystring"->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1117 : HashMap<String, String> = {"mystring"->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1118 : HashMap<String, String> = {"mystring"->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1119 : HashMap<String, String> = {"mystring"->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1120 : HashMap<String, String> = {"mystring"->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.STRINGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1121 : HashMap<String, String> = {"mystring"->"mystring"}
  var stringHashMap1122 : HashMap<String, String> = {"mystring"->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1123 : HashMap<String, String> = {"mystring"->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1124 : HashMap<String, String> = {"mystring"->'ccc'}

  var stringHashMap1211 : HashMap<String, String> = {'c'-> "mystring"}
  var stringHashMap1210 : HashMap<String, String> = {1b-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1212 : HashMap<String, String> = {1s-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1213 : HashMap<String, String> = {42-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1214 : HashMap<String, String> = {42.5f-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1215 : HashMap<String, String> = {42.5-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1216 : HashMap<String, String> = {BigInteger.ONE-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1217 : HashMap<String, String> = {BigDecimal.TEN-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1218 : HashMap<String, String> = {d1-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1219 : HashMap<String, String> = {o-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1220 : HashMap<String, String> = {aaa-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.STRINGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1221 : HashMap<String, String> = {"mystring"-> "mystring"}
  var stringHashMap1222 : HashMap<String, String> = {arrayList-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1223 : HashMap<String, String> = {hashMap-> "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1224 : HashMap<String, String> = {'ccc'-> "mystring"}

  var stringHashMap1311 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->'c'}
  var stringHashMap1310 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1312 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1313 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1314 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1315 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1316 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1317 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1318 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1319 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1320 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.STRINGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1321 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->"mystring"}
  var stringHashMap1322 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1323 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1324 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring1"->'ccc'}

  var stringHashMap1411 : HashMap<String, String> = {"mystring1"->"mystring2", 'c'->"mystring2"}
  var stringHashMap1410 : HashMap<String, String> = {"mystring1"->"mystring2", 1b->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1412 : HashMap<String, String> = {"mystring1"->"mystring2", 1s->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1413 : HashMap<String, String> = {"mystring1"->"mystring2", 42->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1414 : HashMap<String, String> = {"mystring1"->"mystring2", 42.5f->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1415 : HashMap<String, String> = {"mystring1"->"mystring2", 42.5->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1416 : HashMap<String, String> = {"mystring1"->"mystring2", BigInteger.ONE->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1417 : HashMap<String, String> = {"mystring1"->"mystring2", BigDecimal.TEN->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1418 : HashMap<String, String> = {"mystring1"->"mystring2", d1->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1419 : HashMap<String, String> = {"mystring1"->"mystring2", o->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1420 : HashMap<String, String> = {"mystring1"->"mystring2", aaa->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.STRINGHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1421 : HashMap<String, String> = {"mystring1"->"mystring2", "mystring"->"mystring2"}
  var stringHashMap1422 : HashMap<String, String> = {"mystring1"->"mystring2", arrayList->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1423 : HashMap<String, String> = {"mystring1"->"mystring2", hashMap->"mystring2"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
  var stringHashMap1424 : HashMap<String, String> = {"mystring1"->"mystring2", 'ccc'->"mystring2"}

}