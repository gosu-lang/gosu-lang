package gw.internal.gosu.compiler.sample.expression.blocks

uses java.util.*

class GenericsBlocks {

  function callsSimpleGenericIdentity() : Object {
    return simpleGenericIdentity( "test" )
  }

  function simpleGenericIdentity<T>( obj : T ) : Object {
    return \->obj
  }

  function callsMap() : Object {
    var lstWrapper = new InnerGeneric<String>( {"a", "ab", "abc" } )
    return lstWrapper.map( \ s -> s.length() )
  }

  static class InnerGeneric<T> {

    var _lst : List<T>

    construct( l : List<T> ) {
      _lst = l
    }

    function map<Q>( mapper : block(T):Q ) : List<Q>
    {
      var lst = new ArrayList<Q>()
      for( elt in _lst ) {
        lst.add( mapper( elt ) )
      }
      return lst    
    }
  }

}