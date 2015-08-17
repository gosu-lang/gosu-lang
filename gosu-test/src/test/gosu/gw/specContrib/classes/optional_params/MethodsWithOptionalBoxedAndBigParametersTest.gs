package gw.specContrib.classes.optional_params

uses java.math.BigDecimal
uses java.math.BigInteger
uses gw.BaseVerifyErrantTest

class MethodsWithOptionalBoxedAndBigParametersTest extends BaseVerifyErrantTest {
  final static var m : int = 55
  final static var b: boolean = false;
  
  static function boxedInt(i : Integer = m): Integer { return i }
  static function boxedLong(i : Long = m): Long { return i }
  static function boxedFloat(i : Float = m): Float { return i }
  static function boxedDouble(i : Double = m): Double { return i }
  static function boxedChar(i : Character = m as Character): Character { return i}
  static function boxedBoolean(i : Boolean = b): Boolean { return i }
  static function bigDec(i : BigDecimal = m): BigDecimal { return i }
  static function bigInt(i : BigInteger = m): BigInteger { return i }

  static function boxedIntList(i : List<Integer> = {m}): List<Integer> { return i }
  static function boxedLongList(i : List<Long> = {m}): List<Long> { return i }
  static function boxedFloatList(i : List<Float> = {m}): List<Float> { return i }
  static function boxedDoubleList(i : List<Double> = {m}): List<Double> { return i }
  static function boxedCharList(i : List<Character> = {m as char} ): List<Character> { return i}
  static function boxedBooleanList(i : List<Boolean> = {b}): List<Boolean> { return i }
  static function bigDecList(i : List<BigDecimal> = {m}): List<BigDecimal> { return i }
  static function bigIntList(i : List<BigInteger> = {m}): List<BigInteger> { return i }

  static function boxedIntArray(i : Integer[] = {m}): Integer[] { return i }
  static function boxedLongArray(i : Long[] = {m}): Long[] { return i }
  static function boxedFloatArray(i : Float[] = {m}): Float[] { return i }
  static function boxedDoubleArray(i : Double[] = {m}): Double[] { return i }
  static function boxedCharArray(i : Character[] = {m as Character}): Character[] { return i}
  static function boxedBooleanArray(i : Boolean[] = {false}): Boolean[] { return i }
  static function bigDecArray(i : BigDecimal[] = {m}): BigDecimal[] { return i }
  static function bigIntArray(i : BigInteger[] = {m}): BigInteger[] { return i }

  static function boxedInt_Literal(i : Integer = 88): Integer { return i }
  static function boxedLong_Literal(i : Long = 88): Long { return i }
  static function boxedFloat_Literal(i : Float = 88): Float { return i }
  static function boxedDouble_Literal(i : Double = 88): Double { return i }
  static function boxedChar_Literal(i : Character = 88 as Character): Character { return i}
  static function boxedBoolean_Literal(i : Boolean = false): Boolean { return i }
  static function bigDec_Literal(i : BigDecimal = 88): BigDecimal { return i }
  static function bigInt_Literal(i : BigInteger = 88): BigInteger { return i }

  static function boxedIntList_Literal(i : List<Integer> = {88}): List<Integer> { return i }
  static function boxedLongList_Literal(i : List<Long> = {88}): List<Long> { return i }
  static function boxedFloatList_Literal(i : List<Float> = {88}): List<Float> { return i }
  static function boxedDoubleList_Literal(i : List<Double> = {88}): List<Double> { return i }
  static function boxedCharList_Literal(i : List<Character> = {88 as char}): List<Character> { return i}
  static function boxedBooleanList_Literal(i : List<Boolean> = {false}): List<Boolean> { return i }
  static function bigDecList_Literal(i : List<BigDecimal> = {88}): List<BigDecimal> { return i }
  static function bigIntList_Literal(i : List<BigInteger> = {88}): List<BigInteger> { return i }

  static function boxedIntArray_Literal(i : Integer[] = {88}): Integer[] { return i }
  static function boxedLongArray_Literal(i : Long[] = {88}): Long[] { return i }
  static function boxedFloatArray_Literal(i : Float[] = {88}): Float[] { return i }
  static function boxedDoubleArray_Literal(i : Double[] = {88}): Double[] { return i }
  static function boxedCharArray_Literal(i : Character[] = {88 as Character}): Character[] { return i}
  static function boxedBooleanArray_Literal(i : Boolean[] = {false}): Boolean[] { return i }
  static function bigDecArray_Literal(i : BigDecimal[] = {88}): BigDecimal[] { return i }
  static function bigIntArray_Literal(i : BigInteger[] = {88}): BigInteger[] { return i }

  static function boxedIntList_Empty(i : List<Integer> = {}): List<Integer> { return i }
  static function boxedLongList_Empty(i : List<Long> = {}): List<Long> { return i }
  static function boxedFloatList_Empty(i : List<Float> = {}): List<Float> { return i }
  static function boxedDoubleList_Empty(i : List<Double> = {}): List<Double> { return i }
  static function boxedCharList_Empty(i : List<Character> = {}): List<Character> { return i}
  static function boxedBooleanList_Empty(i : List<Boolean> = {}): List<Boolean> { return i }
  static function bigDecList_Empty(i : List<BigDecimal> = {}): List<BigDecimal> { return i }
  static function bigIntList_Empty(i : List<BigInteger> = {}): List<BigInteger> { return i }

