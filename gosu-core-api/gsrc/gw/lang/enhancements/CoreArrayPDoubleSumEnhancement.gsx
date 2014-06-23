package gw.lang.enhancements

/**
 * The overloaded versions of the sum() method had to be moved to separate enhancements due to the way block type erasure
 * works (all blocks with the same arity have the same erasure).  Splitting the methods up into different enhancements
 * prevents them from conflicting, since they become part of different Java classes.
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayPDoubleSumEnhancement<T> : T[] {
  /**
   * Sums up the values of the target of the mapper argument
   */
  function sum( mapper(elt:T):double ) : double {
    var sum  = 0.0
    for( elt in this ) {
      sum += mapper( elt )
    }
    return sum
  }
}