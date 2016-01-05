package gw.lang.spec_old.generics

uses java.lang.Comparable 
class IndirectRecursiveGenericType<T extends Comparable<T>> implements Comparable<IndirectRecursiveGenericType<T>> { 
  var v : T as Value 
  construct(val : T) { 
    this.v = val 
  } 
  override function compareTo(other : IndirectRecursiveGenericType<T>) : int { 
    return this.Value.compareTo(other.Value) 
  } 
} 