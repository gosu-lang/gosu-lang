package gw.lang.enhancements

uses java.lang.Comparable
uses java.util.ArrayList
uses java.util.Collections
uses java.lang.IllegalArgumentException
uses java.util.Map
uses java.util.HashMap
uses java.util.Comparator
uses java.util.List

/**
 * List goodies
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreListEnhancement<T> : List<T>  {

  /**
   * Returns this list cast to a List<N>, checking each element in the
   * list to ensure the cast is legal.
   */
  function cast<N>( type : Type<N> ) : List<N> {
    for( elt in this ) {
      if( not type.Type.isAssignableFrom( typeof elt ) ) {
        throw new IllegalArgumentException( "The element ${elt} is not of type ${type.Type.Name}" )
      }
    }
    return this as List<N>
  }

  /**
   * Partitions each element into a Map where the keys are the value produce by the mapper block and the
   * values lists of elements of the Collection that map to that value.
   */
  function partition<Q>( partitioner(elt : T):Q ) : Map<Q, List<T>> {
    var returnMap = new HashMap<Q, List<T>>()
    var autoMap = returnMap.toAutoMap( \ q -> new ArrayList<T>() )
    for( elt in this )
    {
      autoMap[ partitioner( elt ) ].add( elt )
    }
    return returnMap
  }

  /**
   * Returns this list of elements sorted by the given sort block.  This method
   * sorts this list in place, unlike orderBy()
   */
  function sort( isBefore(elt1 : T, elt2 : T):Boolean ) : List<T>
  {
    var comparator = new BlockSortComparator( isBefore )
    Collections.sort( this, comparator )
    return this
  }
  
  /**
   * Sorts this list in place in ascending order on the Comparable value returned by the given value block.
   */
  function sortBy( value(elt : T):Comparable, comparator : Comparator = null) : List<T>
  {
    var blockComparator = new BlockSortByComparator( value, true, comparator )
    Collections.sort( this, blockComparator )
    return this
  }

  /**
   * Sorts this list in place in descending order on the Comparable value returned by the given value block
   */
  function sortByDescending( value(elt : T):Comparable, comparator : Comparator = null ) : List<T>
  {
    var blockComparator = new BlockSortByComparator( value, false, comparator )
    Collections.sort( this, blockComparator )
    return this
  }
 
   /**
    * Returns a new randomly shuffled list based on this list
    */
   function shuffle() : List<T> {
     Collections.shuffle( this )
     return this
   }
   
   /**
    * Returns a read-only version of this list
    */
   function freeze() : List<T> {
     return Collections.unmodifiableList( this )
   }
   
   /**
    * Returns a copy of this list
    */
   function copy() : List<T> {
     return new ArrayList<T>(this)
   }
}