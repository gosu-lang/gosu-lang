/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.protocols.gosuclass;

import gw.fs.IFile;
import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.compiler.SingleServingGosuClassLoader;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.TypeSystemLockHelper;
import gw.util.GosuExceptionUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 */
public class GosuClassesUrlConnection extends URLConnection {
  private static final String[] JAVA_NAMESPACES_TO_IGNORE = {
    "java/", "javax/", "sun/"
  };
  private static final String META_INF_MANIFEST_MF = "META-INF/MANIFEST.MF";

  private ICompilableType _type;
  private ClassLoader _loader;
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
      String strPath = URLDecoder.decode( getURL().getPath() );
      String strClass = strPath.substring( 1 );
      if( isManifest( strClass ) ) {
        // Some tools (Equinox) expect to find a jar manifest file in the path entry, so we fake an empty one here
        return true;
      }
      if( !ignoreJavaClass( strClass ) ) {
        String strType = strClass.replace( '/', '.' );
        int iIndexClass = strType.lastIndexOf( ".class" );
        if( iIndexClass > 0 ) {
          strType = strType.substring( 0, iIndexClass ).replace( '$', '.' );
          maybeAssignGosuType( findClassLoader( getURL().getHost() ), strType );
        }
        else if( strPath.endsWith( "/" ) ) {
          _bDirectory = true;
        }
      }
      _bInvalid = _type == null && !_bDirectory;
    }
    return !_bInvalid;
  }

  private boolean isManifest( String strClass ) {
    return strClass.equalsIgnoreCase( META_INF_MANIFEST_MF );
  }

  private ClassLoader findClassLoader( String host ) {
    int identityHash = Integer.parseInt( host );
    ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
    while( loader != null ) {
      if( System.identityHashCode( loader ) == identityHash ) {
        return loader;
      }
      loader = loader.getParent();
    }
    throw new IllegalStateException( "Can't find ClassLoader with identity hash: " + identityHash );
  }

  private void maybeAssignGosuType( ClassLoader loader, String strType ) {
    if( strType.contains( IGosuProgram.NAME_PREFIX + "eval_" ) ) {
      // Never load an eval class here, they should always load in a single-serving loader
      return;
    }
    TypeSystemLockHelper.getTypeSystemLockWithMonitor( loader );
    try {
      IModule global = TypeSystem.getGlobalModule();
      IType type;
      TypeSystem.pushModule( global );
      try {
        type = TypeSystem.getByFullNameIfValidNoJava( strType );
      }
      finally {
        TypeSystem.popModule( global );
      }
      if( type instanceof ICompilableType ) {
        if( !isInSingleServingLoader( type.getEnclosingType() ) ) {
          if( !GosuClassPathThing.canWrapChain() ) {
            _type = (ICompilableType)type;
            _loader = loader;
          }
          else {
            handleChainedLoading( loader, (ICompilableType)type );
          }
        }
      }
    }
    finally {
      TypeSystem.unlock();
    }
  }

  private void handleChainedLoading( ClassLoader loader, ICompilableType type ) {
    String ext = getFileExt( type );
    if( ext == null ) {
      // This is a program or some other intangible, make sure we load these in the base loader
      if( loader == TypeSystem.getGosuClassLoader().getActualLoader() ) {
        _type = (ICompilableType)type;
        _loader = loader;
      }
    }
    else if( isResourceInLoader( loader, ext ) ) {
      _type = (ICompilableType)type;
      _loader = loader;
    }
  }

//  private void crap( ICompilableType type, ClassLoader loader, String ext ) {
//    System.out.println( "Loading: " + type.getName() + "   ext: " + ext );
//    System.out.println( "Source File: " + type.getSourceFileHandle().getFile() == null ? "none" : type.getSourceFileHandle().getFile().getPath().getFileSystemPathString() );
//    System.out.println( "Module: " + type.getTypeLoader().getModule().getName() );
//    System.out.println( "Current Loader: " + loader.getClass() );
//    System.out.println( "Gosu Loader: " + TypeSystem.getGosuClassLoader().getActualLoader().getClass() );
//    System.out.println( "Loader Chain from current: " + loaderChain( loader ) );
//    if( ext != null ) {
//      System.out.println( "Resource in loader?: " + isResourceInLoader( loader, ext ) );
//    }
//  }

  private String loaderChain( ClassLoader loader ) {
    if( loader == null ) {
      return "<null>";
    }
    return loader.getClass().getName() + " -> " + loaderChain( loader.getParent() );
  }

  /**
   * @param type a type that is dynamically compiled to bytecode from source by Gosu
   * @return the corresponding file extension to replace the URL's .class extension when
   *  searching for the source file to compile.  Otherwise if the type has no physical
   *  file or the file is not obtained from the classpath corresponding with a ClassLoader,
   *  returns null.
   */
  private String getFileExt( ICompilableType type ) {
    while( type instanceof ICompilableType ) {
      ISourceFileHandle sfh = type.getSourceFileHandle();
      IFile file = sfh.getFile();
      if( file != null ) {
        if( !sfh.isStandardPath() ) {
          // The path is not in the target classpath of any ClassLoader e.g., it's added to Gosu's type sys repo in StandardEntityAccess#getAdditionalSourceRoots()
          return null;
        }
        return '.' + file.getExtension();
      }
      type = type.getEnclosingType();
    }
    return null;
  }

  private static Method _findResource = null;
  private boolean isResourceInLoader( ClassLoader loader, String ext ) {
    String strPath = URLDecoder.decode( getURL().getPath() );
    strPath = strPath.substring( 1 );
    int iIndex = strPath.indexOf( "$" ); // Get the location of the top-level type (only one file for a nesting of types)
    iIndex = iIndex < 0 ? strPath.lastIndexOf( ".class" ) : iIndex;
    if( iIndex > 0 ) {
      strPath = strPath.substring( 0, iIndex ) + ext;
    }
    if( loader instanceof URLClassLoader ) {
      return ((URLClassLoader)loader).findResource( strPath ) != null;
    }
    else {
      try {
        if( _findResource == null ) {
          _findResource = ClassLoader.class.getDeclaredMethod( "findResource", new Class[] {String.class} );
        }
        return _findResource.invoke( loader, strPath ) != null;
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
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
    for( String namespace : JAVA_NAMESPACES_TO_IGNORE ) {
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
    else if( getURL().getPath().toUpperCase().endsWith( META_INF_MANIFEST_MF ) ) {
      return new ByteArrayInputStream( new byte[0] );
    }
    throw new IOException( "Invalid or missing Gosu class for: " + url.toString() );
  }

  public boolean isValid() {
    return connectImpl();
  }

  class LazyByteArrayInputStream extends InputStream {
    protected byte _buf[];
    protected int _pos;
    protected int _mark;
    protected int _count;

    private void init() {
      if( _buf == null ) {
        TypeSystemLockHelper.getTypeSystemLockWithMonitor( _loader );
        try {
          //System.out.println( "Compiling: " + _type.getName() );
          _buf = GosuClassLoader.instance().getBytes( _type );
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
