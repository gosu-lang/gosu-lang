/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.util.cache.FqnCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 */
public class AsmClassLoader {
  private Object _module;
  private FqnCache<AsmClass> _cache;

  public AsmClassLoader( Object module ) {
    _module = module;
    _cache = new FqnCache<>();
  }

  public AsmClass findClass( String fqn, InputStream is ) {
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
      int count;
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
