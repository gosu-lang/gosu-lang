package gw.util.math

uses java.math.BigDecimal

enhancement BigDecimalMathEnhancement : BigDecimal {
  property get SquareRoot() : BigDecimal {
    return BigDecimalUtil.bigSqrt( this )
  }
}