package gw.specContrib.operators.arithmetic.bitwise

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_BitwiseXOR {
  var date1 : Date

  var bitwiseXOR010 = 'c' ^ false      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'
  var bitwiseXOR011 = 'c' ^ 'c'
  var bitwiseXOR012 = 'c' ^ 3b
  var bitwiseXOR013 = 'c' ^ 3s
  var bitwiseXOR014 = 'c' ^ 3
  var bitwiseXOR015 = 'c' ^ 3L
  var bitwiseXOR016 = 'c' ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'FLOAT'
  var bitwiseXOR017 = 'c' ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'DOUBLE'
  var bitwiseXOR019 = 'c' ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
  var bitwiseXOR0191 = 'c' ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.OBJECT'
  var bitwiseXOR020 = 'c' ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var bitwiseXOR021 = 'c' ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'NULL'
  var bitwiseXOR022 = 'c' ^ {1,2,3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR023 = 'c' ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR024 = 'c' ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'CHAR', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseXOR110 = 10b ^ false      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'
  var bitwiseXOR111 = 10b ^ 'c'
  var bitwiseXOR112 = 10b ^ 3b
  var bitwiseXOR113 = 10b ^ 3s
  var bitwiseXOR114 = 10b ^ 3
  var bitwiseXOR115 = 10b ^ 3L
  var bitwiseXOR116 = 10b ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'FLOAT'
  var bitwiseXOR117 = 10b ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'DOUBLE'
  var bitwiseXOR119 = 10b ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var bitwiseXOR1191 = 10b ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.OBJECT'
  var bitwiseXOR120 = 10b ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var bitwiseXOR121 = 10b ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'NULL'
  var bitwiseXOR122 = 10b ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR123 = 10b ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR124 = 10b ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BYTE', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseXOR210 = 10s ^ false      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'
  var bitwiseXOR211 = 10s ^ 'c'
  var bitwiseXOR212 = 10s ^ 3b
  var bitwiseXOR213 = 10s ^ 3s
  var bitwiseXOR214 = 10s ^ 3
  var bitwiseXOR215 = 10s ^ 3L
  var bitwiseXOR216 = 10s ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'FLOAT'
  var bitwiseXOR217 = 10s ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'DOUBLE'
  var bitwiseXOR219 = 10s ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var bitwiseXOR2191 = 10s ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.OBJECT'
  var bitwiseXOR220 = 10s ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var bitwiseXOR221 = 10s ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'NULL'
  var bitwiseXOR222 = 10s ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR223 = 10s ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR224 = 10s ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseXOR310 = 10 ^ false      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'
  var bitwiseXOR311 = 10 ^ 'c'
  var bitwiseXOR312 = 10 ^ 3b
  var bitwiseXOR313 = 10 ^ 3s
  var bitwiseXOR314 = 10 ^ 3
  var bitwiseXOR315 = 10 ^ 3L
  var bitwiseXOR316 = 10 ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'FLOAT'
  var bitwiseXOR317 = 10 ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var bitwiseXOR319 = 10 ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var bitwiseXOR3191 = 10 ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.OBJECT'
  var bitwiseXOR320 = 10 ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var bitwiseXOR321 = 10 ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'NULL'
  var bitwiseXOR322 = 10 ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR323 = 10 ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR324 = 10 ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseXOR410 = 10L ^ false      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'
  var bitwiseXOR411 = 10L ^ 'c'
  var bitwiseXOR412 = 10L ^ 3b
  var bitwiseXOR413 = 10L ^ 3s
  var bitwiseXOR414 = 10L ^ 3
  var bitwiseXOR415 = 10L ^ 3L
  var bitwiseXOR416 = 10L ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'FLOAT'
  var bitwiseXOR417 = 10L ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'DOUBLE'
  var bitwiseXOR419 = 10L ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var bitwiseXOR4191 = 10L ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.OBJECT'
  var bitwiseXOR420 = 10L ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var bitwiseXOR421 = 10L ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'NULL'
  var bitwiseXOR422 = 10L ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR423 = 10L ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR424 = 10L ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'LONG', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseXOR510 = 42.5f ^ false      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'
  var bitwiseXOR511 = 42.5f ^ 'c'      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'CHAR'
  var bitwiseXOR512 = 42.5f ^ 3b      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'BYTE'
  var bitwiseXOR513 = 42.5f ^ 3s      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'SHORT'
  var bitwiseXOR514 = 42.5f ^ 3      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'INT'
  var bitwiseXOR515 = 42.5f ^ 3L      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'LONG'
  var bitwiseXOR516 = 42.5f ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'FLOAT'
  var bitwiseXOR517 = 42.5f ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'DOUBLE'
  var bitwiseXOR519 = 42.5f ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var bitwiseXOR5191 = 42.5f ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.OBJECT'
  var bitwiseXOR520 = 42.5f ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var bitwiseXOR521 = 42.5f ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'NULL'
  var bitwiseXOR522 = 42.5f ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR523 = 42.5f ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR524 = 42.5f ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseXOR610 = 42.55 ^ false      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var bitwiseXOR611 = 42.55 ^ 'c'      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'CHAR'
  var bitwiseXOR612 = 42.55 ^ 3b      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'BYTE'
  var bitwiseXOR613 = 42.55 ^ 3s      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'SHORT'
  var bitwiseXOR614 = 42.55 ^ 3      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'INT'
  var bitwiseXOR615 = 42.55 ^ 3L      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'LONG'
  var bitwiseXOR616 = 42.55 ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'FLOAT'
  var bitwiseXOR617 = 42.55 ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'DOUBLE'
  var bitwiseXOR619 = 42.55 ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var bitwiseXOR6191 = 42.55 ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.OBJECT'
  var bitwiseXOR620 = 42.55 ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var bitwiseXOR621 = 42.55 ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'NULL'
  var bitwiseXOR622 = 42.55 ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR623 = 42.55 ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR624 = 42.55 ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.MATH.BIGDECIMAL'

  //IDE-585 Parser does not show error. OS Gosu does
  var bitwiseXOR710 = true ^ true
  var bitwiseXOR711 = true ^ 'c'      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'CHAR'
  var bitwiseXOR712 = true ^ 3b      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'BYTE'
  var bitwiseXOR713 = true ^ 3s      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'SHORT'
  var bitwiseXOR714 = true ^ 3      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var bitwiseXOR715 = true ^ 3L      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'LONG'
  var bitwiseXOR717 = true ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'FLOAT'
  var bitwiseXOR7171 = true ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'DOUBLE'
  var bitwiseXOR719 = true ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.STRING'
  var bitwiseXOR7191 = true ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.OBJECT'
  var bitwiseXOR720 = true ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.DATE'
  var bitwiseXOR721 = true ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'NULL'
  var bitwiseXOR722 = true ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR723 = true ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR724 = true ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseXOR810 = "string" ^ true      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BOOLEAN'
  var bitwiseXOR811 = "string" ^ 'c'      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
  var bitwiseXOR812 = "string" ^ 3b      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var bitwiseXOR813 = "string" ^ 3s      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var bitwiseXOR814 = "string" ^ 3      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var bitwiseXOR815 = "string" ^ 3L      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var bitwiseXOR817 = "string" ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var bitwiseXOR8171 = "string" ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
  var bitwiseXOR819 = "string" ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var bitwiseXOR8191 = "string" ^ new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.OBJECT'
  var bitwiseXOR820 = "string" ^ date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var bitwiseXOR821 = "string" ^ null      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'NULL'
  var bitwiseXOR822 = "string" ^ {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR823 = "string" ^ BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR824 = "string" ^ BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseXOR910 = BigInteger.ONE ^ true      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'
  var bitwiseXOR911 = BigInteger.ONE ^ 'c'      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'CHAR'
  var bitwiseXOR912 = BigInteger.ONE ^ 3b      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BYTE'
  var bitwiseXOR913 = BigInteger.ONE ^ 3s      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'SHORT'
  var bitwiseXOR914 = BigInteger.ONE ^ 3      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'INT'
  var bitwiseXOR915 = BigInteger.ONE ^ 3L      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'LONG'
  var bitwiseXOR917 = BigInteger.ONE ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'FLOAT'
  var bitwiseXOR9171= BigInteger.ONE ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'DOUBLE'
  var bitwiseXOR919 = BigInteger.ONE ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var bitwiseXOR9191= BigInteger.ONE ^  new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.OBJECT'
  var bitwiseXOR920 = BigInteger.ONE ^  date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  var bitwiseXOR921 = BigInteger.ONE ^  null  //PL-31628 - issue in Parser logged      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'NULL'
  var bitwiseXOR922 = BigInteger.ONE ^  {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR923 = BigInteger.ONE ^  BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR924 = BigInteger.ONE ^  BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseXOR1010 = BigDecimal.ONE ^ true      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'
  var bitwiseXOR1011 = BigDecimal.ONE ^ 'c'      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'CHAR'
  var bitwiseXOR1012 = BigDecimal.ONE ^ 3b      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BYTE'
  var bitwiseXOR1013 = BigDecimal.ONE ^ 3s      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'SHORT'
  var bitwiseXOR1014 = BigDecimal.ONE ^ 3      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'INT'
  var bitwiseXOR1015 = BigDecimal.ONE ^ 3L      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'LONG'
  var bitwiseXOR1017 = BigDecimal.ONE ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'FLOAT'
  var bitwiseXOR10171= BigDecimal.ONE ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'DOUBLE'
  var bitwiseXOR1019 = BigDecimal.ONE ^ "string"      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var bitwiseXOR10191= BigDecimal.ONE ^  new Object()      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.OBJECT'
  var bitwiseXOR1020 = BigDecimal.ONE ^  date1      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var bitwiseXOR1021 = BigDecimal.ONE ^  null  //PL-31628 - issue in Parser logged      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'NULL'
  var bitwiseXOR1022 = BigDecimal.ONE ^  {1, 2, 3}      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseXOR1023 = BigDecimal.ONE ^  BigInteger.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.MATH.BIGINTEGER'
  var bitwiseXOR1024 = BigDecimal.ONE ^  BigDecimal.ONE      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseXOR1210 = 1b ^ !2      //## issuekeys: OPERATOR '!' CANNOT BE APPLIED TO 'INT'
  var bitwiseXOR1211 = 42 ^ ~32
  var bitwiseXOR1212 = 42 ^ 3b ^ 42
  var bitwiseXOR1213 = 42.5f ^ 3s ^ 3s      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'FLOAT', 'SHORT'
  var bitwiseXOR1214 = 42.5 ^^ true ^ 3      //## issuekeys: UNEXPECTED TOKEN: ^
  var bitwiseXOR1215 = 42 ^ -3L
  var bitwiseXOR1217 = -42s ^ 3.5f      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'SHORT', 'FLOAT'
  var bitwiseXOR12171 = 42 ^ 10 ^ 3.5      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var bitwiseXOR1219 = 42.5 ^ 10 ^ 6      //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'DOUBLE', 'INT'

}