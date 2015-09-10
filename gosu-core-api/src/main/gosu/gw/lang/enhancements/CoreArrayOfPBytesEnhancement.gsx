package gw.lang.enhancements
uses java.lang.Byte
uses java.util.ArrayList
uses java.util.List
uses java.lang.StringBuilder

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfPBytesEnhancement : byte[] {
  
  property get Count() : int {
    return this.length
  }

  function toList() : List<Byte> {
    var lst = new ArrayList<Byte>()
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
