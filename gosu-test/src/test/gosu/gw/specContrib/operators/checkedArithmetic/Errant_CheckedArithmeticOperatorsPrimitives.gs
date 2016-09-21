package gw.specContrib.operators.checkedArithmetic

uses java.math.BigDecimal
uses java.math.BigInteger


class Errant_CheckedArithmeticOperatorsPrimitives {
  var date1 = new Date()
  var date2 = new Date()


  //////////////////////////////Addition.//////////////////////////////
  var addition010 = 'c' !+ false                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'
  var addition011 = 'c' !+ 'c'
  var addition012 = 'c' !+ 3b
  var addition013 = 'c' !+ 3s
  var addition014 = 'c' !+ 3
  var addition015 = 'c' !+ 3L
  var addition016 = 'c' !+ 3.5f
  var addition017 = 'c' !+ 3.5
  //IDE-3045 need to be fixed in OS Gosu
  var addition019 = 'c' !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
  var addition0191 = 'c' !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.OBJECT'
  var addition020 = 'c' !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var addition021 = 'c' !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'CHAR', 'NULL'
  var addition022 = 'c' !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition023 = 'c' !+ BigInteger.ONE
  var addition024 = 'c' !+ BigDecimal.ONE


  var addition110 = 10b !+ false                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'
  var addition111 = 10b !+ 'c'
  var addition112 = 10b !+ 3b
  var addition113 = 10b !+ 3s
  var addition114 = 10b !+ 3
  var addition115 = 10b !+ 3L
  var addition116 = 10b !+ 3.5f
  var addition117 = 10b !+ 3.5
  var addition119 = 10b !+ "string"             //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var addition1191 = 10b !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.OBJECT'
  var addition120 = 10b !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var addition121 = 10b !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BYTE', 'NULL'
  var addition122 = 10b !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition123 = 10b !+ BigInteger.ONE
  var addition124 = 10b !+ BigDecimal.ONE

  var addition210 = 10s !+ false                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'
  var addition211 = 10s !+ 'c'
  var addition212 = 10s !+ 3b
  var addition213 = 10s !+ 3s
  var addition214 = 10s !+ 3
  var addition215 = 10s !+ 3L
  var addition216 = 10s !+ 3.5f
  var addition217 = 10s !+ 3.5
  var addition219 = 10s !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var addition2191 = 10s !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.OBJECT'
  var addition220 = 10s !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var addition221 = 10s !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'SHORT', 'NULL'
  var addition222 = 10s !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition223 = 10s !+ BigInteger.ONE
  var addition224 = 10s !+ BigDecimal.ONE

  var addition310 = 10 !+ false                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'
  var addition311 = 10 !+ 'c'
  var addition312 = 10 !+ 3b
  var addition313 = 10 !+ 3s
  var addition314 = 10 !+ 3
  var addition315 = 10 !+ 3L
  var addition316 = 10 !+ 3.5f
  var addition317 = 10 !+ 3.5
  var addition319 = 10 !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var addition3191 = 10 !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.OBJECT'
  var addition320 = 10 !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var addition321 = 10 !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'INT', 'NULL'
  var addition322 = 10 !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition323 = 10 !+ BigInteger.ONE
  var addition324 = 10 !+ BigDecimal.ONE

  var addition410 = 10L !+ false                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'
  var addition411 = 10L !+ 'c'
  var addition412 = 10L !+ 3b
  var addition413 = 10L !+ 3s
  var addition414 = 10L !+ 3
  var addition415 = 10L !+ 3L
  var addition416 = 10L !+ 3.5f
  var addition417 = 10L !+ 3.5
  var addition419 = 10L !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var addition4191 = 10L !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.OBJECT'
  var addition420 = 10L !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var addition421 = 10L !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'LONG', 'NULL'
  var addition422 = 10L !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition423 = 10L !+ BigInteger.ONE
  var addition424 = 10L !+ BigDecimal.ONE


  var addition510 = 42.5f !+ false                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'
  var addition511 = 42.5f !+ 'c'
  var addition512 = 42.5f !+ 3b
  var addition513 = 42.5f !+ 3s
  var addition514 = 42.5f !+ 3
  var addition515 = 42.5f !+ 3L
  var addition516 = 42.5f !+ 3.5f
  var addition517 = 42.5f !+ 3.5
  var addition519 = 42.5f !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var addition5191 = 42.5f !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.OBJECT'
  var addition520 = 42.5f !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var addition521 = 42.5f !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'FLOAT', 'NULL'
  var addition522 = 42.5f !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition523 = 42.5f !+ BigInteger.ONE
  var addition524 = 42.5f !+ BigDecimal.ONE

