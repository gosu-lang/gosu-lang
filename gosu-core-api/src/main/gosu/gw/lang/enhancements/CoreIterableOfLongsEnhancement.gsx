package gw.lang.enhancements
uses java.lang.Long

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIterableOfLongsEnhancement : java.lang.Iterable<Long> {
  function sum() : Long {
    var sum = 0 as Long
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
