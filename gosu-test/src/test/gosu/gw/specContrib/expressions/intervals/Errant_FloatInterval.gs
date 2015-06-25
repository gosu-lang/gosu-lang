package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_FloatInterval {

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
  var float1011 = 42.5f..43.5f
  var float1012 = 42.5f..|43.5f
  var float1013 = 42.5f|..43.5f
  var float1014 = 42.5f|..|43.5f
  var float1015 = 42.5f..-43.5f
  var float1016 = -42.5f..43.5f
  var float1017 = -42.5f..-43.5f
  var float1018 = 42.5f..+43.5f
  var float1019 = +42.5f..+43.5f

  //Lower end point is float. upper limit various types
  var float1111 = (42.5f..'c')
  var float1112 = (42.5f..1b)
  var float1113 = (42.5f..1 as short)
  var float1114 = (42.5f..10)
  var float1115 = (42.5f..10L)
  var float1116 = (42.5f..10.5f)
  var float1117 = (42.5f..10.5)
  var float1118 = (42.5f..BigInteger.TEN)     //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'BIGINTEGER'
  var float1119 = (42.5f..BigDecimal.TEN)
  var float1120 = (42.5f.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var float1121 = (42.5f..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var float1122 = (42.5f..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_FLOAT.A'
  var float1123 = (42.5f..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'

  //Lower end point is float with step function
  var float1211 = (42.5f..'c').step(1)
  var float1212 = (42.5f..1b).step(1)
  //IDE-1282
  var float1213 = (42.5f..1 as short).step(1)
  var float1214 = (42.5f..10).step(1)
  var float1215 = (42.5f..10L).step(1)
  var float1216 = (42.5f..10.5f).step(1)
  var float1217 = (42.5f..10.5).step(1)
  var float1218 = (42.5f..BigInteger.TEN).step(1)    //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'BIGINTEGER'
  var float1219 = (42.5f..BigDecimal.TEN).step(1)
  var float1220 = (42.5f.."mystring").step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var float1221 = (42.5f..(new Date())).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var float1222 = (42.5f..aaa).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_FLOAT.A'


  //Both end points are 'float' but step function has parameter of different type
  var float1311 = (42.5f..43.5f).step('c')
  var float1312 = (42.5f..43.5f).step(1b)
  var float1313 = (42.5f..43.5f).step(1 as short)
  var float1314 = (42.5f..43.5f).step(42)
  var float1315 = (42.5f..43.5f).step(42.5f)
  var float1316 = (42.5f..43.5f).step(42L)
  var float1317 = (42.5f..43.5f).step(42.5)
  var float1318 = (42.5f..43.5f).step(BigInteger.ONE)
  var float1319 = (42.5f..43.5f).step(BigDecimal.TEN)
  var float1320 = (42.5f..43.5f).step("mystring")      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var float1321 = (42.5f..43.5f).step(d1)      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var float1322 = (42.5f..43.5f).step(aaa)      //## issuekeys: 'STEP(JAVA.MATH.BIGDECIMAL)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_FLOAT.A)'

  //for loop
  function testForLoop() {
    for (i in (42.5f..'c')) {}
    for (i in (42.5f..1b)) {}
    for (i in (42.5f..1 as short)) {}
    for (i in (42.5f..10)) {}
    for (i in (42.5f..10L)) {}
    for (i in (42.5f..10.5f)) {}
    for (i in (42.5f..10.5)) {}
    for (i in (42.5f..BigInteger.TEN)) {}     //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'BIGINTEGER'
    for (i in (42.5f..BigDecimal.TEN)) {}
    for (i in (42.5f.."mystring")) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
    for (i in (42.5f..(new Date()))) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
    for (i in (42.5f..aaa)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_FLOAT.A'
  }

}