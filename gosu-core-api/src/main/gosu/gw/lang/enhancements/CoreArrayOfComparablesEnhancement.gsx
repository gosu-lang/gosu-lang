package gw.lang.enhancements
uses java.lang.Comparable
uses java.util.Collections
uses java.util.Arrays
uses java.util.Comparator

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayOfComparablesEnhancement<T extends Comparable> : T[]
{
  /**
   * Returns the minium non-null element in this collection, or null
   * if all elements are null or the colleciton is empty.
   */
  reified function min() : T {
    return this.toList().min( \ e -> e )
  }

  /**
   * Returns the maximum non-null element in this collection, or null if
   * all elements are null or the collection is empty.
   */
  reified function max() : T {
    return this.toList().max( \ e -> e )
  }

  /**
   * Sorts this array on the natural order of its elements
   */
  function sort() : T[] {
    return sort(null)
  }
  
  /**
   * Sorts this array using the comparator
   */
  function sort(comparator : Comparator) : T[]{
    if( this typeis Object[] ) {
      if (comparator != null) {
        Arrays.sort( this, comparator)
      } else {
        Arrays.sort( this )
      }
    } else {
      throw "This array is not a java-based non-primitive array, and thus is not sortable"
    }
    return this
  }
  
  /**
   * Sorts this array descending on the natural order of its elements
   */
  function sortDescending() : T[] {
    return sortDescending(null)
  }

  /**
   * Sorts this array descending usint the comparator
   */  
  function sortDescending(comparator : Comparator) : T[]{
    if( this typeis Object[] ) {
      if (comparator != null) {
        var descendingComparator = new Comparator() {
          override function compare(o1 : Object, o2 : Object) : int {
            var cmp = comparator.compare(o1, o2)
            return cmp < 0 ? 1 : - cmp
          }
        }
        Arrays.sort( this, descendingComparator )
      } else {
        Arrays.sort( this, Collections.reverseOrder() )
      }
    } else {
      throw "This array is not a java-based non-primitive array, and thus is not sortable"
    }
    return this
  }
  
}