  var addition610 = 42.55 !+ false                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var addition611 = 42.55 !+ 'c'
  var addition612 = 42.55 !+ 3b
  var addition613 = 42.55 !+ 3s
  var addition614 = 42.55 !+ 3
  var addition615 = 42.55 !+ 3L
  var addition616 = 42.55 !+ 3.5f
  var addition617 = 42.55 !+ 3.5
  var addition619 = 42.55 !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var addition6191 = 42.55 !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.OBJECT'
  var addition620 = 42.55 !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var addition621 = 42.55 !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'DOUBLE', 'NULL'
  var addition622 = 42.55 !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition623 = 42.55 !+ BigInteger.ONE
  var addition624 = 42.55 !+ BigDecimal.ONE

  var addition710 = true !+ true                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var addition711 = true !+ 'c'                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'CHAR'
  var addition712 = true !+ 3b                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'BYTE'
  var addition713 = true !+ 3s                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'SHORT'
  var addition714 = true !+ 3                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var addition715 = true !+ 3L                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'LONG'
  var addition717 = true !+ 3.5f                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'FLOAT'
  var addition7171 = true !+ 3.5                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'DOUBLE'
  var addition719 = true !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.STRING'
  var addition7191 = true !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.OBJECT'
  var addition720 = true !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.DATE'
  var addition721 = true !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'NULL'
  var addition722 = true !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition723 = true !+ BigInteger.ONE                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGINTEGER'
  var addition724 = true !+ BigDecimal.ONE                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGDECIMAL'

  var addition810 = "string" !+ true      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BOOLEAN'
  var addition811 = "string" !+ 'c'      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
  var addition812 = "string" !+ 3b      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var addition813 = "string" !+ 3s      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var addition814 = "string" !+ 3      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var addition815 = "string" !+ 3L      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var addition817 = "string" !+ 3.5f      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var addition8171 = 3.5 !+ "string" !+ 3.5      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var addition819 = "string" !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var addition8191 = "string" !+ new Object()      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.OBJECT'
  var addition820 = "string" !+ date1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var addition821 = "string" !+ null      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'NULL'
  var addition822 = "string" !+ {1, 2, 3}      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition823 = "string" !+ BigInteger.ONE      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var addition824 = "string" !+ BigDecimal.ONE      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'

  var addition910 = BigInteger.ONE !+ true                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'
  var addition911 = BigInteger.ONE !+ 'c'
  var addition9111 = addition911 !+ 42
  var addition912 = BigInteger.ONE !+ 3b
  var addition913 = BigInteger.ONE !+ 3s
  var addition914 = BigInteger.ONE !+ 3
  var addition915 = BigInteger.ONE !+ 3L
  var addition917 = BigInteger.ONE !+ 3.5f
  var addition9171 = BigInteger.ONE !+ 3.5
  var addition919 = BigInteger.ONE !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var addition9191 = BigInteger.ONE !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.OBJECT'
  var addition920 = BigInteger.ONE !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  var addition921 = BigInteger.ONE !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'NULL'
  var addition922 = BigInteger.ONE !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition923 = BigInteger.ONE !+ BigInteger.ONE
  var addition924 = BigInteger.ONE !+ BigDecimal.ONE


  var addition1010 = BigDecimal.ONE !+ true                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'
  var addition1011 = BigDecimal.ONE !+ 'c'
  var addition1012 = BigDecimal.ONE !+ 3b
  var addition1013 = BigDecimal.ONE !+ 3s
  var addition1014 = BigDecimal.ONE !+ 3
  var addition1015 = BigDecimal.ONE !+ 3L
  var addition1017 = BigDecimal.ONE !+ 3.5f
  var addition10171 = BigDecimal.ONE !+ 3.5
  var addition1019 = BigDecimal.ONE !+ "string"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var addition10191 = BigDecimal.ONE !+ new Object()                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.OBJECT'
  var addition1020 = BigDecimal.ONE !+ date1                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var addition1021 = BigDecimal.ONE !+ null                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'NULL'
  var addition1022 = BigDecimal.ONE !+ {1, 2, 3}                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var addition1023 = BigDecimal.ONE !+ BigInteger.ONE
  var addition1024 = BigDecimal.ONE !+ BigDecimal.ONE

