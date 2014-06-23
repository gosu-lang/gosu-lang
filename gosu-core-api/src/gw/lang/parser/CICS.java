/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class CICS implements CharSequence, Serializable
{
  private static final Map<CharSequence, WeakReference<CICS>> CASE_SENSITIVE_CACHE =
          new ConcurrentHashMap<CharSequence, WeakReference<CICS>>(997);

  private static final ReentrantLock LOCK = new ReentrantLock();

  static int _nextUniqueCode = 0;

  String _string;
  int _hashCode;
  int _uniqueCode;

  public static CICS get( CharSequence rawCharSequence )
  {
    if( rawCharSequence instanceof CICS )
    {
      return (CICS)rawCharSequence;
    }

    if( rawCharSequence == null )
    {
      return null;
    }

    WeakReference<CICS> cicsRef = CASE_SENSITIVE_CACHE.get(rawCharSequence);
    CICS cics = cicsRef == null ? null : cicsRef.get();
    if (cics == null)
    {
      LOCK.lock();
      try
      {
        cicsRef = CASE_SENSITIVE_CACHE.get(rawCharSequence);
        cics = cicsRef == null ? null : cicsRef.get();
        if (cics == null) {
          cics = new CICS(rawCharSequence);
          cicsRef = new WeakReference<CICS>(cics);
          CASE_SENSITIVE_CACHE.put(rawCharSequence, cicsRef);
        }
      }
      finally
      {
        LOCK.unlock();
      }
    }
    return cics;
  }

  public static void cleanupWeakReferences() {
    LOCK.lock();
    try
    {
      for( Iterator<Map.Entry<CharSequence, WeakReference<CICS>>> it = CASE_SENSITIVE_CACHE.entrySet().iterator(); it.hasNext(); )
      {
        Map.Entry<CharSequence, WeakReference<CICS>> charSequenceWeakReferenceEntry = it.next();
        if( charSequenceWeakReferenceEntry.getValue().get() == null )
        {
          it.remove();
        }
      }
    }
    finally
    {
      LOCK.unlock();
    }
  }

  /**
   */
  CICS( CharSequence rawCharSequence )
  {
    _string = rawCharSequence.toString();
    _hashCode = getLowerCaseHashCode( rawCharSequence );
    _uniqueCode = _nextUniqueCode++;
  }

  /**
   */
  public int length()
  {
    return _string.length();
  }

  /**
   */
  public char charAt( int index )
  {
    return _string.charAt( index );
  }

  /**
   */
  public CharSequence subSequence( int start, int end )
  {
    return _string.subSequence( start, end );
  }

  /**
   */
  public String toString()
  {
    return _string;
  }

  public int hashCode()
  {
    return _hashCode;
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }

    if( !(o instanceof CICS) )
    {
      return false;
    }

    CICS cics = (CICS)o;
    if( _uniqueCode == cics._uniqueCode )
    {
      return true;
    }

    if( _hashCode != cics._hashCode )
    {
      return false;
    }

    return _string.equalsIgnoreCase( cics._string );
  }

  public static int getLowerCaseHashCode( CharSequence charSeq )
  {
    int iHashCode = 0;
    int iLength = charSeq.length();
    for( int i = 0; i < iLength; i++ )
    {
      iHashCode = 31 * iHashCode + Character.toLowerCase( charSeq.charAt( i ) );
    }
    return iHashCode;
  }

  public boolean equalsIgnoreCase( CharSequence cs )
  {
    return equalsIgnoreCase( this, cs );
  }

  public static boolean equalsIgnoreCase( CharSequence cs1, CharSequence cs2 )
  {
    if( cs1.equals( cs2 ) )
    {
      return true;
    }
    return cs1.toString().equalsIgnoreCase( cs2.toString() );
  }

  private Object readResolve()
  {
    return get( _string );
  }

  public String toLowerCase()
  {
    return _string.toLowerCase();
  }

  public static void resetForTesting()
  {
    CASE_SENSITIVE_CACHE.clear();
  }
}
