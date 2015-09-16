package gw.specContrib.expressions.cast

uses java.lang.*
uses java.lang.Character
uses java.util.ArrayList
uses java.util.HashMap

class Errant_TypeCastArrayList {
  function testArrayList(b : ArrayList) {
    var x111 = b as ArrayList<Boolean>
    var x112 = b as ArrayList<Character>
    var x113 = b as ArrayList<Byte>
    var x114 = b as ArrayList<Short>
    var x115 = b as ArrayList<Integer>
    var x116 = b as ArrayList<Float>
    var x117 = b as ArrayList<Long>
    var x118 = b as ArrayList<Double>

    var x211 = b as ArrayList  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x212 = b as ArrayList<String>
    var x213 = b as ArrayList<Object[]>
    var x214 = b as ArrayList<ArrayList>
    var x215 = b as ArrayList<ArrayList<Integer>>
    var x216 = b as ArrayList<HashMap>
  }
  function testArrayListBoolean(b : ArrayList<Boolean>) {
    var x111 = b as ArrayList<Boolean>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x112 = b as ArrayList<Character>      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as ArrayList<Byte>      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as ArrayList<Short>      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as ArrayList<Integer>      //## issuekeys: MSG_TYPE_MISMATCH
    var x116 = b as ArrayList<Float>      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as ArrayList<Long>      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as ArrayList<Double>      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as ArrayList  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x212 = b as ArrayList<String>      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as ArrayList<Object[]>      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as ArrayList<ArrayList>      //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as ArrayList<ArrayList<Integer>>      //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as ArrayList<HashMap>      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testArrayListInteger(b : ArrayList<Integer>) {
    var x111 = b as ArrayList<Boolean>      //## issuekeys: MSG_TYPE_MISMATCH
    var x112 = b as ArrayList<Character>      //## issuekeys: MSG_TYPE_MISMATCH
    var x113 = b as ArrayList<Byte>      //## issuekeys: MSG_TYPE_MISMATCH
    var x114 = b as ArrayList<Short>      //## issuekeys: MSG_TYPE_MISMATCH
    var x115 = b as ArrayList<Integer>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x116 = b as ArrayList<Float>      //## issuekeys: MSG_TYPE_MISMATCH
    var x117 = b as ArrayList<Long>      //## issuekeys: MSG_TYPE_MISMATCH
    var x118 = b as ArrayList<Double>      //## issuekeys: MSG_TYPE_MISMATCH

    var x211 = b as ArrayList  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x212 = b as ArrayList<String>      //## issuekeys: MSG_TYPE_MISMATCH
    var x213 = b as ArrayList<Object[]>      //## issuekeys: MSG_TYPE_MISMATCH
    var x214 = b as ArrayList<ArrayList>      //## issuekeys: MSG_TYPE_MISMATCH
    var x215 = b as ArrayList<ArrayList<Integer>>      //## issuekeys: MSG_TYPE_MISMATCH
    var x216 = b as ArrayList<HashMap>      //## issuekeys: MSG_TYPE_MISMATCH
  }


}