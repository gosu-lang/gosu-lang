package gw.lang.enhancements
uses java.math.BigDecimal
uses java.lang.Iterable

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIterableOfNumbersEnhancement<N extends java.lang.Number> : Iterable<N> {

  function average() : BigDecimal {
    return this.average( \ i -> i )
  }  
}
