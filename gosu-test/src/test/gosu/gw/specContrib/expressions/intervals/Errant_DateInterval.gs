package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_DateInterval {
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
  var date1011 = d1..d2
  var date1012 = d1..|d2
  var date1013 = d1|..d2
  var date1014 = d1|..|d2
  var date1015 = d1..-d2      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE'
  var date1016 = -d1..d2      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE'
  var date1017 = -d1..-d2      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE'
  var date1018 = d1..+d2      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE'
  var date1019 = +d1..+d2      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE'

  //Lower end point is date. upper limit various types
  var date1111 = (d1..'c')      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'CHAR'
  var date1112 = (d1..1b)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'BYTE'
  var date1113 = (d1..1 as short)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'SHORT'
  var date1114 = (d1..10)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'INT'
  var date1115 = (d1..10L)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'LONG'
  var date1116 = (d1..10.5f)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'FLOAT'
  var date1117 = (d1..10.5)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'DOUBLE'
  var date1118 = (d1..BigInteger.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.MATH.BIGINTEGER'
  var date1119 = (d1..BigDecimal.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.MATH.BIGDECIMAL'
  var date1120 = (d1.."mystring")           //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.LANG.STRING'
  var date1121 = (d1..(new Date()))
  var date1122 = (d1..aaa)            //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_DATE.A'
  var date1123 = (d1..false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'BOOLEAN'

  //Lower end point is datetime with step function
  var date1211 = (d1..'c').step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'CHAR'
  var date1212 = (d1..1b).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'BYTE'
  var date1213 = (d1..1 as short).step(1)   //IDE-1282      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'SHORT'
  var date1214 = (d1..10).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'INT'
  var date1215 = (d1..10L).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'LONG'
  var date1216 = (d1..10.5f).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'FLOAT'
  var date1217 = (d1..10.5).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'DOUBLE'
  var date1218 = (d1..BigInteger.TEN).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.MATH.BIGINTEGER'
  var date1219 = (d1..BigDecimal.TEN).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.MATH.BIGDECIMAL'
  var date1220 = (d1.."mystring").step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.LANG.STRING'
  var date1221 = (d1..(new Date())).step(1)
  var date1222 = (d1..aaa).step(1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_DATE.A'


  //Both end points are 'char' but step function has parameter of different type
  var date1311 = (d1..d2).step('c')
  var date1312 = (d1..d2).step(1b)
  var date1313 = (d1..d2).step(1 as short)
  var date1314 = (d1..d2).step(42)
  var date1315 = (d1..d2).step(42.5f)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1316 = (d1..d2).step(42L)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1317 = (d1..d2).step(42.5)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1318 = (d1..d2).step(BigInteger.ONE)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1319 = (d1..d2).step(BigDecimal.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1320 = (d1..d2).step("mystring")      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1321 = (d1..d2).step(d1)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1322 = (d1..d2).step(aaa)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.UTIL.DATE'
  var date1323 = (new Date()..new Date()).step(2).unit(DAYS)
  var date1324 = (new Date()..new Date()).step(2).unit(WEEKS)
  var date1325 = (new Date()..new Date()).step(2).unit(MONTHS)
  var date1326 = (new Date()..new Date()).step(2).unit(YEARS)

  //for loop
  function testForLoop() {
    for (i in (d1..'k')) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'CHAR'
    }
    for (i in (d1..1b)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'BYTE'
    }
    for (i in (d1..1 as short)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'SHORT'
    }
    for (i in (d1..10)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'INT'
    }
    for (i in (d1..10L)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'LONG'
    }
    for (i in (d1..10.5f)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'FLOAT'
    }
    for (i in (d1..10.5)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'DOUBLE'
    }
    for (i in (d1..BigInteger.TEN)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.MATH.BIGINTEGER'
    }
    for (i in (d1..BigDecimal.TEN)) {      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.MATH.BIGDECIMAL'
    }
    for (i in (d1.."mystring")) {           //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'JAVA.LANG.STRING'
    }
    for (i in (d1..(new Date()))) {
    }
    for (i in (d1..aaa)) {            //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INTERVALSANDDIMENSIONS.INTERVALSBASIC.INTERVAL_DATE.A'
    }
  }

}