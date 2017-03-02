package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_LongInterval {
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
  var long1011 = 10L..20L
  var long1012 = 10L..|20L
  var long1013 = 10L|..20L
  var long1014 = 10L|..|20L
  var long1015 = 10L..-20L
  var long1016 = -10L..20L
  var long1017 = -10L..-20L
  var long1018 = 10L..+20L
  var long1019 = +10L..+20L

  //Lower end point is long. upper limit various types
  var long1111 = (42L..'c')
  var long1112 = (42L..1b)
  var long1113 = (42L..1 as short)
  var long1114 = (42L..10)
  var long1115 = (42L..10L)
  var long1116 = (42L..10.5f)
  var long1117 = (42L..10.5)
  var long1118 = (42L..BigInteger.TEN)
  var long1119 = (42L..BigDecimal.TEN)
  var long1120 = (42L.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var long1121 = (42L..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var long1122 = (42L..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_LONG.A'
  var long1123 = (42L..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'

  //Lower end point is long with step function
  var long1211 = (42L..'c').step(42)
  var long1212 = (42L..1b).step(42)
  var long1213 = (42L..1 as short).step(42)   //IDE-1282
  var long1214 = (42L..10).step(42)
  var long1215 = (42L..10L).step(42)
  var long1216 = (42L..10.5f).step(42)
  var long1217 = (42L..10.5).step(42)
  var long1218 = (42L..BigInteger.TEN).step(42)
  var long1219 = (42L..BigDecimal.TEN).step(42)
  var long1220 = (42L.."mystring").step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var long1221 = (42L..(new Date())).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var long1222 = (42L..aaa).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_LONG.A'


  //Both end points are 'long' but step function has parameter of different type
  var long1311 = (42L..43L).step('c')
  var long1312 = (42L..43L).step(1b)
  var long1313 = (42L..43L).step(1 as short)
  var long1314 = (42L..43L).step(42)
  var long1315 = (42L..43L).step(42.5f)      //## issuekeys: 'STEP(JAVA.LANG.LONG)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(FLOAT)'
  var long1316 = (42L..43L).step(42L)
  var long1317 = (42L..43L).step(42.5)      //## issuekeys: 'STEP(JAVA.LANG.LONG)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(DOUBLE)'
  var long1318 = (42L..43L).step(BigInteger.ONE)      //## issuekeys: 'STEP(JAVA.LANG.LONG)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGINTEGER)'
  var long1319 = (42L..43L).step(BigDecimal.TEN)      //## issuekeys: 'STEP(JAVA.LANG.LONG)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGDECIMAL)'
  var long1320 = (42L..43L).step("mystring")      //## issuekeys: 'STEP(JAVA.LANG.LONG)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var long1321 = (42L..43L).step(d1)      //## issuekeys: 'STEP(JAVA.LANG.LONG)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var long1322 = (42L..43L).step(aaa)      //## issuekeys: 'STEP(JAVA.LANG.LONG)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_LONG.A)'

  //for loop
  function testForLoop() {
    for (i in (42L..'c')) {}
    for (i in (42L..1b)) {}
    for (i in (42L..1 as short)) {}
    for (i in (42L..10)) {}
    for (i in (42L..10L)) {}
    for (i in (42L..10.5f)) {}
    for (i in (42L..10.5)) {}
    for (i in (42L..BigInteger.TEN)) {}
    for (i in (42L..BigDecimal.TEN)) {}
    for (i in (42L.."mystring")) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
    for (i in (42L..(new Date()))) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
    for (i in (42L..aaa)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_LONG.A'
  }

}