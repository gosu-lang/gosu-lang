package gw.specContrib.generics

uses java.lang.*
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.LinkedList
uses java.util.Map

class Errant_GenericAssignment {


  var x111 : ArrayList<String>
  var y111 : ArrayList<CharSequence> = x111
  var x112 : ArrayList<CharSequence>
  var y112 : ArrayList<String> = x112      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.CHARSEQUENCE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>'

  var x113 : ArrayList<Integer>
  var y113 : ArrayList = x113
  var x114 : ArrayList<ArrayList<Integer>>
  //IDE-1274 Parser bug
  var y114 : ArrayList<ArrayList> = x114

  //  Boxed Primitive types
  var x115 : ArrayList<Byte>
  var y115 : ArrayList<Short> = x115      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.SHORT>'

  var x116 : ArrayList<Byte>
  var y116 : ArrayList<java.lang.Number> = x116

  //Collection of Collection
  var x117  : ArrayList<ArrayList<ArrayList<Integer>>>
  var y1171 : ArrayList = x117
  var y1172 : ArrayList<ArrayList> = x117
  var y1173 : ArrayList<ArrayList<ArrayList>> = x117
  var y1174 : ArrayList<ArrayList<ArrayList<Object>>> = x117

  var x118  : ArrayList<HashMap<Integer, Integer>>
  var x1181 : ArrayList = x118
  var x1182 : ArrayList<HashMap> = x118
  var x1183 : ArrayList<HashMap<Object, Object>> = x118
  var x1184 : ArrayList<HashMap<Integer, Object>> = x118
  var x1185 : ArrayList<HashMap<Integer, Integer>> = x118


  //ArrayLists and Lists
  var x119  : List<Integer>
  var y1191 : ArrayList = x119      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.ARRAYLIST'
  var y1192 : ArrayList<Integer> = x119      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var x120  : ArrayList<Integer>
  var y1201 : List = x120
  var y1202 : List<Integer> = x120
  var y1203 : List<String> = x120      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
  var y1204 : List<java.lang.Number> = x120
  var y1205 : List<Double> = x120      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
  var y1206 : LinkedList<Integer> = x120      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LINKEDLIST<JAVA.LANG.INTEGER>'

  //Maps
  var h111 : HashMap<String, String>
  var m111 : HashMap<CharSequence, CharSequence> = h111
  var h112 : HashMap<CharSequence, CharSequence>
  var m112 : HashMap<String, String> = h112      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.CHARSEQUENCE,JAVA.LANG.CHARSEQUENCE>', REQUIRED: 'JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>'

  var h113 : HashMap<Integer, Integer>
  var m113 : HashMap = h113
  var h114 : HashMap<ArrayList<Integer>, Integer>
  var m114 : HashMap<ArrayList, Integer> = h114

  //  Boxed Primitive types
  var h115 : HashMap<Byte, Byte>
  var m115 : HashMap<Short, Short> = h115      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.BYTE,JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.HASHMAP<JAVA.LANG.SHORT,JAVA.LANG.SHORT>'

  var h116 : HashMap<Byte, Byte>
  var m116 : HashMap<java.lang.Number, java.lang.Number> = h116


  //Collection of Collection
  var h117  : HashMap<HashMap<HashMap<Integer, Integer>, Integer>, Integer>
  var m1171 : HashMap = h117
  var m1172 : HashMap<HashMap, Integer> = h117
  var m1173 : HashMap<HashMap<HashMap, Integer>, Integer> = h117
  var m1174 : HashMap<HashMap<HashMap<Object, Object>, Object>, Object> = h117

  var h118  : HashMap<ArrayList<Integer>, Integer>
  var h1181 : HashMap = h118
  var h1182 : HashMap<ArrayList, Integer> = h118
  var h1183 : HashMap<ArrayList<Object>, Integer> = h118
  var h1184 : HashMap<ArrayList<Integer>, Object> = h118
  var h1185 : HashMap<ArrayList<Object>, Object> = h118
  var h1186 : HashMap<ArrayList<Integer>, Integer> = h118

  //ArrayLists and Lists
  var h119  : Map<Integer, Integer>
  var m1191 : HashMap = h119      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.MAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.HASHMAP'
  var m1192 : HashMap<Integer, Integer> = h119      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.MAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>'

  var h120  : HashMap<Integer, Integer>
  var m1201 : Map = h120
  var m1202 : Map<Integer, Integer> = h120
  var m1203 : Map<String, Integer> = h120      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.MAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>'
  var m1204 : Map<java.lang.Number, java.lang.Number> = h120
  var m1205 : Map<Double, Double> = h120      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.MAP<JAVA.LANG.DOUBLE,JAVA.LANG.DOUBLE>'

}