  var addition1910 = 1b !+ !2                        //## issuekeys: OPERATOR '!' CANNOT BE APPLIED TO 'INT'
  var addition1911 = 42 !+ ~32
  var addition1912 = 42 !+ 3b & 42
  var addition1913 = 42.5f & 3s !+ 3s /4                        //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'FLOAT', 'INT'
  var addition1914 = 42.5 && true !+ 3                        //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var addition1915 = 42 !+ !-3L
  var addition1917 = !-42s !+ 3.5f
  var addition1918 = 42 | 10 !+ 3.5                        //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var addition1919 = 42.5 | 10 !+ 3.5                        //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'DOUBLE'
  var addition1920 = 10 !+ 3.5
  var addition1921 = 10 !+ 3.5

  //////////////////////////////Addition.//////////////////////////////


  //////////////////////////////subtraction//////////////////////////////
  var Subtraction010 = 'c' !- false                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'
  var Subtraction011 = 'c' !- 'c'
  var Subtraction012 = 'c' !- 3b
  var Subtraction013 = 'c' !- 3s
  var Subtraction014 = 'c' !- 3
  var Subtraction015 = 'c' !- 3L
  var Subtraction016 = 'c' !- 3.5f
  var Subtraction017 = 'c' !- 3.5
  var Subtraction019 = 'c' !- "string"                       //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
  var Subtraction0191 = 'c' !- new Object()                  //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.OBJECT'
  var Subtraction020 = 'c' !- date1                          //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var Subtraction021 = 'c' !- null                           //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'CHAR', 'NULL'
  var Subtraction022 = 'c' !- {1, 2, 3}                      //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction023 = 'c' !- BigInteger.ONE
  var Subtraction024 = 'c' !- BigDecimal.ONE

  var Subtraction110 = 10b !- false                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'
  var Subtraction111 = 10b !- 'c'
  var Subtraction112 = 10b !- 3b
  var Subtraction113 = 10b !- 3s
  var Subtraction114 = 10b !- 3
  var Subtraction115 = 10b !- 3L
  var Subtraction116 = 10b !- 3.5f
  var Subtraction117 = 10b !- 3.5
  var Subtraction119 = 10b !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var Subtraction1191 = 10b !- new Object()                   //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.OBJECT'
  var Subtraction120 = 10b !- date1                           //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var Subtraction121 = 10b !- null                            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BYTE', 'NULL'
  var Subtraction122 = 10b !- {1, 2, 3}                       //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction123 = 10b !- BigInteger.ONE
  var Subtraction124 = 10b !- BigDecimal.ONE

  var Subtraction210 = 10s !- false                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'
  var Subtraction211 = 10s !- 'c'
  var Subtraction212 = 10s !- 3b
  var Subtraction213 = 10s !- 3s
  var Subtraction214 = 10s !- 3
  var Subtraction215 = 10s !- 3L
  var Subtraction216 = 10s !- 3.5f
  var Subtraction217 = 10s !- 3.5
  var Subtraction219 = 10s !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var Subtraction2191 = 10s !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.OBJECT'
  var Subtraction220 = 10s !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var Subtraction221 = 10s !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'SHORT', 'NULL'
  var Subtraction222 = 10s !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction223 = 10s !- BigInteger.ONE
  var Subtraction224 = 10s !- BigDecimal.ONE

  var Subtraction310 = 10 !- false                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'
  var Subtraction311 = 10 !- 'c'
  var Subtraction312 = 10 !- 3b
  var Subtraction313 = 10 !- 3s
  var Subtraction314 = 10 !- 3
  var Subtraction315 = 10 !- 3L
  var Subtraction316 = 10 !- 3.5f
  var Subtraction317 = 10 !- 3.5
  var Subtraction319 = 10 !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var Subtraction3191 = 10 !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.OBJECT'
  var Subtraction320 = 10 !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var Subtraction321 = 10 !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'INT', 'NULL'
  var Subtraction322 = 10 !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction323 = 10 !- BigInteger.ONE
  var Subtraction324 = 10 !- BigDecimal.ONE

