package gw.lang.gosuc.simple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;

public class SoutCompilerDriver implements ICompilerDriver {
  private final boolean _echo;
  private final boolean _includeWarnings;
  private List<String> errors = new ArrayList<>();
  private List<String> warnings = new ArrayList<>();

  public SoutCompilerDriver() {
    this( false, true );
  }

  public SoutCompilerDriver( boolean echo, boolean warnings ) {
    _echo = echo;
    _includeWarnings = warnings;
  }

  @Override
  public void sendCompileIssue( File file, int category, long offset, long line, long column, String message )
  {
    sendCompileIssue( file, category, offset, line, column, message );
  }

  @Override
  public void sendCompileIssue(Object file, int category, long offset, long line, long column, String message) {
    if (category == WARNING) {
      String warning = String.format( "%s:[%s,%s] warning: %s", file.toString(), line, column, message );
      warnings.add( warning );
      if( _echo && _includeWarnings ) {
        System.out.println( warning );
      }
    } else if (category == ERROR) {
      String error = String.format( "%s:[%s,%s] error: %s", file.toString(), line, column, message );
      errors.add( error );
      if( _echo ) {
        System.out.println( error );
      }
    }
  }

  @Override
  public void sendCompileIssue( Diagnostic d )
  {
    sendCompileIssue( d.getSource(),
      d.getKind() == Diagnostic.Kind.ERROR ? ICompilerDriver.ERROR : ICompilerDriver.WARNING,
      d.getStartPosition(),
      d.getLineNumber(),
      d.getColumnNumber(),
      d.getMessage( Locale.getDefault() ) );
  }

  @Override
  public void registerOutput(Object sourceFile, File outputFile) {
    // nothing to do
  }

  public boolean isIncludeWarnings()
  {
    return _includeWarnings;
  }

  public boolean hasErrors() {
    return errors.size() > 0;
  }

  public List<String> getErrors() {
    return errors;
  }

  public List<String> getWarnings() {
    return warnings;
  }
}
