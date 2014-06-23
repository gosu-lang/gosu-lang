package gw.lang.enhancements
uses java.math.BigInteger

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfBigIntegersEnhancement : BigInteger[] {
  function sum() : BigInteger {
    var sum = BigInteger.ZERO
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
