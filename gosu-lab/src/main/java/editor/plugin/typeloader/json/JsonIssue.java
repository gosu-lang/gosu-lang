package editor.plugin.typeloader.json;

import manifold.internal.javac.IIssue;
import javax.script.ScriptException;

/**
 */
public class JsonIssue implements IIssue
{
  private ScriptException _issue;
  private int _line;
  private int _column;

  public JsonIssue( ScriptException issue )
  {
    _issue = issue;
  }

  @Override
  public Kind getKind()
  {
    return Kind.Error;
  }

  @Override
  public int getStartOffset()
  {
    return 0;
  }

  @Override
  public int getEndOffset()
  {
    return 0;
  }

  @Override
  public int getLine()
  {
    if( _line > 0 )
    {
      return _line;
    }
    
    int line = _issue.getLineNumber();
    if( line < 0 )
    {
      deriveFromMessage();
      line = _line;
    }
    return _line = line;
  }

  @Override
  public int getColumn()
  {
    if( _column > 0 )
    {
      return _column;
    }
    
    int column = _issue.getColumnNumber();
    if( column < 0 )
    {
      deriveFromMessage();
      column = _column;
    }
    return _column = column;
  }

  @Override
  public String getMessage()
  {
    return _issue.getMessage();
  }

  private void deriveFromMessage()
  {
    String message = _issue.getMessage();
    String prefix = "Found errors:\n[";
    if( message.startsWith( prefix ) )
    {
      message = message.substring( prefix.length() );
      StringBuilder line = new StringBuilder();
      StringBuilder column = new StringBuilder();
      StringBuilder csr = line;
      for( int i = 0; i < message.length(); i++ )
      {
        char c = message.charAt( i );
        if( c == ':' )
        {
          csr = column;
        }
        else if( c == ']' )
        {
          break;
        }
        else
        {
          csr.append( c );
        }
      }
      if( line.length() > 0 )
      {
        _line = Integer.parseInt( line.toString() );
      }
      if( column.length() > 0 )
      {
        _column = Integer.parseInt( column.toString() );
      }
    }
  }
}
