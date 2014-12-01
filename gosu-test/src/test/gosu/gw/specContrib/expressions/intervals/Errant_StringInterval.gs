package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_StringInterval {
  class A {
  }

  class B {
  }

  var aaa: A
  var bbb: B
  var d1: DateTime
  var d2: DateTime
  var o: Object

  var string1011 = 'abc'..'ijk'
  var string1012 = 'abc'..|'ijk'
  var string1013 = 'abc'|..'ijk'
  var string1014 = 'abc'|..|'ijk'
  var string1015 = 'abc'..-'ijk'      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING'
  var string1016 = -'abc'..'ijk'      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING'
  var string1017 = -'abc'..-'ijk'      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING'
  var string1018 = 'abc'..+'ijk'      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING'
  var string1019 = +'abc'..+'ijk'      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING'

  //Lower end point is String. upper limit various types
  var string1109 = ('c'.."mystring")
  var char1 = 'c'
  var string1110 = (char1.."mystring")
  var string1111 = ("mystring"..'c')
  var string1112 = ("mystring"..1b)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var string1113 = ("mystring"..1s)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var string1114 = ("mystring"..10)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var string1115 = ("mystring"..10L)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var string1116 = ("mystring"..10.5f)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var string1117 = ("mystring"..10.5)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
  var string1118 = ("mystring"..BigInteger.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var string1119 = ("mystring"..BigDecimal.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'
  var string1120 = ("mystring".."mystring")
  var string1121 = ("mystring"..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var string1122 = ("mystring"..aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_STRING.A'

  //Lower end point is string with step function
  var string1211 = ("mystring"..'c').step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
  var string1212 = ("mystring"..1b).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var string1213 = ("mystring"..1s).step(1)        //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var string1214 = ("mystring"..10).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var string1215 = ("mystring"..10L).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var string1216 = ("mystring"..10.5f).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var string1217 = ("mystring"..10.5).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
  var string1218 = ("mystring"..BigInteger.TEN).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var string1219 = ("mystring"..BigDecimal.TEN).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'
  var string1220 = ("mystring".."mystring").step(1)      //## issuekeys: STEP FUNCTION CANNOT BE APPLIED
  var string1221 = ("mystring"..(new Date())).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var string1222 = ("mystring"..aaa).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_STRING.A'


  //Both end points are 'String' but step function has parameter of different type
  var string1311 = ("mystring"..'mystring2').step('c')      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1312 = ("mystring"..'mystring2').step(1b)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1313 = ("mystring"..'mystring2').step(1s)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1314 = ("mystring"..'mystring2').step(42)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1315 = ("mystring"..'mystring2').step(42.5f)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1316 = ("mystring"..'mystring2').step(42L)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1317 = ("mystring"..'mystring2').step(42.5)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1318 = ("mystring"..'mystring2').step(BigInteger.ONE)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1319 = ("mystring"..'mystring2').step(BigDecimal.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1320 = ("mystring"..'mystring2').step("mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1321 = ("mystring"..'mystring2').step(d1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var string1322 = ("mystring"..'mystring2').step(aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'

  //for loop
  function testForLoop() {
    for (i in ("mystring"..'k')) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
    for (i in ("mystring"..1b)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
    for (i in ("mystring"..1s)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
    for (i in ("mystring"..10)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
    for (i in ("mystring"..10L)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
    for (i in ("mystring"..10.5f)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
    for (i in ("mystring"..10.5)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
    for (i in ("mystring"..BigInteger.TEN)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
    for (i in ("mystring"..BigDecimal.TEN)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'
    for (i in ("mystring".."mystring")) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
    for (i in ("mystring"..(new Date()))) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
    for (i in ("mystring"..aaa)) {}      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_STRING.A'
  }

}