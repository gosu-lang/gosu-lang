package gw.specContrib.classes.method_Scoring

uses java.lang.Double
uses java.lang.Integer
uses java.lang.Long
uses java.util.ArrayList

uses gw.specContrib.Type1
uses gw.specContrib.Type2

class Errant_CollectionMethodScoring {

  function fun2(i: int, l: ArrayList<Integer>): Type1 { return null }
  function fun2(d: double, l: ArrayList<Double>): Type2 { return null }

  function fun3(i: int, l: ArrayList<Integer>): Type1 { return null }
  function fun3(i: long, l: ArrayList<Object>): Type2 { return null }

  function fun4(i: int, l: ArrayList<Integer>): Type1 { return null }
  function fun4(i: long, l: ArrayList<Long>): Type2 { return null }

  function caller() {
    var r8: Type2 = fun3(1, new ArrayList())
    var r9: Type1 = fun3(1, {1, 2, 3})
    var r10: Type2 = fun4(1, new ArrayList<Long>())
    var r11 = fun2(1.5, new ArrayList<Integer>())        //## issuekeys: 'FUNC(DOUBLE, JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>)' IN 'GW.SPECCONTRIB.INTDOUBLEARRAYLIST' CANNOT BE APPLIED TO '(DOUBLE, JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)'
  }

}
