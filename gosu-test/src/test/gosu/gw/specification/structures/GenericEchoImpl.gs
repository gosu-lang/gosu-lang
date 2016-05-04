package gw.specification.structures

uses java.lang.CharSequence
uses java.util.Map
uses java.util.HashMap

class GenericEchoImpl<E extends CharSequence> {
  function echo( t: E ) : E {
    return t
  }

  function echo<F>( e: E, f: F ) : Map<E, F> {
    return {e->f}
  }

  function echo<F>( f: F, t: E) : F {
    return f
  }
}