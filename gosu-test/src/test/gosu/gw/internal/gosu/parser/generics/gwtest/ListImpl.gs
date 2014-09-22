package gw.internal.gosu.parser.generics.gwtest
uses java.util.AbstractList
uses java.util.Collection
uses java.util.ArrayList

class ListImpl<E> extends AbstractList<E>
{
  var _list : ArrayList<E> = new ArrayList<E>()

  override function get( p0: int ) : E
  {
    return _list.get( p0 )
  }

  override function size() : int
  {
    return _list.size()
  }

  override function add( p : E ) : boolean
  {
    return _list.add( p )
  }

  override function addAll( p : Collection<E > ) : boolean
  {
    return p == null
  }
}
