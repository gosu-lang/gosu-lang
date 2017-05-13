package gw.lang.gosuc.simple;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.config.IMemoryMonitor;
import gw.config.IPlatformHelper;
import gw.config.Registry;
import manifold.api.fs.FileFactory;
import manifold.api.fs.IDirectory;
import manifold.api.fs.IFile;
import gw.lang.gosuc.GosucModule;
import gw.lang.gosuc.cli.CommandLineOptions;
import gw.lang.init.GosuInitialization;
import manifold.internal.javac.IJavaParser;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IExecutionEnvironment;
import manifold.api.fs.IFileSystem;
import gw.lang.reflect.module.IModule;
import gw.util.PathUtil;
import gw.util.StreamUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import manifold.internal.javac.InMemoryClassJavaFileObject;
import manifold.internal.javac.SourceJavaFileObject;


import static gw.lang.gosuc.simple.ICompilerDriver.ERROR;
import static gw.lang.gosuc.simple.ICompilerDriver.WARNING;

public class GosuCompiler implements IGosuCompiler
{
  private static final String[] SOURCE_EXTS = { ".gs", ".gsx", ".gst", ".java" };

  protected GosuInitialization _gosuInitialization;

  @Override
  public boolean compile( List<String> gosuInputFiles, JavacProcessingEnvironment jpe, JavaFileManager fileManager )
  {
    if( gosuInputFiles.isEmpty() )
    {
      return true;
    }

    DiagnosticListener dc = jpe.getContext().get( DiagnosticListener.class );
    //noinspection unchecked
    JavacCompilerDriver driver = new JavacCompilerDriver( dc, jpe.getFiler(), false, true ); //## todo: get warnings option
    return compileGosuSources( makeOptions( jpe, gosuInputFiles ), driver, gosuInputFiles );
  }

  private CommandLineOptions makeOptions( JavacProcessingEnvironment jpe, List<String> gosuInputFiles )
  {
    //## todo: transfer options from javac command line
    CommandLineOptions clo = new CommandLineOptions();
    clo.setSourceFiles( gosuInputFiles );
    return clo;
  }

  @Override
  public boolean compile( CommandLineOptions options, ICompilerDriver driver )
  {
    List<String> gosuFiles = new ArrayList<>();
    List<String> javaFiles = new ArrayList<>();
    for( String fileName : getSourceFiles( options ) )
    {
      if( fileName.toLowerCase().endsWith( ".java" ) )
      {
        javaFiles.add( fileName );
      }
      else
      {
        gosuFiles.add( fileName );
      }
    }

    if( !gosuFiles.isEmpty() )
    {
      if( compileGosuSources( options, driver, gosuFiles ) )
      {
        return true;
      }
    }

    if( !javaFiles.isEmpty() )
    {
      if( compileJavaSources( options, driver, javaFiles ) )
      {
        return true;
      }
    }
    return false;
  }

  private List<String> getSourceFiles( CommandLineOptions options )
  {
    List<String> sourceFiles = options.getSourceFiles();
    if( !sourceFiles.isEmpty() )
    {
      return sourceFiles;
    }

    String sourcepath = options.getSourcepath();
    if( sourcepath.isEmpty() )
    {
      return Collections.emptyList();
    }

    sourceFiles = new ArrayList<>();
    for( StringTokenizer tok = new StringTokenizer( File.pathSeparator ); tok.hasMoreTokens(); )
    {
      String path = tok.nextToken();
      Path sourcePath = PathUtil.create( path );
      addToSources( sourcePath, sourceFiles );
    }

    return sourceFiles;
  }

  private void addToSources( Path sourcePath, List<String> sourceFiles )
  {
    if( !PathUtil.exists( sourcePath ) )
    {
      return;
    }

    if( Files.isDirectory( sourcePath ) )
    {
      for( Path child : PathUtil.listFiles( sourcePath ) )
      {
        addToSources( child, sourceFiles );
      }
    }
    else
    {
      String absolutePathName = PathUtil.getAbsolutePathName( sourcePath );
      if( isSourceFile( absolutePathName ) )
      {
        sourceFiles.add( absolutePathName );
      }
    }
  }

  private boolean isSourceFile( String absolutePathName )
  {
    return Arrays.stream( SOURCE_EXTS ).anyMatch( e -> absolutePathName.toLowerCase().endsWith( e ) );
  }

  private boolean compileGosuSources( CommandLineOptions options, ICompilerDriver driver, List<String> gosuFiles )
  {
    boolean thresholdExceeded = false;
    for( String fileName : gosuFiles )
    {
      File file = new File( fileName );

      if( options.isVerbose() )
      {
        System.out.println( "gosuc: about to compile file: " + file );
      }

      compile( file, driver );

      if( driver.getErrors().size() > options.getMaxErrs() )
      {
        System.out.printf( "\nError threshold of %d exceeded; aborting compilation.", options.getMaxErrs() );
        thresholdExceeded = true;
        break;
      }
      if( !options.isNoWarn() && driver.getWarnings().size() > options.getMaxWarns() )
      {
        System.out.printf( "\nWarning threshold of %d exceeded; aborting compilation.", options.getMaxWarns() );
        thresholdExceeded = true;
        break;
      }
    }
    return thresholdExceeded;
  }

