package gw.lang.enhancements
uses java.util.ArrayList
uses java.util.List
uses java.lang.Integer
uses java.lang.StringBuilder
uses java.math.BigDecimal

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfPIntsEnhancement : int[] {

  property get Count() : int {
    return this.length
  }

  function toList() : List<Integer> {
    var lst = new ArrayList<Integer>()
    for( elt in this ) {
      lst.add( elt ) 
    }
    return lst
  }

  function sum() : int {
    var sum = 0
    for (elt in this) {
      sum += elt  
    }
    return sum
  }

  function average() : BigDecimal {
     return (this.sum() as BigDecimal) / (this.Count as BigDecimal)
  }

  function join( delimiter : String  ) : String {
    var retVal = new StringBuilder()
    for( elt in this index i ) {
      if( i > 0 ) {
        retVal.append( delimiter )
      }
      retVal.append( elt as String )
    }
    return retVal.toString()
  }
  
}
