package gw.internal.gosu.parser.java.compiler;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import gw.fs.IResource;
import gw.lang.javac.ClassJavaFileObject;
import gw.lang.javac.IJavaParser;
import gw.lang.javac.StringJavaFileObject;
import gw.lang.reflect.TypeSystem;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

/**
 */
public class JavaParser implements IJavaParser
{
  private static final JavaParser INSTANCE = new JavaParser();
  public static JavaParser instance()
  {
    return INSTANCE;
  }

  private JavaCompiler _javac;
  private StandardJavaFileManager _fileManager;
  private GosuJavaFileManager _gfm;

  private JavaParser()
  {
  }


  private void init()
  {
    if( _javac == null )
    {
      _javac = JavacTool.create();
      _fileManager = _javac.getStandardFileManager( null, null, Charset.forName( "UTF-8" ) );


      //JavaFileObject compilationUnit = new StringJavaFileObject( fqn, src );

      try
      {
        _fileManager.setLocation( StandardLocation.SOURCE_PATH, TypeSystem.getGlobalModule().getSourcePath().stream().map( IResource::toJavaFile ).collect( Collectors.toList() ) );
        _gfm = new GosuJavaFileManager( _fileManager );
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  public boolean parse( String fqn, String src, List<CompilationUnitTree> trees, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    try
    {
      init();

      ArrayList<StringJavaFileObject> javaStringObjects = new ArrayList<>();
      javaStringObjects.add( new StringJavaFileObject( fqn, src ) );
      JavaCompiler.CompilationTask task = _javac.getTask( null, _fileManager, errorHandler, Arrays.asList( "-proc:none" ), null, javaStringObjects );
      JavacTaskImpl javacTask = (JavacTaskImpl)task;
      Iterable<? extends CompilationUnitTree> iterable = javacTask.parse();
      for( CompilationUnitTree x : iterable )
      {
        trees.add( x );
      }

      _fileManager.close();
    }
    catch( Exception e )
    {
      return false;
    }
    return true;
  }

  public List<ClassJavaFileObject> compile( String fqn, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    init();

    try
    {
      JavaFileObject fileObj = _gfm.getJavaFileForInput( StandardLocation.SOURCE_PATH, fqn, JavaFileObject.Kind.SOURCE );
      JavaCompiler.CompilationTask compilationTask = _javac.getTask( null, _gfm, errorHandler, null, null, Arrays.asList( fileObj ) );
      compilationTask.call();

      _gfm.beginSession();
      compilationTask.call();
      return _gfm.endSession();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public List<ClassJavaFileObject> compile( JavaFileObject jfo, String fqn, Iterable<String> options, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    init();

    JavaCompiler.CompilationTask compilationTask = _javac.getTask( null, _gfm, errorHandler, options, null, Arrays.asList( jfo ) );
    _gfm.beginSession();
    compilationTask.call();
    return _gfm.endSession();
  }

  public JavaFileObject findJavaSource( String fqn )
  {
    init();

    try
    {
      JavaFileObject fileObj = _gfm.getJavaFileForInput( StandardLocation.SOURCE_PATH, fqn, JavaFileObject.Kind.SOURCE );
      if( fileObj == null )
      {
        int iDot = fqn.lastIndexOf( '.' );
        if( iDot > 0 )
        {
          String enclosingFqn = fqn.substring( 0, iDot );
          fileObj = findJavaSource( enclosingFqn );
        }
      }
      return fileObj;
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

}
