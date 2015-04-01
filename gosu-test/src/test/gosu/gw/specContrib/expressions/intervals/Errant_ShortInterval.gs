package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_ShortInterval {
  class A {
  }

  class B {
  }

  var aaa: A
  var bbb: B
  var d1: DateTime
  var d2: DateTime
  var o: Object

  //Positive cases
  var short1011 = 1s..2s
  var short1012 = 1s..|2s
  var short1013 = 1s|..2s
  var short1014 = 1s|..|2s
  var short1015 = 1s..-2s
  var short1016 = -1s..2s
  var short1017 = -1s..-2s
  var short1018 = 1s..+2s
  var short1019 = +1s..+2s

  //Lower end point is short. upper limit various types
  var short1111 = (1s..'c')   //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'CHAR'
  var short1112 = (1s..1b)
  var short1113 = (1s..1s)
  var short1114 = (1s..10)
  var short1115 = (1s..10L)
  var short1116 = (1s..10.5f)
  var short1117 = (1s..10.5)
  var short1118 = (1s..BigInteger.TEN)
  var short1119 = (1s..BigDecimal.TEN)
  var short1120 = (1s.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var short1121 = (1s..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var short1122 = (1s..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A'

  //Lower end point is short with step function
  var short1211 = (1s..'c').step(1s)    //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'CHAR'
  var short1212 = (1s..1b).step(1s)
  var short1213 = (1s..1s).step(1s)   //IDE-1282
  var short1214 = (1s..10).step(1s)
  var short1215 = (1s..10L).step(1s)
  var short1216 = (1s..10.5f).step(1s)
  var short1217 = (1s..10.5).step(1s)
  var short1218 = (1s..BigInteger.TEN).step(1s)
  var short1219 = (1s..BigDecimal.TEN).step(1s)
  var short1220 = (1s.."mystring").step(1s)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var short1221 = (1s..(new Date())).step(1s)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var short1222 = (1s..aaa).step(1s)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A'


  //Both end points are 'short' but step function has parameter of different type
  //IDE-1282 - for all the following cases
  var short1311 = (1s..2s).step('c')
  var short1312 = (1s..2s).step(1b)
  var short1313 = (1s..2s).step(1s)
  var short1314 = (1s..2s).step(42)
  var short1315 = (1s..2s).step(42.5f)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(FLOAT)'
  var short1316 = (1s..2s).step(42L)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(LONG)'
  var short1317 = (1s..2s).step(42.5)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(DOUBLE)'
  var short1318 = (1s..2s).step(BigInteger.ONE)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.MATH.BIGINTEGER)'
  var short1319 = (1s..2s).step(BigDecimal.TEN)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.MATH.BIGDECIMAL)'
  var short1320 = (1s..2s).step("mystring")      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.LANG.STRING)'
  var short1321 = (1s..2s).step(d1)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.UTIL.DATE)'
  var short1322 = (1s..2s).step(aaa)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A)'

  //for loop
  function testForLoop() {
    for (i in (1s..'c')) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'CHAR'
    for (i in (1s..1b)) {}
    for (i in (1s..1s)) {}
    for (i in (1s..10)) {}
    for (i in (1s..10L)) {}
    for (i in (1s..10.5f)) {}
    for (i in (1s..10.5)) {}
    for (i in (1s..BigInteger.TEN)) {}
    for (i in (1s..BigDecimal.TEN)) {}
    for (i in (1s.."mystring")) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
    for (i in (1s..(new Date()))) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
    for (i in (1s..aaa)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A'
  }
}