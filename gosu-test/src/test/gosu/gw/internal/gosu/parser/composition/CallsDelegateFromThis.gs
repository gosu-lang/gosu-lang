package gw.internal.gosu.parser.composition
uses java.util.ArrayList

class CallsDelegateFromThis<E> implements java.util.List<E>
{ 
  delegate _map represents java.util.List<E>

  construct()
  {
    _map = new ArrayList<E>()
  } 

  function addSpecial( e : E )
  {
    this.add( e )
    print( "fooy" ) 
  }
} 
