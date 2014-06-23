package gw.lang.enhancements

/**
 * This enhancement allows for some overloading of methods that would be in CoreArrayEnhancement,
 * but can't because of the methods erasure
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreArrayEnhancementHelper<T> : T[] {
  
  function flatMap<R>( mapper(elt:T):R[] ) : R[] {
    return this.fastList().flatMap( \ elt -> mapper( elt ).fastList() ).toTypedArray() 
  }

}
