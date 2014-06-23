package gw.lang.enhancements
uses java.util.ArrayList
uses java.lang.StringBuilder

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfPBooleansEnhancement : boolean[] {
  
  property get Count() : int {
    return this.length
  }

  function toList() : List<Boolean> {
    var lst = new ArrayList<Boolean>()
    for( elt in this ) {
      lst.add( elt ) 
    }
    return lst
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
