package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_ByteInterval {

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
  var byte1011 = 1b..2b
  var byte1012 = 1b..|2b
  var byte1013 = 1b|..2b
  var byte1014 = 1b|..|2b
  var byte1015 = 1b..-2b
  var byte1016 = -1b..2b
  var byte1017 = -1b..-2b
  var byte1018 = 1b..+2b
  var byte1019 = +1b..+2b

  //Lower end point is byte. upper limit various types
  //IDE-1296
  var byte1111 = (1b..'c')        //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'CHAR'
  var byte1112 = (1b..1b)
  var byte1113 = (1b..1 as short)
  var byte1114 = (1b..10)
  var byte1115 = (1b..10L)
  var byte1116 = (1b..10.5f)
  var byte1117 = (1b..10.5)
  var byte1118 = (1b..BigInteger.TEN)
  var byte1119 = (1b..BigDecimal.TEN)
  var byte1120 = (1b.."mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var byte1121 = (1b..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var byte1122 = (1b..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BYTE.A'
  var byte1123 = (1b..false)            //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'

  //Lower end point is byte with step function
  var byte1211 = (1b..'c').step(1b)   //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'CHAR'
  var byte1212 = (1b..1b).step(1b)
  //IDE-1282
  var byte1213 = (1b..1 as short).step(1b)
  var byte1214 = (1b..10).step(1b)
  var byte1215 = (1b..10L).step(1b)
  var byte1216 = (1b..10.5f).step(1b)
  var byte1217 = (1b..10.5).step(1b)
  var byte1218 = (1b..BigInteger.TEN).step(1b)
  var byte1219 = (1b..BigDecimal.TEN).step(1b)
  var byte1220 = (1b.."mystring").step(1b)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var byte1221 = (1b..(new Date())).step(1b)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var byte1222 = (1b..aaa).step(1b)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BYTE.A'


  //Both end points are 'byte' but step function has parameter of different type
  var byte1311 = (1b..2b).step('c')
  var byte1312 = (1b..2b).step(1b)
  var byte1313 = (1b..2b).step(1 as short)
  var byte1314 = (1b..2b).step(42)
  var byte1315 = (1b..2b).step(42.5f)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(FLOAT)'
  var byte1316 = (1b..2b).step(42L)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(LONG)'
  var byte1317 = (1b..2b).step(42.5)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(DOUBLE)'
  var byte1318 = (1b..2b).step(BigInteger.ONE)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGINTEGER)'
  var byte1319 = (1b..2b).step(BigDecimal.TEN)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.MATH.BIGDECIMAL)'
  var byte1320 = (1b..2b).step("mystring")      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  var byte1321 = (1b..2b).step(d1)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(JAVA.UTIL.DATE)'
  var byte1322 = (1b..2b).step(aaa)      //## issuekeys: 'STEP(JAVA.LANG.INTEGER)' IN 'GW.LANG.REFLECT.INTERVAL.ITERABLEINTERVAL' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BYTE.A)'

  //for loop
  function testForLoop() {
    for (i in (1b..'c')) {     //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'CHAR'
    }
    for (i in (1b..1b)) {
    }
    for (i in (1b..1 as short)) {
    }
    for (i in (1b..10)) {
    }
    for (i in (1b..10L)) {
    }
    for (i in (1b..10.5f)) {
    }
    for (i in (1b..10.5)) {
    }
    for (i in (1b..BigInteger.TEN)) {
    }
    for (i in (1b..BigDecimal.TEN)) {
    }
    for (i in (1b.."mystring")) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
    }
    for (i in (1b..(new Date()))) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
    }
    for (i in (1b..aaa)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_BYTE.A'
    }
  }


}