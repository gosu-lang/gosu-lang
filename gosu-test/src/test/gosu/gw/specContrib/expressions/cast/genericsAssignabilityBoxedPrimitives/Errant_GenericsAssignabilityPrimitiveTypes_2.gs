package gw.specContrib.expressions.cast.genericsAssignabilityBoxedPrimitives

/**
 * Primitive types - assignability where
 * the type may be assignable to another type but is not a subtype
 * Tests involve assigning list of one type to list of another type
 */
class Errant_GenericsAssignabilityPrimitiveTypes_2 {
  var charList: List<char>       //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var byteList : List<byte>      //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var shortList : List<short>    //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var intList: List<int>         //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var floatList : List<float>    //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var longList : List<long>      //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM
  var doubleList : List<double>  //## issuekeys: MSG_PRIMITIVE_TYPE_PARAM

  function testCharacter() {
    charList = charList
    charList = byteList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<BYTE>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
    charList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<CHAR>'
  }
  function testByte() {
    byteList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = byteList
    byteList = shortList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<SHORT>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
    byteList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<BYTE>'
  }
  function testShort() {
    shortList = charList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<CHAR>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    //IDE-1859 - Parser issue
    shortList = byteList        //## issuekeys: INCOMPATIBLE TYPES.
    shortList = shortList
    shortList = intList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<INT>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
    shortList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<SHORT>'
  }
  function testInteger() {
    //IDE-1859 - Parser issue
    intList = charList            //## issuekeys: INCOMPATIBLE TYPES.
    intList = byteList            //## issuekeys: INCOMPATIBLE TYPES.
    intList = shortList           //## issuekeys: INCOMPATIBLE TYPES.
    intList = intList
    intList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = longList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<LONG>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
    intList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<INT>'
  }
  function testFloat() {
    floatList = charList          //## issuekeys: INCOMPATIBLE TYPES.
    floatList = byteList          //## issuekeys: INCOMPATIBLE TYPES.
    floatList = shortList         //## issuekeys: INCOMPATIBLE TYPES.
    floatList = intList           //## issuekeys: INCOMPATIBLE TYPES.
    floatList = floatList
    floatList = longList          //## issuekeys: INCOMPATIBLE TYPES.
    floatList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<FLOAT>'
  }
  function testLong() {
    longList = charList            //## issuekeys: INCOMPATIBLE TYPES.
    longList = byteList            //## issuekeys: INCOMPATIBLE TYPES.
    longList = shortList           //## issuekeys: INCOMPATIBLE TYPES.
    longList = intList             //## issuekeys: INCOMPATIBLE TYPES.
    longList = floatList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<FLOAT>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
    longList = longList
    longList = doubleList      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<DOUBLE>', REQUIRED: 'JAVA.UTIL.LIST<LONG>'
  }
  function testDouble() {
    doubleList = charList          //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = byteList          //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = shortList        //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = intList          //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = floatList        //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = longList         //## issuekeys: INCOMPATIBLE TYPES.
    doubleList = doubleList
  }

}