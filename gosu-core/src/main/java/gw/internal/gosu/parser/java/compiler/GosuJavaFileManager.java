package gw.internal.gosu.parser.java.compiler;

import gw.internal.gosu.parser.GosuClass;
import gw.lang.ir.SignatureUtil;
import gw.lang.javac.InMemoryClassJavaFileObject;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.ISourceProducer;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
  private final boolean _fromJavaC;
  private FqnCache<InMemoryClassJavaFileObject> _classFiles;
  private FqnCache<JavaFileObject> _generatedFiles;
  private JavaFileManager _javacMgr;

  GosuJavaFileManager( JavaFileManager fileManager, boolean fromJavaC )
  {
    super( fileManager );
    _fromJavaC = fromJavaC;
    _javacMgr = fileManager;
    _classFiles = new FqnCache<>();
    _generatedFiles = new FqnCache<>();
    TypeSystem.addTypeLoaderListenerAsWeakRef( this );
  }

  @Override
  public JavaFileObject getJavaFileForOutput( Location location, String className, JavaFileObject.Kind kind, FileObject sibling ) throws IOException
  {
    if( !_fromJavaC || kind == JavaFileObject.Kind.CLASS && sibling instanceof GeneratedJavaStubFileObject )
    {
      InMemoryClassJavaFileObject file = new InMemoryClassJavaFileObject( className, kind );
      _classFiles.add( className, file );
      className = className.replace( '$', '.' );
      _classFiles.add( className, file );
      return file;
    }
    return super.getJavaFileForOutput( location, className, kind, sibling );
  }

  public InMemoryClassJavaFileObject findCompiledFile( String fqn )
  {
    return _classFiles.get( fqn );
  }

  public JavaFileObject getSourceFileForInput( Location location, String fqn, JavaFileObject.Kind kind ) throws IOException
  {
    JavaFileObject file = super.getJavaFileForInput( location, fqn, kind );
    if( file != null )
    {
      return file;
    }

    return findGeneratedFile( fqn.replace( '$', '.' ), TypeSystem.getCurrentModule() );
  }

  public Iterable<JavaFileObject> list( Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse ) throws IOException
  {
    Iterable<JavaFileObject> list = super.list( location, packageName, kinds, recurse );
    if( kinds.contains( JavaFileObject.Kind.SOURCE ) && (location == StandardLocation.SOURCE_PATH || location == StandardLocation.CLASS_PATH) )
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
      for( TypeName tn : children )
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
          JavaFileObject file = findGeneratedFile( tn.name.replace( '$', '.' ), tn.getModule() );
          if( file != null )
          {
            newList.add( file );
          }
        }
      }
      list = newList;
    }
    return list;
  }

  private JavaFileObject findGeneratedFile( String fqn, IModule module )
  {
    FqnCacheNode<JavaFileObject> node = _generatedFiles.getNode( fqn );
    if( node != null )
    {
      return node.getUserData();
    }

    IType type = TypeSystem.getByFullNameIfValid( fqn, module );
    JavaFileObject file = null;
    if( type != null && type instanceof IFileRepositoryBasedType && !GosuClass.ProxyUtil.isProxy( type ) && !isErrantTestArtifact( fqn ) )
    {
      if( !(type instanceof IJavaType) )
      {
        file = makeJavaStub( type );
        _generatedFiles.add( type.getName(), file );
      }
      else
      {
        ISourceFileHandle sourceFileHandle = ((IJavaType)type).getSourceFileHandle();
        if( sourceFileHandle != null )
        {
          ISourceProducer sourceProducer = sourceFileHandle.getSourceProducer();
          if( sourceProducer != null )
          {
            // The source for this type is not on disk, but is instead generated on demand
            file = produceSource( (IFileRepositoryBasedType)type );
            _generatedFiles.add( type.getName(), file );
          }
        }
      }
    }

    if( file == null )
    {
      // cache the miss
      _generatedFiles.add( fqn, null );
    }

    return file;
  }

  private boolean isErrantTestArtifact( String fqn )
  {
    return fqn.contains( ".Errant_" );
  }

  private Set<String> makeNames( Iterable<JavaFileObject> list )
  {
    HashSet<String> set = new HashSet<>();
    for( JavaFileObject file : list )
    {
      String name = file.getName();
      if( name.endsWith( ".java" ) )
      {
        set.add( name.substring( name.lastIndexOf( File.separatorChar ) + 1, name.lastIndexOf( '.' ) ) );
      }
    }
    return set;
  }

  private JavaFileObject produceSource( IFileRepositoryBasedType loadableType )
  {
    return new GeneratedJavaStubFileObject( loadableType.getName(), () -> loadableType.getSourceFileHandle().getSource().getSource() );
  }

  private JavaFileObject makeJavaStub( IType loadableType )
  {
    return new GeneratedJavaStubFileObject( loadableType.getName(), () -> JavaStubGenerator.instance().genStub( loadableType ) );
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

  public Collection<InMemoryClassJavaFileObject> getCompiledFiles()
  {
    HashSet<InMemoryClassJavaFileObject> files = new HashSet<>();
    _classFiles.visitDepthFirst(
      o -> {
        if( o != null )
        {
          files.add( o );
        }
        return true;
      } );
    return files;
  }

  @Override
  public String inferBinaryName( Location location, JavaFileObject fileObj )
  {
    if( fileObj instanceof GeneratedJavaStubFileObject )
    {
      return removeExtension( fileObj.getName() ).replace( File.separatorChar, '.' ).replace( '/', '.' );
    }
    return super.inferBinaryName( location, fileObj );
  }

  @Override
  public boolean isSameFile( FileObject var1, FileObject var2 )
  {
    if( var1 instanceof GeneratedJavaStubFileObject || var2 instanceof GeneratedJavaStubFileObject )
    {
      return var1.equals( var2 );
    }
    return super.isSameFile( var1, var2 );
  }

  protected static String removeExtension( String var0 )
  {
    int var1 = var0.lastIndexOf( "." );
    return var1 == -1 ? var0 : var0.substring( 0, var1 );
  }
}
