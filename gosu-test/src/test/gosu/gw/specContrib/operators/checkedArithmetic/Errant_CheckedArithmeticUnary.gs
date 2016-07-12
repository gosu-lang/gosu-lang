package gw.specContrib.operators.checkedArithmetic

uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_CheckedArithmeticUnary {
  var x = 5
  var y = 42



  var z01 = !+x            //## issuekeys: UNEXPECTED TOKEN: !+
  var z02 = !-x
  var z03 = !*x            //## issuekeys: UNEXPECTED TOKEN: !*
  var z04 = !+(-x)              //## issuekeys: UNEXPECTED TOKEN: !+
  var z05 = +(!-x)
  var z06 = !+(!-x)             //## issuekeys: UNEXPECTED TOKEN: !+
  var z07 = !-(+x)
  var z08 = -(!+x)              //## issuekeys: UNEXPECTED TOKEN: !+
  var z09 = !-(!+x)             //## issuekeys: UNEXPECTED TOKEN: !+
  var z10 = !+(+x)              //## issuekeys: UNEXPECTED TOKEN: !+
  var z11 = +(!+x)             //## issuekeys: UNEXPECTED TOKEN: !+
  var z12 = !+(!+x)            //## issuekeys: UNEXPECTED TOKEN: !+
  var z13 = !-(-x)
  var z14 = -(!-x)
  var z15 = !-(!-x)

  var z21 = !--x            //## issuekeys: UNEXPECTED TOKEN: -
  var z22 = !++x            //## issuekeys: UNEXPECTED TOKEN: !+
  var z23 = !**x            //## issuekeys: UNEXPECTED TOKEN: !*

  //Primitives
  var primitives000 = !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN'
  var primitives001 = !- 'c'
  var primitives002 = !- 3b
  var primitives003 = !- 3s
  var primitives004 = !- 3
  var primitives005 = !- 3L
  var primitives006 = !- 3.5f
  var primitives007 = !- 3.5
  var primitives009 = !- "string"            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.STRING'
  var primitives010 = !- new Object()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.OBJECT'
  var primitives011 = !- null            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'NULL'
  var primitives012 = !- {1, 2, 3}            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>'
  var primitives013 = !- BigInteger.ONE
  var primitives014 = !- BigDecimal.ONE
  var primitives015 = !- new Date()            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE'

  var char1 : Character = 'c'
  var byte1 : Byte = 2b
  var short1 : Short = 3s
  var int1 : Integer = 42
  var float1 : Float = 42.5f
  var long1 : Long = 100L
  var double1 : Double = 42.5
  var boolean1 : Boolean = false

  var boxed000 = !- false            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'BOOLEAN'
  var boxed001 = !- char1
  var boxed002 = !- byte1
  var boxed003 = !- short1
  var boxed004 = !- int1
  var boxed005 = !- float1
  var boxed006 = !- long1
  var boxed007 = !- double1
  var boxed008 = !- boolean1            //## issuekeys: OPERATOR '!-' CANNOT BE APPLIED TO 'JAVA.LANG.BOOLEAN'

}