package editor.plugin.typeloader.java;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import editor.util.transform.java.JavaStringObject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;

/**
 */
public class JavaParser
{
  private static final JavaParser INSTANCE = new JavaParser();
  public static JavaParser instance()
  {
    return INSTANCE;
  }

  private JavacTool _javac;
  private JavacFileManager _fileManager;

  private JavaParser()
  {
  }

  public boolean parse( String src, List<CompilationUnitTree> trees, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    try
    {
      if( _javac == null )
      {
        _javac = JavacTool.create();
        _fileManager = _javac.getStandardFileManager( null, null, Charset.forName( "UTF-8" ) );
      }

      ArrayList<JavaStringObject> javaStringObjects = new ArrayList<>();
      javaStringObjects.add( new JavaStringObject( src ) );
      JavaCompiler.CompilationTask task = _javac.getTask( null, _fileManager, null, Arrays.asList( "-proc:none" ), null, javaStringObjects );
      JavacTaskImpl javacTask = (JavacTaskImpl)task;
      Iterable<? extends CompilationUnitTree> iterable = javacTask.parse();
      for( CompilationUnitTree x : iterable )
      {
        trees.add( x );
      }

//      JavaCompiler.CompilationTask compilerTask = _javac.getTask( null, _fileManager, errorHandler, null, null, javaStringObjects );
//      compilerTask.call();
//      for( Diagnostic<? extends JavaFileObject> error : errorHandler.getDiagnostics() )
//      {
//        System.out.format( "Error on line %d in %s%n",
//                           error.getLineNumber(),
//                           error.getSource().toUri() );
//      }

      _fileManager.close();
    }
    catch( Exception e )
    {
      return false;
    }
    return true;
  }
}
