package gw.specContrib.interop.java
uses java.util.concurrent.ConcurrentHashMap

class MyGenericGosu<T extends CharSequence> {
  property Tee: T
  property Name: String
  
  // NOTE: FromJavaSubclass references MyGenericGosu in its extends clause, so this verifeis bi-directional
  // reference support at the *header* declaration level
  property FJS: FromJavaSubclass<String>
  
  construct()
  {
    _Name = "no_args"
  }
  
  construct( name: String )
  {
    _Name = name  
  }
  
  construct( t: T, name: String )
  {
    _Tee = t
    _Name = name
  }
  
  function foo<E>( t: T, e: E ) : Map<T,E>
  {
    return new ConcurrentHashMap<T,E>()
  }
  
  function void_block_no_args( cb() )
  {
    cb()
  }
  function void_block_one_arg<E>( cb(e:E), e: E )
  {
    cb( e )
  }
  function void_block_two_args<E, F>( cb(e:E, f:F), e: E, f: F )
  {
    cb( e, f )
  }
  
  function ret_block_no_args<R>( cb():R ) : R
  {
    return cb()
  }
  function ret_block_one_arg<R, E>( cb(e:E):R, e: E ) : R
  {
    return cb( e )
  }
  function ret_block_two_args<R, E, F>( cb(e:E, f:F):R, e: E, f: F ) : R
  {
    return cb( e, f )
  }
}