package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_DoubleInterval {

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
  var double1011 = 42.5..43.5
  var double1012 = 42.5..|43.5
  var double1013 = 42.5|..43.5
  var double1014 = 42.5|..|43.5
  var double1015 = 42.5..-43.5
  var double1016 = -42.5..43.5
  var double1017 = -42.5..-43.5
  var double1018 = 42.5..+43.5
  var double1019 = +42.5..+43.5

  //Lower end point is double. upper limit various types
  var double1111 = (42.5..'c')
  var double1112 = (42.5..1b)
  var double1113 = (42.5..1 as short)
  var double1114 = (42.5..10)
  var double1115 = (42.5..10L)
  var double1116 = (42.5..10.5f)
  var double1117 = (42.5..10.5)
  var double1118 = (42.5..BigInteger.TEN)   //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'BIGINTEGER'
  var double1119 = (42.5..BigDecimal.TEN)
  var double1120 = (42.5.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var double1121 = (42.5..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var double1122 = (42.5..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_DOUBLE.A'
  var double1123 = (42.5..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'

  //Lower end point is double with step function
  var double1211 = (42.5..'c').step(1)
  var double1212 = (42.5..1b).step(1)
  //IDE-1282
  var double1213 = (42.5..1 as short).step(1)
  var double1214 = (42.5..10).step(1)
  var double1215 = (42.5..10L).step(1)
  var double1216 = (42.5..10.5f).step(1)
  var double1217 = (42.5..10.5).step(1)
  var double1218 = (42.5..BigInteger.TEN).step(1)   //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'BIGINTEGER'
  var double1219 = (42.5..BigDecimal.TEN).step(1)
  var double1220 = (42.5.."mystring").step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var double1221 = (42.5..(new Date())).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var double1222 = (42.5..aaa).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_DOUBLE.A'


  //Both end points are 'double' but step function has parameter of different type
  var double1311 = (42.5..43.5).step('c')
  var double1312 = (42.5..43.5).step(1b)
  var double1313 = (42.5..43.5).step(1 as short)
  var double1314 = (42.5..43.5).step(42)
  var double1315 = (42.5..43.5).step(42.5f)
  var double1316 = (42.5..43.5).step(42L)
  var double1317 = (42.5..43.5).step(42.5)
  var double1318 = (42.5..43.5).step(BigInteger.ONE)
  var double1319 = (42.5..43.5).step(BigDecimal.TEN)
  var double1320 = (42.5..43.5).step("mystring")      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var double1321 = (42.5..43.5).step(d1)      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var double1322 = (42.5..43.5).step(aaa)      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_DOUBLE.A)'

  //for loop
  function testForLoop() {
    for (i in (42.5..'c')) {
    }
    for (i in (42.5..1b)) {
    }
    for (i in (42.5..1 as short)) {
    }
    for (i in (42.5..10)) {
    }
    for (i in (42.5..10L)) {
    }
    for (i in (42.5..10.5f)) {
    }
    for (i in (42.5..10.5)) {
    }
    for (i in (42.5..BigInteger.TEN)) {   //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'BIGINTEGER'
    }
    for (i in (42.5..BigDecimal.TEN)) {
    }
    for (i in (42.5.."mystring")) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
    }
    for (i in (42.5..(new Date()))) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
    }
    for (i in (42.5..aaa)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_DOUBLE.A'
    }
  }
}