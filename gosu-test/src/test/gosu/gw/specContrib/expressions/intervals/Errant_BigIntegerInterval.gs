package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_BigIntegerInterval {
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
  var bigInteger1011 = BigInteger.ONE..BigInteger.TEN
  var bigInteger1012 = BigInteger.ONE..|BigInteger.TEN
  var bigInteger1013 = BigInteger.ONE|..BigInteger.TEN
  var bigInteger1014 = BigInteger.ONE|..|BigInteger.TEN
  var bigInteger1015 = BigInteger.ONE..-BigInteger.TEN
  var bigInteger1016 = -BigInteger.ONE..BigInteger.TEN
  var bigInteger1017 = -BigInteger.ONE..-BigInteger.TEN
  var bigInteger1018 = BigInteger.ONE..+BigInteger.TEN
  var bigInteger1019 = +BigInteger.ONE..+BigInteger.TEN

  //Lower end point is BigInteger. upper limit various types
  var bigInteger1111 = (BigInteger.TEN..'c')
  var bigInteger1112 = (BigInteger.TEN..1b)
  var bigInteger1113 = (BigInteger.TEN..1 as short)
  var bigInteger1114 = (BigInteger.TEN..10)
  var bigInteger1115 = (BigInteger.TEN..10L)
  //IDE-1296
  var bigInteger1116 = (BigInteger.TEN..10.5f)        //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.FLOAT'
  var bigInteger1117 = (BigInteger.TEN..10.5)         //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.DOUBLE'
  var bigInteger1118 = (BigInteger.TEN..BigInteger.TEN)
  var bigInteger1119 = (BigInteger.TEN..BigDecimal.TEN)
  var bigInteger1120 = (BigInteger.TEN.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var bigInteger1121 = (BigInteger.TEN..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  var bigInteger1122 = (BigInteger.TEN..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGINTEGER.A'
  var bigInteger1123 = (BigInteger.TEN..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'

  //Lower end point is BigInteger with step function
  var bigInteger1211 = (BigInteger.ONE..'c').step(42)
  var bigInteger1212 = (BigInteger.ONE..1b).step(42)
  //IDE-1282
  var bigInteger1213 = (BigInteger.ONE..1 as short).step(42)
  var bigInteger1214 = (BigInteger.ONE..10).step(42)
  var bigInteger1215 = (BigInteger.ONE..10L).step(42)
  var bigInteger1216 = (BigInteger.ONE..10.5f).step(42)    //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.FLOAT'
  var bigInteger1217 = (BigInteger.ONE..10.5).step(42)     //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.DOUBLE'
  var bigInteger1218 = (BigInteger.ONE..BigInteger.TEN).step(42)
  var bigInteger1219 = (BigInteger.ONE..BigDecimal.TEN).step(42)
  var bigInteger1220 = (BigInteger.ONE.."mystring").step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var bigInteger1221 = (BigInteger.ONE..(new Date())).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  var bigInteger1222 = (BigInteger.ONE..aaa).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGINTEGER.A'


  //Both end points are 'BigInteger' but step function has parameter of different type
  var bigInteger1311 = (BigInteger.ONE..BigInteger.TEN).step('c')
  var bigInteger1312 = (BigInteger.ONE..BigInteger.TEN).step(1b)
  var bigInteger1313 = (BigInteger.ONE..BigInteger.TEN).step(1 as short)
  var bigInteger1314 = (BigInteger.ONE..BigInteger.TEN).step(42)
  var bigInteger1315 = (BigInteger.ONE..BigInteger.TEN).step(42.5f)      //## issuekeys: 'STEP(JAVA.MATH.BIGINTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(FLOAT)'
  var bigInteger1316 = (BigInteger.ONE..BigInteger.TEN).step(42L)
  var bigInteger1317 = (BigInteger.ONE..BigInteger.TEN).step(42.5)      //## issuekeys: 'STEP(JAVA.MATH.BIGINTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(DOUBLE)'
  var bigInteger1318 = (BigInteger.ONE..BigInteger.TEN).step(BigInteger.ONE)
  var bigInteger1319 = (BigInteger.ONE..BigInteger.TEN).step(BigDecimal.TEN)      //## issuekeys: 'STEP(JAVA.MATH.BIGINTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGDECIMAL)'
  var bigInteger1320 = (BigInteger.ONE..BigInteger.TEN).step("mystring")      //## issuekeys: 'STEP(JAVA.MATH.BIGINTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var bigInteger1321 = (BigInteger.ONE..BigInteger.TEN).step(d1)      //## issuekeys: 'STEP(JAVA.MATH.BIGINTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var bigInteger1322 = (BigInteger.ONE..BigInteger.TEN).step(aaa)      //## issuekeys: 'STEP(JAVA.MATH.BIGINTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGINTEGER.A)'

  //for loop
  function testForLoop() {
    for (i in (BigInteger.ONE..'c')) {
    }
    for (i in (BigInteger.ONE..1b)) {
    }
    for (i in (BigInteger.ONE..1 as short)) {
    }
    for (i in (BigInteger.ONE..10)) {
    }
    for (i in (BigInteger.ONE..10L)) {
    }
    for (i in (BigInteger.ONE..10.5f)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.FLOAT'
    }
    for (i in (BigInteger.ONE..10.5)) {       //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.DOUBLE'
    }
    for (i in (BigInteger.ONE..BigInteger.TEN)) {
    }
    for (i in (BigInteger.ONE..BigDecimal.TEN)) {
    }
    for (i in (BigInteger.ONE.."mystring")) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
    }
    for (i in (BigInteger.ONE..(new Date()))) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
    }
    for (i in (BigInteger.ONE..aaa)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BIGINTEGER.A'
    }
  }

}