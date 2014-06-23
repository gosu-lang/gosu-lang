package gw.lang.enhancements
uses java.lang.Double
uses java.util.ArrayList
uses java.lang.StringBuilder
uses java.math.BigDecimal

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfPDoublesEnhancement : double[] {

  property get Count() : int {
    return this.length
  }

  function toList() : List<Double> {
    var lst = new ArrayList<Double>()
    for( elt in this ) {
      lst.add( elt ) 
    }
    return lst
  }
  
  function sum() : double {
    var sum = 0.0
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
