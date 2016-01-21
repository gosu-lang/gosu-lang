/**
 * HTMLUtil.java [Mar 5, 2003 2:32:14 PM]
 *
 * 2003 CaseCenter by Guidewire Software
 *
 */
package editor.util;

/**
 * Static utilities for writing out HTML
 */
public class HTMLEscapeUtil
{

  // --------------------------------------------------------- Static Variables

  /**
   * Constant representing non-breaking space escape character
   */
  public static final String NBSP = "&nbsp;";

  /**
   * Character used to separate parameters in a URL query string
   */
  public static final String URL_PARAMETER_SEPARATOR = "&";

  // ------------------------------------------------------------- Constructors

  private HTMLEscapeUtil()
  {
  }

  // ----------------------------------------------------- Generic HTML methods

  /**
   * Escape a string by replacing all occurrences of special characters (such
   * as &gt; and &lt;) by their corresponding escapes.
   *
   * @param string the string to be escaped
   *
   * @return the escaped string or, if the original string does not
   * contain any special characters, the original string.
   */
  public static String escape( String string )
  {
    return escape( string, true );
  }

  public static String escape( String string, boolean escapeNewLine )
  {
    return escape( string, escapeNewLine, false );
  }

  public static String escapeForTextArea( String string )
  {
    return escape( string, false, true );
  }

  private static String escape( String string, boolean escapeNewLine, boolean isTextArea )
  {
    if( string == null || string.length() == 0 )
    {
      return string;
    }
    StringBuilder resultBuffer = null;
    char last = 0;
    for( int i = 0, length = string.length(); i < length; i++ )
    {
      String entity = null;
      char ch = string.charAt( i );
      switch( ch )
      {
        case '<':
        {
          entity = "&lt;";
          break;
        }
        case ' ':
          if( last == ' ' && !isTextArea )
          {
            entity = "&nbsp;";
          }
          break;
        case '>':
          entity = "&gt;";
          break;
        case '&':
          entity = "&amp;";
          break;
        case '"':
          entity = "&quot;";
          break;
        case '\n':
          if( escapeNewLine )
          {
            entity = "<br>";
          }
          break;
        default:
          break;
      }
      if( entity != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuilder( string );
          resultBuffer.setLength( i );
        }
        resultBuffer.append( entity );
      }
      else if( resultBuffer != null )
      {
        resultBuffer.append( ch );
      }
      last = ch;
    }
    return (resultBuffer != null) ? resultBuffer.toString() : string;
  }

  /**
   * Escape a string for use as an HTML attribute by replacing all double
   * quotes and '&'.
   */
  public static String escapeAttribute( String string )
  {
    if( string == null || string.length() == 0 )
    {
      return string;
    }
    StringBuilder resultBuffer = null;
    for( int i = 0, length = string.length(); i < length; i++ )
    {
      String entity = null;
      char ch = string.charAt( i );
      switch( ch )
      {
        case '"':
          entity = "&quot;";
          break;
        case '&':
          entity = "&amp;";
          break;
        default:
          break;
      }
      if( entity != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuilder( string );
          resultBuffer.setLength( i );
        }
        resultBuffer.append( entity );
      }
      else if( resultBuffer != null )
      {
        resultBuffer.append( ch );
      }
    }
    return (resultBuffer != null) ? resultBuffer.toString() : string;
  }

}