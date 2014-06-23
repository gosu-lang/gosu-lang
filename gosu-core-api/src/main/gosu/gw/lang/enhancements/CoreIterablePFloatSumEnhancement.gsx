package gw.lang.enhancements

uses java.lang.Iterable

/**
 * The overloaded versions of the sum() method had to be moved to separate enhancements due to the way block type erasure
 * works (all blocks with the same arity have the same erasure).  Splitting the methods up into different enhancements
 * prevents them from conflicting, since they become part of different Java classes.
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIterablePFloatSumEnhancement<T> : Iterable<T> {
  /**
   * Sums up the values of the target of the mapper argument
   */
  function sum( mapper(elt:T):float ) : float {
    var sum = 0.0 as float
    for( elt in this ) {
      sum += mapper( elt )
    }
    return sum
  }
}