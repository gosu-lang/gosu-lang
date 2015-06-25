package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastAssignment_2 {

  function blah() {
    var array2 = {1, 2, 3}.toArray() as Object[] //## issuekeys: MSG_UNNECESSARY_COERCION
  }

  var xx = "someValue".length as Object[]       //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'INT' TO 'JAVA.LANG.OBJECT[]'
  var yy = {"someValue", 1}.toArray() as String[]
  //Add null and casting as String[]
  var aa = {"Country", "ZoneType", null}.toArray() as String[]

  var intValue = 1
  var zzz =  {intValue, 0}.toArray() as Integer[]

  //java.util.map
  var bb = {"exposuresByState", 0} as Map<String, Integer>


}
