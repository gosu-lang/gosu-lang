package gw.internal.gosu.parser.java.compiler;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import gw.fs.IResource;
import gw.lang.javac.ClassJavaFileObject;
import gw.lang.javac.IJavaParser;
import gw.lang.javac.StringJavaFileObject;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.util.Pair;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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
        IModule globalModule = TypeSystem.getGlobalModule();
        if( globalModule != null )
        {
          _fileManager.setLocation( StandardLocation.SOURCE_PATH, globalModule.getSourcePath().stream().map( IResource::toJavaFile ).collect( Collectors.toList() ) );
          _fileManager.setLocation( StandardLocation.CLASS_PATH, globalModule.getJavaClassPath().stream().map( IResource::toJavaFile ).collect( Collectors.toList() ) );
        }
        _gfm = new GosuJavaFileManager( _fileManager );
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  public boolean parseType( String fqn, List<CompilationUnitTree> trees, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    try
    {
      init();

      Pair<JavaFileObject, String> pair = findJavaSource( fqn );
      StringWriter errors = new StringWriter();
      JavaCompiler.CompilationTask task = _javac.getTask( errors, _fileManager, errorHandler, Arrays.asList( "-proc:none" ), null, Arrays.asList( pair.getFirst() ) );
      JavacTaskImpl javacTask = (JavacTaskImpl)task;
      Iterable<? extends CompilationUnitTree> iterable = javacTask.parse();
      for( CompilationUnitTree x : iterable )
      {
        trees.add( x );
      }
    }
    catch( Exception e )
    {
      return false;
    }
    return true;
  }

  public boolean parseText( String src, List<CompilationUnitTree> trees, Consumer<SourcePositions> sourcePositions, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    try
    {
      init();

      ArrayList<JavaFileObject> javaStringObjects = new ArrayList<>();
      javaStringObjects.add( new StringJavaFileObject( "sample", src ) );
      StringWriter errors = new StringWriter();
      JavaCompiler.CompilationTask task = _javac.getTask( errors, _fileManager, errorHandler, Arrays.asList( "-proc:none" ), null, javaStringObjects );
      JavacTaskImpl javacTask = (JavacTaskImpl)task;
      Iterable<? extends CompilationUnitTree> iterable = javacTask.parse();
      for( CompilationUnitTree x : iterable )
      {
        trees.add( x );
      }
      if( sourcePositions != null )
      {
        sourcePositions.accept( Trees.instance( javacTask ).getSourcePositions() );
      }
    }
    catch( Exception e )
    {
      return false;
    }
    return true;
  }

  /**
   * Compiles specified Java class name.  Maintains cache between calls to this method, therefore subsequent calls to this
   * method will consult the cache and return the previously compiled class if cached.
   *
   * @param fqn
   * @param options
   * @param errorHandler
   * @return
   */
  public ClassJavaFileObject compile( String fqn, Iterable<String> options, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    init();

    ClassJavaFileObject compiledClass = _gfm.findCompiledFile( fqn );
    if( compiledClass != null )
    {
      return compiledClass;
    }

    Pair<JavaFileObject, String> fileObj = findJavaSource( fqn );
    if( fileObj == null )
    {
      return null;
    }

    StringWriter errors = new StringWriter();
    JavaCompiler.CompilationTask compilationTask = _javac.getTask( errors, _gfm, errorHandler, options, null, Arrays.asList( fileObj.getFirst() ) );
    compilationTask.call();
    return _gfm.findCompiledFile( fileObj.getSecond() );
  }

  /**
   * Compiles fresh, no caching.  Intended for use with parser feedback tooling e.g., a Java editor.
   *
   * @param jfo
   * @param fqn
   * @param options
   * @param errorHandler
   * @return
   */
  public ClassJavaFileObject compile( JavaFileObject jfo, String fqn, Iterable<String> options, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    init();

    StringWriter errors = new StringWriter();
    JavaCompiler.CompilationTask compilationTask = _javac.getTask( errors, _gfm, errorHandler, options, null, Arrays.asList( jfo ) );
    compilationTask.call();
    return _gfm.findCompiledFile( fqn );
  }

  public Pair<JavaFileObject, String> findJavaSource( String fqn )
  {
    init();

    try
    {
      JavaFileObject fileObj = _gfm.getJavaFileForInput( StandardLocation.SOURCE_PATH, fqn, JavaFileObject.Kind.SOURCE );
      Pair<JavaFileObject, String> pair = new Pair<>( fileObj, fqn );
      if( pair.getFirst() == null )
      {
        int iDot = fqn.lastIndexOf( '.' );
        if( iDot > 0 )
        {
          String enclosingFqn = fqn.substring( 0, iDot );
          pair = findJavaSource( enclosingFqn );
        }
      }
      return pair;
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public void clear()
  {
    _javac = null;
    try
    {
      _fileManager.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }
}
