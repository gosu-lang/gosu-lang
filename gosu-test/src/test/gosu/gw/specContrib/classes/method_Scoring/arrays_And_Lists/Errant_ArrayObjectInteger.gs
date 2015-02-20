package gw.specContrib.classes.method_Scoring.arrays_And_Lists

uses java.lang.Integer
uses java.util.ArrayList

class Errant_ArrayObjectInteger {

  class AAA {
  }

  class BBB {
  }

  var integerArray: Integer[] = {1,2,3,4}
  var integerList: List<Integer> = {1,2,3,4}
  var integerArrayList: ArrayList<Integer> = {1,2,3,4}


  class ObjectArrayObjectList {
    function funAA(o: Object[]): AAA {
      return null
    }
    function funAA(o: Integer[]): BBB {
      return null
    }

    function caller() {
      var aa1: AAA = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'OBJECTARRAYOBJECTLIST.FUNAA(OBJECT[])' AND 'OBJECTARRAYOBJECTLIST.FUNAA(INTEGER[])' MATCH
      var aa3: AAA = funAA(integerArray)      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ARRAYARRAYINITIALIZER_OBJECTINTEGER.BBB', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ARRAYARRAYINITIALIZER_OBJECTINTEGER.AAA'
      var aa4: AAA = funAA(integerList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.LIST<JAVA.LANG.INTEGER>)'
      var aa5: AAA = funAA(integerArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)'

      //IDE-1496
      var bb1: BBB = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'OBJECTARRAYOBJECTLIST.FUNAA(OBJECT[])' AND 'OBJECTARRAYOBJECTLIST.FUNAA(INTEGER[])' MATCH
      var bb3: BBB = funAA(integerArray)
      var bb4: BBB = funAA(integerList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.LIST<JAVA.LANG.INTEGER>)'
      var bb5: BBB = funAA(integerArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)'


      var dd1 = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'OBJECTARRAYOBJECTLIST.FUNAA(OBJECT[])' AND 'OBJECTARRAYOBJECTLIST.FUNAA(INTEGER[])' MATCH
      var dd3 = funAA(integerArray)
      var dd4 = funAA(integerList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.LIST<JAVA.LANG.INTEGER>)'
      var dd5 = funAA(integerArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)'
    }
  }
}