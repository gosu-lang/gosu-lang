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
  var d1: java.util.Date
  var d2: java.util.Date
  var o: Object

  //Positive cases
  var short1011 = 1 as short..2 as short
  var short1012 = 1 as short..|2 as short
  var short1013 = 1 as short|..2 as short
  var short1014 = 1 as short|..|2 as short
  var short1015 = 1 as short..-2 as short
  var short1016 = -1 as short..2 as short
  var short1017 = -1 as short..-2 as short
  var short1018 = 1 as short..+2 as short
  var short1019 = +1 as short..+2 as short

  //Lower end point is short. upper limit various types
  var short1111 = (1 as short..'c')   //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'CHAR'
  var short1112 = (1 as short..1b)
  var short1113 = (1 as short..1 as short)
  var short1114 = (1 as short..10)
  var short1115 = (1 as short..10L)
  var short1116 = (1 as short..10.5f)
  var short1117 = (1 as short..10.5)
  var short1118 = (1 as short..BigInteger.TEN)
  var short1119 = (1 as short..BigDecimal.TEN)
  var short1120 = (1 as short.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var short1121 = (1 as short..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var short1122 = (1 as short..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A'
  var short1123 = (1s..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'

  //Lower end point is short with step function
  var short1211 = (1 as short..'c').step(1 as short)    //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'CHAR'
  var short1212 = (1 as short..1b).step(1 as short)
  var short1213 = (1 as short..1 as short).step(1 as short)   //IDE-1282
  var short1214 = (1 as short..10).step(1 as short)
  var short1215 = (1 as short..10L).step(1 as short)
  var short1216 = (1 as short..10.5f).step(1 as short)
  var short1217 = (1 as short..10.5).step(1 as short)
  var short1218 = (1 as short..BigInteger.TEN).step(1 as short)
  var short1219 = (1 as short..BigDecimal.TEN).step(1 as short)
  var short1220 = (1 as short.."mystring").step(1 as short)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var short1221 = (1 as short..(new Date())).step(1 as short)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var short1222 = (1 as short..aaa).step(1 as short)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A'


  //Both end points are 'short' but step function has parameter of different type
  //IDE-1282 - for all the following cases
  var short1311 = (1 as short..2 as short).step('c')
  var short1312 = (1 as short..2 as short).step(1b)
  var short1313 = (1 as short..2 as short).step(1 as short)
  var short1314 = (1 as short..2 as short).step(42)
  var short1315 = (1 as short..2 as short).step(42.5f)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(FLOAT)'
  var short1316 = (1 as short..2 as short).step(42L)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(LONG)'
  var short1317 = (1 as short..2 as short).step(42.5)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(DOUBLE)'
  var short1318 = (1 as short..2 as short).step(BigInteger.ONE)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.MATH.BIGINTEGER)'
  var short1319 = (1 as short..2 as short).step(BigDecimal.TEN)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.MATH.BIGDECIMAL)'
  var short1320 = (1 as short..2 as short).step("mystring")      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.LANG.STRING)'
  var short1321 = (1 as short..2 as short).step(d1)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(JAVA.UTIL.DATE)'
  var short1322 = (1 as short..2 as short).step(aaa)      //## issuekeys: CANNOT RESOLVE METHOD 'STEP(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A)'

  //for loop
  function testForLoop() {
    for (i in (1 as short..'c')) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'CHAR'
    for (i in (1 as short..1b)) {}
    for (i in (1 as short..1 as short)) {}
    for (i in (1 as short..10)) {}
    for (i in (1 as short..10L)) {}
    for (i in (1 as short..10.5f)) {}
    for (i in (1 as short..10.5)) {}
    for (i in (1 as short..BigInteger.TEN)) {}
    for (i in (1 as short..BigDecimal.TEN)) {}
    for (i in (1 as short.."mystring")) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
    for (i in (1 as short..(new Date()))) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
    for (i in (1 as short..aaa)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_SHORT.A'
  }
}