/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.protocols.gosuclass;

import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.compiler.SingleServingGosuClassLoader;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.TypeSystemLockHelper;
import gw.util.GosuExceptionUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 */
public class GosuClassesUrlConnection extends URLConnection {
  private static final String[] JAVA_NAMESPACES_TO_IGNORE = {
    "java/", "javax/", "sun/"
  };
  private ICompilableType _type;
  private boolean _bDirectory;
  private boolean _bInvalid;

  public GosuClassesUrlConnection( URL url ) {
    super( url );
  }

  @Override
  public void connect() throws IOException {
    if( _bInvalid ) {
      throw new IOException();
    }
    connectImpl();
    if( _bInvalid ) {
      throw new IOException();
    }
  }

  private boolean connectImpl() {
    if( _bInvalid ) {
      return false;
    }
    if( _type == null && !_bDirectory ) {
      String strPath = URLDecoder.decode(getURL().getPath());
      String strClass = strPath.substring( 1 );
      if( !ignoreJavaClass( strClass ) ) {
        String strType = strClass.replace( '/', '.' );
        int iIndexClass = strType.lastIndexOf( ".class" );
        if( iIndexClass > 0 ) {
          strType = strType.substring( 0, iIndexClass ).replace( '$', '.' );
          maybeAssignGosuType( strType );
        }
        else if( strPath.endsWith( "/" ) ) {
          _bDirectory = true;
        }
      }
      _bInvalid = _type == null && !_bDirectory;
    }
    return !_bInvalid;
  }

  private void maybeAssignGosuType( String strType ) {
    if( strType.contains( IGosuProgram.NAME_PREFIX + "eval_" ) ) {
      // Never load an eval class here, they should always load in a single-serving loader
      return;
    }
    ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
    TypeSystemLockHelper.getTypeSystemLockWithMonitor( loader );
    try {

      IModule global = TypeSystem.getGlobalModule();
      IType type;
      TypeSystem.pushModule(global);
      try {
        type = TypeSystem.getByFullNameIfValidNoJava( strType );
      } finally {
        TypeSystem.popModule(global);
      }
      if( type instanceof ICompilableType ) {
        if( !isInSingleServingLoader( type.getEnclosingType() ) ) {
          _type = (ICompilableType)type;
        }
      }
    }
    finally {
      TypeSystem.unlock();
    }
  }

  private boolean isInSingleServingLoader( IType type ) {
    if( type instanceof IJavaBackedType ) {
      return ((IJavaBackedType)type).getBackingClass().getClassLoader() instanceof SingleServingGosuClassLoader;
    }
    if( type instanceof IHasJavaClass ) {
      return ((IHasJavaClass)type).getBackingClass().getClassLoader() instanceof SingleServingGosuClassLoader;
    }
    return false;
  }

  private boolean ignoreJavaClass( String strClass ) {
    for( String namespace: JAVA_NAMESPACES_TO_IGNORE ) {
      if( strClass.startsWith( namespace ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    if( _type != null ) {
      // Avoid compiling until the bytes are actually requested;
      // sun.misc.URLClassPath grabs the inputstream twice, the first time is for practice :)
      return new LazyByteArrayInputStream();
    }
    else if( _bDirectory ) {
      return new ByteArrayInputStream( new byte[0] );
    }
    throw new IOException( "Invalid or missing Gosu class for: " + url.toString() );
  }

  public boolean isValid() {
    return connectImpl();
  }

  class LazyByteArrayInputStream extends InputStream
  {
    protected byte _buf[];
    protected int _pos;
    protected int _mark;
    protected int _count;

    private void init() {
      if( _buf == null ) {
        ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
        TypeSystemLockHelper.getTypeSystemLockWithMonitor( loader );
        try {
          //System.out.println( "Compiling: " + _type.getName() );
          _buf = GosuClassLoader.instance().getBytes( _type);
          _pos = 0;
          _count = _buf.length;
        }
        catch( Throwable e ) {
          // log the exception, it tends to get swollowed esp. if it's the class doesn't parse
          e.printStackTrace();
          throw GosuExceptionUtil.forceThrow( e );
        }
        finally {
          TypeSystem.unlock();
        }
      }
    }

    public int read() {
      init();
      return (_pos < _count) ? (_buf[_pos++] & 0xff) : -1;
    }

    @Override
    public int read( byte[] b ) throws IOException {
      init();
      return super.read( b );
    }

    public int read( byte b[], int off, int len ) {
      init();
      if( b == null ) {
        throw new NullPointerException();
      }
      else if( off < 0 || len < 0 || len > b.length - off ) {
        throw new IndexOutOfBoundsException();
      }
      if( _pos >= _count ) {
        return -1;
      }
      if( _pos + len > _count ) {
        len = _count - _pos;
      }
      if( len <= 0 ) {
        return 0;
      }
      System.arraycopy( _buf, _pos, b, off, len );
      _pos += len;
      return len;
    }

    public long skip( long n ) {
      if( _pos + n > _count ) {
        n = _count - _pos;
      }
      if( n < 0 ) {
        return 0;
      }
      _pos += n;
      return n;
    }

    public int available() {
      init();
      return _count - _pos;
    }

    public boolean markSupported() {
      return true;
    }

    public void mark( int readAheadLimit ) {
      _mark = _pos;
    }

    public void reset() {
      _pos = _mark;
    }

    public void close() throws IOException {
    }
  }
}
