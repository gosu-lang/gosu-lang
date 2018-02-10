package gw.lang.gosuc.simple;

import java.io.File;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;

/**
 */
public interface ICompilerDriver
{
  int ERROR = 0;
  int WARNING = 1;

  void sendCompileIssue( File file, int category, long offset, long line, long column, String message );

  default void sendCompileIssue( Object file, int category, long offset, long line, long column, String message )
  {
    sendCompileIssue( (File)file, category, offset, line, column, message );
  }

  default void sendCompileIssue( Diagnostic d )
  {
  }

  JavaFileObject createClassFile( String fqn );
  FileObject createResourceFile( String pkg, String filename );

  default boolean isIncludeWarnings()
  {
    throw new UnsupportedOperationException();
  }

  default boolean hasErrors()
  {
    throw new UnsupportedOperationException();
  }

  default List<String> getErrors()
  {
    throw new UnsupportedOperationException();
  }

  default List<String> getWarnings()
  {
    throw new UnsupportedOperationException();
  }
}
