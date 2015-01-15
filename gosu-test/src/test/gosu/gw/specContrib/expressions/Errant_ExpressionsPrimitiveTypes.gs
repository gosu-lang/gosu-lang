package gw.specContrib.expressions

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_ExpressionsPrimitiveTypes {

  var date1 = new DateTime()
  var date2 = new DateTime()

  //char
  var char1: char = 3b      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char2: char = 3s      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char3: char = 3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char4: char = 3L      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char5: char = 3.3f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char6: char = 3.3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char7: char = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char8: char = 'c'
  var char9: char = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var char10: char = new Object()  //## issuekeys: MSG_TYPE_MISMATCH
  var char11: char = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var char12: char = BigInteger.TEN      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char13: char = BigDecimal.ZERO      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var char14: char = '\u1ab8'
  var char15: char = '\u55555'   //## issuekeys: MSG_TYPE_MISMATCH
  var char16: char = '\123'
  var char17: char = '\7'
  var char18: char = '\77'
  var char19: char = '\18'       //## issuekeys: MSG_TYPE_MISMATCH
  var char20: char = '\18'       //## issuekeys: MSG_TYPE_MISMATCH
  var char21: char = '\400'      //## issuekeys: MSG_TYPE_MISMATCH
  var char22: char = '\b'
  var char23: char = '\t'
  var char24: char = '\n'
  var char25: char = '\f'
  var char26: char = '\r'
  var char27: char = '\''
  var char28: char = '\"'
  var char29: char = '\\'
  var char30: char = '\a'        //## issuekeys: ILLEGAL ESCAPE CHARACTER

  //byte
  var byte1: byte = 1b
  var byte2: byte = 1s      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte31: byte = 127
  var byte32: byte = -129      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte33: byte = 300      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte34 = 300
  var byte35: byte = byte34      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte4: byte = 1L      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte5: byte = 1.1f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte6: byte = 1.1      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte7: byte = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte8: byte = 'c'      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte9: byte = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var byte10: byte = new Date()      //## issuekeys: MSG_TYPE_MISMATCH
  var byte11: byte = date1      //## issuekeys: MSG_TYPE_MISMATCH
  //  IDE-1276 : OS Gosu issue in the following case
  var byte12: byte = new Object()      //## issuekeys: MSG_TYPE_MISMATCH
  var byte13: byte = BigInteger.ONE      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte14: byte = BigDecimal.ONE      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var byte15: byte = null      //## issuekeys: MSG_TYPE_MISMATCH

  //short
  var short1: short = 2b
  var short2: short = 2s
  var short31: short = 31111
  var short32: short = 33333      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var short4: short = 2L      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var short5: short = 2.2f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var short6: short = 2.2      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var short7: short = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var short8: short = 'c'      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var short9: short = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var short10: short = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var short11: short = new Object()     //## issuekeys: MSG_TYPE_MISMATCH
  var short12: short = BigInteger.ONE      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var short13: short = BigDecimal.ONE      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  //int
  var int1: int = 3b
  var int2: int = 3s
  var int3: int = 3
  var int4: int = 3L      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var int5: int = 3.3f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var int6: int = 3.3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var int7: int = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var int8: int = 'c'
  var int9: int = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var int10: int = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var int11: int = new Object()     //## issuekeys: MSG_TYPE_MISMATCH
  var int12: int = BigInteger.ONE      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var int13: int = BigDecimal.TEN       //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  //long
  var long1: long = 3b
  var long2: long = 3s
  var long3: long = 3
  var long4: long = 3L
  var long5: long = 3.3f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var long6: long = 3.3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var long7: long = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var long8: long = 'c'
  var long9: long = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var long10: long = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var long11: long = new Object()       //## issuekeys: MSG_TYPE_MISMATCH
  var long12: long = BigInteger.ONE      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var long13: long = BigDecimal.ZERO      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  //float
  var float1: float = 3b
  var float2: float = 3s
  var float3: float = 3
  var float4: float = 3L
  var float5: float = 3.3f
  //  IDE-494  May need change once the bug is fixed  //Can a double be stored in a float? //Same behavior in parser and OS Gosu though
  var float61: float = 42.5
  //If it is extracted to a variable then it shows error
  var somedouble: double = 42.5
  var float62: float = somedouble       //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var anotherDouble = 43.5
  var float63: float = anotherDouble      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var float7: float = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var float8: float = 'c'
  var float9: float = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var float10: float = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var float11: float = new Object()      //## issuekeys: MSG_TYPE_MISMATCH
  var float12: float = BigInteger.ONE      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var float13: float = BigDecimal.TEN      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  //double
  var double1: double = 3b
  var double2: double = 3s
  var double3: double = 3
  var double4: double = 3L
  var double5: double = 3.3f
  var double6: double = 3.3
  var double7: double = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var double8: double = 'c'
  var double9: double = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var double10: double = new Object()  //## issuekeys: MSG_TYPE_MISMATCH
  var double11: double = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var double12: double = BigInteger.TEN      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var double13: double = BigDecimal.ZERO      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  //boolean
  var boolean1: boolean = 3b      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var boolean2: boolean = 3s      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var boolean3: boolean = 3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var boolean4: boolean = 3L      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var boolean5: boolean = 3.3f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var boolean6: boolean = 3.3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var boolean7: boolean = true
  var boolean8: boolean = 'c'      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var boolean9: boolean = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var boolean10: boolean = new Object()  //## issuekeys: MSG_TYPE_MISMATCH
  var boolean11: boolean = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var boolean12: boolean = BigInteger.TEN      //## issuekeys: MSG_TYPE_MISMATCH
  var boolean13: boolean = BigDecimal.ONE      //## issuekeys: MSG_TYPE_MISMATCH

  //BigInteger
  var bigInteger1: BigInteger = 3b
  var bigInteger2: BigInteger = 3s
  var bigInteger3: BigInteger = 3
  var bigInteger4: BigInteger = 3L
  var bigInteger5: BigInteger = 3.3f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var bigInteger6: BigInteger = 3.3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var bigInteger7: BigInteger = true      //## issuekeys: MSG_TYPE_MISMATCH
  var bigInteger8: BigInteger = 'c'
  var bigInteger9: BigInteger = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var bigInteger10: BigInteger = new Object()      //## issuekeys: MSG_TYPE_MISMATCH
  var bigInteger11: BigInteger = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var bigInteger12: BigInteger = BigInteger.TEN
  var bigInteger13: BigInteger = BigDecimal.ZERO      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  //BigDecimal
  var bigDecimal1: BigDecimal = 3b
  var bigDecimal2: BigDecimal = 3s
  var bigDecimal3: BigDecimal = 3
  var bigDecimal4: BigDecimal = 3L
  var bigDecimal5: BigDecimal = 3.3f
  var bigDecimal6: BigDecimal = 3.3
  var bigDecimal7: BigDecimal = true      //## issuekeys: MSG_TYPE_MISMATCH
  var bigDecimal8: BigDecimal = 'c'
  var bigDecimal9: BigDecimal = date1      //## issuekeys: MSG_TYPE_MISMATCH
  var bigDecimal10: BigDecimal = new Object()      //## issuekeys: MSG_TYPE_MISMATCH
  var bigDecimal11: BigDecimal = "string"      //## issuekeys: MSG_TYPE_MISMATCH
  var bigDecimal12: BigDecimal = BigInteger.TEN
  var bigDecimal13: BigDecimal = BigDecimal.ZERO

  var string1: String = 3b      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string2: String = 3s      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string3: String = 3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string4: String = 3L      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string5: String = 3.3f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string6: String = 3.3      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string7: String = true      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string8: String = 'c'
  var string9: String = date1      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string10:String = new Object()      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string11:String = "string"
  var string12:String = BigInteger.TEN      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var string13:String = BigDecimal.ZERO      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

}