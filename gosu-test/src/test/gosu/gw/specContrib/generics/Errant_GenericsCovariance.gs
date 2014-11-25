package gw.specContrib.generics

uses java.util.ArrayList

class Errant_GenericsCovariance {

  function caller() {
    var a1: ArrayList<java.lang.Integer>
    var a2: ArrayList<java.lang.Double>
    var a3: ArrayList<java.lang.Number>
    a2 = a1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>'
    a2 = a3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.DOUBLE>'
    a3 = a2      // this is OK
  }

}