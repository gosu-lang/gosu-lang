package gw.specContrib.expressions.cast.genericsAssignabilityBoxedPrimitives

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

/**
 * Boxed types & Primitive types
 * the type may be assignable to another type but is not a subtype
 * Tests involve assigning list of one type to list of another type
 */
class Errant_GenericsAssignabilityBoxedPrimitiveTypes_3 {

  var charList: List<char>         //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var byteList : List<byte>        //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var shortList : List<short>      //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var intList: List<int>           //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var floatList : List<float>      //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var longList : List<long>        //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var doubleList : List<double>    //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM


  var characterBoxedList: List<Character>
  var byteBoxedList: List<Byte>
  var shortBoxedList: List<Short>
  var integerBoxedList: List<Integer>
  var floatBoxedList: List<Float>
  var longBoxedList: List<Long>
  var doubleBoxedList: List<Double>

  var bigIntegerList : List<BigInteger>
  var bigDecimalList : List<BigDecimal>

  var objectList : List<Object>
  var stringList : List<String>
  var booleanList : List<Boolean>
  var dateList : List<DateTime>

  var numberList : List<java.lang.Number>

  function testCharacter() {
    characterBoxedList = charList
    characterBoxedList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterBoxedList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterBoxedList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterBoxedList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterBoxedList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterBoxedList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'

  }
  function testByte() {
    byteBoxedList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteBoxedList = byteList
    byteBoxedList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteBoxedList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteBoxedList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteBoxedList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteBoxedList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'

  }
  function testShort() {
    shortBoxedList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortBoxedList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortBoxedList = shortList
    shortBoxedList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortBoxedList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortBoxedList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortBoxedList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'

   }
  function testInteger() {
    integerBoxedList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerBoxedList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerBoxedList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerBoxedList = intList
    integerBoxedList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerBoxedList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerBoxedList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
   }
  function testFloat() {
    floatBoxedList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatBoxedList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatBoxedList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatBoxedList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatBoxedList = floatList
    floatBoxedList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatBoxedList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'

   }
  function testLong() {
    longBoxedList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longBoxedList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longBoxedList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longBoxedList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longBoxedList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longBoxedList = longList
    longBoxedList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
  }
  function testDouble() {
    doubleBoxedList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleBoxedList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleBoxedList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleBoxedList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleBoxedList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleBoxedList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleBoxedList = doubleList
   }
  function testBigInteger() {
    bigIntegerList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
  }
  function testBigDecimal() {
    bigDecimalList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
   }
  function testObject() {
    objectList = charList
    objectList = byteList
    objectList = shortList
    objectList = intList
    objectList = floatList
    objectList = longList
    objectList = doubleList
   }
  function testString() {
    stringList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'

  }
  function testBoolean() {
    booleanList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
  }
  function testDate() {
    dateList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
  }
  function testNumber() {
    numberList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>'
    numberList = byteList
    numberList = shortList
    numberList = intList
    numberList = floatList
    numberList = longList
    numberList = doubleList
  }
}