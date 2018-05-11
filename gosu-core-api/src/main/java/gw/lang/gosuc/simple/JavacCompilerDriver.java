package gw.lang.gosuc.simple;

import com.sun.tools.javac.util.Log;
import gw.util.GosuExceptionUtil;
import manifold.internal.javac.IDynamicJdk;

import javax.annotation.processing.Filer;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class JavacCompilerDriver extends SoutCompilerDriver
{
  private final Log _log;
  private final Filer _filer;

  public JavacCompilerDriver(Log log, Filer filer, boolean echo, boolean warnings )
  {
    super( echo, warnings );
    _log = log;
    _filer = filer; //TODO Objects.requireNonNull(filer, filer.getClass().toString() + " may not be null.");
  }

  @Override
  public void sendCompileIssue( File file, int category, long offset, long line, long column, String message )
  {
    sendCompileIssue( (Object)file, category, offset, line, column, message );
  }

  @Override
  public void sendCompileIssue( Object file, int category, long offset, long line, long column, String message )
  {
    GosuDiagnostic diagnostic = new GosuDiagnostic((JavaFileObject) file, category, offset, line, column, message);

    IDynamicJdk.instance().report(_log, diagnostic);
  }

  @Override
  public JavaFileObject createClassFile( String fqn )
  {
    try
    {
      return _filer.createClassFile( fqn );
    }
    catch( IOException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }
  @Override
  public FileObject createResourceFile( String pkg, String filename )
  {
    try
    {
      return _filer.createResource( StandardLocation.CLASS_OUTPUT, pkg, filename );
    }
    catch( IOException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  public static class GosuDiagnostic implements Diagnostic<JavaFileObject>
  {
    private final JavaFileObject _file;
    private final int _category;
    private final long _offset;
    private final long _line;
    private final long _column;
    private final String _message;

    public GosuDiagnostic( JavaFileObject file, int category, long offset, long line, long column, String message )
    {
      _file = file;
      _category = category;
      _offset = offset;
      _line = line;
      _column = column;
      _message = message;
    }

    @Override
    public Kind getKind()
    {
      switch( _category )
      {
        case ICompilerDriver.WARNING:
          return Kind.WARNING;
        case ICompilerDriver.ERROR:
          return Kind.ERROR;
        default:
          return Kind.OTHER;
      }
    }

    @Override
    public JavaFileObject getSource()
    {
      return _file;
    }

    @Override
    public long getPosition()
    {
      return _offset;
    }

    @Override
    public long getStartPosition()
    {
      return _offset;
    }

    @Override
    public long getEndPosition()
    {
      return _offset;
    }

    @Override
    public long getLineNumber()
    {
      return _line;
    }

    @Override
    public long getColumnNumber()
    {
      return _column;
    }

    @Override
    public String getCode()
    {
      return null;
    }

    @Override
    public String getMessage( Locale locale )
    {
      return _message;
    }
  }
}
