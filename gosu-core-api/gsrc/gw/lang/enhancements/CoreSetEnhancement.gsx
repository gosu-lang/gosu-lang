package gw.lang.enhancements

uses java.lang.*
uses java.util.Set
uses java.util.HashSet
uses java.util.Collections
uses java.util.HashMap
uses java.util.Map

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreSetEnhancement<T> :  Set<T>
{

  /**
   * Returns this Set cast to a Set<N>, checking each element in the
   * list to ensure the cast is legal.
   */
  function cast<N>( type : Type<N> ) : Set<N> {
    for( elt in this ) {
      if( not type.isAssignableFrom( typeof elt ) ) {
        throw new IllegalArgumentException( "The element ${elt} is not of type ${type.Name}" )
      }
    }
    return this as Set<N>
  }

  /**
   * Partitions each element into a Map where the keys are the value produce by the mapper block and the
   * values lists of elements of the Collection that map to that value.
   */
  function partition<Q>( partitioner(elt : T):Q ) : Map<Q, Set<T>> {
    var returnMap = new HashMap<Q, Set<T>>()
    var autoMap = returnMap.toAutoMap( \ q -> new HashSet<T>() )
    for( elt in this )
    {
      autoMap[ partitioner( elt ) ].add( elt )
    }
    return returnMap
  }

  /**
   * Returns the powerset of this set.  That is, it returns all possible subsets
   * of this set.  An exception will be thrown if this set is larger than 10, to avoid
   * very long-running calculations
   */
  function powerSet() : Set<Set<T>> {
    if( this.size() > 10 ) {
      throw "You cannot call powerSet() on a set that is larger than size 10.  It will kill the CPU."
    } else {
      var returnSet = new HashSet<HashSet<T>>(){ new HashSet<T>(){} }
      for( t in this ) {
        var temp = new HashSet<HashSet<T>>()
        for( h in returnSet ) {
          var copy = h.clone() as HashSet<T>
          copy.add( t )
          temp.add( copy ) 
        }
        returnSet.addAll( temp )
      }
      return returnSet
    }
  }

   /**
    * Returns a read-only version of this set
    */
   function freeze() : Set<T> {
     return Collections.unmodifiableSet( this )
   }

   /**
    * Returns a copy of this set
    */
   function copy() : Set<T> {
     return new HashSet<T>(this)
   }
   
}