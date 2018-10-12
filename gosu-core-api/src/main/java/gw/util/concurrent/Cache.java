/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.concurrent;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import gw.util.ILogger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * static var MY_CACHE = new Cache<Foo, Bar>( 1000, \ foo -> getBar( foo ) )
 */
public class Cache<K, V>
{
  // Note stats incur a perf penalty
  private static final boolean ENABLE_STATS = System.getProperty( "gosu.cache.stats", "" ).length() > 0;

  private final String _name;
  private final int _size;
  private final CacheLoader<K, V> _loader;
  private com.github.benmanes.caffeine.cache.Cache<K, V> _cacheImpl;

  private ScheduledFuture<?> _loggingTask;

  public Cache( String name, int size, CacheLoader<K, V> loader )
  {
    _name = name;
    _size = size;
    _loader = loader;
    clearCacheImpl();
  }

  private void clearCacheImpl()
  {
    // Using a Cache as opposed to a LoadingCache because our
    // use-cases involve the _loader making recursive calls back
    // into the cache, which causes the LoadingCache's backing
    // ConcurrentHashMap to hang,
    // see:
    //  - ConcurrentHashMapTest also
    //  - rsmckinney's comments in:
    //      https://stackoverflow.com/questions/28840047/recursive-concurrenthashmap-computeifabsent-call-never-terminates-bug-or-fea/28845674?noredirect=1#comment92206723_28845674
    //  - https://github.com/ben-manes/caffeine/wiki/Faq#recursive-computations
    //noinspection unchecked
    Caffeine<K, V> builder = (Caffeine<K, V>)Caffeine
      .newBuilder()
      .maximumSize( _size );
    if( ENABLE_STATS )
    {
      builder.recordStats();
    }
    _cacheImpl = builder.build();
  }

  /**
   * This will evict a specific key from the cache.
   *
   * @param key the key to evict
   */
  public void evict( K key )
  {
    _cacheImpl.invalidate( key );
  }

  /**
   * This will put a specific entry in the cache
   *
   * @param key   this is the key, must not be null
   * @param value this is the value, null value is not entered into the map
   *
   * @return the old value for this key
   */
  public void put( K key, V value )
  {
    _cacheImpl.put( key, value );
  }

  /**
   * This will get a specific entry calling the loader if no entry exists
   *
   * @param key the object to find
   *
   * @return the found object (may be null)
   */
  public V get( K key )
  {
    V value = _cacheImpl.getIfPresent( key );
    if( value == null )
    {
      try
      {
        value = _loader.load( key );
        if( value != null )
        {
          _cacheImpl.put( key, value );
        }
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return value;
  }

  public CacheStats getStats()
  {
    return _cacheImpl.stats();
  }

  public String getName()
  {
    return _name;
  }

  /**
   * Sets up a recurring task every n seconds to report on the status of this cache.  This can be useful
   * if you are doing exploratory caching and wish to monitor the performance of this cache with minimal fuss.
   *
   * @param seconds how often to log the entry
   * @param logger  the logger to use
   *
   * @return this
   */
  public synchronized Cache<K, V> logEveryNSeconds( int seconds, final ILogger logger )
  {
    if( _loggingTask == null )
    {
      ScheduledExecutorService service = Executors.newScheduledThreadPool( 1 );
      _loggingTask = service.scheduleAtFixedRate( () -> logger.info( Cache.this ), seconds, seconds, TimeUnit.SECONDS );
    }
    else
    {
      throw new IllegalStateException( "Logging for " + this + " is already enabled" );
    }
    return this;
  }

  public synchronized void stopLogging()
  {
    if( _loggingTask != null )
    {
      _loggingTask.cancel( false );
    }
  }

  public void clear()
  {
    clearCacheImpl();
  }

  @Override
  public String toString()
  {
    return getStats().toString();
  }

  public static <KK, VV> Cache<KK, VV> make( String name, int size, CacheLoader<KK, VV> loader )
  {
    return new Cache<>( name, size, loader );
  }

  /**
   * @deprecated use {@link CacheLoader} instead
   */
  public interface MissHandler<L, W> extends CacheLoader<L,W> {
  }
}
