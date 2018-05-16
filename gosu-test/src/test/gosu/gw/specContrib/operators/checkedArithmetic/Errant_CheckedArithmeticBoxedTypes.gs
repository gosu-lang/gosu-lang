package gw.specContrib.operators.checkedArithmetic

uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_CheckedArithmeticBoxedTypes {

  var char1 : Character = 'c'
  var byte1 : Byte = 2b
  var short1 : Short = 3 as short
  var int1 : Integer = 42
  var float1 : Float = 42.5f
  var long1 : Long = 100L
  var double1 : Double = 42.5



  var c001 = char1 !+ char1
  var c002 = char1 !+ byte1
  var c003 = char1 !+ short1
  var c004 = char1 !+ int1
  var c005 = char1 !+ float1
  var c006 = char1 !+ long1
  var c007 = char1 !+ double1
  var c008 = char1 !+ BigDecimal.TEN
  var c009 = char1 !+ BigInteger.ONE
  //IDE-3045 need to be fixed in OS Gosu
  var c010 = char1 !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.LANG.STRING'
  var c011 = char1 !+ new Object()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.LANG.OBJECT'
  var c012 = char1 !+ new Date()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.UTIL.DATE'
  var c013 = char1 !+ null            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'NULL'
  var c014 = char1 !+ false            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'BOOLEAN'
  var c015 = char1 !+ {1,2,3}            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'


  var b001 = byte1 !+ char1
  var b002 = byte1 !+ byte1
  var b003 = byte1 !+ short1
  var b004 = byte1 !+ int1
  var b005 = byte1 !+ float1
  var b006 = byte1 !+ long1
  var b007 = byte1 !+ double1
  var b008 = byte1 !+ BigDecimal.TEN
  var b009 = byte1 !+ BigInteger.ONE
  var b010 = byte1 !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.LANG.STRING'
  var b011 = byte1 !+ new Object()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.LANG.OBJECT'
  var b012 = byte1 !+ new Date()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.UTIL.DATE'
  var b013 = byte1 !+ null            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'NULL'
  var b014 = byte1 !+ false            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'BOOLEAN'
  var b015 = byte1 !+ {1,2,3}            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var s001 = short1 !+ char1
  var s002 = short1 !+ byte1
  var s003 = short1 !+ short1
  var s004 = short1 !+ int1
  var s005 = short1 !+ float1
  var s006 = short1 !+ long1
  var s007 = short1 !+ double1
  var s008 = short1 !+ BigDecimal.TEN
  var s009 = short1 !+ BigInteger.ONE
  var s010 = short1 !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.STRING'
  var s011 = short1 !+ new Object()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.OBJECT'
  var s012 = short1 !+ new Date()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.UTIL.DATE'
  var s013 = short1 !+ null            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'NULL'
  var s014 = short1 !+ false            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'BOOLEAN'
  var s015 = short1 !+ {1,2,3}            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var i001 = int1 !+ char1
  var i002 = int1 !+ byte1
  var i003 = int1 !+ short1
  var i004 = int1 !+ int1
  var i005 = int1 !+ float1
  var i006 = int1 !+ long1
  var i007 = int1 !+ double1
  var i008 = int1 !+ BigDecimal.TEN
  var i009 = int1 !+ BigInteger.ONE
  var i010 = int1 !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.STRING'
  var i011 = int1 !+ new Object()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.OBJECT'
  var i012 = int1 !+ new Date()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.UTIL.DATE'
  var i013 = int1 !+ null            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'NULL'
  var i014 = int1 !+ false            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'BOOLEAN'
  var i015 = int1 !+ {1,2,3}            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var f001 = float1 !+ char1
  var f002 = float1 !+ byte1
  var f003 = float1 !+ short1
  var f004 = float1 !+ int1
  var f005 = float1 !+ float1
  var f006 = float1 !+ long1
  var f007 = float1 !+ double1
  var f008 = float1 !+ BigDecimal.TEN
  var f009 = float1 !+ BigInteger.ONE
  var f010 = float1 !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.LANG.STRING'
  var f011 = float1 !+ new Object()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.LANG.OBJECT'
  var f012 = float1 !+ new Date()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.UTIL.DATE'
  var f013 = float1 !+ null            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'NULL'
  var f014 = float1 !+ false            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'BOOLEAN'
  var f015 = float1 !+ {1,2,3}            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var l001 = long1 !+ char1
  var l002 = long1 !+ byte1
  var l003 = long1 !+ short1
  var l004 = long1 !+ int1
  var l005 = long1 !+ float1
  var l006 = long1 !+ long1
  var l007 = long1 !+ double1
  var l008 = long1 !+ BigDecimal.TEN
  var l009 = long1 !+ BigInteger.ONE
  var l010 = long1 !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.LANG.STRING'
  var l011 = long1 !+ new Object()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.LANG.OBJECT'
  var l012 = long1 !+ new Date()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.UTIL.DATE'
  var l013 = long1 !+ null            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'NULL'
  var l014 = long1 !+ false            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'BOOLEAN'
  var l015 = long1 !+ {1,2,3}            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var d001 = double1 !+ char1
  var d002 = double1 !+ byte1
  var d003 = double1 !+ short1
  var d004 = double1 !+ int1
  var d005 = double1 !+ float1
  var d006 = double1 !+ long1
  var d007 = double1 !+ double1
  var d008 = double1 !+ BigDecimal.TEN
  var d009 = double1 !+ BigInteger.ONE
  var d010 = double1 !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.LANG.STRING'
  var d011 = double1 !+ new Object()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.LANG.OBJECT'
  var d012 = double1 !+ new Date()            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.UTIL.DATE'
  var d013 = double1 !+ null            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'NULL'
  var d014 = double1 !+ false            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'BOOLEAN'
  var d015 = double1 !+ {1,2,3}            //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  //IDE-3045 need to be fixed in OS Gosu
  var st001 = "test" !+ char1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.CHARACTER'
  var st002 = "test" !+ byte1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.BYTE'
  var st003 = "test" !+ short1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.SHORT'
  var st004 = "test" !+ int1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.INTEGER'
  var st005 = "test" !+ float1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.FLOAT'
  var st006 = "test" !+ long1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.LONG'
  var st007 = "test" !+ double1      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.DOUBLE'
  var st008 = "test" !+ BigDecimal.TEN      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGDECIMAL'
  var st009 = "test" !+ BigInteger.ONE      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.MATH.BIGINTEGER'
  var st010 = "test" !+ "mystring"      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
  var st011 = "test" !+ new Object()      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.OBJECT'
  var st012 = "test" !+ new Date()      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.DATE'
  var st013 = "test" !+ null      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'NULL'
  var st014 = "test" !+ false      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'BOOLEAN'
  var st015 = "test" !+ {1,2,3}      //## issuekeys: OPERATOR '!+' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  //Subtraction
  var c101 = char1 !- char1
  var c102 = char1 !- byte1
  var c103 = char1 !- short1
  var c104 = char1 !- int1
  var c105 = char1 !- float1
  var c106 = char1 !- long1
  var c107 = char1 !- double1
  var c108 = char1 !- BigDecimal.TEN
  var c109 = char1 !- BigInteger.ONE
  var c110 = char1 !- "mystring"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.LANG.STRING'
  var c111 = char1 !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.LANG.OBJECT'
  var c112 = char1 !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.UTIL.DATE'
  var c113 = char1 !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'NULL'
  var c114 = char1 !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'BOOLEAN'
  var c115 = char1 !- {1,2,3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'


  var b101 = byte1 !- char1
  var b102 = byte1 !- byte1
  var b103 = byte1 !- short1
  var b104 = byte1 !- int1
  var b105 = byte1 !- float1
  var b106 = byte1 !- long1
  var b107 = byte1 !- double1
  var b108 = byte1 !- BigDecimal.TEN
  var b109 = byte1 !- BigInteger.ONE
  var b110 = byte1 !- "mystring"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.LANG.STRING'
  var b111 = byte1 !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.LANG.OBJECT'
  var b112 = byte1 !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.UTIL.DATE'
  var b113 = byte1 !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'NULL'
  var b114 = byte1 !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'BOOLEAN'
  var b115 = byte1 !- {1,2,3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var s101 = short1 !- char1
  var s102 = short1 !- byte1
  var s103 = short1 !- short1
  var s104 = short1 !- int1
  var s105 = short1 !- float1
  var s106 = short1 !- long1
  var s107 = short1 !- double1
  var s108 = short1 !- BigDecimal.TEN
  var s109 = short1 !- BigInteger.ONE
  var s110 = short1 !- "mystring"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.STRING'
  var s111 = short1 !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.OBJECT'
  var s112 = short1 !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.UTIL.DATE'
  var s113 = short1 !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'NULL'
  var s114 = short1 !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'BOOLEAN'
  var s115 = short1 !- {1,2,3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var i101 = int1 !- char1
  var i102 = int1 !- byte1
  var i103 = int1 !- short1
  var i104 = int1 !- int1
  var i105 = int1 !- float1
  var i106 = int1 !- long1
  var i107 = int1 !- double1
  var i108 = int1 !- BigDecimal.TEN
  var i109 = int1 !- BigInteger.ONE
  var i110 = int1 !- "mystring"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.STRING'
  var i111 = int1 !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.OBJECT'
  var i112 = int1 !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.UTIL.DATE'
  var i113 = int1 !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'NULL'
  var i114 = int1 !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'BOOLEAN'
  var i115 = int1 !- {1,2,3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var f101 = float1 !- char1
  var f102 = float1 !- byte1
  var f103 = float1 !- short1
  var f104 = float1 !- int1
  var f105 = float1 !- float1
  var f106 = float1 !- long1
  var f107 = float1 !- double1
  var f108 = float1 !- BigDecimal.TEN
  var f109 = float1 !- BigInteger.ONE
  var f110 = float1 !- "mystring"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.LANG.STRING'
  var f111 = float1 !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.LANG.OBJECT'
  var f112 = float1 !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.UTIL.DATE'
  var f113 = float1 !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'NULL'
  var f114 = float1 !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'BOOLEAN'
  var f115 = float1 !- {1,2,3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var l101 = long1 !- char1
  var l102 = long1 !- byte1
  var l103 = long1 !- short1
  var l104 = long1 !- int1
  var l105 = long1 !- float1
  var l106 = long1 !- long1
  var l107 = long1 !- double1
  var l108 = long1 !- BigDecimal.TEN
  var l109 = long1 !- BigInteger.ONE
  var l110 = long1 !- "mystring"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.LANG.STRING'
  var l111 = long1 !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.LANG.OBJECT'
  var l112 = long1 !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.UTIL.DATE'
  var l113 = long1 !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'NULL'
  var l114 = long1 !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'BOOLEAN'
  var l115 = long1 !- {1,2,3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var d101 = double1 !- char1
  var d102 = double1 !- byte1
  var d103 = double1 !- short1
  var d104 = double1 !- int1
  var d105 = double1 !- float1
  var d106 = double1 !- long1
  var d107 = double1 !- double1
  var d108 = double1 !- BigDecimal.TEN
  var d109 = double1 !- BigInteger.ONE
  var d110 = double1 !- "mystring"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.LANG.STRING'
  var d111 = double1 !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.LANG.OBJECT'
  var d112 = double1 !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.UTIL.DATE'
  var d113 = double1 !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'NULL'
  var d114 = double1 !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'BOOLEAN'
  var d115 = double1 !- {1,2,3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var s101 = "test" !- char1            //## issuekeys: VARIABLE 'S101' IS ALREADY DEFINED IN THE SCOPE
  var s102 = "test" !- byte1            //## issuekeys: VARIABLE 'S102' IS ALREADY DEFINED IN THE SCOPE
  var s103 = "test" !- short1            //## issuekeys: VARIABLE 'S103' IS ALREADY DEFINED IN THE SCOPE
  var s104 = "test" !- int1            //## issuekeys: VARIABLE 'S104' IS ALREADY DEFINED IN THE SCOPE
  var s105 = "test" !- float1            //## issuekeys: VARIABLE 'S105' IS ALREADY DEFINED IN THE SCOPE
  var s106 = "test" !- long1            //## issuekeys: VARIABLE 'S106' IS ALREADY DEFINED IN THE SCOPE
  var s107 = "test" !- double1            //## issuekeys: VARIABLE 'S107' IS ALREADY DEFINED IN THE SCOPE
  var s108 = "test" !- BigDecimal.TEN            //## issuekeys: VARIABLE 'S108' IS ALREADY DEFINED IN THE SCOPE
  var s109 = "test" !- BigInteger.ONE            //## issuekeys: VARIABLE 'S109' IS ALREADY DEFINED IN THE SCOPE
  var s110 = "test" !- "mystring"            //## issuekeys: VARIABLE 'S110' IS ALREADY DEFINED IN THE SCOPE
  var s111 = "test" !- new Object()            //## issuekeys: VARIABLE 'S111' IS ALREADY DEFINED IN THE SCOPE
  var s112 = "test" !- new Date()            //## issuekeys: VARIABLE 'S112' IS ALREADY DEFINED IN THE SCOPE
  var s113 = "test" !- null            //## issuekeys: VARIABLE 'S113' IS ALREADY DEFINED IN THE SCOPE
  var s114 = "test" !- false            //## issuekeys: VARIABLE 'S114' IS ALREADY DEFINED IN THE SCOPE
  var s115 = "test" !- {1,2,3}            //## issuekeys: VARIABLE 'S115' IS ALREADY DEFINED IN THE SCOPE



  //Multiplication
  var c201 = char1 !* char1
  var c202 = char1 !* byte1
  var c203 = char1 !* short1
  var c204 = char1 !* int1
  var c205 = char1 !* float1
  var c206 = char1 !* long1
  var c207 = char1 !* double1
  var c208 = char1 !* BigDecimal.TEN
  var c209 = char1 !* BigInteger.ONE
  var c210 = char1 !* "mystring"            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.LANG.STRING'
  var c211 = char1 !* new Object()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.LANG.OBJECT'
  var c212 = char1 !* new Date()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.UTIL.DATE'
  var c213 = char1 !* null            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'NULL'
  var c214 = char1 !* false            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'BOOLEAN'
  var c215 = char1 !* {1,2,3}            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.CHARACTER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'


  var b201 = byte1 !* char1
  var b202 = byte1 !* byte1
  var b203 = byte1 !* short1
  var b204 = byte1 !* int1
  var b205 = byte1 !* float1
  var b206 = byte1 !* long1
  var b207 = byte1 !* double1
  var b208 = byte1 !* BigDecimal.TEN
  var b209 = byte1 !* BigInteger.ONE
  var b210 = byte1 !* "mystring"            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.LANG.STRING'
  var b211 = byte1 !* new Object()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.LANG.OBJECT'
  var b212 = byte1 !* new Date()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.UTIL.DATE'
  var b213 = byte1 !* null            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'NULL'
  var b214 = byte1 !* false            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'BOOLEAN'
  var b215 = byte1 !* {1,2,3}            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.BYTE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var s201 = short1 !* char1
  var s202 = short1 !* byte1
  var s203 = short1 !* short1
  var s204 = short1 !* int1
  var s205 = short1 !* float1
  var s206 = short1 !* long1
  var s207 = short1 !* double1
  var s208 = short1 !* BigDecimal.TEN
  var s209 = short1 !* BigInteger.ONE
  var s210 = short1 !* "mystring"            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.STRING'
  var s211 = short1 !* new Object()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.OBJECT'
  var s212 = short1 !* new Date()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.UTIL.DATE'
  var s213 = short1 !* null            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'NULL'
  var s214 = short1 !* false            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'BOOLEAN'
  var s215 = short1 !* {1,2,3}            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var i201 = int1 !* char1
  var i202 = int1 !* byte1
  var i203 = int1 !* short1
  var i204 = int1 !* int1
  var i205 = int1 !* float1
  var i206 = int1 !* long1
  var i207 = int1 !* double1
  var i208 = int1 !* BigDecimal.TEN
  var i209 = int1 !* BigInteger.ONE
  var i210 = int1 !* "mystring"            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.STRING'
  var i211 = int1 !* new Object()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.OBJECT'
  var i212 = int1 !* new Date()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.UTIL.DATE'
  var i213 = int1 !* null            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'NULL'
  var i214 = int1 !* false            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'BOOLEAN'
  var i215 = int1 !* {1,2,3}            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var f201 = float1 !* char1
  var f202 = float1 !* byte1
  var f203 = float1 !* short1
  var f204 = float1 !* int1
  var f205 = float1 !* float1
  var f206 = float1 !* long1
  var f207 = float1 !* double1
  var f208 = float1 !* BigDecimal.TEN
  var f209 = float1 !* BigInteger.ONE
  var f210 = float1 !* "mystring"            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.LANG.STRING'
  var f211 = float1 !* new Object()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.LANG.OBJECT'
  var f212 = float1 !* new Date()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.UTIL.DATE'
  var f213 = float1 !* null            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'NULL'
  var f214 = float1 !* false            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'BOOLEAN'
  var f215 = float1 !* {1,2,3}            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.FLOAT', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var l201 = long1 !* char1
  var l202 = long1 !* byte1
  var l203 = long1 !* short1
  var l204 = long1 !* int1
  var l205 = long1 !* float1
  var l206 = long1 !* long1
  var l207 = long1 !* double1
  var l208 = long1 !* BigDecimal.TEN
  var l209 = long1 !* BigInteger.ONE
  var l210 = long1 !* "mystring"            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.LANG.STRING'
  var l211 = long1 !* new Object()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.LANG.OBJECT'
  var l212 = long1 !* new Date()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.UTIL.DATE'
  var l213 = long1 !* null            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'NULL'
  var l214 = long1 !* false            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'BOOLEAN'
  var l215 = long1 !* {1,2,3}            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.LONG', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var d201 = double1 !* char1
  var d202 = double1 !* byte1
  var d203 = double1 !* short1
  var d204 = double1 !* int1
  var d205 = double1 !* float1
  var d206 = double1 !* long1
  var d207 = double1 !* double1
  var d208 = double1 !* BigDecimal.TEN
  var d209 = double1 !* BigInteger.ONE
  var d210 = double1 !* "mystring"            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.LANG.STRING'
  var d211 = double1 !* new Object()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.LANG.OBJECT'
  var d212 = double1 !* new Date()            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.UTIL.DATE'
  var d213 = double1 !* null            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'NULL'
  var d214 = double1 !* false            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'BOOLEAN'
  var d215 = double1 !* {1,2,3}            //## issuekeys: OPERATOR '!*' CANNOT BE APPLIED TO 'JAVA.LANG.DOUBLE', 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'

  var s201 = "test" !* char1            //## issuekeys: VARIABLE 'S201' IS ALREADY DEFINED IN THE SCOPE
  var s202 = "test" !* byte1            //## issuekeys: VARIABLE 'S202' IS ALREADY DEFINED IN THE SCOPE
  var s203 = "test" !* short1            //## issuekeys: VARIABLE 'S203' IS ALREADY DEFINED IN THE SCOPE
  var s204 = "test" !* int1            //## issuekeys: VARIABLE 'S204' IS ALREADY DEFINED IN THE SCOPE
  var s205 = "test" !* float1            //## issuekeys: VARIABLE 'S205' IS ALREADY DEFINED IN THE SCOPE
  var s206 = "test" !* long1            //## issuekeys: VARIABLE 'S206' IS ALREADY DEFINED IN THE SCOPE
  var s207 = "test" !* double1            //## issuekeys: VARIABLE 'S207' IS ALREADY DEFINED IN THE SCOPE
  var s208 = "test" !* BigDecimal.TEN            //## issuekeys: VARIABLE 'S208' IS ALREADY DEFINED IN THE SCOPE
  var s209 = "test" !* BigInteger.ONE            //## issuekeys: VARIABLE 'S209' IS ALREADY DEFINED IN THE SCOPE
  var s210 = "test" !* "mystring"            //## issuekeys: VARIABLE 'S210' IS ALREADY DEFINED IN THE SCOPE
  var s211 = "test" !* new Object()            //## issuekeys: VARIABLE 'S211' IS ALREADY DEFINED IN THE SCOPE
  var s212 = "test" !* new Date()            //## issuekeys: VARIABLE 'S212' IS ALREADY DEFINED IN THE SCOPE
  var s213 = "test" !* null            //## issuekeys: VARIABLE 'S213' IS ALREADY DEFINED IN THE SCOPE
  var s214 = "test" !* false            //## issuekeys: VARIABLE 'S214' IS ALREADY DEFINED IN THE SCOPE
  var s215 = "test" !* {1,2,3}            //## issuekeys: VARIABLE 'S215' IS ALREADY DEFINED IN THE SCOPE
}