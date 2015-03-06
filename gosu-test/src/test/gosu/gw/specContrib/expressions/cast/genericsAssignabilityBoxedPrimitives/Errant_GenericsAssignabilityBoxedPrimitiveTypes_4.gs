package gw.specContrib.expressions.cast.genericsAssignabilityBoxedPrimitives

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

/**
 * Primitives & Boxed types
 * the type may be assignable to another type but is not a subtype
 * Tests involve assigning list of one type to list of another type
 */
class Errant_GenericsAssignabilityBoxedPrimitiveTypes_4 {

  var charList: List<char>          //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var byteList : List<byte>         //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var shortList : List<short>       //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var intList: List<int>            //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var floatList : List<float>       //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var longList : List<long>         //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var doubleList : List<double>     //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM


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
    charList = characterBoxedList
    charList = byteBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = shortBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = integerBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = floatBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = longBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = doubleBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'

  }
  function testByte() {
    byteList = characterBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = byteBoxedList
    byteList = shortBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = integerBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = floatBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = longBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = doubleBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'

  }
  function testShort() {
    shortList = characterBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    //IDE-1859 - Parser issue
    shortList = byteBoxedList           //## issuekeys: INCOMPATIBLE TYPES.
    shortList = shortBoxedList
    shortList = integerBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = floatBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = longBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = doubleBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'

   }
  function testInteger() {
    //IDE-1859 - Parser issue
    intList = characterBoxedList      //## issuekeys: INCOMPATIBLE TYPES.
    intList = byteBoxedList            //## issuekeys: INCOMPATIBLE TYPES.
    intList = shortBoxedList           //## issuekeys: INCOMPATIBLE TYPES.
    intList = integerBoxedList
    intList = floatBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = longBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = doubleBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
   }
  function testFloat() {
    floatList = characterBoxedList     //## issuekeys: INCOMPATIBLE TYPES.
    floatList = byteBoxedList          //## issuekeys: INCOMPATIBLE TYPES.
    floatList = shortBoxedList          //## issuekeys: INCOMPATIBLE TYPES.
    floatList = integerBoxedList        //## issuekeys: INCOMPATIBLE TYPES.
    floatList = floatBoxedList
    floatList = longBoxedList           //## issuekeys: INCOMPATIBLE TYPES.
    floatList = doubleBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'
    floatList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'
    floatList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'
    floatList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'
    floatList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'
    floatList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'
    floatList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'

   }
  function testLong() {
    longList = characterBoxedList      //## issuekeys: INCOMPATIBLE TYPES.
    longList = byteBoxedList           //## issuekeys: INCOMPATIBLE TYPES.
    longList = shortBoxedList           //## issuekeys: INCOMPATIBLE TYPES.
    longList = integerBoxedList         //## issuekeys: INCOMPATIBLE TYPES.
    longList = floatBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = longBoxedList
    longList = doubleBoxedList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
  }
  function testDouble() {
    doubleList = characterBoxedList       //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = byteBoxedList            //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = shortBoxedList            //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = integerBoxedList          //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = floatBoxedList            //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = longBoxedList             //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = doubleBoxedList
    doubleList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<DOUBLE>'
    doubleList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<DOUBLE>'
    doubleList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<DOUBLE>'
    doubleList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<DOUBLE>'
    doubleList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<DOUBLE>'
    doubleList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<DOUBLE>'
   }
}