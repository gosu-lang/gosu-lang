package gw.specContrib.expressions.intervals

uses java.lang.Integer
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Character
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date
uses gw.lang.reflect.interval.IntegerInterval

class Errant_IntegerInterval {
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
  var int1011 = 1..2
  var int1012 = 1..|2
  var int1013 = 1|..2
  var int1014 = 1|..|2
  var int1015 = 1..-2
  var int1016 = -1..2
  var int1017 = -1..-2
  var int1018 = 1..+2
  var int1019 = +1..+2

  //Lower end point is int. upper limit various types
  var int1111 = (42..'c')
  var int1112 = (42..1b)
  var int1113 = (42..1 as short)
  var int1114 = (42..10)
  var int1115 = (42..10L)
  var int1116 = (42..10.5f)
  var int1117 = (42..10.5)
  var int1118 = (42..BigInteger.TEN)
  var int1119 = (42..BigDecimal.TEN)
  var int1120 = (42.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var int1121 = (42..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var int1122 = (42..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_INTEGER.A'
  var int1123 = (42..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'

  //Lower end point is int with step function
  var int1211 = (42..'c').step(42)
  var int1212 = (42..1b).step(42)
  //IDE-1282
  var int1213 = (42..1 as short).step(42)
  var int1214 = (42..10).step(42)
  var int1215 = (42..10L).step(42)
  var int1216 = (42..10.5f).step(42)
  var int1217 = (42..10.5).step(42)
  var int1218 = (42..BigInteger.TEN).step(42)
  var int1219 = (42..BigDecimal.TEN).step(42)
  var int1220 = (42.."mystring").step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var int1221 = (42..(new Date())).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var int1222 = (42..aaa).step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_INTEGER.A'


  //Both end points are 'int' but step function has parameter of different type
  var int1311 = (42..43).step('c')
  var int1312 = (42..43).step(1b)
  var int1313 = (42..43).step(1 as short)
  var int1314 = (42..43).step(42)
  var int1315 = (42..43).step(42.5f)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(FLOAT)'
  var int1316 = (42..43).step(42L)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(LONG)'
  var int1317 = (42..43).step(42.5)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(DOUBLE)'
  var int1318 = (42..43).step(BigInteger.ONE)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGINTEGER)'
  var int1319 = (42..43).step(BigDecimal.TEN)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGDECIMAL)'
  var int1320 = (42..43).step("mystring")      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var int1321 = (42..43).step(d1)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var int1322 = (42..43).step(aaa)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_INTEGER.A)'

  //for loop
  function testForLoop() {
    for (i in (42..'c')) {}
    for (i in (42..1b)) {}
    for (i in (42..1 as short)) {}
    for (i in (42..10)) {}
    for (i in (42..10L)) {}
    for (i in (42..10.5f)) {}
    for (i in (42..10.5)) {}
    for (i in (42..BigInteger.TEN)) {}
    for (i in (42..BigDecimal.TEN)) {}
    for (i in (42.."mystring")) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
    for (i in (42..(new Date()))) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
    for (i in (42..aaa)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_INTEGER.A'
  }

  function testBoxedTypes() {
    var ch: Character
    var chI: IntegerInterval = 0..ch
    var b: Byte
    var bI: IntegerInterval = 0..b
    var s: Short
    var sI: IntegerInterval = 0..s
    var i: Integer
    var iI: IntegerInterval = 0..i
  }
}