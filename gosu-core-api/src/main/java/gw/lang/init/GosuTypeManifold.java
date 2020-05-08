package gw.lang.init;

import com.sun.tools.javac.util.Context;
import gw.config.CommonServices;
import gw.config.IPlatformHelper;
import gw.config.Registry;
import gw.lang.Gosu;
import gw.lang.GosuShop;
import gw.lang.gosuc.GosucModule;
import gw.lang.gosuc.GosucUtil;
import gw.lang.gosuc.simple.CompilerPlatformHelper;
import gw.lang.ir.SignatureUtil;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.util.GosuClassUtil;
import gw.util.concurrent.LocklessLazyVar;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
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
import manifold.api.util.JavacDiagnostic;
import manifold.internal.host.JavacManifoldHost;
import manifold.internal.javac.IssueReporter;
import manifold.internal.javac.JavacPlugin;
import manifold.internal.javac.SourceJavaFileObject;


import static gw.lang.Gosu.deriveClasspathFrom;
import static javax.tools.StandardLocation.CLASS_OUTPUT;

public class GosuTypeManifold implements ITypeManifold
{
  private static final LocklessLazyVar<GosuTypeManifold> JAVAC_INSTANCE = LocklessLazyVar.make( () -> {
    if( JavacPlugin.instance() == null )
    {
      return null;
    }
    return (GosuTypeManifold)JavacPlugin.instance().getHost().getSingleModule().getTypeManifolds().stream()
      .filter( tm -> tm instanceof GosuTypeManifold )
      .findFirst().orElseThrow( IllegalStateException::new );
  } );

  public static boolean isPostJava()
  {
    GosuTypeManifold instance = JAVAC_INSTANCE.get();
    return instance != null && instance.isPostJavaMode();
  }

  private IModule _module;
  private boolean _postJava;

  @Override
  public void init( IModule module )
  {
    _module = module;

    initCompiler();
  }

