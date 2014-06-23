package gw.lang.enhancements

uses java.util.Collection
uses java.lang.IllegalStateException
uses java.lang.Comparable
uses gw.util.IOrderedList
uses java.util.Comparator

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreCollectionEnhancement<T> : Collection<T> {

  //!! The following methods are here instead of CoreIterableEnhancement because 
  //!! they otherwise interfere with IQueryResult methods.


  /**
   * Returns a lazily-computed List that consists of the elements of this Collection, ordered
   * by the value mapped to by the given block.
   */
  function orderBy<R extends Comparable>( value(elt:T):R ) : IOrderedList<T> {
    return orderBy(value, null)
  }
  
  /**
   * Returns a lazily-computed List that consists of the elements of this Collection, ordered
   * by the value mapped to by the given block using the specified comparator.
   */
  function orderBy<R extends Comparable>( value(elt:T):R, comparator : Comparator ) : IOrderedList<T> {
    if( this typeis IOrderedList ) {
      throw new IllegalStateException( "You must only call thenBy() after an orderBy()" )
    }
    var ordered = new OrderedList<T>( this )
    ordered.addOrderBy( value, comparator )
    return ordered
  }

  /**
   * Returns a lazily-computed List that consists of the elements of this Collection, in descending order
   * by the value mapped to by the given block.
   */
  function orderByDescending<R extends Comparable>( value(elt:T):R ) : IOrderedList<T> {
    return orderByDescending(value, null)
  }

  /**
   * Returns a lazily-computed List that consists of the elements of this Collection, in descending order
   * by the value mapped to by the given block using the specified comparator.
   */
  function orderByDescending<R extends Comparable>( value(elt:T):R, comparator : Comparator ) : IOrderedList<T> {
    if( this typeis IOrderedList ) {
      throw new IllegalStateException( "You must only call thenBy() after an orderBy()" )
    }
    var ordered = new OrderedList<T>( this )
    ordered.addOrderByDescending( value, comparator )
    return ordered
  }

}