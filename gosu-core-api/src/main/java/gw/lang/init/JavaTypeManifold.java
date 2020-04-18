package gw.lang.init;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import manifold.api.fs.IFile;
import manifold.api.host.IModule;
import manifold.api.host.IRuntimeManifoldHost;
import manifold.api.host.RefreshKind;
import manifold.api.type.ClassType;
import manifold.api.type.ContributorKind;
import manifold.api.type.ISourceKind;
import manifold.api.type.ITypeManifold;
import manifold.api.type.TypeName;
import manifold.internal.javac.JavacPlugin;

/**
 * Loads Java from source (*.java files) using Gosu's Java support.
 */
public class JavaTypeManifold implements ITypeManifold
{
  private IModule _module;

  @Override
  public boolean accept( IModule module )
  {
    return module.getHost() instanceof IRuntimeManifoldHost;
  }

  @Override
  public void init( IModule module )
  {
    _module = module;
  }

  @Override
  public IModule getModule()
  {
    return _module;
  }

  @Override
  public ISourceKind getSourceKind()
  {
    return ISourceKind.Java;
  }

  @Override
  public ContributorKind getContributorKind()
  {
    return ContributorKind.Primary;
  }

  @Override
  public boolean isType( String fqn )
  {
    if( JavacPlugin.instance() != null ) {
      return false;
    }
    return findJavaClass( fqn ) != null;
  }

  @Override
  public boolean isTopLevelType( String fqn )
  {
    if( !isType( fqn ) )
    {
      return false;
    }

    IType type = findJavaClass( fqn );
    return type != null && type.getEnclosingType() == null;
  }

  @Override
  public boolean isPackage( String pkg )
  {
    return TypeSystem.getNamespace( pkg ) != null;
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    IType type = findJavaClass( fqn );
    return type == null ? null : ClassType.JavaClass;
  }

  @Override
  public String getPackage( String fqn )
  {
    IType type = findJavaClass( fqn );
    return type == null ? null : type.getNamespace();
  }

  @Override
  public String contribute( JavaFileManager.Location location, String fqn, boolean genStubs, String existing, DiagnosticListener<JavaFileObject> errorHandler )
  {
    // DebugLogUtil.log( "c:\\temp\\gosu_type_man_log.log", "JAVA: " + fqn, true );
    IJavaType javaClass = (IJavaType)findJavaClass( fqn );
    return javaClass.getSourceFileHandle().getSource().getSource();
  }

  @Override
  public Collection<String> getAllTypeNames()
  {
    //## todo: is this ok?  don't want to invoke if unnecessary
    return Collections.emptyList();
  }

  @Override
  public Collection<TypeName> getTypeNames( String pkg )
  {
    //noinspection unchecked
    return (Collection)TypeSystem.getDefaultTypeLoader().getTypeNames( pkg );
  }

  @Override
  public List<IFile> findFilesForType( String fqn )
  {
    IJavaType javaClass = (IJavaType)findJavaClass( fqn );
    if( javaClass == null )
    {
      return Collections.emptyList();
    }

    ISourceFileHandle sfh = javaClass.getSourceFileHandle();
    if( sfh == null )
    {
      return Collections.emptyList();
    }

    gw.fs.IFile file = sfh.getFile();
    if( file == null )
    {
      return Collections.emptyList();
    }

    URL url;
    try
    {
      url = file.toURI().toURL();
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( e );
    }

    return Collections.singletonList( GosuRuntimeManifoldHost.get().getFileSystem().getIFile( url ) );
  }

  @Override
  public void clear()
  {

  }

  @Override
  public boolean handlesFileExtension( String s )
  {
    return false;
  }

  @Override
  public boolean handlesFile( IFile file )
  {
    return "java".equalsIgnoreCase( file.getExtension() );
  }

  @Override
  public String[] getTypesForFile( IFile iFile )
  {
    return new String[0];
  }

  @Override
  public RefreshKind refreshedFile( IFile iFile, String[] strings, RefreshKind refreshKind )
  {
    return null;
  }

  private IType findJavaClass( String fqn )
  {
    IType type = TypeSystem.getByFullNameIfValid( fqn );
    if( type instanceof IJavaType )
    {
      ISourceFileHandle sfh = ((IJavaType)type).getSourceFileHandle();
      if( sfh != null && sfh.getFile() != null && sfh.getFile().isJavaFile() && sfh.getFile().getExtension().equals( "java" ) )
      {
        return type;
      }
    }
    return null;
  }
}
