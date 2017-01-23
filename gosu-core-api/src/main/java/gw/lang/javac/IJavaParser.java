package gw.lang.javac;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.SourcePositions;
import gw.util.Pair;
import java.util.List;
import java.util.function.Consumer;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 */
public interface IJavaParser
{
  boolean parseText( String src, List<CompilationUnitTree> trees, Consumer<SourcePositions> sourcePositions, DiagnosticCollector<JavaFileObject> errorHandler );
  boolean parseType( String fqn, List<CompilationUnitTree> trees, DiagnosticCollector<JavaFileObject> errorHandler );

  ClassJavaFileObject compile( JavaFileObject jfo, String fqn, Iterable<String> options, DiagnosticCollector<JavaFileObject> errorHandler );
  ClassJavaFileObject compile( String fqn, Iterable<String> options, DiagnosticCollector<JavaFileObject> errorHandler );

  Pair<JavaFileObject, String> findJavaSource( String fqn );

  void clear();
}
