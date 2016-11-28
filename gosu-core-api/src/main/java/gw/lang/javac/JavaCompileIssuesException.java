package gw.lang.javac;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 */
public class JavaCompileIssuesException extends RuntimeException
{
  private DiagnosticCollector<JavaFileObject> _errorHandler;

  public JavaCompileIssuesException( DiagnosticCollector<JavaFileObject> errorHandler )
  {
    _errorHandler = errorHandler;
  }

  public DiagnosticCollector<JavaFileObject> getErrorHandler()
  {
    return _errorHandler;
  }
}
