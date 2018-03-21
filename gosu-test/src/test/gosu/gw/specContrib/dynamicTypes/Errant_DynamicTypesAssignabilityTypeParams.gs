package gw.specContrib.dynamicTypes

/**
 * Created by vdahuja on 11/17/16.
 */
class Errant_DynamicTypesAssignabilityTypeParams {
  //IDE-3770
  function typeParams1(list1 : List<Integer>) {
    var foo11 : List<Dynamic> = list1
    var foo12 : List<Integer> = new ArrayList<Dynamic>()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<DYNAMIC.DYNAMIC>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
  }

  function typeParams1(map2 : Map<Integer, String>) {
    var foo21 : Map<Dynamic, String> = map2
    var foo22 : Map<Dynamic, Dynamic> = map2
    var foo23 : Map<Integer, Dynamic> = map2

    var foo31: Map<Integer, Integer> = new HashMap<Dynamic, Integer>()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<DYNAMIC.DYNAMIC,JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.MAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>'
    var foo32: Map<Integer, Integer> = new HashMap<Dynamic, Dynamic>()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<DYNAMIC.DYNAMIC,DYNAMIC.DYNAMIC>', REQUIRED: 'JAVA.UTIL.MAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>'
    var foo33: Map<Integer, Integer> = new HashMap<Integer, Dynamic>()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,DYNAMIC.DYNAMIC>', REQUIRED: 'JAVA.UTIL.MAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>'
  }
}