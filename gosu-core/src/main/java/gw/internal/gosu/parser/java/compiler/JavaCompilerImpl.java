package gw.internal.gosu.parser.java.compiler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

/**
 */
public class JavaCompilerImpl implements JavaCompiler
{
  private JavaCompiler _delegate;

  public JavaCompilerImpl( JavaCompiler delegate )
  {
    _delegate = delegate;
  }

  @Override
  public CompilationTask getTask( Writer out, JavaFileManager fileManager, DiagnosticListener<? super JavaFileObject> diagnosticListener, Iterable<String> options, Iterable<String> classes, Iterable<? extends JavaFileObject> compilationUnits )
  {
    return _delegate.getTask( out, makeFileManager( fileManager ), diagnosticListener, options, classes, compilationUnits );
  }

  @Override
  public StandardJavaFileManager getStandardFileManager( DiagnosticListener<? super JavaFileObject> diagnosticListener, Locale locale, Charset charset )
  {
    return _delegate.getStandardFileManager( diagnosticListener, locale, charset );
  }

  @Override
  public int isSupportedOption( String option )
  {
    return _delegate.isSupportedOption( option );
  }

  @Override
  public int run( InputStream in, OutputStream out, OutputStream err, String... arguments )
  {
    return _delegate.run( in, out, err, arguments );
  }

  @Override
  public Set<SourceVersion> getSourceVersions()
  {
    return _delegate.getSourceVersions();
  }

  private GosuJavaFileManager makeFileManager( JavaFileManager fileMgr )
  {
    if( fileMgr instanceof GosuJavaFileManager )
    {
      return (GosuJavaFileManager)fileMgr;
    }

    JavaFileManager delegateFileMgr = fileMgr == null
                                      ? _delegate.getStandardFileManager( null, null, Charset.forName( "UTF-8" ) )
                                      : fileMgr;

    return new GosuJavaFileManager( delegateFileMgr );
  }
}
