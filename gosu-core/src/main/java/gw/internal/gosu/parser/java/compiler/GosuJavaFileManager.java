package gw.internal.gosu.parser.java.compiler;

import gw.lang.javac.ClassJavaFileObject;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.util.cache.FqnCache;
import java.io.IOException;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
*/
class GosuJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> implements ITypeLoaderListener
{
  private FqnCache<ClassJavaFileObject> _classFiles;

  GosuJavaFileManager( JavaFileManager fileManager )
  {
    super( fileManager );
    _classFiles = new FqnCache<>();
    TypeSystem.addTypeLoaderListenerAsWeakRef( this );
  }

  @Override
  public JavaFileObject getJavaFileForOutput( Location location, String className, JavaFileObject.Kind kind, FileObject sibling ) throws IOException
  {
    ClassJavaFileObject file = new ClassJavaFileObject( className, kind );
    _classFiles.add( className, file );
    className = className.replace( '$', '.' );
    _classFiles.add( className, file );
    return file;
  }

  public ClassJavaFileObject findCompiledFile( String fqn )
  {
    return _classFiles.get( fqn );
  }

  public void remove( String fqn )
  {
    _classFiles.remove( fqn );
  }

  @Override
  public void refreshedTypes( RefreshRequest request )
  {
    switch( request.kind )
    {
      case MODIFICATION:
      case DELETION:
        _classFiles.remove( request.types );
    }
  }

  @Override
  public void refreshed()
  {
    _classFiles = new FqnCache<>();
  }
}
