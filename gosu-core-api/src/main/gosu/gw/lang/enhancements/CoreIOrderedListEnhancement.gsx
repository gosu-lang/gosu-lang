package gw.lang.enhancements

uses java.lang.Comparable
uses gw.util.IOrderedList
uses gw.lang.enhancements.OrderedList
uses java.lang.IllegalStateException
uses java.util.Comparator

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIOrderedListEnhancement<T> : IOrderedList<T> {

  /**
   * Returns a new lazily constructed list secondarily ordered by the given
   * block
   */
  reified function thenBy<R extends Comparable>( by(elt:T):R, comparator : Comparator = null ) : IOrderedList<T> {
    if( this typeis OrderedList ) {
      var typedThis = this as OrderedList<T>
      return typedThis.addThenBy( by, comparator )
    } else {
      throw new IllegalStateException( "Cannot call thenBy() on anything except an OrderedList" )
    }
  }
  
  /**
   * Returns a new lazily constructed list secondarily descendingly ordered by the given
   * block
   */
  reified function thenByDescending<R extends Comparable>( by(elt:T):R, comparator : Comparator = null ) : IOrderedList<T> {
    if( this typeis OrderedList ) {
      var typedThis = this as OrderedList<T>
      return typedThis.addThenByDescending( by, comparator )
    } else {
      throw new IllegalStateException( "Cannot call thenBy() on anything except an OrderedList" )
    }
  }
  
}
