package gw.specContrib.initializers.hashMapInitializers

uses java.lang.Character
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_CharacterHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var charHashMap1111 : HashMap<Character, Character> = {'c'->'c'}
  var charHashMap1110 : HashMap<Character, Character> = {'c'->1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1112 : HashMap<Character, Character> = {'c'->1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1113 : HashMap<Character, Character> = {'c'->42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1114 : HashMap<Character, Character> = {'c'->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1115 : HashMap<Character, Character> = {'c'->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1116 : HashMap<Character, Character> = {'c'->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1117 : HashMap<Character, Character> = {'c'->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1118 : HashMap<Character, Character> = {'c'->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1119 : HashMap<Character, Character> = {'c'->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1120 : HashMap<Character, Character> = {'c'->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.CHARACTERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1121 : HashMap<Character, Character> = {'c'->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1122 : HashMap<Character, Character> = {'c'->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1123 : HashMap<Character, Character> = {'c'->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'

  var charHashMap1211 : HashMap<Character, Character> = {'c'-> 'c'}
  var charHashMap1210 : HashMap<Character, Character> = {1b-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1212 : HashMap<Character, Character> = {1s-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1213 : HashMap<Character, Character> = {42-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1214 : HashMap<Character, Character> = {42.5f-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1215 : HashMap<Character, Character> = {42.5-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1216 : HashMap<Character, Character> = {BigInteger.ONE-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1217 : HashMap<Character, Character> = {BigDecimal.TEN-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1218 : HashMap<Character, Character> = {d1-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1219 : HashMap<Character, Character> = {o-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1220 : HashMap<Character, Character> = {aaa-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.CHARACTERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1221 : HashMap<Character, Character> = {"mystring"-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1222 : HashMap<Character, Character> = {arrayList-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1223 : HashMap<Character, Character> = {hashMap-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'

  var charHashMap1311 : HashMap<Character, Character> = {'c'->'k', 'c'->'c'}
  var charHashMap1310 : HashMap<Character, Character> = {'c'->'k', 'c'->1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1312 : HashMap<Character, Character> = {'c'->'k', 'c'->1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1313 : HashMap<Character, Character> = {'c'->'k', 'c'->42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1314 : HashMap<Character, Character> = {'c'->'k', 'c'->42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1315 : HashMap<Character, Character> = {'c'->'k', 'c'->42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1316 : HashMap<Character, Character> = {'c'->'k', 'c'->BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1317 : HashMap<Character, Character> = {'c'->'k', 'c'->BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1318 : HashMap<Character, Character> = {'c'->'k', 'c'->d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1319 : HashMap<Character, Character> = {'c'->'k', 'c'->o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1320 : HashMap<Character, Character> = {'c'->'k', 'c'->aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.CHARACTERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1321 : HashMap<Character, Character> = {'c'->'k', 'c'->"mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1322 : HashMap<Character, Character> = {'c'->'k', 'c'->arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1323 : HashMap<Character, Character> = {'c'->'k', 'c'->hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'

  var charHashMap1411 : HashMap<Character, Character> = {'c'->'k', 'c'-> 'c'}
  var charHashMap1410 : HashMap<Character, Character> = {'c'->'k', 1b-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1412 : HashMap<Character, Character> = {'c'->'k', 1s-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1413 : HashMap<Character, Character> = {'c'->'k', 42-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1414 : HashMap<Character, Character> = {'c'->'k', 42.5f-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1415 : HashMap<Character, Character> = {'c'->'k', 42.5-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1416 : HashMap<Character, Character> = {'c'->'k', BigInteger.ONE-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1417 : HashMap<Character, Character> = {'c'->'k', BigDecimal.TEN-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1418 : HashMap<Character, Character> = {'c'->'k', d1-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1419 : HashMap<Character, Character> = {'c'->'k', o-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1420 : HashMap<Character, Character> = {'c'->'k', aaa-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.HASHMAPINITIALIZER.CHARACTERHASHMAPINITIALIZER.A', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1421 : HashMap<Character, Character> = {'c'->'k', "mystring"-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1422 : HashMap<Character, Character> = {'c'->'k', arrayList-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'
  var charHashMap1423 : HashMap<Character, Character> = {'c'->'k', hashMap-> 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.CHARACTER'

}