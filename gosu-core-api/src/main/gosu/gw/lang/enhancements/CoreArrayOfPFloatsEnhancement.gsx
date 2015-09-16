package gw.lang.enhancements
uses java.lang.Float
uses java.util.ArrayList
uses java.util.List
uses java.lang.StringBuilder
uses java.math.BigDecimal

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfPFloatsEnhancement : float[] {
  
  property get Count() : int {
    return this.length
  }

  function toList() : List<Float> {
    var lst = new ArrayList<Float>()
    for( elt in this ) {
      lst.add( elt ) 
    }
    return lst
  }
  
  function sum() : float {
    var sum = 0.0 as float
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
