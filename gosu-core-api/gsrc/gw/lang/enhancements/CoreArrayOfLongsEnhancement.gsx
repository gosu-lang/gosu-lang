package gw.lang.enhancements
uses java.lang.Long

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfLongsEnhancement : Long[] {
  function sum() : Long {
    var sum = 0 as long
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
