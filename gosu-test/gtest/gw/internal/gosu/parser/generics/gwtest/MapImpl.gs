package gw.internal.gosu.parser.generics.gwtest
uses java.lang.Integer
uses java.util.Map
uses java.util.Collection
uses java.util.Set

class MapImpl implements Map<String,Integer>
{
  override property get Empty() : boolean
  {
    return true //## todo: Implement me
  }

  override function clear() : void
  {
    //## todo: Implement me
  }

  override function containsKey( p0: Object ) : boolean
  {
    return false //## todo: Implement me
  }

  override function containsValue( p0: Object ) : boolean
  {
    return false //## todo: Implement me
  }

  override function entrySet() : Set<Map.Entry<String,Integer>>
  {
    return null //## todo: Implement me
  }

  override function get( p0: Object ) : Integer
  {
    print( "get" )
    return null //## todo: Implement me
  }

  override function keySet() : Set<String>
  {
    return null //## todo: Implement me
  }

  override function put( p0: String, p1: Integer ) : Integer
  {
    print( "put" )
    return null //## todo: Implement me
  }

  override function putAll( p0: Map<String, Integer> ) : void
  {
    //## todo: Implement me
  }

  override function remove( p0: Object ) : Integer
  {
    return null //## todo: Implement me
  }

  override function size() : int
  {
    return 0 //## todo: Implement me
  }

  override function values() : Collection<Integer>
  {
    return null //## todo: Implement me
  }
}