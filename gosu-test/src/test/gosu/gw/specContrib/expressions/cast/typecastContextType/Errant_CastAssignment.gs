package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastAssignment {

  function blah() {
    var array1 = {1, 2, 3} as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.OBJECT[]'
    var x: byte[] = {0, 1} as byte[]       //## issuekeys: MSG_UNNECESSARY_COERCION
    var x1: int[] = {0, 1} as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BYTE[]'
    var x2: Integer[] = {0, 1} as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BYTE[]'
    var x3: Object[] = {0, 1} as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BYTE[]'
    var x31: Object[] = {0, 1} as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.FLOAT[]'
    var x32: Object[] = {0, 1} as Integer[]  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x33 = {1, 2, 3} as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.INTEGER[]'
    var x4: Integer[] = {0, 1} as Object[]      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT[]', REQUIRED: 'JAVA.LANG.INTEGER[]'


    var x5: ArrayList<Object> = {1, 2, 3} as ArrayList<Float>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>'

    var lll: byte[] = {1b}
    var jjj: int[] = {1}
    var kkk: byte[] = jjj as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BYTE[]'
  }

  var mmm: Object[] = {1, 2, 3} as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.STRING[]'
  var nnn: Object[] = {"sdf", "sdf"} as String[]  //## issuekeys: MSG_UNNECESSARY_COERCION
}
