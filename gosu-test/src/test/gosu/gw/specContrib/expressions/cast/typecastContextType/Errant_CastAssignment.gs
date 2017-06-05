package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastAssignment {

  function blah() {
    var array1 = {1, 2, 3} as Object[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.OBJECT[]'
    var array2 = {1, 2, 3}.toArray() as Object[]
    var x: byte[] = {0, 1} as byte[]
    var x1: int[] = {0, 1} as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BYTE[]'
    var x2: Integer[] = {0, 1} as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BYTE[]'
    var x3: Object[] = {0, 1} as Byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.BYTE[]'
    var x31: Object[] = {0, 1} as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.FLOAT[]'
    var x32: Object[] = {0, 1} as Integer[]
    var x33 = {1, 2, 3} as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.INTEGER[]'
    var x4: Integer[] = {0, 1} as Object[]      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT[]', REQUIRED: 'JAVA.LANG.INTEGER[]'


    var x5: ArrayList<Object> = {1, 2, 3} as ArrayList<Float>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>'

    var lll: byte[] = {1b}
    var jjj: int[] = {1}
    var kkk: byte[] = jjj as byte[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT[]' TO 'BYTE[]'
  }

  var mmm: Object[] = {1, 2, 3} as String[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.STRING[]'
  var nnn: Object[] = {"sdf", "sdf"} as String[]   //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>' TO 'JAVA.LANG.STRING[]'
  var xx = "someValue".length as Object[]       //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT' TO 'JAVA.LANG.OBJECT[]'
  var yy = {"someValue", 1}.toArray() as String[]
  //Add null and casting as String[]
  var aa = {"Country", "ZoneType", null}.toArray() as String[]

  var intValue = 1
  var zzz =  {intValue, 0}.toArray() as java.lang.Integer[]

  //java.util.map
  var bb = {"exposuresByState", 0} as java.util.Map<String, Integer>


}