  static function boxedIntArray_Empty(i : Integer[] = {}): Integer[] { return i }
  static function boxedLongArray_Empty(i : Long[] = {}): Long[] { return i }
  static function boxedFloatArray_Empty(i : Float[] = {}): Float[] { return i }
  static function boxedDoubleArray_Empty(i : Double[] = {}): Double[] { return i }
  static function boxedCharArray_Empty(i : Character[] = {}): Character[] { return i}
  static function boxedBooleanArray_Empty(i : Boolean[] = {}): Boolean[] { return i }
  static function bigDecArray_Empty(i : BigDecimal[] = {}): BigDecimal[] { return i }
  static function bigIntArray_Empty(i : BigInteger[] = {}): BigInteger[] { return i }
  
  function testBoxedAndBigOptionalParams() {
    assertEquals( boxedInt(), m )
    assertEquals( boxedLong(), m as long )
    assertEquals( boxedFloat(), m as float )
    assertEquals( boxedDouble(), m as Double )
    assertEquals( boxedChar(), m as Character )
    assertEquals( boxedBoolean(), b )
    assertEquals( bigDec(), m as BigDecimal )
    assertEquals( bigInt(), m as BigInteger )

    assertEquals( boxedIntList(), {m} )
    assertEquals( boxedLongList(), {m as long} as List<long> )
    assertEquals( boxedFloatList(), {m as float} as List<float> )
    assertEquals( boxedDoubleList(), {m as double} as List<Double> )
    assertEquals( boxedCharList(), {m as char} as List<Character> )
    assertEquals( boxedBooleanList(), {b} )
    assertEquals( bigDecList(), {m as BigDecimal} as List<BigDecimal> )
    assertEquals( bigIntList(), {m as BigInteger} as List<BigInteger> )
  
    assertArrayEquals( boxedIntArray(), new Integer[] {m} )
    assertArrayEquals( boxedLongArray(), new Long[] {m} )
    assertArrayEquals( boxedFloatArray(), new Float[] {m} )
    assertArrayEquals( boxedDoubleArray(), new Double[] {m} )
    assertArrayEquals( boxedCharArray(), new Character[] {m as char} )
    assertArrayEquals( boxedBooleanArray(), {b} )
    assertArrayEquals( bigDecArray(), new BigDecimal[] {m as BigDecimal})
    assertArrayEquals( bigIntArray(), new BigInteger[] {m as BigInteger} )
  
    assertEquals( boxedInt_Literal(), 88 )
    assertEquals( boxedLong_Literal(), 88 as long )
    assertEquals( boxedFloat_Literal(), 88 as float )
    assertEquals( boxedDouble_Literal(), 88 as Double )
    assertEquals( boxedChar_Literal(), 88 as Character )
    assertEquals( boxedBoolean_Literal(), false )
    assertEquals( bigDec_Literal(), 88 as BigDecimal )
    assertEquals( bigInt_Literal(), 88 as BigInteger )

    assertEquals( boxedIntList_Literal(), {88} )
    assertEquals( boxedLongList_Literal(), {88L} )
    assertEquals( boxedFloatList_Literal(), {88F} as List<Float> )
    assertEquals( boxedDoubleList_Literal(), {88D} as List<Double> )
    assertEquals( boxedCharList_Literal(), {88 as Character} as List<Character> )
    assertEquals( boxedBooleanList_Literal(), {b} )
    assertEquals( bigDecList_Literal(), {88BD} as List<BigDecimal> )
    assertEquals( bigIntList_Literal(), {88BI} as List<BigInteger> )
  
    assertArrayEquals( boxedIntArray_Literal(), new Integer[]  {88} )
    assertArrayEquals( boxedLongArray_Literal(), new Long[] {88L} )
    assertArrayEquals( boxedFloatArray_Literal(), new Float[] {88F} )
    assertArrayEquals( boxedDoubleArray_Literal(), new Double[] {88D}  )
    assertArrayEquals( boxedCharArray_Literal(), new Character[] {88 as Character} )
    assertArrayEquals( boxedBooleanArray_Literal(), {false} )
    assertArrayEquals( bigDecArray_Literal(), new BigDecimal[] {88}  )
    assertArrayEquals( bigIntArray_Literal(), new BigInteger[] {88} )
  
    assertEquals( boxedIntList_Empty(), {} )
    assertEquals( boxedLongList_Empty(), {} )
    assertEquals( boxedFloatList_Empty(), {} as List<Float> )
    assertEquals( boxedDoubleList_Empty(), {} as List<Double> )
    assertEquals( boxedCharList_Empty(), {} as List<Character> )
    assertEquals( boxedBooleanList_Empty(), {} )
    assertEquals( bigDecList_Empty(), {} as List<BigDecimal> )
    assertEquals( bigIntList_Empty(), {} as List<BigInteger> )
  
    assertArrayEquals( boxedIntArray_Empty(), new Integer[]  {} )
    assertArrayEquals( boxedLongArray_Empty(), new Long[] {} )
    assertArrayEquals( boxedFloatArray_Empty(), new Float[] {} )
    assertArrayEquals( boxedDoubleArray_Empty(), new Double[] {}  )
    assertArrayEquals( boxedCharArray_Empty(), new Character[] {} )
    assertArrayEquals( boxedBooleanArray_Empty(), {} )
    assertArrayEquals( bigDecArray_Empty(), new BigDecimal[] {}  )
    assertArrayEquals( bigIntArray_Empty(), new BigInteger[] {} )
  }
}