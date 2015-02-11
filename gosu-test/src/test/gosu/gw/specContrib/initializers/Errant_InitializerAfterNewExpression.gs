package gw.specContrib.initializers

uses java.util.List
uses java.util.ArrayList
uses java.util.HashMap

class Errant_InitializerAfterNewExpression {
  class A {}

  function test(o1: Object, o2: Object) {
    var list0 = new List() { o1, o2 }
    var list2 = new ArrayList() { o1, o2 }
    var list3 = new ArrayList<String>() { o1 }  //## issuekeys: INCOMPATIBLE TYPES
    var list4: List = new() { o1, o2 }
    var list5: ArrayList = new() { o1, o2 }
    var list6: List<String> = new() { o1, o2 }  //## issuekeys: INCOMPATIBLE TYPES
    var list7 = new A() { o1, o2 }              //## issuekeys: UNEXPECTED COLLECTION INIITIALIZER
    var list8: A = new() { o1, o2 }             //## issuekeys: UNEXPECTED COLLECTION INIITIALIZER
    var list9: ArrayList = new() { o1 -> o2 }   //## issuekeys: UNEXPECTED MAP INIITIALIZER
    var list10: ArrayList = new() { { o1 -> o2 } }

    var map0 = new HashMap() { o1 -> o2 }
    var map1 = new HashMap<String, String>() { o1 -> o2 }     //## issuekeys: INCOMPATIBLE TYPES
    var map2: HashMap<Object, Object> = new() { o1 -> o2 }
    var map3: HashMap = new() { o1, o2 }       //## issuekeys: MAP INIITIALIZER EXPECTED
    var map6: HashMap = new() { { o1 -> o2} -> { o1 -> o2 } }
    // IDE-1754
    var map4 = new A() { o1 -> o2 }            //## issuekeys: UNEXPECTED MAP INIITIALIZER
    var map5: A = new() { o1 -> o2 }           //## issuekeys: UNEXPECTED MAP INIITIALIZER

    var arr0 = new Object[] { o1, o2 }
    var arr1 = new int[] { o1, o2 }            //## issuekeys: INCOMPATIBLE TYPES
  }
}