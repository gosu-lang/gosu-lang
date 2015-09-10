package gw.specContrib.expressions.cast

uses java.lang.*
uses java.util.ArrayList
uses java.util.HashMap

class Errant_TypeCastHashMap {
  function testHashMap1(b : HashMap) {
    var x111 = b as HashMap<Boolean, Boolean>
    var x112 = b as HashMap<Character, Character>
    var x113 = b as HashMap<Byte, Byte>
    var x114 = b as HashMap<Short, Short>
    var x115 = b as HashMap<Integer, Integer>
    var x116 = b as HashMap<Float, Float>
    var x117 = b as HashMap<Long, Long>
    var x118 = b as HashMap<Double, Double>

    var x211 = b as HashMap  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x212 = b as HashMap<String, String>
    var x213 = b as HashMap<Object[], Object[]>
    var x214 = b as HashMap<ArrayList, ArrayList>
    var x215 = b as HashMap<ArrayList<Integer>, ArrayList<Integer>>
    var x216 = b as HashMap<HashMap, HashMap>
    var x217 = b as HashMap<HashMap<Integer, Integer>, HashMap<Integer, Integer>>
  }
  function testHashMap2(b : HashMap<Boolean, Boolean>) {
    var x111 = b as HashMap<Boolean, Boolean>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x112 = b as HashMap<Character, Character>            //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as HashMap<Byte, Byte>            //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as HashMap<Short, Short>            //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as HashMap<Integer, Integer>            //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as HashMap<Float, Float>            //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as HashMap<Long, Long>            //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as HashMap<Double, Double>            //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as HashMap  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x212 = b as HashMap<String, String>            //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as HashMap<Object[], Object[]>            //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as HashMap<ArrayList, ArrayList>            //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as HashMap<ArrayList<Integer>, ArrayList<Integer>>            //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as HashMap<HashMap, HashMap>            //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as HashMap<HashMap<Integer, Integer>, HashMap<Integer, Integer>>            //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testHashMap3(b : HashMap<Integer, Integer>) {
    var x111 = b as HashMap<Boolean, Boolean>            //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as HashMap<Character, Character>            //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as HashMap<Byte, Byte>            //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as HashMap<Short, Short>            //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as HashMap<Integer, Integer>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x116 = b as HashMap<Float, Float>            //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as HashMap<Long, Long>            //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as HashMap<Double, Double>            //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as HashMap  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x212 = b as HashMap<String, String>            //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as HashMap<Object[], Object[]>            //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as HashMap<ArrayList, ArrayList>            //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as HashMap<ArrayList<Integer>, ArrayList<Integer>>            //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as HashMap<HashMap, HashMap>            //## issuekeys: MSG_TYPE_MISMATCH
    var x217 = b as HashMap<HashMap<Integer, Integer>, HashMap<Integer, Integer>>            //## issuekeys: MSG_TYPE_MISMATCH
  }




}