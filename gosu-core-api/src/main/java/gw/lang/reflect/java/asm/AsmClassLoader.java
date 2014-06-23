/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.lang.reflect.TypeSystem;
import gw.util.cache.FqnCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class AsmClassLoader {
  private static final Map<Object, AsmClassLoader> CACHE_BY_MOD = new HashMap<Object, AsmClassLoader>();
  private Object _module;
  private FqnCache<AsmClass> _cache;

  public static AsmClass loadClass( Object module, String fqn, InputStream is ) {
    AsmClassLoader loader = getAsmClassLoader( module );
    return loader.findClass( fqn, is );
  }

  private static AsmClassLoader getAsmClassLoader( Object module ) {
    AsmClassLoader loader = CACHE_BY_MOD.get( module );
    if( loader == null ) {
      TypeSystem.lock();
      try {
        loader = CACHE_BY_MOD.get( module );
        if( loader == null ) {
          CACHE_BY_MOD.put( module, loader = new AsmClassLoader( module ) );
        }
      }
      finally {
        TypeSystem.unlock();
      }
    }
    return loader;
  }

  private AsmClassLoader( Object module ) {
    _module = module;
    _cache = new FqnCache<AsmClass>();
  }

  private AsmClass findClass( String fqn, InputStream is ) {
    AsmClass asmClass = _cache.get( fqn );
    if( asmClass == null ) {
      asmClass = _cache.get( fqn );
      if( asmClass == null ) {
        asmClass = new AsmClass( _module, getContent( is ) );
        _cache.add( fqn, asmClass );
      }
    }
    return asmClass;
  }

  private static byte[] getContent( InputStream is ) {
    byte[] buf = new byte[1024];
    ExposedByteArrayOutputStream out = new ExposedByteArrayOutputStream();
    while( true ) {
      int count = 0;
      try {
        count = is.read( buf );
      }
      catch( IOException e ) {
        throw new RuntimeException( e );
      }
      if( count < 0 ) {
        break;
      }
      out.write( buf, 0, count );
    }
    try {
      out.flush();
      is.close();
      return out.getByteArray();
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public static class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
    public ExposedByteArrayOutputStream() {
      super( 1024 );
    }

    public byte[] getByteArray() {
      return buf;
    }
  }

}
