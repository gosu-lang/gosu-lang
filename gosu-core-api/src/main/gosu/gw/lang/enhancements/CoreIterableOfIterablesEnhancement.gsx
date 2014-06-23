package gw.lang.enhancements
uses java.lang.Iterable
uses java.util.Iterator

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIterableOfIterablesEnhancement<E, T extends Iterable<E>> : Iterable<T> {
  function flatten() : Iterable<E> {
    var outerIterable = this
    return new Iterable<E>() {
      override function iterator() : Iterator<E> { 
        return outerIterable.map(\ t -> t.iterator()).iterator().flatten()
      }
    }
  }  
}