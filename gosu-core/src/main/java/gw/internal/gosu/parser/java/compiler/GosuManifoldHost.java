package gw.internal.gosu.parser.java.compiler;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import gw.config.CommonServices;
import gw.internal.gosu.compiler.SingleServingGosuClassLoader;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.Gosu;
import gw.lang.gosuc.GosucUtil;
import gw.lang.gosuc.simple.GosuCompiler;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.init.GosuInitialization;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.Keyword;
import gw.lang.reflect.Expando;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IInjectableClassLoader;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.IType;
import gw.lang.reflect.module.TypeSystemLockHelper;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import manifold.api.fs.IFile;
import manifold.api.fs.IFileSystem;
import manifold.api.host.IManifoldHost;
import manifold.api.host.IModule;
import manifold.api.host.ITypeLoader;
import manifold.api.host.ITypeLoaderListener;
import manifold.api.service.BaseService;
import manifold.api.sourceprod.TypeName;
import manifold.internal.runtime.Bootstrap;

/**
 */
public class GosuManifoldHost extends BaseService implements IManifoldHost
{
  private String[] _reservedWords;

  public IFileSystem getFileSystem()
  {
    return CommonServices.getFileSystem();
  }

  public ClassLoader getActualClassLoader()
  {
    return TypeSystem.getGosuClassLoader().getActualLoader();
  }

  public void bootstrap()
  {
    if( Gosu.bootstrapGosuWhenInitiatedViaClassfile() )
    {
      // Assuming we are in runtime, we push the root module in the case where the process was started with java.exe and not gosu.cmd
      // In other words a Gosu class can be loaded directly from classfile in a bare bones Java program where only the Gosu runtime is
      // on the classpath and no module was pushed prior to loading.
      TypeSystem.pushModule( TypeSystem.getGlobalModule() );
    }
  }

  public IModule getGlobalModule()
  {
    return TypeSystem.getGlobalModule();
  }


  public IModule getCurrentModule()
  {
    return TypeSystem.getCurrentModule();
  }

  public void resetLanguageLevel()
  {
    ILanguageLevel.Util.reset();
  }

  public boolean isPathIgnored( String path )
  {
    return CommonServices.getPlatformHelper().isPathIgnored( path );
  }

  public ITypeLoader getLoader( IFile file, IModule module )
  {
    for( gw.lang.reflect.ITypeLoader loader: ((gw.lang.reflect.module.IModule)module).getModuleTypeLoader().getTypeLoaderStack() )
    {
      if( loader.handlesFile( file ) )
      {
        return loader;
      }
    }
    throw new RuntimeException( "No type loader for file: " + file );
  }

  public String[] getAllReservedWords()
  {
    return _reservedWords == null
           ? _reservedWords = Keyword.getAll().stream().filter( Keyword::isReservedKeyword ).toArray( String[]::new )
           : _reservedWords;
  }

  public Expando createBindings()
  {
    return new Expando();
  }

  public void addTypeLoaderListenerAsWeakRef( ITypeLoaderListener listener )
  {
    TypeSystem.addTypeLoaderListenerAsWeakRef( listener );
  }

  public JavaFileObject produceFile( String fqn, IModule module )
  {
    return module.produceFile( fqn );
  }

  public void maybeAssignGousType( ClassLoader loader, String strType, URL url, BiConsumer<String, Supplier<byte[]>> assigner )
  {
    if( strType.contains( IGosuProgram.NAME_PREFIX + "eval_" ) ) {
      // Never load an eval class here, they should always load in a single-serving loader
      return;
    }
    TypeSystemLockHelper.getTypeSystemLockWithMonitor( loader );
    try {
      gw.lang.reflect.module.IModule global = TypeSystem.getGlobalModule();
      IType type;
      TypeSystem.pushModule( global );
      try {
        type = TypeSystem.getByFullNameIfValidNoJava( strType );
        if( ILanguageLevel.Util.STANDARD_GOSU() && (type == null || !(type instanceof INonLoadableType)) ) {
          assigner.accept( strType, null );
        }
      }
      finally {
        TypeSystem.popModule( global );
      }
      if( type instanceof ICompilableType ) {
        if( !isInSingleServingLoader( type.getEnclosingType() ) ) {
          if( !Bootstrap.canWrapChain() ) {
            if( !hasClassFileOnDiskInParentLoaderPath( loader, type ) ) {
              assigner.accept( null, type::compile );
            }
          }
          else {
            handleChainedLoading( loader, (ICompilableType)type, url, assigner );
          }
        }
      }
    }
    catch( Exception e ) {
      throw new RuntimeException( "Type: " + strType, e );
    }
    finally {
      TypeSystem.unlock();
    }
  }

