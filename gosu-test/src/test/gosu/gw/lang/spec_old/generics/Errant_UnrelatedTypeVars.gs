package gw.lang.spec_old.generics

uses java.util.Iterator
uses java.sql.Date
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_UnrelatedTypeVars<T> {
  
  var _val : T
  construct( val : T ) {
    _val = val 
  }
  
  function foo<R>( cond(elt:T):boolean ) : Iterator<R> {
    var a = new MyIterator<R>( \-> \-> _val ) //_val is T and, therefore, shouldn't match up with R
    var b = new MyIterator<Date>( \-> \-> _val ) //_val is T and, therefore, shouldn't match up with Date
    var c = new MyIterator<T>( \-> \-> _val ) //_val is T and, therefore, should match up with T
    return a
  }
  
  class MyIterator<Q> implements Iterator<Q> {
    construct( block():block():Q ) {
    }
    override function hasNext() : boolean {
      return true
    }
    override function next() : Q {
      return null
    }
    override function remove() {}
  }
}