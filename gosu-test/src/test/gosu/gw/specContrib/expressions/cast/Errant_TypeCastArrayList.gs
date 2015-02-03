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

    var x211 = b as ArrayList
    var x212 = b as ArrayList<String>
    var x213 = b as ArrayList<Array>
    var x214 = b as ArrayList<ArrayList>
    var x215 = b as ArrayList<ArrayList<Integer>>
    var x216 = b as ArrayList<HashMap>
  }
  function testArrayListBoolean(b : ArrayList<Boolean>) {
    var x111 = b as ArrayList<Boolean>
    var x112 = b as ArrayList<Character>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.CHARACTER>'
    var x113 = b as ArrayList<Byte>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BYTE>'
    var x114 = b as ArrayList<Short>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.SHORT>'
    var x115 = b as ArrayList<Integer>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
    var x116 = b as ArrayList<Float>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>'
    var x117 = b as ArrayList<Long>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>'
    var x118 = b as ArrayList<Double>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>'

    var x211 = b as ArrayList
    var x212 = b as ArrayList<String>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>'
    var x213 = b as ArrayList<Array>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<_DUMMY_.__ARRAY__>'
    var x214 = b as ArrayList<ArrayList>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.ARRAYLIST>'
    var x215 = b as ArrayList<ArrayList<Integer>>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>'
    var x216 = b as ArrayList<HashMap>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.HASHMAP>'
  }
  function testArrayListInteger(b : ArrayList<Integer>) {
    var x111 = b as ArrayList<Boolean>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BOOLEAN>'
    var x112 = b as ArrayList<Character>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.CHARACTER>'
    var x113 = b as ArrayList<Byte>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.BYTE>'
    var x114 = b as ArrayList<Short>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.SHORT>'
    var x115 = b as ArrayList<Integer>
    var x116 = b as ArrayList<Float>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>'
    var x117 = b as ArrayList<Long>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.LONG>'
    var x118 = b as ArrayList<Double>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>'

    var x211 = b as ArrayList
    var x212 = b as ArrayList<String>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>'
    var x213 = b as ArrayList<Array>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<_DUMMY_.__ARRAY__>'
    var x214 = b as ArrayList<ArrayList>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.ARRAYLIST>'
    var x215 = b as ArrayList<ArrayList<Integer>>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>'
    var x216 = b as ArrayList<HashMap>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.HASHMAP>'
  }


}