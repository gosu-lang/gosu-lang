package gw.specContrib.operators.arithmetic.bitwise

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_BitwiseAND {
  var date1 : Date

  var bitwiseAnd010 = 'c'& false      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'
  //IDE-334, IDE-1196
  var bitwiseAnd011 = 'c' & 'c'
  var bitwiseAnd012 = 'c' & 3b
  var bitwiseAnd013 = 'c' & 3s
  var bitwiseAnd014 = 'c' & 3
  var bitwiseAnd015 = 'c' & 3L
  var bitwiseAnd016 = 'c' & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'FLOAT'
  var bitwiseAnd017 = 'c' & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'DOUBLE'
  var bitwiseAnd019 = 'c' & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
  var bitwiseAnd0191 = 'c' & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.OBJECT'
  var bitwiseAnd020 = 'c' & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var bitwiseAnd021 = 'c' & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'NULL'
  var bitwiseAnd022 = 'c' & {1,2,3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd023 = 'c' & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd024 = 'c' & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'CHAR', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseAnd110 = 10b & false      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'
  var bitwiseAnd111 = 10b&'c'
  var bitwiseAnd112 = 10b & 3b
  var bitwiseAnd113 = 10b & 3s
  var bitwiseAnd114 = 10b & 3
  var bitwiseAnd115 = 10b & 3L
  var bitwiseAnd116 = 10b & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'FLOAT'
  var bitwiseAnd117 = 10b & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'DOUBLE'
  var bitwiseAnd119 = 10b & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var bitwiseAnd1191 = 10b & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.OBJECT'
  var bitwiseAnd120 = 10b & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var bitwiseAnd121 = 10b & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'NULL'
  var bitwiseAnd122 = 10b & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd123 = 10b & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd124 = 10b & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BYTE', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseAnd210 = 10s&false      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'
  var bitwiseAnd211 = 10s & 'c'
  var bitwiseAnd212 = 10s & 3b
  var bitwiseAnd213 = 10s & 3s
  var bitwiseAnd214 = 10s & 3
  var bitwiseAnd215 = 10s & 3L
  var bitwiseAnd216 = 10s & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'FLOAT'
  var bitwiseAnd217 = 10s & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'DOUBLE'
  var bitwiseAnd219 = 10s & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var bitwiseAnd2191 = 10s & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.OBJECT'
  var bitwiseAnd220 = 10s & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var bitwiseAnd221 = 10s & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'NULL'
  var bitwiseAnd222 = 10s & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd223 = 10s & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd224 = 10s & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseAnd310 = 10 & false      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'
  var bitwiseAnd311 = 10 & 'c'
  var bitwiseAnd312 = 10 & 3b
  var bitwiseAnd313 = 10 & 3s
  var bitwiseAnd314 = 10 & 3
  var bitwiseAnd315 = 10 & 3L
  var bitwiseAnd316 = 10 & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'FLOAT'
  var bitwiseAnd317 = 10 & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var bitwiseAnd319 = 10 & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var bitwiseAnd3191 = 10 & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.OBJECT'
  var bitwiseAnd320 = 10 & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var bitwiseAnd321 = 10 & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'NULL'
  var bitwiseAnd322 = 10 & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd323 = 10 & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd324 = 10 & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseAnd410 = 10L & false      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'
  var bitwiseAnd411 = 10L & 'c'
  var bitwiseAnd412 = 10L & 3b
  var bitwiseAnd413 = 10L & 3s
  var bitwiseAnd414 = 10L & 3
  var bitwiseAnd415 = 10L & 3L
  var bitwiseAnd416 = 10L & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'FLOAT'
  var bitwiseAnd417 = 10L & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'DOUBLE'
  var bitwiseAnd419 = 10L & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var bitwiseAnd4191 = 10L & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.OBJECT'
  var bitwiseAnd420 = 10L & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var bitwiseAnd421 = 10L & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'NULL'
  var bitwiseAnd422 = 10L & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd423 = 10L & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd424 = 10L & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'LONG', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseAnd510 = 42.5f & false      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'
  var bitwiseAnd511 = 42.5f & 'c'      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'CHAR'
  var bitwiseAnd512 = 42.5f & 3b      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'BYTE'
  var bitwiseAnd513 = 42.5f & 3s      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'SHORT'
  var bitwiseAnd514 = 42.5f & 3      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'INT'
  var bitwiseAnd515 = 42.5f & 3L      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'LONG'
  var bitwiseAnd516 = 42.5f & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'FLOAT'
  var bitwiseAnd517 = 42.5f & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'DOUBLE'
  var bitwiseAnd519 = 42.5f & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var bitwiseAnd5191 = 42.5f & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.OBJECT'
  var bitwiseAnd520 = 42.5f & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var bitwiseAnd521 = 42.5f & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'NULL'
  var bitwiseAnd522 = 42.5f & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd523 = 42.5f & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd524 = 42.5f & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseAnd610 = 42.55 & false      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var bitwiseAnd611 = 42.55 & 'c'      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'CHAR'
  var bitwiseAnd612 = 42.55 & 3b      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'BYTE'
  var bitwiseAnd613 = 42.55 & 3s      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'SHORT'
  var bitwiseAnd614 = 42.55 & 3      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'INT'
  var bitwiseAnd615 = 42.55 & 3L      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'LONG'
  var bitwiseAnd616 = 42.55 & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'FLOAT'
  var bitwiseAnd617 = 42.55 & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'DOUBLE'
  var bitwiseAnd619 = 42.55 & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var bitwiseAnd6191 = 42.55 & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.OBJECT'
  var bitwiseAnd620 = 42.55 & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var bitwiseAnd621 = 42.55 & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'NULL'
  var bitwiseAnd622 = 42.55 & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd623 = 42.55 & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd624 = 42.55 & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.MATH.BIGDECIMAL'

  //IDE-585 Parser does not show error. OS Gosu does
  var bitwiseAnd710 = true & false    //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var bitwiseAnd7101 = true | false   //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var bitwiseAnd7102 = true ^ false   //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'

  var bitwiseAnd711 = true & 'c'      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'CHAR'
  var bitwiseAnd712 = true & 3b      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'BYTE'
  var bitwiseAnd713 = true & 3s      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'SHORT'
  var bitwiseAnd714 = true & 3      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var bitwiseAnd715 = true & 3L      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'LONG'
  var bitwiseAnd717 = true & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'FLOAT'
  var bitwiseAnd7171 = true & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'DOUBLE'
  var bitwiseAnd719 = true & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.STRING'
  var bitwiseAnd7191 = true & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.OBJECT'
  var bitwiseAnd720 = true & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.DATE'
  var bitwiseAnd721 = true & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'NULL'
  var bitwiseAnd722 = true & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd723 = true & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd724 = true & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseAnd810 = "string" & true      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BOOLEAN'
  var bitwiseAnd811 = "string" & 'c'      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
  var bitwiseAnd812 = "string" & 3b      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var bitwiseAnd813 = "string" & 3s      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var bitwiseAnd814 = "string" & 3      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var bitwiseAnd815 = "string" & 3L      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var bitwiseAnd817 = "string" & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var bitwiseAnd8171 = "string" & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
  var bitwiseAnd819 = "string" & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var bitwiseAnd8191 = "string" & new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.OBJECT'
  var bitwiseAnd820 = "string" & date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var bitwiseAnd821 = "string" & null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'NULL'
  var bitwiseAnd822 = "string" & {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd823 = "string" & BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd824 = "string" & BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'

  var bitwiseAnd910 = BigInteger.ONE & true      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'
  var bitwiseAnd911 = BigInteger.ONE & 'c'      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'CHAR'
  var bitwiseAnd912 = BigInteger.ONE & 3b      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BYTE'
  var bitwiseAnd913 = BigInteger.ONE & 3s      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'SHORT'
  var bitwiseAnd914 = BigInteger.ONE & 3      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'INT'
  var bitwiseAnd915 = BigInteger.ONE & 3L      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'LONG'
  var bitwiseAnd917 = BigInteger.ONE & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'FLOAT'
  var bitwiseAnd9171= BigInteger.ONE & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'DOUBLE'
  var bitwiseAnd919 = BigInteger.ONE & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var bitwiseAnd9191= BigInteger.ONE &  new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.OBJECT'
  var bitwiseAnd920 = BigInteger.ONE &  date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  var bitwiseAnd921 = BigInteger.ONE &  null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'NULL'
  var bitwiseAnd922 = BigInteger.ONE &  {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd923 = BigInteger.ONE &  BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd924 = BigInteger.ONE &  BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseAnd1010 = BigDecimal.ONE & true      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'
  var bitwiseAnd1011 = BigDecimal.ONE & 'c'      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'CHAR'
  var bitwiseAnd1012 = BigDecimal.ONE & 3b      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BYTE'
  var bitwiseAnd1013 = BigDecimal.ONE & 3s      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'SHORT'
  var bitwiseAnd1014 = BigDecimal.ONE & 3      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'INT'
  var bitwiseAnd1015 = BigDecimal.ONE & 3L      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'LONG'
  var bitwiseAnd1017 = BigDecimal.ONE & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'FLOAT'
  var bitwiseAnd10171= BigDecimal.ONE & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'DOUBLE'
  var bitwiseAnd1019 = BigDecimal.ONE & "string"      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var bitwiseAnd10191= BigDecimal.ONE &  new Object()      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.OBJECT'
  var bitwiseAnd1020 = BigDecimal.ONE &  date1      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var bitwiseAnd1021 = BigDecimal.ONE &  null      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'NULL'
  var bitwiseAnd1022 = BigDecimal.ONE &  {1, 2, 3}      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var bitwiseAnd1023 = BigDecimal.ONE &  BigInteger.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.MATH.BIGINTEGER'
  var bitwiseAnd1024 = BigDecimal.ONE &  BigDecimal.ONE      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.MATH.BIGDECIMAL'


  var bitwiseAnd1110 = 1b & !2      //## issuekeys: OPERATOR '!' CANNOT BE APPLIED TO 'INT'
  var bitwiseAnd1111 = 42 & ~32
  var bitwiseAnd1112 = 42 & 3b & 42
  var bitwiseAnd1113 = 42.5f & 3s & 3s      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'SHORT'
  var bitwiseAnd1114 = 42.5 && true & 3      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var bitwiseAnd1115 = 42 & -3L
  var bitwiseAnd1117 = -42s & 3.5f      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'SHORT', 'FLOAT'
  var bitwiseAnd11171 = 42 | 10 & 3.5      //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var bitwiseAnd1119 = 42.5 | 10 & 6      //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'INT'

}