  var Subtraction410 = 10L !- false                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'
  var Subtraction411 = 10L !- 'c'
  var Subtraction412 = 10L !- 3b
  var Subtraction413 = 10L !- 3s
  var Subtraction414 = 10L !- 3
  var Subtraction415 = 10L !- 3L
  var Subtraction416 = 10L !- 3.5f
  var Subtraction417 = 10L !- 3.5
  var Subtraction419 = 10L !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var Subtraction4191 = 10L !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.OBJECT'
  var Subtraction420 = 10L !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var Subtraction421 = 10L !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'LONG', 'NULL'
  var Subtraction422 = 10L !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction423 = 10L !- BigInteger.ONE
  var Subtraction424 = 10L !- BigDecimal.ONE

  var Subtraction510 = 42.5f !- false                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'
  var Subtraction511 = 42.5f !- 'c'
  var Subtraction512 = 42.5f !- 3b
  var Subtraction513 = 42.5f !- 3s
  var Subtraction514 = 42.5f !- 3
  var Subtraction515 = 42.5f !- 3L
  var Subtraction516 = 42.5f !- 3.5f
  var Subtraction517 = 42.5f !- 3.5
  var Subtraction519 = 42.5f !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var Subtraction5191 = 42.5f !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.OBJECT'
  var Subtraction520 = 42.5f !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var Subtraction521 = 42.5f !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'FLOAT', 'NULL'
  var Subtraction522 = 42.5f !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction523 = 42.5f !- BigInteger.ONE
  var Subtraction524 = 42.5f !- BigDecimal.ONE

  var Subtraction610 = 42.55 !- false                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var Subtraction611 = 42.55 !- 'c'
  var Subtraction612 = 42.55 !- 3b
  var Subtraction613 = 42.55 !- 3s
  var Subtraction614 = 42.55 !- 3
  var Subtraction615 = 42.55 !- 3L
  var Subtraction616 = 42.55 !- 3.5f
  var Subtraction617 = 42.55 !- 3.5
  var Subtraction619 = 42.55 !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var Subtraction6191 = 42.55 !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.OBJECT'
  var Subtraction620 = 42.55 !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var Subtraction621 = 42.55 !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'DOUBLE', 'NULL'
  var Subtraction622 = 42.55 !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction623 = 42.55 !- BigInteger.ONE
  var Subtraction624 = 42.55 !- BigDecimal.ONE

  var Subtraction710 = true !- true                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var Subtraction711 = true !- 'c'                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'CHAR'
  var Subtraction712 = true !- 3b                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'BYTE'
  var Subtraction713 = true !- 3s                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'SHORT'
  var Subtraction714 = true !- 3                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var Subtraction715 = true !- 3L                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'LONG'
  var Subtraction717 = true !- 3.5f                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'FLOAT'
  var Subtraction7171 = true !- 3.5                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'DOUBLE'
  var Subtraction719 = true !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.STRING'
  var Subtraction7191 = true !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.OBJECT'
  var Subtraction720 = true !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.DATE'
  var Subtraction721 = true !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'NULL'
  var Subtraction722 = true !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction723 = true !- BigInteger.ONE                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGINTEGER'
  var Subtraction724 = true !- BigDecimal.ONE                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGDECIMAL'

  var Subtraction810 = "string" !- true                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BOOLEAN'
  var Subtraction811 = "string" !- 'c'                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
  var Subtraction812 = "string" !- 3b                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var Subtraction813 = "string" !- 3s                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var Subtraction814 = "string" !- 3                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var Subtraction815 = "string" !- 3L                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var Subtraction817 = "string" !- 3.5f                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var Subtraction8171 = "string" !- 3.5                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
  var Subtraction819 = "string" !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var Subtraction8191 = "string" !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.OBJECT'
  var Subtraction820 = "string" !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var Subtraction821 = "string" !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'NULL'
  var Subtraction822 = "string" !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction823 = "string" !- BigInteger.ONE                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var Subtraction824 = "string" !- BigDecimal.ONE                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'

