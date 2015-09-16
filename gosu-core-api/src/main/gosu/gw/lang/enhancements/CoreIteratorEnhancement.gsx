package gw.lang.enhancements
uses java.util.Iterator
uses java.util.LinkedList
uses java.util.List

/**
 * Defines the core enhancements to the standard java.util.Iterator interface that
 * Guidewire provides.
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIteratorEnhancement<T> : java.util.Iterator<T> {

  /**
   * Note that this is a one-shot method, with the whole content of the
   * iterator copied to the list.  After calling this method, the iterator
   * will be exhausted, and future invocations will return an empty list!
   * (Too bad we can't differentiate between an empty iterator and an
   *  iterator that has already been iterated.)
   */
  function toList() : List<T> {
    var ll = new LinkedList<T>()
    for( e in this ) {
      ll.add( e )
    }
    return ll
  }
  
  
}
