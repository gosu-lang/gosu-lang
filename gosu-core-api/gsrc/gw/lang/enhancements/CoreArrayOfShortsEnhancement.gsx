package gw.lang.enhancements
uses java.lang.Short

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfShortsEnhancement : Short[] {
  function sum() : int {
    var sum = 0
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
