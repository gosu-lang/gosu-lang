package gw.lang.enhancements
uses java.lang.Double

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIterableOfDoublesEnhancement : java.lang.Iterable<Double> {
  function sum() : Double {
    var sum = 0.0
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
