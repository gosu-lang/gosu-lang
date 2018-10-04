/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import com.github.benmanes.caffeine.cache.CacheLoader;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.util.concurrent.Cache;

public class TypeSystemAwareCache<K, V> extends Cache<K, V>
{

  @SuppressWarnings({"FieldCanBeLocal"})
  private final AbstractTypeSystemListener _cacheClearer = new CacheClearer( this );

  public static <K, V> TypeSystemAwareCache<K, V> make( String name, int size, CacheLoader<K, V> loader )
  {
    return new TypeSystemAwareCache<>( name, size, loader );
  }

  public TypeSystemAwareCache( String name, int size, CacheLoader<K, V> loader )
  {
    super( name, size, loader );
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
    public void refreshedTypes( RefreshRequest request )
    {
      _cache.clear();
    }

  }
}