  var Subtraction910 = BigInteger.ONE !- true                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'
  var Subtraction911 = BigInteger.ONE !- 'c'
  var Subtraction912 = BigInteger.ONE !- 3b
  var Subtraction913 = BigInteger.ONE !- 3s
  var Subtraction914 = BigInteger.ONE !- 3
  var Subtraction915 = BigInteger.ONE !- 3L
  var Subtraction917 = BigInteger.ONE !- 3.5f
  var Subtraction9171 = BigInteger.ONE !- 3.5
  var Subtraction919 = BigInteger.ONE !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var Subtraction9191 = BigInteger.ONE !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.OBJECT'
  var Subtraction920 = BigInteger.ONE !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  var Subtraction921 = BigInteger.ONE !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'NULL'
  var Subtraction922 = BigInteger.ONE !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction923 = BigInteger.ONE !- BigInteger.ONE
  var Subtraction924 = BigInteger.ONE !- BigDecimal.ONE


  var Subtraction1010 = BigDecimal.ONE !- true                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'
  var Subtraction1011 = BigDecimal.ONE !- 'c'
  var Subtraction1012 = BigDecimal.ONE !- 3b
  var Subtraction1013 = BigDecimal.ONE !- 3s
  var Subtraction1014 = BigDecimal.ONE !- 3
  var Subtraction1015 = BigDecimal.ONE !- 3L
  var Subtraction1017 = BigDecimal.ONE !- 3.5f
  var Subtraction10171 = BigDecimal.ONE !- 3.5
  var Subtraction1019 = BigDecimal.ONE !- "string"                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var Subtraction10191 = BigDecimal.ONE !- new Object()                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.OBJECT'
  var Subtraction1020 = BigDecimal.ONE !- date1                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var Subtraction1021 = BigDecimal.ONE !- null                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'NULL'
  var Subtraction1022 = BigDecimal.ONE !- {1, 2, 3}                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var Subtraction1023 = BigDecimal.ONE !- BigInteger.ONE
  var Subtraction1024 = BigDecimal.ONE !- BigDecimal.ONE


  var Subtraction1910 = 1b !- !2                        //## issuekeys: OPERATOR '!' CANNOT BE APPLIED TO 'INT'
  var Subtraction1911 = 42 !- ~32
  var Subtraction1912 = 42 !- 3b !- 42
  var Subtraction1913 = 42.5f !- 3s !- 3s
  var Subtraction1914 = 42.5 !- true !- 3                        //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var Subtraction1915 = 42 !- !-3L
  var Subtraction1916 = !-42s !- 3.5f
  var Subtraction1917 = 42 | 10 !- 3.5                        //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'INT', 'DOUBLE'
  var Subtraction1918 = 42.5 | 10 !- 6                        //## issuekeys: OPERATOR '|' CANNOT BE APPLIED TO 'DOUBLE', 'INT'
/////////////////////Subtraction ENDS////////////////////


  //////////////////////////////BITWISE XOR//////////////////////////////
  var multiplication010 = 'c' !* false                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'
  var multiplication011 = 'c' !* 'c'
  var multiplication012 = 'c' !* 3b
  var multiplication013 = 'c' !* 3s
  var multiplication014 = 'c' !* 3
  var multiplication015 = 'c' !* 3L
  var multiplication016 = 'c' !* 3.5f
  var multiplication017 = 'c' !* 3.5
  var multiplication019 = 'c' !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.STRING'
  var multiplication0191 = 'c' !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'CHAR', 'JAVA.LANG.OBJECT'
  var multiplication020 = 'c' !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.DATE'
  var multiplication021 = 'c' !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'CHAR', 'NULL'
  var multiplication022 = 'c' !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'CHAR', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication023 = 'c' !* BigInteger.ONE
  var multiplication024 = 'c' !* BigDecimal.ONE


  var multiplication110 = 10b !* false                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'
  var multiplication111 = 10b !* 'c'
  var multiplication112 = 10b !* 3b
  var multiplication113 = 10b !* 3s
  var multiplication114 = 10b !* 3
  var multiplication115 = 10b !* 3L
  var multiplication116 = 10b !* 3.5f
  var multiplication117 = 10b !* 3.5
  var multiplication119 = 10b !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.STRING'
  var multiplication1191 = 10b !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BYTE', 'JAVA.LANG.OBJECT'
  var multiplication120 = 10b !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.DATE'
  var multiplication121 = 10b !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BYTE', 'NULL'
  var multiplication122 = 10b !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication123 = 10b !* BigInteger.ONE
  var multiplication124 = 10b !* BigDecimal.ONE

