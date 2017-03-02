package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_CharInterval {

  class A {
  }

  class B {
  }

  var aaa: A
  var bbb: B
  var d1: java.util.Date
  var d2: java.util.Date
  var o: Object

  //Positive cases
  var char1011 = 'c'..'k'
  var char1012 = 'c'..|'k'
  var char1013 = 'c'|..'k'
  var char1014 = 'c'|..|'k'
  //IDE-1196 - Parser does not show error here - GOOD. OS Gosu issue - does show an error here. IDE-1196
  var char1015 = 'c'..-'k'
  var char1016 = -'c'..'k'
  var char1017 = -'c'..-'k'
  var char1018 = 'c'..+'k'
  var char1019 = +'c'..+'k'

  //Lower end point is char. upper limit various types
  var char1111 = ('c'..'c')
  var char1112 = ('c'..1b)          //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'BYTE'
  var char1113 = ('c'..1 as short)          //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'SHORT'
  var char1114 = ('c'..10)
  var char1115 = ('c'..10L)
  var char1116 = ('c'..10.5f)
  var char1117 = ('c'..10.5)
  var char1118 = ('c'..BigInteger.TEN)
  var char1119 = ('c'..BigDecimal.TEN)
  var char1120 = ('c'.."mystring")
  var char1121 = ('c'..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var char1122 = ('c'..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_CHAR.A'
  var char1123 = ('c'..false)                  //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'

  //Lower end point is char with step function
  var char1211 = ('c'..'c').step(1)
  var char1212 = ('c'..1b).step(1)         //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'BYTE'
  //IDE-1282. Also IDE-1296. Should show error for IDE-1296 and not for IDE-1282
  var char1213 = ('c'..1 as short).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'SHORT'
  var char1214 = ('c'..10).step(1)
  var char1215 = ('c'..10L).step(1)
  var char1216 = ('c'..10.5f).step(1)
  var char1217 = ('c'..10.5).step(1)
  var char1218 = ('c'..BigInteger.TEN).step(1)
  var char1219 = ('c'..BigDecimal.TEN).step(1)
  var char1220 = ('c'.."mystring").step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
  var char1221 = ('c'..(new Date())).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var char1222 = ('c'..aaa).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_CHAR.A'


  //Both end points are 'char' but step function has parameter of different type
  var char1311 = ('a'..'c').step('c')
  var char1312 = ('a'..'c').step(1b)
  var char1313 = ('a'..'c').step(1 as short)
  var char1314 = ('a'..'c').step(42)
  var char1315 = ('a'..'c').step(42.5f)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(FLOAT)'
  var char1316 = ('a'..'c').step(42L)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(LONG)'
  var char1317 = ('a'..'c').step(42.5)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(DOUBLE)'
  var char1318 = ('a'..'c').step(BigInteger.ONE)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGINTEGER)'
  var char1319 = ('a'..'c').step(BigDecimal.TEN)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGDECIMAL)'
  var char1320 = ('a'..'c').step("mystring")      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var char1321 = ('a'..'c').step(d1)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var char1322 = ('a'..'c').step(aaa)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_CHAR.A)'

  //for loop
  function testForLoop() {
    for (i in ('c'..'k')) {
    }
    for (i in ('c'..1b)) {         //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'BYTE'
    }
    for (i in ('c'..1 as short)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'SHORT'
    }
    for (i in ('c'..10)) {
    }
    for (i in ('c'..10L)) {
    }
    for (i in ('c'..10.5f)) {
    }
    for (i in ('c'..10.5)) {
    }
    for (i in ('c'..BigInteger.TEN)) {
    }
    for (i in ('c'..BigDecimal.TEN)) {
    }
    for (i in ('c'.."mystring")) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
    }
    for (i in ('c'..(new Date()))) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
    }
    for (i in ('c'..aaa)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_CHAR.A'
    }
  }

}