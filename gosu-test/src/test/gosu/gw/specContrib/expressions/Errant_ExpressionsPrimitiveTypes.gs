package gw.specContrib.expressions

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.Date

class Errant_ExpressionsPrimitiveTypes {

  var date1 = new DateTime()
  var date2 = new DateTime()

  //char
  var char1: char = 3b      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'CHAR'
  var char2: char = 3s      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'CHAR'
  var char3: char = 3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'CHAR'
  var char4: char = 3L      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'LONG', REQUIRED: 'CHAR'
  var char5: char = 3.3f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'CHAR'
  var char6: char = 3.3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'CHAR'
  var char7: char = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'CHAR'
  var char8: char = 'c'
  var char9: char = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'CHAR'
  var char10: char = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'CHAR'
  var char11: char = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'CHAR'
  var char12: char = BigInteger.TEN      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'CHAR'
  var char13: char = BigDecimal.ZERO      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'CHAR'

  //byte
  var byte1: byte = 1b
  var byte2: byte = 1s      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'BYTE'
  var byte31: byte = 127
  var byte32: byte = -129      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'BYTE'
  var byte33: byte = 300      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'BYTE'
  var byte34 = 300
  var byte35: byte = byte34      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'BYTE'
  var byte4: byte = 1L      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'LONG', REQUIRED: 'BYTE'
  var byte5: byte = 1.1f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'BYTE'
  var byte6: byte = 1.1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'BYTE'
  var byte7: byte = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'BYTE'
  var byte8: byte = 'c'      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'BYTE'
  var byte9: byte = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'BYTE'
  var byte10: byte = new Date()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'BYTE'
  var byte11: byte = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'BYTE'
  //  IDE-1276 : OS Gosu issue in the following case
  var byte12: byte = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'BYTE'
  var byte13: byte = BigInteger.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'BYTE'
  var byte14: byte = BigDecimal.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'BYTE'
  var byte15: byte = null      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'NULL', REQUIRED: 'BYTE'

  //short
  var short1: short = 2b
  var short2: short = 2s
  var short31: short = 31111
  var short32: short = 33333      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'SHORT'
  var short4: short = 2L      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'LONG', REQUIRED: 'SHORT'
  var short5: short = 2.2f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'SHORT'
  var short6: short = 2.2      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'SHORT'
  var short7: short = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'SHORT'
  var short8: short = 'c'      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'SHORT'
  var short9: short = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'SHORT'
  var short10: short = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'SHORT'
  var short11: short = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'SHORT'
  var short12: short = BigInteger.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'SHORT'
  var short13: short = BigDecimal.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'SHORT'

  //int
  var int1: int = 3b
  var int2: int = 3s
  var int3: int = 3
  var int4: int = 3L      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'LONG', REQUIRED: 'INT'
  var int5: int = 3.3f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'INT'
  var int6: int = 3.3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'INT'
  var int7: int = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'INT'
  var int8: int = 'c'
  var int9: int = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'INT'
  var int10: int = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'INT'
  var int11: int = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'INT'
  var int12: int = BigInteger.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'INT'
  var int13: int = BigDecimal.TEN       //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'INT'

  //long
  var long1: long = 3b
  var long2: long = 3s
  var long3: long = 3
  var long4: long = 3L
  var long5: long = 3.3f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'LONG'
  var long6: long = 3.3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'LONG'
  var long7: long = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'LONG'
  var long8: long = 'c'
  var long9: long = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'LONG'
  var long10: long = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'LONG'
  var long11: long = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'LONG'
  var long12: long = BigInteger.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'LONG'
  var long13: long = BigDecimal.ZERO      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'LONG'

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
  var float62: float = somedouble       //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'FLOAT'
  var anotherDouble = 43.5
  var float63: float = anotherDouble      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'FLOAT'
  var float7: float = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'FLOAT'
  var float8: float = 'c'
  var float9: float = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'FLOAT'
  var float10: float = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'FLOAT'
  var float11: float = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'FLOAT'
  var float12: float = BigInteger.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'FLOAT'
  var float13: float = BigDecimal.TEN      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'FLOAT'