  var multiplication210 = 10s !* false                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'
  var multiplication211 = 10s !* 'c'
  var multiplication212 = 10s !* 3b
  var multiplication213 = 10s !* 3s
  var multiplication214 = 10s !* 3
  var multiplication215 = 10s !* 3L
  var multiplication216 = 10s !* 3.5f
  var multiplication217 = 10s !* 3.5
  var multiplication219 = 10s !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.STRING'
  var multiplication2191 = 10s !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'SHORT', 'JAVA.LANG.OBJECT'
  var multiplication220 = 10s !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.DATE'
  var multiplication221 = 10s !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'SHORT', 'NULL'
  var multiplication222 = 10s !* BigInteger.ONE
  var multiplication223 = 10s !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication224 = 10s !* BigDecimal.ONE

  var multiplication310 = 10 !* false                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'
  var multiplication311 = 10 !* 'c'
  var multiplication312 = 10 !* 3b
  var multiplication313 = 10 !* 3s
  var multiplication314 = 10 !* 3
  var multiplication315 = 10 !* 3L
  var multiplication316 = 10 !* 3.5f
  var multiplication317 = 10 !* 3.5
  var multiplication319 = 10 !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.STRING'
  var multiplication3191 = 10 !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'INT', 'JAVA.LANG.OBJECT'
  var multiplication320 = 10 !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.DATE'
  var multiplication321 = 10 !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'INT', 'NULL'
  var multiplication322 = 10 !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'INT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication323 = 10 !* BigInteger.ONE
  var multiplication324 = 10 !* BigDecimal.ONE

  var multiplication410 = 10L !* false                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'
  var multiplication411 = 10L !* 'c'
  var multiplication412 = 10L !* 3b
  var multiplication413 = 10L !* 3s
  var multiplication414 = 10L !* 3
  var multiplication415 = 10L !* 3L
  var multiplication416 = 10L !* 3.5f
  var multiplication417 = 10L !* 3.5
  var multiplication419 = 10L !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.STRING'
  var multiplication4191 = 10L !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'LONG', 'JAVA.LANG.OBJECT'
  var multiplication420 = 10L !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.DATE'
  var multiplication421 = 10L !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'LONG', 'NULL'
  var multiplication422 = 10L !* BigInteger.ONE
  var multiplication423 = 10L !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication424 = 10L !* BigDecimal.ONE


  var multiplication510 = 42.5f !* false                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'
  var multiplication511 = 42.5f !* 'c'
  var multiplication512 = 42.5f !* 3b
  var multiplication513 = 42.5f !* 3s
  var multiplication514 = 42.5f !* 3
  var multiplication515 = 42.5f !* 3L
  var multiplication516 = 42.5f !* 3.5f
  var multiplication517 = 42.5f !* 3.5
  var multiplication519 = 42.5f !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.STRING'
  var multiplication5191 = 42.5f !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.LANG.OBJECT'
  var multiplication520 = 42.5f !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.DATE'
  var multiplication521 = 42.5f !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'FLOAT', 'NULL'
  var multiplication522 = 42.5f !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication523 = 42.5f !* BigInteger.ONE
  var multiplication524 = 42.5f !* BigDecimal.ONE

  var multiplication610 = 42.55 !* false                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var multiplication611 = 42.55 !* 'c'
  var multiplication612 = 42.55 !* 3b
  var multiplication613 = 42.55 !* 3s
  var multiplication614 = 42.55 !* 3
  var multiplication615 = 42.55 !* 3L
  var multiplication616 = 42.55 !* 3.5f
  var multiplication617 = 42.55 !* 3.5
  var multiplication619 = 42.55 !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.STRING'
  var multiplication6191 = 42.55 !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.LANG.OBJECT'
  var multiplication620 = 42.55 !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.DATE'
  var multiplication621 = 42.55 !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'DOUBLE', 'NULL'
  var multiplication622 = 42.55 !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication623 = 42.55 !* BigInteger.ONE
  var multiplication624 = 42.55 !* BigDecimal.ONE

