package gw.specContrib.operators.arithmetic.bitwise

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_BitwiseOR {
  var date1 : Date

  var bitwiseOR010 = 'c' | false      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'
  var bitwiseOR011 = 'c' | 'c'
  var bitwiseOR012 = 'c' | 3b
  var bitwiseOR013 = 'c' | 3s
  var bitwiseOR014 = 'c' | 3
  var bitwiseOR015 = 'c' | 3L
  var bitwiseOR016 = 'c' | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'FLOAT'
  var bitwiseOR017 = 'c' | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'DOUBLE'
  var bitwiseOR019 = 'c' | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
  var bitwiseOR0191 = 'c' | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.OBJECT'
  var bitwiseOR020 = 'c' | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var bitwiseOR021 = 'c' | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'NULL'
  var bitwiseOR022 = 'c' | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR023 = 'c' | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR024 = 'c' | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'CHAR', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseOR110 = 10b | false      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'
  var bitwiseOR111 = 10b | 'c'
  var bitwiseOR112 = 10b | 3b
  var bitwiseOR113 = 10b | 3s
  var bitwiseOR114 = 10b | 3
  var bitwiseOR115 = 10b | 3L
  var bitwiseOR116 = 10b | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'FLOAT'
  var bitwiseOR117 = 10b | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'DOUBLE'
  var bitwiseOR119 = 10b | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var bitwiseOR1191 = 10b | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.OBJECT'
  var bitwiseOR120 = 10b | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var bitwiseOR121 = 10b | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'NULL'
  var bitwiseOR122 = 10b | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR123 = 10b | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR124 = 10b | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BYTE', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseOR210 = 10s | false      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'
  var bitwiseOR211 = 10s | 'c'
  var bitwiseOR212 = 10s | 3b
  var bitwiseOR213 = 10s | 3s
  var bitwiseOR214 = 10s | 3
  var bitwiseOR215 = 10s | 3L
  var bitwiseOR216 = 10s | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'FLOAT'
  var bitwiseOR217 = 10s | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'DOUBLE'
  var bitwiseOR219 = 10s | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var bitwiseOR2191 = 10s | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.OBJECT'
  var bitwiseOR220 = 10s | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var bitwiseOR221 = 10s | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'NULL'
  var bitwiseOR222 = 10s | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR223 = 10s | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR224 = 10s | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseOR310 = 10 | false      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'
  var bitwiseOR311 = 10 | 'c'
  var bitwiseOR312 = 10 | 3b
  var bitwiseOR313 = 10 | 3s
  var bitwiseOR314 = 10 | 3
  var bitwiseOR315 = 10 | 3L
  var bitwiseOR316 = 10 | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'FLOAT'
  var bitwiseOR317 = 10 | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var bitwiseOR319 = 10 | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var bitwiseOR3191 = 10 | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.OBJECT'
  var bitwiseOR320 = 10 | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var bitwiseOR321 = 10 | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'NULL'
  var bitwiseOR322 = 10 | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR323 = 10 | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR324 = 10 | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseOR410 = 10L | false      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'
  var bitwiseOR411 = 10L | 'c'
  var bitwiseOR412 = 10L | 3b
  var bitwiseOR413 = 10L | 3s
  var bitwiseOR414 = 10L | 3
  var bitwiseOR415 = 10L | 3L
  var bitwiseOR416 = 10L | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'FLOAT'
  var bitwiseOR417 = 10L | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'DOUBLE'
  var bitwiseOR419 = 10L | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var bitwiseOR4191 = 10L | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.OBJECT'
  var bitwiseOR420 = 10L | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var bitwiseOR421 = 10L | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'NULL'
  var bitwiseOR422 = 10L | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR423 = 10L | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR424 = 10L | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'LONG', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseOR510 = 42.5f | false      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'
  var bitwiseOR511 = 42.5f | 'c'       //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'CHAR'
  var bitwiseOR512 = 42.5f | 3b      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'BYTE'
  var bitwiseOR513 = 42.5f | 3s      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'SHORT'
  var bitwiseOR514 = 42.5f | 3      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'INT'
  //IDE-407
  var bitwiseOR515 = 42.5f | 3L      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'LONG'
  var bitwiseOR516 = 42.5f | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'FLOAT'
  var bitwiseOR517 = 42.5f | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'DOUBLE'
  var bitwiseOR519 = 42.5f | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var bitwiseOR5191 = 42.5f | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.OBJECT'
  var bitwiseOR520 = 42.5f | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var bitwiseOR521 = 42.5f | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'NULL'
  var bitwiseOR522 = 42.5f | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR523 = 42.5f | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR524 = 42.5f | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseOR610 = 42.55 | false      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var bitwiseOR611 = 42.55 | 'c'      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'CHAR'
  var bitwiseOR612 = 42.55 | 3b      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'BYTE'
  var bitwiseOR613 = 42.55 | 3s      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'SHORT'
  var bitwiseOR614 = 42.55 | 3      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'INT'
  var bitwiseOR615 = 42.55 | 3L      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'LONG'
  var bitwiseOR616 = 42.55 | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'FLOAT'
  var bitwiseOR617 = 42.55 | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'DOUBLE'
  var bitwiseOR619 = 42.55 | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var bitwiseOR6191 = 42.55 | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.OBJECT'
  var bitwiseOR620 = 42.55 | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var bitwiseOR621 = 42.55 | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'NULL'
  var bitwiseOR622 = 42.55 | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR623 = 42.55 | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR624 = 42.55 | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.MATH.BIGDECIMAL'

  //IDE-585 OS Gosu Shows error here. Parser does not
  var bitwiseOR710 = true | true
  var bitwiseOR711 = true | 'c'      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'CHAR'
  var bitwiseOR712 = true | 3b      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'BYTE'
  var bitwiseOR713 = true | 3s      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'SHORT'
  var bitwiseOR714 = true | 3      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var bitwiseOR715 = true | 3L      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'LONG'
  var bitwiseOR717 = true | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'FLOAT'
  var bitwiseOR7171 = true | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'DOUBLE'
  var bitwiseOR719 = true | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.STRING'
  var bitwiseOR7191 = true | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.OBJECT'
  var bitwiseOR720 = true | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.DATE'
  var bitwiseOR721 = true | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'NULL'
  var bitwiseOR722 = true | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR723 = true | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR724 = true | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseOR810 = "string" | true      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BOOLEAN'
  var bitwiseOR811 = "string" | 'c'      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
  var bitwiseOR812 = "string" | 3b      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var bitwiseOR813 = "string" | 3s      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var bitwiseOR814 = "string" | 3      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var bitwiseOR815 = "string" | 3L      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var bitwiseOR817 = "string" | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var bitwiseOR8171 = "string" | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
  var bitwiseOR819 = "string" | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var bitwiseOR8191 = "string" | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.OBJECT'
  var bitwiseOR820 = "string" | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var bitwiseOR821 = "string" | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'NULL'
  var bitwiseOR822 = "string" | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOR823 = "string" | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOR824 = "string" | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseOr910 = BigInteger.ONE | true      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'
  var bitwiseOr911 = BigInteger.ONE | 'c'      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'CHAR'
  var bitwiseOr912 = BigInteger.ONE | 3b      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BYTE'
  var bitwiseOr913 = BigInteger.ONE | 3s      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'SHORT'
  var bitwiseOr914 = BigInteger.ONE | 3      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'INT'
  var bitwiseOr915 = BigInteger.ONE | 3L      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'LONG'
  var bitwiseOr917 = BigInteger.ONE | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'FLOAT'
  var bitwiseOr9171 = BigInteger.ONE | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'DOUBLE'
  var bitwiseOr919 = BigInteger.ONE | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var bitwiseOr9191 = BigInteger.ONE | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.OBJECT'
  var bitwiseOr920 = BigInteger.ONE | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  //IDE-866
  var bitwiseOr921 = BigInteger.ONE | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'NULL'
  var bitwiseOr922 = BigInteger.ONE | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOr923 = BigInteger.ONE | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOr924 = BigInteger.ONE | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseOr1010 = BigDecimal.ONE | true      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'
  var bitwiseOr1011 = BigDecimal.ONE | 'c'      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'CHAR'
  var bitwiseOr1012 = BigDecimal.ONE | 3b      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BYTE'
  var bitwiseOr1013 = BigDecimal.ONE | 3s      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'SHORT'
  var bitwiseOr1014 = BigDecimal.ONE | 3      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'INT'
  var bitwiseOr1015 = BigDecimal.ONE | 3L      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'LONG'
  var bitwiseOr1017 = BigDecimal.ONE | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'FLOAT'
  var bitwiseOr10171 = BigDecimal.ONE | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'DOUBLE'
  var bitwiseOr1019 = BigDecimal.ONE | "string"      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var bitwiseOr10191 = BigDecimal.ONE | new Object()      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.OBJECT'
  var bitwiseOr1020 = BigDecimal.ONE | date1      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var bitwiseOr1021 = BigDecimal.ONE | null      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'NULL'
  var bitwiseOr1022 = BigDecimal.ONE | {1, 2, 3}      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseOr1023 = BigDecimal.ONE | BigInteger.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.MATH.BIGINTEGER'
  var bitwiseOr1024 = BigDecimal.ONE | BigDecimal.ONE      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseOR910 = 1b | !2      //## issuekeys: OPERATOR '!' CANNOT BE APPLIED TO 'INT'
  var bitwiseOR911 = 42 | ~32
  var bitwiseOR912 = 42 | 3b | 42
  var bitwiseOR913 = 42.5f | 3s | 3s      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'FLOAT', 'SHORT'
  var bitwiseOR914 = 42 | 3s | 3s
  var bitwiseOR915 = 42.5 || true | 3      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  //should promote to Long
  var bitwiseOR916 = 42 | -3L
  var bitwiseOR917 = -42s | 3.5f      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'SHORT', 'FLOAT'
  var bitwiseOR9171 = 42 | 10 | 3.5      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var bitwiseOR919 = 42.5 | 10 | 6      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'INT'

}