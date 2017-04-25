package gw.internal.gosu.parser.java.compiler;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import gw.lang.gosuc.GosucUtil;
import gw.lang.gosuc.simple.GosuCompiler;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.init.GosuInitialization;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;


import static javax.lang.model.SourceVersion.RELEASE_8;

/**
 */
@SupportedSourceVersion(RELEASE_8)
@SupportedAnnotationTypes({})
public class JavacJacker extends AbstractProcessor
{
  private static JavacJacker INSTANCE = null;

  public static final String GOSU_SOURCE_FILES = "gosu.source.files";

  private JavacProcessingEnvironment _jpe;
  private Context _ctx;
  private GosuJavaFileManager _gosuFileManager;
  private JavaFileManager _fileManager;
  private Set<JavaFileObject> _javaInputFiles;
  private List<String> _gosuInputFiles;

  static JavacJacker instancne()
  {
    return INSTANCE;
  }

  public JavacJacker()
  {
    INSTANCE = this;
  }

  @Override
  public synchronized void init( ProcessingEnvironment processingEnv )
  {
    super.init( processingEnv );
    _jpe = (JavacProcessingEnvironment)processingEnv;
    _ctx = _jpe.getContext();
    _fileManager = _ctx.get( JavaFileManager.class );
    _javaInputFiles = fetchJavaInputFiles();
    _gosuInputFiles = fetchGosuInputFiles();
    //JavacTask.instance( _jpe );

    hijackJavacFileManager();
  }

  GosuJavaFileManager getGosuFileManager()
  {
    return _gosuFileManager;
  }

  JavaFileManager getJavaFileManager()
  {
    return _fileManager;
  }

  private void hijackJavacFileManager()
  {
    if( !(_fileManager instanceof GosuJavaFileManager) )
    {
      injectGosuFileManager();
      if( !GosuInitialization.isAnythingInitialized() )
      {
        IGosuCompiler gosuc = initializeGosu();
        compileGosuSourceFiles( gosuc );
      }
    }
  }

  private void injectGosuFileManager()
  {
    _gosuFileManager = new GosuJavaFileManager( _fileManager, true );
    _ctx.put( JavaFileManager.class, (JavaFileManager)null );
    _ctx.put( JavaFileManager.class, _gosuFileManager );
  }

  private IGosuCompiler initializeGosu()
  {
    Set<String> sourcepath = deriveSourcePath();
    List<String> classpath = deriveClasspath();
    String outputPath = deriveOutputPath();
//    RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
//    List<String> arguments = runtimeMxBean.getInputArguments();

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

  private void compileGosuSourceFiles( IGosuCompiler gosuc )
  {
    if( !_gosuInputFiles.isEmpty() )
    {
      gosuc.compile( _gosuInputFiles, _jpe, _fileManager );
    }
  }

  private String deriveOutputPath()
  {
    try
    {
      String ping = "__dummy__";
      //JavaFileObject classFile = _jpe.getFiler().createClassFile( ping );
      JavaFileObject classFile = _fileManager.getJavaFileForOutput( StandardLocation.CLASS_OUTPUT, ping, JavaFileObject.Kind.CLASS, null );
      File dummyFile = new File( classFile.toUri() );
      String path = dummyFile.getAbsolutePath();
      path = path.substring( 0, path.length() - (File.separatorChar + ping + ".class").length() );
      return path;
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  private List<String> deriveClasspath()
  {
    URL[] classpathUrls = ((URLClassLoader)_jpe.getProcessorClassLoader()).getURLs();
    return Arrays.stream( classpathUrls )
      .map( url ->
           {
             try
             {
               return new File( url.toURI() ).getAbsolutePath();
             }
             catch( URISyntaxException e )
             {
               throw new RuntimeException( e );
             }
           } ).collect( Collectors.toList() );
  }

  private Set<String> deriveSourcePath()
  {
    Set<String> sourcePath = new HashSet<>();
    deriveSourcePath( _javaInputFiles, sourcePath );
    return sourcePath;
  }

  private void deriveSourcePath( Set<JavaFileObject> inputFiles, Set<String> sourcePath )
  {
    outer:
    for( JavaFileObject inputFile: inputFiles )
    {
      for( String sp: sourcePath )
      {
        if( inputFile.getName().startsWith( sp + File.separatorChar ) )
        {
          continue outer;
        }
      }
      TypeElement type = findType( inputFile );
      if( type != null )
      {
        String path = derivePath( type, inputFile );
        sourcePath.add( path );
      }
      else
      {
        System.err.println( "Could not find type for file: " + inputFile );
      }
    }
  }

  private String derivePath( TypeElement type, JavaFileObject inputFile )
  {
    String filename = inputFile.getName();
    int iDot = filename.lastIndexOf( '.' );
    String ext = iDot > 0 ? filename.substring( iDot ) : "";
    String pathRelativeFile = type.getQualifiedName().toString().replace( '.', File.separatorChar ) + ext;
    assert filename.endsWith( pathRelativeFile );
    return filename.substring( 0, filename.indexOf( pathRelativeFile )-1 );
  }

  private TypeElement findType( JavaFileObject inputFile )
  {
    String path = inputFile.getName();
    int dotJava = path.lastIndexOf( ".java" );
    if( dotJava < 0 )
    {
      return null;
    }
    path = path.substring( 0, dotJava );
    List<String> tokens = new ArrayList<>();
    for( StringTokenizer tokenizer = new StringTokenizer( path, File.separator, false ); tokenizer.hasMoreTokens(); )
    {
      tokens.add( tokenizer.nextToken() );
    }
    String typeName = "";
    TypeElement type = null;
    for( int i = tokens.size()-1; i >= 0; i-- )
    {
      typeName = tokens.get( i ) + typeName;
      TypeElement csr = getType( typeName );
      if( csr != null )
      {
        type = csr;
      }
      if( i > 0 )
      {
        typeName = '.' + typeName;
      }
    }
    return type;
  }

  private Set<JavaFileObject> fetchJavaInputFiles()
  {
    try
    {
      JavaCompiler javaCompiler = JavaCompiler.instance( _ctx );
      Field field = javaCompiler.getClass().getDeclaredField( "inputFiles" );
      field.setAccessible( true );
      //noinspection unchecked
      return (Set<JavaFileObject>)field.get( javaCompiler );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private List<String> fetchGosuInputFiles()
  {
    String property = System.getProperty( GOSU_SOURCE_FILES, "" );
    if( !property.isEmpty() )
    {
      return Arrays.asList( property.split( " " ) );
    }
    return Collections.emptyList();
  }

  private TypeElement getType( String className )
  {
    return processingEnv.getElementUtils().getTypeElement( className );
    // DeclaredType declaredType = jpe.getTypeUtils().getDeclaredType( typeElement );
    // return new ElementTypePair( typeElement, declaredType );
  }

  @Override
  public boolean process( Set<? extends TypeElement> annotations, RoundEnvironment roundEnv )
  {
    return false;
  }
}
