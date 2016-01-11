package gw.lang.spec_old.arrays
uses java.util.ArrayList
uses java.util.AbstractList

class ArrayAccessOnCollectionsTestClass
{
  construct()
  {
    var l : java.util.List
    print( l[0] )
    
    var al : ArrayList
    print( al[0] )
    
    var ab : AbstractList
    print( ab[0] ) 
  }
}
