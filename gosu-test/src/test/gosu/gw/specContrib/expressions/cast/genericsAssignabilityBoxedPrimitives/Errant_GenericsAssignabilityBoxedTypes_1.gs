package gw.specContrib.expressions.cast.genericsAssignabilityBoxedPrimitives

uses java.lang.*
uses java.math.BigDecimal
uses java.math.BigInteger

/**
 * Boxed types - assignability where
 * the type may be assignable to another type but is not a subtype
 * Tests involve assigning list of one type to list of another type
 */
class Errant_GenericsAssignabilityBoxedTypes_1 {
  var characterList : List<Character>
  var byteList : List<Byte>
  var shortList : List<Short>
  var integerList : List<Integer>
  var floatList : List<Float>
  var longList : List<Long>
  var doubleList : List<Double>

  var bigIntegerList : List<BigInteger>
  var bigDecimalList : List<BigDecimal>

  var objectList : List<Object>
  var stringList : List<String>
  var booleanList : List<Boolean>
  var dateList : List<DateTime>

  var numberList : List<java.lang.Number>

  function testCharacter() {
    characterList = characterList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    characterList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'

    characterList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'

    characterList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
    characterList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>'
  }
  function testByte() {
    byteList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = byteList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    byteList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
    byteList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>'
  }
  function testShort() {
    shortList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = shortList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    shortList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
    shortList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>'
  }
  function testInteger() {
    integerList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = integerList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    integerList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
    integerList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
  }
  function testFloat() {
    floatList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = floatList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    floatList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
    floatList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>'
  }
  function testLong() {
    longList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = longList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    longList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
    longList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>'
  }
  function testDouble() {
    doubleList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = doubleList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    doubleList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
    doubleList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>'
  }
  function testBigInteger() {
    bigIntegerList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = bigIntegerList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    bigIntegerList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
    bigIntegerList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>'
  }
  function testBigDecimal() {
    bigDecimalList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = bigDecimalList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    bigDecimalList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
    bigDecimalList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>'
  }
  function testObject() {
    objectList = characterList
    objectList = byteList
    objectList = shortList
    objectList = integerList
    objectList = floatList
    objectList = longList
    objectList = doubleList
    objectList = bigIntegerList
    objectList = bigDecimalList
    objectList = objectList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    objectList = stringList
    objectList = booleanList
    objectList = dateList
    objectList = numberList
  }
  function testString() {
    stringList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = stringList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    stringList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
    stringList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>'
  }
  function testBoolean() {
    booleanList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = booleanList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    booleanList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
    booleanList = numberList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>'
  }
  function testDate() {
    dateList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BYTE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.SHORT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = integerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.LONG>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = bigIntegerList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGINTEGER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = bigDecimalList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.MATH.BIGDECIMAL>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>'
    dateList = dateList  //## issuekeys: MSG_SILLY_ASSIGNMENT
    dateList = numberList      //## issuekeys: MSG_TYPE_MISMATCH
  }
  function testNumber() {
    numberList = characterList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.CHARACTER>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>'
    numberList = byteList
    numberList = shortList
    numberList = integerList
    numberList = floatList
    numberList = longList
    numberList = doubleList
    numberList = bigIntegerList
    numberList = bigDecimalList
    numberList = objectList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>'
    numberList = stringList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>'
    numberList = booleanList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.BOOLEAN>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>'
    numberList = dateList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.LIST<JAVA.LANG.NUMBER>'
    numberList = numberList  //## issuekeys: MSG_SILLY_ASSIGNMENT
  }


}