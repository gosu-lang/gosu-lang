package gw.lang.reflect;

import manifold.rt.api.Bindings;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 */
public class EmptyBindings implements Bindings
{
  private static final Bindings INSTANCE = new EmptyBindings();

  public static Bindings instance() {
    return INSTANCE;
  }

  private EmptyBindings()
  {
  }

  @Override
  public Object put( String name, Object value )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void putAll( Map<? extends String, ? extends Object> toMerge )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> keySet()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<Object> values()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<Entry<String, Object>> entrySet()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isEmpty()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsKey( Object key )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsValue( Object value )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object get( Object key )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object remove( Object key )
  {
    throw new UnsupportedOperationException();
  }
}