  //## hack: total hack to handle misconfigured classloaders where parent loader and child loader have overlapping paths
  //## perf: this is probably not an insignificant perf issue while class loading i.e., the onslaught of ClassNotFoundExceptions handled here is puke worthy
  private boolean hasClassFileOnDiskInParentLoaderPath( ClassLoader loader, IType type ) {
    if( !(loader instanceof IInjectableClassLoader) ) {
      return false;
    }
    ClassLoader parent = loader.getParent();
    while( parent instanceof IInjectableClassLoader ) {
      parent = parent.getParent();
    }
    IType outer = TypeLord.getOuterMostEnclosingClass( type );
    try {
      parent.loadClass( outer.getName() );
      return true;
    }
    catch( ClassNotFoundException e ) {
      return false;
    }
  }

  private void handleChainedLoading( ClassLoader loader, ICompilableType type, URL url, BiConsumer<String, Supplier<byte[]>> assigner ) {
    String ext = getFileExt( type );
    if( ext == null ) {
      // This is a program or some other intangible, make sure we load these in the base loader
      if( loader == TypeSystem.getGosuClassLoader().getActualLoader() ||
        type.getSourceFileHandle().isIncludeModulePath() ) {
        assigner.accept( null, type::compile );
      }
    }
    else if( isResourceInLoader( loader, url, ext ) ) {
      assigner.accept( null, type::compile );
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
//
//  private String loaderChain( ClassLoader loader ) {
//    if( loader == null ) {
//      return "<null>";
//    }
//    return loader.getClass().getName() + " -> " + loaderChain( loader.getParent() );
//  }

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
  private boolean isResourceInLoader( ClassLoader loader, URL url, String ext ) {
    //noinspection deprecation
    String strPath = URLDecoder.decode( url.getPath() );
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
          _findResource = ClassLoader.class.getDeclaredMethod( "findResource", String.class );
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

  public void performLockedOperation( ClassLoader loader, Runnable operation )
  {
    TypeSystemLockHelper.getTypeSystemLockWithMonitor( loader );
    try {
      operation.run();
    }
    finally {
      TypeSystem.unlock();
    }
  }

  public void initializeAndCompileNonJavaFiles(
    JavacProcessingEnvironment jpe, JavaFileManager fileManager,
    List<String> gosuInputFiles,
    Supplier<Set<String>> sourcePath,
    Supplier<List<String>> classpath,
    Supplier<String> outputPath )
  {
    if( !GosuInitialization.isAnythingInitialized() )
    {
      IGosuCompiler gosuc = initializeGosu( sourcePath.get(), classpath.get(), outputPath.get() );
      compileGosuSourceFiles( gosuc, jpe, fileManager, gosuInputFiles );
    }
  }

  private IGosuCompiler initializeGosu( Set<String> sourcepath, List<String> classpath, String outputPath )
  {
    IGosuCompiler gosuc = new GosuCompiler();

    //## todo: use javac's jre jars
    classpath.addAll( GosucUtil.getJreJars() );
    try
    {
      //## todo: maybe don't do this
      classpath.addAll( GosucUtil.getGosuBootstrapJars() );
    }
    catch( ClassNotFoundException cnfe )
    {
      throw new RuntimeException( "Unable to locate Gosu libraries in classpath.\n", cnfe );
    }

    gosuc.initializeGosu( new ArrayList<>( sourcepath ), classpath, outputPath );
    return gosuc;
  }

  private void compileGosuSourceFiles( IGosuCompiler gosuc, JavacProcessingEnvironment jpe, JavaFileManager fileManager, List<String> gosuInputFiles )
  {
    if( !gosuInputFiles.isEmpty() )
    {
      gosuc.compile( gosuInputFiles, jpe, fileManager );
    }
  }

  public Set<TypeName> getChildrenOfNamespace( String packageName )
  {
     INamespaceType namespace = TypeSystem.getNamespace( packageName );
     if( namespace != null )
     {
       return namespace.getChildren( namespace );
     }
     return Collections.emptySet();
  }
}
