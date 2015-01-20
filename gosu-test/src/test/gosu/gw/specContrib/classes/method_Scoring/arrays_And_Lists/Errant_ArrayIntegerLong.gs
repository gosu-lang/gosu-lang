package gw.specContrib.classes.method_Scoring.arrays_And_Lists

uses java.lang.Integer
uses java.lang.Long

class Errant_ArrayIntegerLong {

  class AAA {
  }

  class BBB {
  }

  var intArray: int[] = {1,2,3,4}
  var intObjArray: Integer[] = {1,2,3,4}
  var longArray: long[] = {1L,2L,3L,4L}
  var longObjArray: Long[] = {1L,2L,3L,4L}

  class LongVsInteger {
    function funAA(o: Integer[]): AAA {
      return null
    }
    function funAA(o: Long[]): BBB {
      return null
    }

    function caller() {
      //IDE-1501
      var aa1: AAA = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'LONGVSINTEGER.FUNAA(INTEGER[])' AND 'LONGVSINTEGER.FUNAA(LONG[])' MATCH
      var aa2: AAA = funAA({1L,2L,3L,4L})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.BBB', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.AAA'
      var aa3: AAA = funAA(intArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(INT[])'
      var aa4: AAA = funAA(intObjArray)
      var aa5: AAA = funAA(longArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(LONG[])'
      var aa6: AAA = funAA(longObjArray)      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.BBB', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.AAA'

      var bb1: BBB = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'LONGVSINTEGER.FUNAA(INTEGER[])' AND 'LONGVSINTEGER.FUNAA(LONG[])' MATCH
      var bb2: BBB = funAA({1L,2L,3L,4L})
      var bb3: BBB = funAA(intArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(INT[])'
      var bb4: BBB = funAA(intObjArray)      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.AAA', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.BBB'
      var bb5: BBB = funAA(longArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(LONG[])'
      var bb6: BBB = funAA(longObjArray)

      var cc1 = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'LONGVSINTEGER.FUNAA(INTEGER[])' AND 'LONGVSINTEGER.FUNAA(LONG[])' MATCH
      var cc2 = funAA({1L,2L,3L,4L})
      var cc3 = funAA(intArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(INT[])'
      var cc4 = funAA(intObjArray)
      var cc5 = funAA(longArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(LONG[])'
      var cc6 = funAA(longObjArray)
    }
  }

  class PLongVsPInt {
    function funAA(o: int[]): AAA {
      return null
    }
    function funAA(o: long[]): BBB {
      return null
    }

    function caller() {
      //IDE-1501
      var aa1: AAA = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'PLONGVSPINT.FUNAA(INT[])' AND 'PLONGVSPINT.FUNAA(LONG[])' MATCH
      var aa2: AAA = funAA({1L,2L,3L,4L})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.BBB', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.AAA'
      var aa3: AAA = funAA(intArray)
      var aa4: AAA = funAA(intObjArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.LANG.INTEGER[])'
      var aa5: AAA = funAA(longArray)      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.BBB', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.AAA'
      var aa6: AAA = funAA(longObjArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.LANG.LONG[])'

      var bb1: BBB = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'PLONGVSPINT.FUNAA(INT[])' AND 'PLONGVSPINT.FUNAA(LONG[])' MATCH
      var bb2: BBB = funAA({1L,2L,3L,4L})
      var bb3: BBB = funAA(intArray)      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.AAA', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.METHODSCORINGOVERLOADING.ARRAYS.TOBEPUSHED.ERRANT_ARRAYINTEGERLONG.BBB'
      var bb4: BBB = funAA(intObjArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.LANG.INTEGER[])'
      var bb5: BBB = funAA(longArray)
      var bb6: BBB = funAA(longObjArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.LANG.LONG[])'

      var cc1 = funAA({1,2,3,4})      //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'PLONGVSPINT.FUNAA(INT[])' AND 'PLONGVSPINT.FUNAA(LONG[])' MATCH
      var cc2 = funAA({1L,2L,3L,4L})
      var cc3 = funAA(intArray)
      var cc4 = funAA(intObjArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.LANG.INTEGER[])'
      var cc5 = funAA(longArray)
      var cc6 = funAA(longObjArray)      //## issuekeys: CANNOT RESOLVE METHOD 'FUNAA(JAVA.LANG.LONG[])'
    }
  }
}