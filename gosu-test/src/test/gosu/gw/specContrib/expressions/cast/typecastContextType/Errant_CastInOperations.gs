package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastInOperations {
  function bar() {
    var list = {1, 2, 3}.addAll({1, 2, 3})

    var array1: Object[] = {1, 2, 3}
    var array2: Object[] = {1, 2, 3}
    var bool1 = array1 == array2
    var bool2 = array1.equals(array2)
    var bool3 = (array1 as Object[]).equals(array2 as Object[]) //## issuekeys: MSG_UNNECESSARY_COERCION
    var bool4 = (array1 as Object[]).equals(array2) //## issuekeys: MSG_UNNECESSARY_COERCION
    var bool5 = (array1).equals(array2 as Object[]) //## issuekeys: MSG_UNNECESSARY_COERCION
    var bool51 = array1.equals({1,2,3})
    var bool6 = array1.equals({1, 2, 3} as Object[])                  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.OBJECT[]'

    var bool7 = ({1, 2, 3} as Object[]).equals({1, 2, 3} as Object[])                  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.OBJECT[]'

    var bool8 = (new Integer[]{1, 2, 3}).equals({1, 2, 3} as Integer[])                //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.LANG.INTEGER[]'

    //IDE-4106
    var bool9 = (new Integer[]{1, 2, 3}) == ({1, 2, 3} as Integer[])  //Roperand in parenthesis //## issuekeys: MSG_UNNECESSARY_COERCION
    var bool10 = (new Integer[]{1, 2, 3}) == {1, 2, 3} as Integer[] //## issuekeys: MSG_UNNECESSARY_COERCION

    var bb = {1, 2, 3}.addAll(new Integer[]{1, 2, 3} as ArrayList<Integer>)      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'


  }
}