  private boolean compileJavaSources( CommandLineOptions options, ICompilerDriver driver, List<String> javaFiles )
  {
    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class ).get( 0 );
    DiagnosticCollector<JavaFileObject> errorHandler = new DiagnosticCollector<>();
    List<JavaFileObject> sourceFiles = javaFiles.stream().map( SourceJavaFileObject::new ).collect( Collectors.toList() );
    Collection<InMemoryClassJavaFileObject> files = javaParser.compile( sourceFiles, makeJavacOptions( options ), errorHandler );
    errorHandler.getDiagnostics().forEach( driver::sendCompileIssue );
    createJavaOutputFiles( files, driver );
    if( driver.getErrors().size() > options.getMaxErrs() )
    {
      System.out.printf( "\nError threshold of %d exceeded; aborting compilation.", options.getMaxErrs() );
      return true;
    }
    if( !options.isNoWarn() && driver.getWarnings().size() > options.getMaxWarns() )
    {
      System.out.printf( "\nWarning threshold of %d exceeded; aborting compilation.", options.getMaxWarns() );
      return true;
    }
    return false;
  }

  private List<String> makeJavacOptions( CommandLineOptions options )
  {
    ArrayList<String> javacOpts = new ArrayList<>();
    javacOpts.add( "-g" );
    javacOpts.add( "-Xlint:unchecked" );
    javacOpts.add( "-proc:none" );
    javacOpts.add( "-parameters" );
    if( options.isVerbose() )
    {
      javacOpts.add( "-verbose" );
    }
    if( options.isNoWarn() )
    {
      javacOpts.add( "-nowarn" );
    }
    return javacOpts;
  }

  @Override
  public boolean compile( File sourceFile, ICompilerDriver driver )
  {
    JavaFileObject compilingFile = new SourceJavaFileObject( sourceFile.toURI() );

    IType type = getType( sourceFile );
    if( type == null )
    {
      driver.sendCompileIssue( compilingFile, ERROR, 0, 0, 0, "Cannot find type in the Gosu Type System: " + sourceFile.getAbsolutePath() );
      return false;
    }

    if( isCompilable( type ) )
    {
      try
      {
        if( type.isValid() )
        {
          createGosuOutputFiles( (IGosuClass)type, driver );
        }
      }
      catch( CompilerDriverException ex )
      {
        driver.sendCompileIssue( compilingFile, ERROR, 0, 0, 0, ex.getMessage() );
        return false;
      }
      // output warnings and errors - whether the type was valid or not
      IParsedElement classElement = ((IGosuClass)type).getClassStatement();
      IClassFileStatement classFileStatement = ((IClassStatement)classElement).getClassFileStatement();
      classElement = classFileStatement == null ? classElement : classFileStatement;
      ExecutionMode mode = CommonServices.getPlatformHelper().getExecutionMode();
      for( IParseIssue issue : classElement.getParseIssues() )
      {
        int category = issue instanceof ParseWarning ? WARNING : ERROR;
        String message = mode == ExecutionMode.IDE ? issue.getUIMessage() : issue.getConsoleMessage();
        driver.sendCompileIssue( compilingFile, category, issue.getTokenStart(), issue.getLine(), issue.getColumn(), message );
      }
    }

    return false;
  }

  private IType getType( File file )
  {
    IFile ifile = FileFactory.instance().getIFile( file );
    IModule module = TypeSystem.getGlobalModule();
    String[] typesForFile = TypeSystem.getTypesForFile( module, ifile );
    if( typesForFile.length != 0 )
    {
      return TypeSystem.getByFullNameIfValid( typesForFile[0], module );
    }
    return null;
  }

  private boolean isCompilable( IType type )
  {
    if( !(type instanceof IGosuClass) )
    {
      return false;
    }

    IType doNotVerifyAnnotation = TypeSystem.getByFullNameIfValid( "gw.testharness.DoNotVerifyResource" );
    return !type.getTypeInfo().hasAnnotation( doNotVerifyAnnotation ) &&
           !type.getTypeInfo().hasAnnotation( JavaTypes.DO_NOT_COMPILE() );
  }

  private void createGosuOutputFiles( IGosuClass gsClass, ICompilerDriver driver )
  {
    IDirectory moduleOutputDirectory = TypeSystem.getGlobalModule().getOutputPath();
    if( moduleOutputDirectory == null )
    {
      throw new RuntimeException( "Can't make class file, no output path defined." );
    }

    JavaFileObject classFile = driver.createClassFile( gsClass.getName() );
    populateGosuClassFile( classFile, gsClass, driver );
    maybeCopySourceFile( gsClass, driver );
  }

  private void createJavaOutputFiles( Collection<InMemoryClassJavaFileObject> compiledJavaFiles, ICompilerDriver driver )
  {
    IDirectory moduleOutputDirectory = TypeSystem.getGlobalModule().getOutputPath();
    if( moduleOutputDirectory == null )
    {
      throw new RuntimeException( "Can't make class file, no output path defined." );
    }

    compiledJavaFiles = compiledJavaFiles.stream().filter( e -> TypeSystem.getByFullNameIfValid( e.getClassName().replace( '$', '.' ) ) instanceof IJavaType ).collect( Collectors.toList() );

    for( InMemoryClassJavaFileObject compiledJavaFile: compiledJavaFiles )
    {
      JavaFileObject classFile = driver.createClassFile( compiledJavaFile.getClassName() );
      populateJavaClassFile( classFile, compiledJavaFile.getBytes(), driver );
    }
  }

  public static String getStackTrace( Throwable e )
  {
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace( new PrintWriter( stringWriter ) );
    return stringWriter.toString();
  }

  private String combine( @SuppressWarnings("SameParameterValue") String message1, String message2 )
  {
    if( message1 == null )
    {
      message1 = "";
    }
    else
    {
      message1 = message1 + "\n";
    }
    return message1 + message2;
  }

  private void maybeCopySourceFile( IGosuClass gsClass, ICompilerDriver driver )
  {
    ISourceFileHandle sfh = gsClass.getSourceFileHandle();
    IFile srcFile = sfh.getFile();
    if( srcFile != null )
    {
      File file = new File( srcFile.getPath().getFileSystemPathString() );
      if( file.isFile() )
      {
        FileObject resourceFile = driver.createResourceFile( gsClass.getNamespace(), file.getName() );
        try( InputStream in = new FileInputStream( file );
             OutputStream out = resourceFile.openOutputStream() )
        {
          StreamUtil.copy( in, out );
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }
    }
  }

  private void populateGosuClassFile( JavaFileObject javaFile, IGosuClass gosuClass, ICompilerDriver driver )
  {
    final byte[] bytes = TypeSystem.getGosuClassLoader().getBytes( gosuClass );
    try( OutputStream out = javaFile.openOutputStream() )
    {
      out.write( bytes );
    }
    catch( IOException e )
    {
      driver.sendCompileIssue( javaFile, ERROR, 0, 0, 0, combine( "Cannot create .class files.", getStackTrace( e ) ) );
      return;
    }

    for( IGosuClass innerClass : gosuClass.getInnerClasses() )
    {
      File file = new File( javaFile.toUri() );
      String enclosingName = file.getName().substring( 0, file.getName().lastIndexOf( '.' ) );
      String innerName = innerClass.getRelativeName();
      JavaFileObject innerFile = driver.createClassFile( getPackage( gosuClass ) + '.' + enclosingName + '$' + innerName );
      populateGosuClassFile( innerFile, innerClass, driver );
    }
  }

  private String getPackage( ICompilableType gosuClass )
  {
    ICompilableType enclosingType = gosuClass.getEnclosingType();
    if( enclosingType == null )
    {
      return gosuClass.getNamespace();
    }
    return getPackage( enclosingType );
  }

  private void populateJavaClassFile( JavaFileObject javaFile, byte[] bytes, ICompilerDriver driver )
  {
    try( OutputStream out = javaFile.openOutputStream() )
    {
      out.write( bytes );
    }
    catch( IOException e )
    {
      driver.sendCompileIssue( javaFile, ERROR, 0, 0, 0, combine( "Cannot create .class files.", getStackTrace( e ) ) );
    }
  }

  @Override
  public long initializeGosu( List<String> sourceFolders, List<String> classpath, List<String> backingSourcePath, String outputPath )
  {
    final long start = System.currentTimeMillis();

    CommonServices.getKernel().redefineService_Privileged( IFileSystem.class, createFileSystemInstance() );
    CommonServices.getKernel().redefineService_Privileged( IMemoryMonitor.class, new CompilerMemoryMonitor() );
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

    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment();
    _gosuInitialization = GosuInitialization.instance( execEnv );
    if( _gosuInitialization.isInitialized() )
    {
      _gosuInitialization.uninitializeCompiler();
    }

    GosucModule gosucModule = new GosucModule( IExecutionEnvironment.DEFAULT_SINGLE_MODULE_NAME,
                                               sourceFolders,
                                               classpath,
                                               backingSourcePath,
                                               outputPath,
                                               Collections.emptyList(),
                                               Collections.emptyList() );
    _gosuInitialization.initializeCompiler( gosucModule );

    return System.currentTimeMillis() - start;
  }

  private static IFileSystem createFileSystemInstance()
  {
    try
    {
      Class<?> cls = Class.forName( "manifold.api.fs.def.FileSystemImpl" );
      Constructor m = cls.getConstructor( IFileSystem.CachingMode.class );
      return (IFileSystem)m.newInstance( IFileSystem.CachingMode.FULL_CACHING );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public void uninitializeGosu()
  {
    TypeSystem.shutdown( TypeSystem.getExecutionEnvironment() );
    if( _gosuInitialization != null )
    {
      if( _gosuInitialization.isInitialized() )
      {
        _gosuInitialization.uninitializeCompiler();
      }
      _gosuInitialization = null;
    }
  }

  @Override
  public boolean isPathIgnored( String sourceFile )
  {
    return CommonServices.getPlatformHelper().isPathIgnored( sourceFile );
  }
}
