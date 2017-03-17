package gw.lang.enhancements
uses java.lang.Iterable
uses java.lang.Comparable
uses gw.util.IOrderedList

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIterableOfComparablesEnhancement<T extends Comparable> : Iterable<T> {

  /**
   * Returns the minium non-null element in this collection, or null
   * if all elements are null or the colleciton is empty.
   */
  reified function min() : T {
    return this.min( \ e -> e ) 
  }

  /**
   * Returns the maximum non-null element in this collection, or null if
   * all elements are null or the collection is empty.   
   */
  reified function max() : T {
    return this.max( \ e -> e )
  }

  /**
   * Returns a new list of the elements of this collection ordered naturally
   */
  reified function order() : IOrderedList<T> {
    return this.toList().orderBy( \ c -> c )
  }
  
  /**
   * Returns a new list of the elements of this collection reverese ordered naturally
   */
  reified function orderDescending() : IOrderedList<T> {
    return this.toList().orderByDescending( \ c -> c )
  }
}