  //double
  var double1: double = 3b
  var double2: double = 3s
  var double3: double = 3
  var double4: double = 3L
  var double5: double = 3.3f
  var double6: double = 3.3
  var double7: double = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'DOUBLE'
  var double8: double = 'c'
  var double9: double = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'DOUBLE'
  var double10: double = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'DOUBLE'
  var double11: double = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'DOUBLE'
  var double12: double = BigInteger.TEN      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'DOUBLE'
  var double13: double = BigDecimal.ZERO      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'DOUBLE'

  //boolean
  var boolean1: boolean = 3b      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'BOOLEAN'
  var boolean2: boolean = 3s      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'BOOLEAN'
  var boolean3: boolean = 3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'BOOLEAN'
  var boolean4: boolean = 3L      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'LONG', REQUIRED: 'BOOLEAN'
  var boolean5: boolean = 3.3f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'BOOLEAN'
  var boolean6: boolean = 3.3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'BOOLEAN'
  var boolean7: boolean = true
  var boolean8: boolean = 'c'      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'CHAR', REQUIRED: 'BOOLEAN'
  var boolean9: boolean = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'BOOLEAN'
  var boolean10: boolean = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'BOOLEAN'
  var boolean11: boolean = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'BOOLEAN'
  var boolean12: boolean = BigInteger.TEN      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'BOOLEAN'
  var boolean13: boolean = BigDecimal.ONE      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'BOOLEAN'

  //BigInteger
  var bigInteger1: BigInteger = 3b
  var bigInteger2: BigInteger = 3s
  var bigInteger3: BigInteger = 3
  var bigInteger4: BigInteger = 3L
  var bigInteger5: BigInteger = 3.3f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigInteger6: BigInteger = 3.3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigInteger7: BigInteger = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigInteger8: BigInteger = 'c'
  var bigInteger9: BigInteger = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigInteger10: BigInteger = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigInteger11: BigInteger = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  var bigInteger12: BigInteger = BigInteger.TEN
  var bigInteger13: BigInteger = BigDecimal.ZERO      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.MATH.BIGINTEGER'

  //BigDecimal
  var bigDecimal1: BigDecimal = 3b
  var bigDecimal2: BigDecimal = 3s
  var bigDecimal3: BigDecimal = 3
  var bigDecimal4: BigDecimal = 3L
  var bigDecimal5: BigDecimal = 3.3f
  var bigDecimal6: BigDecimal = 3.3
  var bigDecimal7: BigDecimal = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimal8: BigDecimal = 'c'
  var bigDecimal9: BigDecimal = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimal10: BigDecimal = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimal11: BigDecimal = "string"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.MATH.BIGDECIMAL'
  var bigDecimal12: BigDecimal = BigInteger.TEN
  var bigDecimal13: BigDecimal = BigDecimal.ZERO

  var string1: String = 3b      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BYTE', REQUIRED: 'JAVA.LANG.STRING'
  var string2: String = 3s      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'SHORT', REQUIRED: 'JAVA.LANG.STRING'
  var string3: String = 3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
  var string4: String = 3L      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'LONG', REQUIRED: 'JAVA.LANG.STRING'
  var string5: String = 3.3f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'JAVA.LANG.STRING'
  var string6: String = 3.3      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.STRING'
  var string7: String = true      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BOOLEAN', REQUIRED: 'JAVA.LANG.STRING'
  var string8: String = 'c'
  var string9: String = date1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.DATE', REQUIRED: 'JAVA.LANG.STRING'
  var string10:String = new Object()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.OBJECT', REQUIRED: 'JAVA.LANG.STRING'
  var string11:String = "string"
  var string12:String = BigInteger.TEN      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGINTEGER', REQUIRED: 'JAVA.LANG.STRING'
  var string13:String = BigDecimal.ZERO      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'JAVA.LANG.STRING'

}