  private void initCompiler()
  {
    if( _module.getHost() instanceof JavacManifoldHost )
    {
      CommonServices.getKernel().redefineService_Privileged( IPlatformHelper.class, new CompilerPlatformHelper() );

      if( "gw".equals( System.getProperty( "compiler.type" ) ) )
      {
        try
        {
          IEntityAccess access = (IEntityAccess)Class.forName( "gw.internal.gosu.parser.gwPlatform.GWEntityAccess" ).newInstance();
          ICoercionManager coercionManager = (ICoercionManager)Class.forName( "gw.internal.gosu.parser.gwPlatform.GWCoercionManager" ).newInstance();
          CommonServices.getKernel().redefineService_Privileged( IEntityAccess.class, access );
          CommonServices.getKernel().redefineService_Privileged( ICoercionManager.class, coercionManager );
          Registry.instance().setAllowEntityQueires( true );
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }

      List<String> classpath = _module.getJavaClassPath().stream().map( e -> e.getPath().getFileSystemPathString() ).collect( Collectors.toList() );
      classpath.addAll( deriveClasspathFrom( Gosu.class ).stream().map( e -> e.getPath().getFileSystemPathString() ).collect( Collectors.toList() ) );
      classpath.addAll( GosucUtil.getGosuBootstrapJars().stream().map( uri -> new File( URI.create( uri ) ).getAbsolutePath() ).collect( Collectors.toList() ) );

      IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment();
      GosuInitialization gosuInitialization = GosuInitialization.instance( execEnv );
      GosucModule gosucModule = new GosucModule( IExecutionEnvironment.DEFAULT_SINGLE_MODULE_NAME,
        _module.getSourcePath().stream().map( e -> e.getPath().getFileSystemPathString() ).collect( Collectors.toList() ),
        classpath,
        Collections.emptyList(),
        _module.getOutputPath().isEmpty() ? null : _module.getOutputPath().get( 0 ).getPath().getFileSystemPathString(),
        Collections.emptyList(),
        Collections.emptyList() );
      gosuInitialization.initializeCompiler( gosucModule );
    }
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
  public String contribute( JavaFileManager.Location location, String fqn, boolean genStubs, String existing, DiagnosticListener<JavaFileObject> errorHandler )
  {
    IGosuClass gsClass = findGosuClass( fqn );
    // _postJava is indicative of the class not being referenced from Java, therefore its stub only needs class structure
    return GosuShop.generateJavaStub( gsClass, _postJava );
  }

  @Override
  public Collection<String> getAllTypeNames()
  {
    // necessary for static compilation via -Amanifld.source.<file-ext>=<type-name-regex> javac command line option
    // e.g., to statically compile all Gosu types using javac:  -Amanifld.source.gs=.*
    return GosuClassTypeLoader.getDefaultClassLoader().getAllTypeNames();
  }

  @Override
  public Collection<TypeName> getTypeNames( String pkg )
  {
    return (Collection)GosuClassTypeLoader.getDefaultClassLoader().getTypeNames( pkg );
  }

  @Override
  public List<IFile> findFilesForType( String fqn )
  {
    IGosuClass gsClass = findGosuClass( fqn );
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

    return Collections.singletonList( _module.getHost().getFileSystem().getIFile( url ) );
  }

  @Override
  public void clear()
  {

  }

  @Override
  public boolean handlesFileExtension( String fileExt )
  {
    return Arrays.stream( GosuClassTypeLoader.ALL_EXTS ).anyMatch( ext -> ext.equalsIgnoreCase( '.' + fileExt ) );
  }

  @Override
  public boolean handlesFile( IFile file )
  {
    return handlesFileExtension( file.getExtension() );
  }

  @Override
  public String[] getTypesForFile( IFile file )
  {
    return TypeSystem.getTypesForFile( TypeSystem.getExecutionEnvironment().getJreModule(),
      CommonServices.getFileSystem().getIFile( file.toJavaFile() ) );
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
  public void enterPostJavaCompilation()
  {
    _postJava = true;
  }

  public boolean isPostJavaMode()
  {
    return _postJava;
  }

  @Override
  public void parse( String fqn )
  {
    IGosuClass gosuClass = findGosuClass( fqn );
    gosuClass.isValid();
    reportIssues( gosuClass );
  }

  private void reportIssues( IGosuClass gosuClass )
  {
    ParseResultsException pre = gosuClass.getParseResultsException();
    if( pre == null )
    {
      // no issues
      return;
    }

    List<IParseIssue> parseIssues = pre.getParseIssues();
    if( !parseIssues.isEmpty() )
    {
      JavaFileObject sourceFile = new SourceJavaFileObject( gosuClass.getSourceFileHandle().getFile().getPath().getFileSystemPathString() );
      for( IParseIssue issue: parseIssues )
      {
        report( sourceFile, issue );
      }
    }
  }

  @Override
  public byte[] compile( String fqn )
  {
    IGosuClass gosuClass = findGosuClass( fqn );
    if( !(getModule().getHost() instanceof IRuntimeManifoldHost) )
    {
      compileAnonymousTypes( gosuClass );
    }
    return gosuClass.compile();
  }

  private void compileAnonymousTypes( IGosuClass gosuClass )
  {
    for( IGosuClass inner: gosuClass.getInnerClasses() )
    {
      if( inner.isAnonymous() )
      {
        compileAnonymousType( inner );
      }
    }
  }

  private void compileAnonymousType( IGosuClass lambda )
  {
    try
    {
      String name = SignatureUtil.makeJavaName( lambda );
      String pkg = GosuClassUtil.getPackage( name );
      String simple = SignatureUtil.getSimpleName( name );
      JavaFileManager fm = JavacPlugin.instance().getJavaFileManager();
      FileObject file = fm.getFileForOutput( CLASS_OUTPUT, pkg, simple + ".class", null );
      OutputStream out = file.openOutputStream();
      byte[] bytes = lambda.compile();
      out.write( bytes );
      out.close();
      compileAnonymousTypes( lambda );
    }
    catch( IOException ioe )
    {
      throw new RuntimeException( ioe );
    }
  }

  private IGosuClass findGosuClass( String fqn )
  {
    IType type = TypeSystem.getByFullNameIfValidNoJava( fqn );
    return type instanceof IGosuClass ? (IGosuClass)type : null;
  }

  private void report( JavaFileObject sourceFile, IParseIssue issue )
  {
    Context ctx = JavacPlugin.instance().getContext();
    IssueReporter<JavaFileObject> reporter = new IssueReporter<>( () -> ctx );
    Diagnostic.Kind kind = issue instanceof ParseException ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
    reporter.report( new JavacDiagnostic( sourceFile, kind,
      issue.getTokenStart(), issue.getLine(), issue.getColumn(), issue.getUIMessage() ) );
  }
}
