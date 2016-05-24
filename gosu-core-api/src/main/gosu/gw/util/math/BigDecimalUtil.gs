package gw.util.math

uses java.math.BigDecimal
uses java.math.RoundingMode

/**
 * Utility method used to compute the square root of a BigDecimal.
 *
 * Based on Java code from Luciano Culacciatti
 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
 * Adapted to Gosu by David McReynolds
 */
public class BigDecimalUtil {
  private static final var SQRT_DIG:BigDecimal  = new BigDecimal(150);
  private static final var SQRT_PRE:BigDecimal  = new BigDecimal(10).pow(SQRT_DIG.intValue())

  /**
   * Uses Newton Raphson to compute the square root of a BigDecimal.
   *
   */
  public static  function bigSqrt( c:BigDecimal ): BigDecimal {
      return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE))
  }

  private static function  sqrtNewtonRaphson  (c:BigDecimal, xn:BigDecimal, precision:BigDecimal) : BigDecimal{
    var fx = xn.pow(2).add(c.negate())
    var fpx = xn.multiply(new BigDecimal(2))
    var xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(), RoundingMode.HALF_DOWN)
    xn1 = xn.add(xn1.negate())
    var currentSquare = xn1.pow(2)
    var currentPrecision = currentSquare.subtract(c)
    currentPrecision = currentPrecision.abs()
    if (currentPrecision.compareTo(precision) <= -1){
      return xn1
    }
      return sqrtNewtonRaphson(c, xn1, precision)
  }
}
