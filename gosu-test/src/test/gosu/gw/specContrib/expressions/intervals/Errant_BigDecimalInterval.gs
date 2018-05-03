package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_BigDecimalInterval {
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
  var bigDecimal1011 = BigDecimal.ONE..BigDecimal.TEN
  var bigDecimal1012 = BigDecimal.ONE..|BigDecimal.TEN
  var bigDecimal1013 = BigDecimal.ONE|..BigDecimal.TEN
  var bigDecimal1014 = BigDecimal.ONE|..|BigDecimal.TEN
  var bigDecimal1015 = BigDecimal.ONE..-BigDecimal.TEN
  var bigDecimal1016 = -BigDecimal.ONE..BigDecimal.TEN
  var bigDecimal1017 = -BigDecimal.ONE..-BigDecimal.TEN
  var bigDecimal1018 = BigDecimal.ONE..+BigDecimal.TEN
  var bigDecimal1019 = +BigDecimal.ONE..+BigDecimal.TEN

  //Lower end point is BigDecimal. upper limit various types
  var bigDecimal1111 = (BigDecimal.TEN..'c')
  var bigDecimal1112 = (BigDecimal.TEN..1b)
  var bigDecimal1113 = (BigDecimal.TEN..1 as short)
  var bigDecimal1114 = (BigDecimal.TEN..10)
  var bigDecimal1115 = (BigDecimal.TEN..10L)
  var bigDecimal1116 = (BigDecimal.TEN..10.5f)
  var bigDecimal1117 = (BigDecimal.TEN..10.5)
  var bigDecimal1118 = (BigDecimal.TEN..BigInteger.TEN)
  var bigDecimal1119 = (BigDecimal.TEN..BigDecimal.TEN)
  var bigDecimal1120 = (BigDecimal.TEN.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var bigDecimal1121 = (BigDecimal.TEN..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var bigDecimal1122 = (BigDecimal.TEN..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGDECIMAL.A'
  var bigDecimal1123 = (BigDecimal.TEN..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'

  //Lower end point is BigDecimal with step function
  var bigDecimal1211 = (BigDecimal.ONE..'c').step(42)
  var bigDecimal1212 = (BigDecimal.ONE..1b).step(42)
  //IDE-1282
  var bigDecimal1213 = (BigDecimal.ONE..1 as short).step(42)
  var bigDecimal1214 = (BigDecimal.ONE..10).step(42)
  var bigDecimal1215 = (BigDecimal.ONE..10L).step(42)
  var bigDecimal1216 = (BigDecimal.ONE..10.5f).step(42)
  var bigDecimal1217 = (BigDecimal.ONE..10.5).step(42)
  var bigDecimal1218 = (BigDecimal.ONE..BigInteger.TEN).step(42)
  var bigDecimal1219 = (BigDecimal.ONE..BigDecimal.TEN).step(42)
  var bigDecimal1220 = (BigDecimal.ONE.."mystring").step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var bigDecimal1221 = (BigDecimal.ONE..(new Date())).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var bigDecimal1222 = (BigDecimal.ONE..aaa).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGDECIMAL.A'


  //Both end points are 'BigDecimal' but step function has parameter of different type
  var bigDecimal1311 = (BigDecimal.ONE..BigDecimal.TEN).step('c')
  var bigDecimal1312 = (BigDecimal.ONE..BigDecimal.TEN).step(1b)
  var bigDecimal1313 = (BigDecimal.ONE..BigDecimal.TEN).step(1 as short)
  var bigDecimal1314 = (BigDecimal.ONE..BigDecimal.TEN).step(42)
  var bigDecimal1315 = (BigDecimal.ONE..BigDecimal.TEN).step(42.5f)
  var bigDecimal1316 = (BigDecimal.ONE..BigDecimal.TEN).step(42L)
  var bigDecimal1317 = (BigDecimal.ONE..BigDecimal.TEN).step(42.5)
  var bigDecimal1318 = (BigDecimal.ONE..BigDecimal.TEN).step(BigInteger.ONE)
  var bigDecimal1319 = (BigDecimal.ONE..BigDecimal.TEN).step(BigDecimal.TEN)
  var bigDecimal1320 = (BigDecimal.ONE..BigDecimal.TEN).step("mystring")      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var bigDecimal1321 = (BigDecimal.ONE..BigDecimal.TEN).step(d1)      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var bigDecimal1322 = (BigDecimal.ONE..BigDecimal.TEN).step(aaa)      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGDECIMAL.A)'

  //for loop
  function testForLoop() {
    for (i in (BigDecimal.ONE..'c')) {
    }
    for (i in (BigDecimal.ONE..1b)) {
    }
    for (i in (BigDecimal.ONE..1 as short)) {
    }
    for (i in (BigDecimal.ONE..10)) {
    }
    for (i in (BigDecimal.ONE..10L)) {
    }
    for (i in (BigDecimal.ONE..10.5f)) {
    }
    for (i in (BigDecimal.ONE..10.5)) {
    }
    for (i in (BigDecimal.ONE..BigInteger.TEN)) {
    }
    for (i in (BigDecimal.ONE..BigDecimal.TEN)) {
    }
    for (i in (BigDecimal.ONE.."mystring")) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
    }
    for (i in (BigDecimal.ONE..(new Date()))) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
    }
    for (i in (BigDecimal.ONE..aaa)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGDECIMAL.A'
    }
  }

}