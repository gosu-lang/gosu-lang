package gw.specContrib.classes.method_Scoring.arrays_And_Lists

uses java.util.ArrayList

class Errant_ArrayObjectString {

  class AAA {
  }

  class BBB {
  }

  var strArray: String[] = {"g", "o", "s", "u"}
  var strList: List<String> = {"g", "o", "s", "u"}
  var strArrayList: ArrayList<String> = {"g", "o", "s", "u"}


  class ObjectArrayObjectList {
    function funAA(o: Object[]): AAA {
      return null
    }

    function funAA(o: String[]): BBB {
      return null
    }

    function caller() {
      var aa1: AAA = funAA({"g", "o", "s", "u"})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'OBJECTARRAYOBJECTLIST.FUNAA(OBJECT[])' AND 'OBJECTARRAYOBJECTLIST.FUNAA(STRING[])' MATCH
      var aa2: AAA = funAA(strArray)      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYSVSCOLLECTIONINITIALIZERS.TOBEPUSHED.ARRAYARRAYINITIALIZER_OBJECTSTRING.BBB', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYSVSCOLLECTIONINITIALIZERS.TOBEPUSHED.ARRAYARRAYINITIALIZER_OBJECTSTRING.AAA'
      var aa3: AAA = funAA(strList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.LIST<JAVA.LANG.STRING>)'
      var aa4: AAA = funAA(strArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'

      //IDE-1496
      var bb1: BBB = funAA({"g", "o", "s", "u"})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'OBJECTARRAYOBJECTLIST.FUNAA(OBJECT[])' AND 'OBJECTARRAYOBJECTLIST.FUNAA(STRING[])' MATCH
      var bb2: BBB = funAA(strArray)
      var bb3: BBB = funAA(strList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.LIST<JAVA.LANG.STRING>)'
      var bb4: BBB = funAA(strArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'

      var cc1 = funAA({"g", "o", "s", "u"})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'OBJECTARRAYOBJECTLIST.FUNAA(OBJECT[])' AND 'OBJECTARRAYOBJECTLIST.FUNAA(STRING[])' MATCH
      var cc2 = funAA(strArray)
      var cc3 = funAA(strList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.LIST<JAVA.LANG.STRING>)'
      var cc4 = funAA(strArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
    }
  }
}