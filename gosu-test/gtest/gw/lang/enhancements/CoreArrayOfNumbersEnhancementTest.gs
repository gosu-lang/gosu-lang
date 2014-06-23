package gw.lang.enhancements
uses gw.test.TestClass
uses java.lang.Integer
uses java.lang.Short
uses java.lang.Long
uses java.lang.Double
uses java.lang.Float
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.lang.ArithmeticException

class CoreArrayOfNumbersEnhancementTest extends TestClass
{  
  
  function testSum() {
    assertEquals( 0, new short[]{}.sum() )
    assertEquals( 0, new int[]{}.sum() )
    assertEquals( 0 as long, new long[]{}.sum() )
    assertEquals( 0 as float, new float[]{}.sum(), 0.01f )
    assertEquals( 0 as double, new double[]{}.sum(), 0.01 )
    assertEquals( 0, new Short[]{}.sum() )
    assertEquals( 0 as Integer, new Integer[]{}.sum() )
    assertEquals( 0 as Long, new Long[]{}.sum() )
    assertEquals( 0 as Float, new Float[]{}.sum() )
    assertEquals( 0 as Double, new Double[]{}.sum() )
    assertEquals( 0 as BigDecimal, new BigDecimal[]{}.sum() )
    assertEquals( 0 as BigInteger, new BigInteger[]{}.sum() )
    
    assertEquals( 6, new short[]{1, 2, 3}.sum() )
    assertEquals( 6, new int[]{1, 2, 3}.sum() )
    assertEquals( 6 as long, new long[]{1, 2, 3}.sum() )
    assertEquals( 6.6000004 as float, new float[]{1.1, 2.2, 3.3}.sum(), 0.01f )
    assertEquals( 6.6, new double[]{1.1, 2.2, 3.3}.sum(), 0.01 )
    assertEquals( 6, new Short[]{1, 2, 3}.sum() )
    assertEquals( 6 as Integer, new Integer[]{1, 2, 3}.sum() )
    assertEquals( 6 as Long, new Long[]{1, 2, 3}.sum() )
    assertEquals( 6.6000004 as Float, new Float[]{1.1, 2.2, 3.3}.sum() )
    assertEquals( 6.6 as Double, new Double[]{1.1, 2.2, 3.3}.sum() )
    assertEquals( 6.6 as BigDecimal, new BigDecimal[]{1.1, 2.2, 3.3}.sum() )
    assertEquals( 6 as BigInteger, new BigInteger[]{1, 2, 3}.sum() )
  }

  function testAverage() {
    assertEquals( 1.5 as BigDecimal, new int[]{1, 2}.average() )
    assertEquals( 2 as BigDecimal, new int[]{1, 2, 3}.average() )
    assertEquals( 2 as BigDecimal, new int[]{1, 2, 3, 1, 2, 3}.average() )
    assertEquals( 2 as BigDecimal, new int[]{1, 2, 3, 1, 2, 3, 1, 2, 3}.average() )
    assertEquals( 0 as BigDecimal, new int[]{-1, 1}.average() )
    assertEquals( 0 as BigDecimal, new int[]{-1, 0, 1}.average() )
    assertEquals( 0 as BigDecimal, new int[]{-2, -1, 0, 1, 2}.average() )

    assertEquals( 1.5 as BigDecimal, new int[]{1, 2}.average() )
    assertEquals( 2.0 as BigDecimal, new float[]{1.0, 2.0, 3.0}.average() )
    assertEquals( 2.0 as BigDecimal, new float[]{1.0, 2.0, 3.0, 1.0, 2.0, 3.0}.average() )
    assertEquals( 2.0 as BigDecimal, new float[]{1.0, 2.0, 3.0, 1.0, 2.0, 3.0, 1.0, 2.0, 3.0}.average() )
    assertEquals( 0.0 as BigDecimal, new double[]{-1.0, 1.0}.average() )
    assertEquals( 0.0 as BigDecimal, new float[]{-1.0, 0.0, 1.0}.average() )
    assertEquals( 0.0 as BigDecimal, new float[]{-2.0, -1.0, 0.0, 1.0, 2.0}.average() )
  }

}