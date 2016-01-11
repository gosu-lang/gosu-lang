package gw.lang.spec_old.arrays
uses java.util.LinkedList
uses java.util.Collection
uses java.util.Iterator
uses java.util.Set
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_NonListsNotAllowedAsArrayAccess
{
  construct()
  {        
    var s : Set
    print( s[0] )
    
    var iter : Iterator
    print( iter[0] )
    
    var c : Collection
    print( c[0] )
    
    var ll : LinkedList
    print( ll[0] )
  }
}
