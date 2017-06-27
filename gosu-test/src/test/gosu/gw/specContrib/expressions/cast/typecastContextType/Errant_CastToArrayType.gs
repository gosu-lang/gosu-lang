package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastToArrayType {
  function foo() {
    var ao: Object[] = {1, 2, 3} as Integer[]  //## issuekeys: MSG_UNNECCESARY_COERCION

    var labels1: Object[] = {"1", "2", "3"} as String[]  //## issuekeys: MSG_UNNECCESARY_COERCION
    var labels2: String[] = {"1", "2", "3"} as String[]  //## issuekeys: MSG_UNNECCESARY_COERCION
    var labels3: Integer[] = {"1", "2", "3"} as String[]               //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>' TO 'JAVA.LANG.STRING[]'
    var labels4: String[] = {"1", "2", "3"} as Integer[]                //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.STRING[]' TO 'JAVA.LANG.INTEGER[]'

    var expectedContacts = {} as List<String>

    var labels11: Integer[] = {1, 2, 3} as Integer[]  //## issuekeys: MSG_UNNECCESARY_COERCION
    var labels12: Object[] = {1, 2, 3} as Integer[]  //## issuekeys: MSG_UNNECCESARY_COERCION
    var labels13: Object[] = {1, 2, 3} as String[]                   //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.STRING[]'
    var labels14: Float[] = {1, 2, 3} as Float[]  //## issuekeys: MSG_UNNECCESARY_COERCION

    var labels15 = {1, 2, 3} as float[]                   //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'FLOAT[]'
    var labels16: float[] = {1, 2, 3} as float[]  //## issuekeys: MSG_UNNECCESARY_COERCION

    var labels17: int[] = {1.5f, 1.6f} as int[]                    //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.FLOAT>' TO 'INT[]'
    var labels18: Object[] = {1.5f, 1.6f} as float[]                 //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'FLOAT[]'
    var labels19: float[] = {1.5f, 1.6f} as Object[]                 //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'FLOAT[]' TO 'JAVA.LANG.OBJECT[]'
    var labels20: Float[] = {1.5f, 1.6f} as Object[]                  //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT[]', REQUIRED: 'JAVA.LANG.FLOAT[]'
    var labels21: Float[] = {1.5f, 1.6f} as float[]                    //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'FLOAT[]'


    var labels22 = new ArrayList(){1, 2, 3} as Integer[]                  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST' TO 'JAVA.LANG.INTEGER[]'
    var labels23: Integer[] = new ArrayList(){1, 2, 3} as Integer[]                  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST' TO 'JAVA.LANG.INTEGER[]'

  }
}
