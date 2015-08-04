package gw.lang.enhancements

uses java.lang.Comparable
uses java.util.*
uses gw.util.IOrderedList
uses java.math.BigDecimal
uses gw.util.GosuObjectUtil
uses java.lang.System
uses java.lang.ArrayStoreException

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayEnhancement<T> : T[] {

  /**
   * Converts this array to a mutable List whose values
   * are copied from this array.
   */
  function toList() : java.util.List<T> {
    var retList = new ArrayList<T>(Count)
    for( elt in this ) {
      retList.add( elt )
    }
    return retList
  }
 
  /**
   * Allocates a new array of type N and copies the elements of this
   * array into it.  If every element of this array is not of type N,
   * an ArrayStoreException will be thrown.
   */
  function cast<N>( type : Type<N> ) : N[] {
    var newArray = type.Type.makeArrayInstance(this.length) as N[]
    
    if( GosuObjectUtil.isJavaReferenceArray( this ) and GosuObjectUtil.isJavaReferenceArray( newArray ) ) {
      System.arraycopy(this, 0, newArray, 0, this.length)
    } else {
      for (i in 0..|this.length) {
        if (not (this[i] typeis N)) {
          throw new ArrayStoreException()  
        }
        newArray[i] = this[i] as N  
      }
    }
    
    return newArray
  }

  /**
   * Converts this array to an immutable List whose values
   * are copied from this array.  This is cheaper than
   * toList().
   */
  public function fastList() : java.util.List<T> {
    if( GosuObjectUtil.isJavaReferenceArray( this ) ) {
      return Arrays.asList<T>( this )
    } else {
      return toList()
    }
  }

  function fold( aggregator(elt1 : T, elt2 : T):T ) : T {
    return this.fastList().fold( aggregator )
  }
   
  function reduce<V>( init : V, aggregator(val : V, elt2 : T):V ) : V {
    return this.fastList().reduce( init, aggregator )
  }
   
  function allMatch( cond(elt1 : T):Boolean ) : Boolean {
    return this.fastList().allMatch( cond )
  }

  function concat( that : T[] ) : T[] {
    return this.fastList().concat( that.fastList() ).toTypedArray()
  }
   
  property get HasElements() : Boolean {
    return this.length > 0
  }
   
  function hasMatch( cond(elt1 : T):Boolean ) : Boolean {
    return this.fastList().hasMatch( cond )
  }
         
  function average<N extends java.lang.Number>( select:block(elt:T):N ) : BigDecimal {
    return this.fastList().average( select )
  }

  function contains( elt : T ) : boolean {
    return this.fastList().contains( elt )
  }
  
  @ShortCircuitingProperty
  property get Count() : int {
    return this.length
  }
  
  function countWhere( match(elt:T):boolean ) : int {
    return this.fastList().countWhere( match ) 
  }
    
  property get IsEmpty() : boolean{
    return this.length == 0
  }

  function first() : T {
    return this.fastList().first()
  }
  
  function firstWhere( cond(elt:T):boolean ) : T {
    return this.fastList().firstWhere( cond )
  }

  function intersect( that : T[] ) : Set<T>{
    return this.fastList().intersect( that.fastList() )
  }
  
  function last() : T {
    return this.fastList().last()
  }
    
  function lastWhere( cond(elt:T):boolean ) : T {
    return this.fastList().lastWhere( cond )
  }

  function max<R extends Comparable>( transform(elt:T):R ) : R {
    return this.fastList().max( transform )
  }

  function min<R extends Comparable>( transform(elt:T):R ) : R {
    return this.fastList().min( transform )
  }
  
  function whereTypeIs<R>( type : Type<R> ) : R[]{
    return this.fastList().whereTypeIs( type ).toTypedArray()
  }

  function orderBy<R extends Comparable>( value(elt:T):R, comparator : Comparator = null ) : IOrderedList<T> {
    return this.toList().orderBy( value, comparator )
  }
  
  function orderByDescending<R extends Comparable>( value(elt:T):R, comparator : Comparator = null ) : IOrderedList<T> {
    return this.toList().orderByDescending( value, comparator )
  }
  
  function map<Q>( mapper(elt : T):Q ) : Q[] {
    return this.fastList().map( mapper ).toTypedArray()
  }
  
  function flatMap<R>( mapper(elt:T):Collection<R> ) : R[] {
    return this.fastList().flatMap( mapper ).toTypedArray() 
  }

  function singleWhere( cond(elt:T): boolean ) : T {
    return this.fastList().singleWhere( cond  )
  }
  
  function single() : T {
    return this.fastList().single()
  }
    
  function partitionUniquely<Q>( partitioner(elt : T):Q ) : Map<Q, T> {
    return this.fastList().partitionUniquely( partitioner )    
  }

  function partition<Q>( partitioner(elt : T):Q ) : Map<Q, List<T>> {
    return this.fastList().partition( partitioner )
  }
  
  function union( that : T[] ) : Set<T>{
    return this.fastList().union( that.fastList() )    
  }
  
  function where( cond(elt:T): boolean ) : T[] {
    return this.fastList().where( cond ).toTypedArray()    
  }

  function toSet() : Set<T> {
    return this.fastList().toSet()
  }

  /**
   * Sorts the array in place by the given sorting block
   */
  function sort( isBefore(elt1 : T, elte2 : T):Boolean ) : T[]
  {
    var comparator = new BlockSortComparator(isBefore)
    Arrays.sort( this as Object[], comparator )
    return this
  }

  /**
   * Sorts the array in place in ascending order on the Comparable value returned by the given value block.
   */
  function sortBy( value( elt : T ):Comparable, comparator : Comparator = null ) : T[]
  {
    var blockComparator = new BlockSortByComparator( value, true, comparator )
    Arrays.sort( this as Object[], blockComparator )
    return this
  }

  /**
   * Sorts the array in place in descending order on the Comparable value returned by the given value block
   */
  function sortByDescending( value( elt : T ):Comparable, comparator : Comparator = null ) : T[]
  {
    var blockComparator = new BlockSortByComparator( value, false, comparator )
    Arrays.sort( this as Object[], blockComparator )
    return this
  }

  /**
   * Calls toString on each element in the array and joins them together with the
   * given string
   */
  function join( str : String  ) : String
  {
    return this.fastList().join( str )
  }

  /**
   * Creates a copy of this array
    */
  function copy() : T[] {
    var arr = T.Type.makeArrayInstance( this.length ) as T[]
    for( elt in this index i ) {
      arr[i] = elt
    }
    return arr
  }
    
  function each( operation(elt : T) ) {
    for( elt in this ) {
      operation( elt )
    }
  }

  function eachWithIndex( operation(elt : T, index : int ) ) {
    for( elt in this index i) {
      operation( elt, i )
    }
  }

  /**
   * Returns the minimum value of this collection with respect to the Comparable attribute
   * calculated by the given block.
   */ 
  function minBy( comparison(elt : T):Comparable ) : T {
    return this.fastList().minBy( comparison )
  }
    
  /**
   * Returns the maximum value of this collection with respect to the Comparable attribute
   * calculated by the given block.
   */ 
  function maxBy( comparison(elt : T):Comparable ) : T {
    return this.fastList().maxBy( comparison )
  }
  
  /**
   * Returns a new set, which is this iterable as a set minus the given set.  This is slightly different
   * than except(), which does not convert this Iterble to a set and thus may contain duplicates
   */
  function subtract( otherArray : T[] ) : Set<T> {
    return this.fastList().subtract( otherArray.fastList() )
  }

 /**
  * Returns a new set, which is the disjunction of this set and the given set, that is,
  * all elements that are in one set *not* and not the other
  */
  function disjunction( otherArray : T[] ) : Set<T> {
    return this.fastList().disjunction( otherArray.fastList() )
  }
  
  /**
   * Returns a new array that is the reverse of this array
   */
  function reverse() : T[] {
    return this.toList().reverse().toTypedArray()
  }
}