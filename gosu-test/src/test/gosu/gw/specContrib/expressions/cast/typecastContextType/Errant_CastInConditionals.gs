package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastInConditionals {
  function fooEmpty(param1: int) {

    var Strings1: List<String> = param1 > 2 ? new ArrayList<String>() : {}
    var Strings2: List<Object> = param1 > 2 ? new ArrayList<String>() : {}
    var Strings3: List<String> = param1 > 2 ? new ArrayList<Object>() : {}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'

  }

  function fooEmpty2(param1: int) {

    var Strings1: List<Float> = param1 > 2 ? new ArrayList<Integer>() : {}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.NUMBER & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'

    var Strings2: List<Float> = param1 > 2 ? new ArrayList<Float>() : {1, 2, 3}
    var Strings3: List<Float> = param1 > 2 ? new ArrayList<Integer>() : {1, 2, 3}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.NUMBER & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'

    var Strings4: Float[] = param1 > 2 ? new Float[]{12, 3} : {1, 2, 3} as Float[] //## issuekeys: MSG_UNNECESSARY_COERCION

  }

  function castTest(param1: int) {
    var Strings1: Object[] = param1 > 2 ? new ArrayList<String>() : {}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.IO.SERIALIZABLE & JAVA.LANG.CLONEABLE', REQUIRED: 'JAVA.LANG.OBJECT[]'
    var Strings2: Object[] = param1 > 2 ? {1, 2, 3} : {4, 5, 6}
    var Strings3: Object[] = param1 > 2 ? {1, 2, 3} : new Integer[]{4, 5, 6}
    var Strings4: Float[] = param1 > 2 ? {1, 2, 3} : new Integer[]{4, 5, 6}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.NUMBER & JAVA.LANG.COMPARABLE<JAVA.LANG.NUMBER & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>[]', REQUIRED: 'JAVA.LANG.FLOAT[]'
  }
}
