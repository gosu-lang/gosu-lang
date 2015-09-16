package gw.lang.enhancements
uses java.lang.Comparable
uses java.util.Collections
uses java.util.Comparator
uses java.util.List

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreListOfComparablesEnhancement<T extends Comparable> : List<T>
{
  function sort() : List<T> {
    Collections.sort( this )
    return this
  }

//
//## note: removed to support Java 8 where default interface method List#sort(Comparator) replaces this, but it does not return itself :(
//
//  function sort( comparator : Comparator ) : List<T> {
//    Collections.sort( this, comparator )
//    return this
//  }
//

  function sortDescending() : List<T> {
    Collections.sort( this, Collections.reverseOrder<T>() )
    return this
  }
  
  function sortDescending( comparator : Comparator ) : List<T> {
    if (comparator != null) {
      var descendingComparator = new Comparator() {
        override function compare(o1 : Object, o2 : Object) : int {
          var cmp = comparator.compare(o1, o2)
          return cmp < 0 ? 1 : - cmp 
        }
      }
      Collections.sort( this, descendingComparator )
    } else {
      Collections.sort( this, Collections.reverseOrder() )
    }
    return this
  }

}
