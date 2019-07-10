package gw.lang.enhancements

uses gw.lang.parser.StandardCoercionManager
uses gw.util.Pair
uses java.util.Collection
uses java.lang.Iterable
uses java.util.ArrayList
uses java.lang.IllegalStateException
uses java.util.Set
uses java.util.HashSet
uses java.math.BigDecimal
uses java.util.LinkedHashSet
uses java.lang.StringBuilder
uses java.lang.Comparable
uses java.util.Map
uses java.util.HashMap
uses java.util.Collections

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIterableEnhancement<T> : java.lang.Iterable<T> {    

  /**
   * Return the number of elements in this Iterable object
   */
  @ShortCircuitingProperty
  reified property get Count() : int {
    if( this typeis Collection ) {
      return this.size()
    } else {
      var iter = this.iterator()
      var i = 0
      while(iter.hasNext()) {
        iter.next()
        i++
      }
      return i
    }
  }
 
  /**
   * Returns a single element from this iterable, if only one exists.  It no elements are
   * in this iterable, or if there are more than one elements in it, an IllegalStateException
   * is thrown
   */
  function single() : T { 
    var iter = this.iterator()
    if( not iter.hasNext() ) { 
      throw new IllegalStateException( "This iterable has no elements in it" )
    } 
    var val = iter.next()
    if( iter.hasNext() ) {
      throw new IllegalStateException( "This iterable has more than one element in it" )
    }
    return val
  }

  /**
   * If this Iterable is already a Collection, return this Itearble cast to a Collection.  
   * Otherwise create a new Collection and copy this Iterable into it.
   */
  reified function toCollection() : Collection<T> {
    if( this typeis Collection ) {
      return this as Collection<T>
    } else {
      var lst = new ArrayList<T>()
      for( e in this ){
        lst.add( e )
      }
      return lst
    }
  }

  /**
   * If this Iterable is already a List, return this Iterable cast to a List.  
   * Otherwise create a new List and copy this Iterable into it.
   */
  reified function toList() : List<T> {
    if( this typeis List ) {
      return this as List<T>
    } else {
      var lst = new ArrayList<T>()
      for( e in this ){
        lst.add( e )
      }
      return lst
    }
  }

  /**
   * If this Iterable is already a Set, return this Iterable cast to a Set.  Otherwise create a new
   * Set based on this Iterable.
   */
  reified function toSet() : Set<T> {
    if( this typeis Set ) {
      return this as Set<T>
    } else {
      var st = new HashSet<T>()
      for( e in this ){
        st.add( e )
      }
      return st
    }
  } 

  /**
   * Returns a strongly-typed array of this Iterable, as opposed to the argumentless Iterable#toArray(), which
   * returns an Object array.  This method takes advantage of static reification and, therefore, does not necessarily
   * return an array that matches the theoretical runtime type of the Iterable, if actual reification were supported.
   */
  reified function toTypedArray() : T[]
  {
    var asCollection = this.toCollection()
    var arr = T.Type.makeArrayInstance( asCollection.size() ) as T[]
    for( elt in asCollection index i ) {
      arr[i] = elt
    }
    return arr
  }



//#######################################################


  /**
   * Returns true if all elements in this collection match the given
   * condition and false otherwise
   */
  function allMatch( cond(elt1 : T):boolean ) : boolean {
    for( e in this ) {
      if( not cond( e ) ) return false
    }
    return true
  }

  /**
   * Return the average of the mapped value
   */
  reified function average( select:block(elt:T):java.lang.Number ) : BigDecimal {
     return this.sum( \ elt -> select(elt) as BigDecimal ) / (this.Count as BigDecimal) 
  }
  
  /**
   * Return a new list that is the concatenation of this Iterable and the specified Collection
   */
  reified function concat( that : Collection<T> ) : Collection<T> {
    var returnList = new ArrayList<T>( that.size() + 8 )
    returnList.addAll( toList() )
    returnList.addAll( that )
    return returnList
  }
  
  /**
   * Return the count of elements in this collection that match the
   * given condition
   */
  function countWhere( cond(elt:T):boolean ) : int {
    var i = 0
    for( e in this ) {
      if( cond( e) ) i++
    }
    return i
  }

 /**
  * Returns a the set disjunction of this collection and the other collection, that is,
  * all elements that are in one collection *not* and not the other.
  */
  reified function disjunction( that : Collection<T> ) : Set<T> {
    var intersection = this.intersect( that )
    return this.union( that ).subtract( intersection ) 
  }

  /**
   * This method will invoke the operation on each element in the Collection
   */
  function each( operation(elt : T) ) {
    for( elt in this ) {
      operation( elt )
    }
  }

  /**
   * This method will invoke the operation on each element in the Collection, passing in the
   * index as well as the element
   */
  function eachWithIndex( operation(elt : T, index : int ) ) {
    for( elt in this index i) {
      operation( elt, i )
    }
  }

  function flatMap<R>( mapper(elt:T):Collection<R> ) : List<R>{
    var returnList = new ArrayList<R>()
    for( elt in this ){
      var iter = mapper( elt )
      for( result in iter ) {
        returnList.add( result )
      }
    }
    return returnList
  }

  /**
   * Returns all the values of this collection folded into a single value
   */
  function fold( aggregator(elt1 : T, elt2 : T):T ) : T {
    var retVal : T = null
     for( elt in this index i ) {
       if( i == 0 ) {
         retVal = elt
       } else {
         retVal = aggregator( retVal, elt )
       }
     }
    return retVal
  }
  
  /**
   * Returns the first element in this collection.  If the collection is
   * empty, null is returned.
   */
  reified function first() : T {
    if( this typeis List ) {
      return this.size() > 0 ? this[0] as T : null
    } else {
      var iter = this.iterator()
      return iter.hasNext() ? iter.next() : null
    }
  }

  /**
   * Returns the first element in this collection that matches the given condition. 
   * If no element matches the criteria, null is returned.
   */
  function firstWhere( cond(elt:T):boolean ) : T {
    for( e in this ) {
      if( cond( e ) ) return e
    }
    return null
  }
  
  /**
   * Returns Boolean.TRUE if this collection has elements in it
   * and Boolean.FALSE otherwise.  This property is more consistent
   * across null and empty collections that the Empty property, which
   * returns true if the collection is empty, but null (interpreted
   * as false in if statements by Gosu) if the collection is null.
   */
  reified property get HasElements() : Boolean {
    if( this typeis Collection ) {
      return this.size() > 0
    }
    else {
      return this.iterator().hasNext()
    }
  }

  /**
   * Returns true if any elements in this collection match the given
   * condition and false otherwise
   */
  function hasMatch( cond(elt1 : T):boolean ) : boolean
  {
    for( e in this ) {
      if( cond( e ) ) return true
    }
    return false
  }

  /**
   * Return the set intersection of these two collections. 
   */
  reified function intersect( that : Collection<T> ) : Set<T> {
    var retVal = this typeis Set ? new HashSet<T>(toList()) : new LinkedHashSet<T>( toList() )
    retVal.retainAll( that )
    return retVal
  }

  /**
   * Coerces each element in the collecion to a string and joins them together with the
   * given delimiter
   */
  function join( delimiter : String  ) : String {
    var retVal = new StringBuilder()
    for( elt in this index i ) {
      if( i > 0 ) {
        retVal.append( delimiter )
      }
      retVal.append( gw.config.CommonServices.getCoercionManager().makeStringFrom( elt ) )
    }
    return retVal.toString()
  }

  /**
   * Returns the last element in this collection.  If the collection is 
   * empty, null is returned.
   */
  reified function last() : T {
    if( this typeis List ) {
      return this.size() > 0 ? this[this.size() - 1] as T : null
    } else {
      var ret : T = null
      for( elt in this ) {
        ret = elt
      }
      return ret
    }
  }
  
  /**
   * Returns the last element in this collection that matches the given condition. 
   * If the collection is empty, null is returned.
   */
  function lastWhere( cond(elt:T):boolean ) : T {
    var returnVal : T = null
    var found = false
    for( elt in this ) {
      if( cond( elt ) ) {
        returnVal = elt
        found = true
      }
    }
    return returnVal
  }
  
  /**
   *  Maps the values of the collection to a list of values by calling the
   *  mapper block on each element.
   */
  function map<Q>( mapper(elt : T):Q ) : List<Q> {
    var returnList = new ArrayList<Q>()
    for( elt in this ){
      returnList.add( mapper( elt ) )
    }
    return returnList
  }

  /**
   * Returns the maximum value of the transformed elements.
   */
  reified function max<R extends Comparable>( transform(elt:T):R ) : R {
    if( !HasElements ) {
      throw new IllegalStateException( "${this} is empty" )
    }
    var returnVal : R = null
    for( elt in this ) {
      var eltVal = transform( elt )
      if( eltVal != null ) {
        if( returnVal == null || eltVal > returnVal ) {
          returnVal = eltVal
        }
      }
    }
    if( returnVal == null ) {
      throw new IllegalStateException( "The iterable does not have any max value" )
    }
    return returnVal
  }

  /**
   * Returns the maximum value of this collection with respect to the Comparable attribute
   * calculated by the given block.  If more than one element has the maximum value, the first
   * element encountered is returned.
   */
  function maxBy( comparison(elt : T):Comparable ) : T {
    var max : T = null
    var maxVal : Comparable = null
    for( elt in this ) {
      var altVal = elt == null ? null : comparison(elt)
      if( elt != null and (maxVal == null or maxVal < altVal )) {
        max = elt
        maxVal = altVal
      }
    }
    return max
  }

  /**
   * Returns the minimum value of the transformed elements.
   */
  reified function min<R extends Comparable>( transform(elt:T):R ) : R {
    if( !HasElements ) {
      throw new IllegalStateException( "${this} is empty" )
    }
    var returnVal : R = null
    for( elt in this ) {
      var eltVal = transform( elt )
      if( eltVal != null ) {
        if( returnVal == null || eltVal < returnVal ) {
          returnVal = eltVal
        }
      }
    }
    if( returnVal == null ) {
      throw new IllegalStateException( "The iterable does not have any min value" )
    }
    return returnVal
  }

  /**
   * Returns the minimum value of this collection with respect to the Comparable attribute
   * calculated by the given block.  If more than one element has the minimum value, the first
   * element encountered is returned.
   */
  function minBy( comparison(elt : T):Comparable ) : T {
    var min : T = null
    var minVal : Comparable = null
    for( elt in this ) {
      var altVal = elt == null ? null : comparison(elt)
      if( elt != null and (minVal == null or minVal > altVal  )) {
        min = elt
        minVal = altVal
      }
    }
    return min
  }

  /**
   * Partitions each element into a Map where the keys are the value produce by the mapper block and the
   * values are the elements of the Collection.  If two elements map to the same key an IllegalStateException
   * is thrown.
   */
  function partitionUniquely<Q>( mapper(elt : T):Q ) : Map<Q, T> {
    var returnMap = new HashMap<Q, T>()
    for( elt in this ) {
      var key = mapper( elt )
      var currentVal = returnMap[key]
      if( currentVal != null ) {
        throw new IllegalStateException ( "${mapper} does not define a unique value across all elements of this Collection : " +
                                          " Element ${elt} and element ${currentVal} both have the value ${key}" )

      }
      returnMap[key] = elt
    }
    return returnMap
  }

  /**
   * Returns all the values of this collection down to a single value
   */
  function reduce<V>( init : V, aggregator(val : V, elt2 : T):V ) : V {
    var retVal = init
    for( elt in this ) {
      retVal = aggregator( retVal, elt )
    }
    return retVal
  }

  /**
   * Retains all elements that match the given condition in this collection
   */
  function retainWhere( cond(elt:T):boolean )  {
    var iter = this.iterator()
    while( iter.hasNext() ) {
      if( not cond( iter.next() ) ){
        iter.remove()
      }
    }
  }

  /**
   * Removes all elements that match the given condition in this collection
   */
  function removeWhere( cond(elt:T):boolean )  {
    var iter = this.iterator()
    while( iter.hasNext() ) {
      if( cond( iter.next() ) ){
        iter.remove()
      }
    }
  }

  /**
   * Returns a new list of the elements in the collection, in their
   * reverse iteration order 
   */  
  reified function reverse() : List<T> {
    var returnList = this typeis List ? this.copy() : this.toList()
    Collections.reverse( returnList )
    return returnList as List<T>
  }

  /**
   * Returns a single item matching the given condition.  If there is no such element or if multiple
   * elements match the condition, and IllegalStateException is thrown.
   */
  function singleWhere( cond(elt:T):boolean ) : T {
    var found = false
    var returnVal : T = null
    for( elt in this ) {
      if( cond( elt ) ) {
        if( found ) {
          throw new IllegalStateException( "More than one element matches the given condition" )
        } else {
          returnVal = elt
          found = true
        }
      }
    }

    if( found ) {
      return  returnVal
    } else {
      throw new IllegalStateException( "No elements match the given condition" )
    }
  }

  /**
   * Returns the Set subtraction of that Collection from this Collection
   */
  reified function subtract( that : Collection<T> ) : Set<T> {
    var returnSet = new LinkedHashSet<T>( toList() )
    returnSet.removeAll( that )
    return returnSet
  }

  /**
   * Returns the set union of the two collections.
   */
  reified function union( that : Collection<T> ) : Set<T>{
    var returnSet = new LinkedHashSet<T>( toList() )
    returnSet.addAll( that )
    return returnSet
  }

  /**
   * Returns all the elements of this collection for which the given condition is true
   */
  function where( cond(elt:T): boolean ) : List<T> {
    var result = new ArrayList<T>()
    for( elt in this ) {
      if( cond( elt ) ) {
        result.add( elt )
      }
    }
    return result
  }

  /**
   * Returns all the elements of this collection that are nominally or structurally
   * assignable to the given type
   */
  reified function whereTypeIs<R>( type : Type<R> ) : List<R>{
    var retList = new ArrayList<R>()
    for( elt in this ) {
      if( type.Type.isAssignableFrom( typeof elt ) or StandardCoercionManager.isStructurallyAssignable( type.Type, typeof elt ) ) {
        retList.add( elt as R )
      }
    }
    return retList
  }

  /**
   * takes two lists and returns a list of corresponding <code>gw.util.Pair</code>s.
   * If one input list is short, excess elements of the longer list are discarded.
   */
  function zip<R>( other : Iterable<R>) : List<Pair<T,R>> {
    if (other == null) {
      throw new NullPointerException("other should be non-null")
    }

    var zipped = new LinkedList<Pair<T,R>>()

    var thisIterator = this.iterator()
    var otherIterator = other.iterator()
    while (thisIterator.hasNext() and otherIterator.hasNext()) {
      zipped.add(Pair.make(thisIterator.next(), otherIterator.next()))
    }

    return zipped
  }
}
