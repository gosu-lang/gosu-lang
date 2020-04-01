/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.RefreshRequest;
import gw.util.concurrent.Cache;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.TypeSystem;
import java.util.function.Function;

public class TypeSystemAwareCache<K, V> extends Cache<K, V>
{

  @SuppressWarnings({"FieldCanBeLocal"})
  private final AbstractTypeSystemListener _cacheClearer = new CacheClearer(this);

  public static <K, V> TypeSystemAwareCache<K, V> make(String name, int size, Function<K, V> handler)
  {
    return new TypeSystemAwareCache<K, V>(name, size, handler);
  }

  public TypeSystemAwareCache( String name, int size, Function<K, V> kvMissHandler )
  {
    super( name, size, kvMissHandler );
    TypeSystem.addTypeLoaderListenerAsWeakRef( _cacheClearer );
  }

  private static class CacheClearer extends AbstractTypeSystemListener
  {
    TypeSystemAwareCache _cache;

    private CacheClearer( TypeSystemAwareCache cache )
    {
      _cache = cache;
    }

    @Override
    public void refreshed()
    {
      _cache.clear();
    }

    @Override
    public void refreshedTypes(RefreshRequest request)
    {
      _cache.clear();
    }

  }
}
