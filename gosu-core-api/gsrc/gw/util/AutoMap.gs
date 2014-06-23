package gw.util
uses java.util.Map
uses java.util.HashMap

/**
 * This map wraps another map and provides a default value for the map via the block
 * passed into the constructor.  If a get is called on the map and null is found, the
 * block passed in will be evaluated and the return value will be stored into the map
 * for that key, then returned.  All other methods delegate directly to the wrapped map.
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
class AutoMap<K, V> implements Map<K, V>
{
  var _defaultReturnVal : block(key:K):V as DefaultReturnValue
  delegate _map represents Map<K, V>

  construct( mapToWrap : Map<K, V>, defaultReturnVal(key:K):V )
  {
    _map = mapToWrap
    _defaultReturnVal = defaultReturnVal
  }

  construct( defaultReturnVal(key:K):V )
  {
    _map = new HashMap<K, V>()
    _defaultReturnVal = defaultReturnVal
  }

  override function get( key : Object ) : V {
    var val = _map.get( key )
    if(val != null) {
      return val
    } else {
      val = _defaultReturnVal(key as K)
      _map.put( key as K, val )
      return val
    }
  }

  override function equals( o : Object ) : boolean {
    return _map.equals( o )
  }
  
  override function hashCode() : int {
    return _map.hashCode()
  }
}
