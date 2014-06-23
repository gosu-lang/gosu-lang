package gw.lang.enhancements
uses java.lang.*
uses java.math.*
uses java.util.ArrayList
uses gw.test.TestClass

class CoreIterableOfNumbersEnhancementTest extends TestClass
{
  function testSum() {
//    assertEquals( 0 as short, new ArrayList<short>(){}.sum() )
//    assertEquals( 0 as int, new ArrayList<int>(){}.sum() )
//    assertEquals( 0 as long, new ArrayList<long>(){}.sum() )
//    assertEquals( 0 as float, new ArrayList<float>(){}.sum() )
//    assertEquals( 0 as double, new ArrayList<double>(){}.sum() )
    assertEquals( 0, new ArrayList<Short>(){}.sum() )
    assertEquals( 0 as Integer, new ArrayList<Integer>(){}.sum() )
    assertEquals( 0 as Long, new ArrayList<Long>(){}.sum() )
    assertEquals( 0 as Float, new ArrayList<Float>(){}.sum() )
    assertEquals( 0 as Double, new ArrayList<Double>(){}.sum() )
    assertEquals( 0 as BigDecimal, new ArrayList<BigDecimal>(){}.sum() )
    assertEquals( 0 as BigInteger, new ArrayList<BigInteger>(){}.sum() )
    
//    assertEquals( 6 as short, new ArrayList<short>(){1, 2, 3}.sum() )
//    assertEquals( 6 as int, new ArrayList<int>(){1, 2, 3}.sum() )
//    assertEquals( 6 as long, new ArrayList<long>(){1, 2, 3}.sum() )
//    assertEquals( 6 as float, new ArrayList<float>(){1, 2, 3}.sum() )
//    assertEquals( 6 as double, new ArrayList<double>(){1, 2, 3}.sum() )
    assertEquals( 6, new ArrayList<Short>(){1, 2, 3}.sum() )
    assertEquals( 6 as Integer, new ArrayList<Integer>(){1, 2, 3}.sum() )
    assertEquals( 6 as Long, new ArrayList<Long>(){1, 2, 3}.sum() )
    assertEquals( 6.6000004 as Float, new ArrayList<Float>(){1.1, 2.2, 3.3}.sum() )
    assertEquals( 6.6 as Double, new ArrayList<Double>(){1.1, 2.2, 3.3}.sum() )
    assertEquals( 6.6 as BigDecimal, new ArrayList<BigDecimal>(){1.1, 2.2, 3.3}.sum() )
    assertEquals( 6 as BigInteger, new ArrayList<BigInteger>(){1, 2, 3}.sum() )
  }

  function testAverage() {
    assertEquals( 1.5 as BigDecimal, {1, 2}.average() )
    assertEquals( 2 as BigDecimal, {1, 2, 3}.average() )
    assertEquals( 2 as BigDecimal, {1, 2, 3, 1, 2, 3}.average() )
    assertEquals( 2 as BigDecimal, {1, 2, 3, 1, 2, 3, 1, 2, 3}.average() )
    assertEquals( 0 as BigDecimal, {-1, 1}.average() )
    assertEquals( 0 as BigDecimal, {-1, 0, 1}.average() )
    assertEquals( 0 as BigDecimal, {-2, -1, 0, 1, 2}.average() )

    assertEquals( 1.5 as BigDecimal, {1.0, 2.0}.average() )
    assertEquals( 2.0 as BigDecimal, {1.0, 2.0, 3.0}.average() )
    assertEquals( 2.0 as BigDecimal, {1.0, 2.0, 3.0, 1.0, 2.0, 3.0}.average() )
    assertEquals( 2.0 as BigDecimal, {1.0, 2.0, 3.0, 1.0, 2.0, 3.0, 1.0, 2.0, 3.0}.average() )
    assertEquals( 0.0 as BigDecimal, {-1.0, 1.0}.average() )
    assertEquals( 0.0 as BigDecimal, {-1.0, 0.0, 1.0}.average() )
    assertEquals( 0.0 as BigDecimal, {-2.0, -1.0, 0.0, 1.0, 2.0}.average() )
  }

}
