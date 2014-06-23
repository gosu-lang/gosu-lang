package gw.lang.enhancements

uses java.math.BigInteger

/**
 * The overloaded versions of the sum() method had to be moved to separate enhancements due to the way block type erasure
 * works (all blocks with the same arity have the same erasure).  Splitting the methods up into different enhancements
 * prevents them from conflicting, since they become part of different Java classes.
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayBigIntegerSumEnhancement<T> : T[] {
  /**
   * Sums up the values of the target of the mapper argument
   */
  function sum( mapper(elt:T):BigInteger ) : BigInteger {
    var sum  = new BigInteger("0")
    for( elt in this ) {
      sum += mapper( elt )
    }
    return sum
  }
}