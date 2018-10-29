package gw.lang.init;

import gw.lang.GosuShop;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import manifold.api.fs.IFile;
import manifold.api.host.IModule;
import manifold.api.host.RefreshKind;
import manifold.api.type.ClassType;
import manifold.api.type.ContributorKind;
import manifold.api.type.ISourceKind;
import manifold.api.type.ITypeManifold;
import manifold.api.type.TypeName;
import manifold.util.DebugLogUtil;
import manifold.util.ManExceptionUtil;

public class GosuTypeManifold implements ITypeManifold
{
  private IModule _module;

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
    return TypeSystem.getByFullNameIfValidNoJava( fqn ) instanceof IGosuClass;
  }

  @Override
  public boolean isTopLevelType( String fqn )
  {
    if( !isType( fqn ) )
    {
      return false;
    }

    IType type = TypeSystem.getByFullNameIfValidNoJava( fqn );
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
    IType type = TypeSystem.getByFullNameIfValidNoJava( fqn );
    return type == null ? null : ClassType.JavaClass;
  }

  @Override
  public String getPackage( String fqn )
  {
    IType type = TypeSystem.getByFullNameIfValidNoJava( fqn );
    return type == null ? null : type.getNamespace();
  }

  @Override
  public String contribute( JavaFileManager.Location location, String fqn, String existing, DiagnosticListener<JavaFileObject> errorHandler )
  {
//    DebugLogUtil.log( "c:\\temp\\gosu_type_man_log.log", "GOSU: " + fqn, true );
    IGosuClass gsClass = (IGosuClass)findGosuClass( fqn );
    return GosuShop.generateJavaStub( gsClass );
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
    return (Collection)GosuClassTypeLoader.getDefaultClassLoader().getTypeNames( pkg );
  }

  @Override
  public List<IFile> findFilesForType( String fqn )
  {
    IGosuClass gsClass = (IGosuClass)findGosuClass( fqn );
    if( gsClass == null )
    {
      return Collections.emptyList();
    }

    ISourceFileHandle sfh = gsClass.getSourceFileHandle();
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
    return Arrays.stream( GosuClassTypeLoader.ALL_EXTS ).anyMatch( ext -> ext.equalsIgnoreCase( file.getExtension() ) );
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

  @Override
  public boolean isSelfCompile( String fqn )
  {
    return true;
  }

  @Override
  public byte[] compile( String fqn )
  {
    try
    {
//      DebugLogUtil.log( "c:\\temp\\gosu_type_man_log.log", "COMPILING: " + fqn, true );
      return findGosuClass( fqn ).compile();
    }
    catch( Exception e )
    {
      // wrap in an IOException since ClassLoaders conventionally handle this
      throw ManExceptionUtil.unchecked( new IOException( "Error compiling '" + fqn + "'", e ) );
    }
  }

  private IType findGosuClass( String fqn )
  {
    return TypeSystem.getByFullNameIfValidNoJava( fqn );
  }
}
