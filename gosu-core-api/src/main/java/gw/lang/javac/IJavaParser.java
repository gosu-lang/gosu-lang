package gw.lang.javac;

import com.sun.source.tree.CompilationUnitTree;
import java.util.List;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 */
public interface IJavaParser
{
  boolean parse( String fqn, String src, List<CompilationUnitTree> trees, DiagnosticCollector<JavaFileObject> errorHandler );

  List<ClassJavaFileObject> compile( JavaFileObject jfo, String fqn, Iterable<String> options, DiagnosticCollector<JavaFileObject> errorHandler );

  JavaFileObject findJavaSource( String fqn );
}
