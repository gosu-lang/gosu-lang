package gw.lang.enhancements

uses java.math.BigDecimal

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreBigDecimalEnhancement : BigDecimal
{
  property get IsZero() : boolean
  {
    return BigDecimal.ZERO === this ||
           this.compareTo( BigDecimal.ZERO ) == 0
  }
}
