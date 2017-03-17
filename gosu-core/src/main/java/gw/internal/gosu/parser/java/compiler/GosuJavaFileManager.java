package gw.internal.gosu.parser.java.compiler;

import com.sun.tools.javac.file.JavacFileManager;
import gw.internal.gosu.parser.GosuClass;
import gw.lang.ir.SignatureUtil;
import gw.lang.javac.ClassJavaFileObject;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.TypeName;
import gw.util.cache.FqnCache;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

/**
*/
class GosuJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> implements ITypeLoaderListener
{
  private FqnCache<ClassJavaFileObject> _classFiles;
  private JavacFileManager _javacMgr;

  GosuJavaFileManager( JavaFileManager fileManager )
  {
    super( fileManager );
    _javacMgr = (JavacFileManager)fileManager;
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

  public Iterable<JavaFileObject> list( Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse ) throws IOException
  {
    Iterable<JavaFileObject> list = super.list( location, packageName, kinds, recurse );
    if( kinds.contains( JavaFileObject.Kind.SOURCE )&&
        (location == StandardLocation.SOURCE_PATH) )
    {
      INamespaceType namespace = TypeSystem.getNamespace( packageName );
      if( namespace == null )
      {
        return list;
      }

      ArrayList<JavaFileObject> newList = new ArrayList<>();
      list.forEach( newList::add );
      Set<String> names = makeNames( list );

      Set<TypeName> children = namespace.getChildren( namespace );
      for( TypeName tn: children )
      {
        if( names.contains( SignatureUtil.getSimpleName( tn.name ) ) )
        {
          continue;
        }

        if( tn.kind == TypeName.Kind.NAMESPACE )
        {
          if( recurse )
          {
            Iterable<JavaFileObject> sublist = list( location, tn.name, kinds, recurse );
            sublist.forEach( newList::add );
          }
        }
        else
        {
          IType type = TypeSystem.getByFullNameIfValid( tn.name.replace( '$', '.' ), tn.getModule() );
          if( type instanceof IGosuClass && !GosuClass.ProxyUtil.isProxy( type ) )
          {
            newList.add( makeJavaStub( (IGosuClass)type ) );
          }
        }
      }
      list = newList;
    }
    return list;
  }

  private Set<String> makeNames( Iterable<JavaFileObject> list )
  {
    HashSet<String> set = new HashSet<>();
    for( JavaFileObject file: list )
    {
      String name = file.getName();
      if( name.endsWith( ".java" ) )
      {
        set.add( name.substring( name.lastIndexOf( File.separatorChar ) + 1, name.lastIndexOf( '.' ) ) );
      }
    }
    return set;
  }

  private JavaFileObject makeJavaStub( IGosuClass gsClass )
  {
    return new GeneratedJavaStubFileObject( _javacMgr, gsClass.getName(), () -> JavaStubGenerator.instance().genStub( gsClass ) );
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