  var multiplication710 = true !* true                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var multiplication711 = true !* 'c'                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'CHAR'
  var multiplication712 = true !* 3b                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'BYTE'
  var multiplication713 = true !* 3s                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'SHORT'
  var multiplication714 = true !* 3                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var multiplication715 = true !* 3L                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'LONG'
  var multiplication717 = true !* 3.5f                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'FLOAT'
  var multiplication7171 = true !* 3.5                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'DOUBLE'
  var multiplication719 = true !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.STRING'
  var multiplication7191 = true !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.LANG.OBJECT'
  var multiplication720 = true !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.DATE'
  var multiplication721 = true !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'NULL'
  var multiplication722 = true !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication723 = true !* BigInteger.ONE                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGINTEGER'
  var multiplication724 = true !* BigDecimal.ONE                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGDECIMAL'

  var multiplication810 = "string" !* true                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BOOLEAN'
  var multiplication811 = "string" !* 'c'                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'CHAR'
  var multiplication812 = "string" !* 3b                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BYTE'
  var multiplication813 = "string" !* 3s                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'SHORT'
  var multiplication814 = "string" !* 3                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'INT'
  var multiplication815 = "string" !* 3L                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'LONG'
  var multiplication817 = "string" !* 3.5f                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'FLOAT'
  var multiplication8171 = "string" !* 3.5                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'DOUBLE'
  var multiplication819 = "string" !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var multiplication8191 = "string" !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.OBJECT'
  var multiplication820 = "string" !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var multiplication821 = "string" !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'NULL'
  var multiplication822 = "string" !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication823 = "string" !* BigInteger.ONE                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var multiplication824 = "string" !* BigDecimal.ONE                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'


  var multiplication910 = BigInteger.ONE !* true                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'
  var multiplication911 = BigInteger.ONE !* 'c'
  var multiplication912 = BigInteger.ONE !* 3b
  var multiplication913 = BigInteger.ONE !* 3s
  var multiplication914 = BigInteger.ONE !* 3
  var multiplication915 = BigInteger.ONE !* 3L
  var multiplication917 = BigInteger.ONE !* 3.5f
  var multiplication9171 = BigInteger.ONE !* 3.5
  var multiplication919 = BigInteger.ONE !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.STRING'
  var multiplication9191 = BigInteger.ONE !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.LANG.OBJECT'
  var multiplication920 = BigInteger.ONE !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.DATE'
  var multiplication921 = BigInteger.ONE !* null                         //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'NULL'
  var multiplication922 = BigInteger.ONE !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication923 = BigInteger.ONE !* BigInteger.ONE
  var multiplication924 = BigInteger.ONE !* BigDecimal.ONE


  var multiplication1010 = BigDecimal.ONE !* true                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'
  var multiplication1011 = BigDecimal.ONE !* 'c'
  var multiplication1012 = BigDecimal.ONE !* 3b
  var multiplication1013 = BigDecimal.ONE !* 3s
  var multiplication1014 = BigDecimal.ONE !* 3
  var multiplication1015 = BigDecimal.ONE !* 3L
  var multiplication1017 = BigDecimal.ONE !* 3.5f
  var multiplication10171 = BigDecimal.ONE !* 3.5
  var multiplication1019 = BigDecimal.ONE !* "string"                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.STRING'
  var multiplication10191 = BigDecimal.ONE !* new Object()                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.LANG.OBJECT'
  var multiplication1020 = BigDecimal.ONE !* date1                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.DATE'
  var multiplication1021 = BigDecimal.ONE !* null                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'NULL'
  var multiplication1022 = BigDecimal.ONE !* {1, 2, 3}                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var multiplication1023 = BigDecimal.ONE !* BigInteger.ONE
  var multiplication1024 = BigDecimal.ONE !* BigDecimal.ONE


  var multiplication1910 = 1b !* !2                        //## issuekeys: OPERATOR '!' CANNOT BE APPLIED TO 'INT'
  var multiplication1911 = 42 !* ~32
  var multiplication1912 = 42 !* 3b !* 42
  var multiplication1913 = 42.5f !* 3s !* 3s
  var multiplication1914 = 42.5 !* true !* 3                        //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var multiplication1915 = 42.5 !* !-3 !* ~3
  var multiplication1916 = 42 !* !-3L
  var multiplication1917 = !-42s !* 3.5f
  var multiplication1918 = 42 !* 10 !* 3.5
  var multiplication1919 = 42.5 !* 10 !* 6
/////////////////////MULTIPLICATION ENDS////////////////////


}