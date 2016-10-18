package editor.search;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StringUtil
{
  /**
   * Split up a string into tokens delimited by the specified separator
   * character.  If the string is null or zero length, returns null.
   *
   * @param s         The String to tokenize
   * @param separator The character delimiting tokens
   *
   * @return An array of String tokens, or null is s is null or 0 length.
   */
  public static String[] tokenize( String s, char separator )
  {
    List<String> tokens = tokenizeToList( s, separator );
    return tokens == null ? null : tokens.toArray( new String[tokens.size()] );
  }

  /**
   * Split up a string into tokens delimited by the specified separator
   * character.  If the string is null or zero length, returns null.
   *
   * @param s         The String to tokenize
   * @param separator The character delimiting tokens
   *
   * @return A List of String tokens, or null is s is null or 0 length.
   */
  public static List<String> tokenizeToList( String s, char separator )
  {
    if( s == null || s.length() == 0 )
    {
      return null;
    }
    int start = 0;
    int stop = 0;
    ArrayList<String> tokens = new ArrayList<String>();
    while( start <= s.length() )
    {
      stop = s.indexOf( separator, start );
      if( stop == -1 )
      {
        stop = s.length();
      }
      String token = s.substring( start, stop );
      tokens.add( token );
      start = stop + 1;
    }

    return tokens;
  }

  /**
   * Split up a string into tokens delimited by the specified separator
   * character as long as that character is not escaped with the escape character.
   * If the string is null or zero length, returns null.
   *
   * @param s         The string to tokenize.
   * @param separator Character in <code>s</code> denoting separation between tokens.
   * @param escape    Hint character where the string to tokenize ends.
   *
   * @return Array of tokens from the input string.
   */
  public static String[] tokenizeUnescaped( String s, char separator, char escape )
  {
    if( s == null || s.length() == 0 )
    {
      return null;
    }
    int start = 0;
    int stop = 0;
    int mid = 0;
    ArrayList tokens = new ArrayList();
    while( mid <= s.length() )
    {
      stop = s.indexOf( separator, mid );
      if( stop == -1 )
      {
        stop = s.length();
      }
      else if( isEscaped( s, stop, escape ) )
      { // if character is escaped
        // skip this one because it's escaped and move on to the next one
        mid = stop + 1;
        continue;
      }
      String token = s.substring( start, stop );
      tokens.add( token );
      start = stop + 1;
      mid = start;
    }
    return (String[])tokens.toArray( new String[tokens.size()] );
  }

  /**
   * Private helper method to determine if the char at pos is escaped or not
   */
  private static boolean isEscaped( String s, int pos, char escape )
  {
    // count the number of escape characters before position.
    // it's it is odd, then it's escaped!
    // Ex) In '\\\\\a' , the 'a' is escaped.
    int count = 0;
    while( pos > 0 )
    {
      if( s.charAt( pos - 1 ) == escape )
      {
        count++;
        pos--;
      }
      else
      {
        break;
      }
    }
    return (count % 2 == 1); // true if count is odd (mod 2 = 1)
  }

  /**
   * Removes "chars" from start of baseString if baseString starts with chars.  For example
   * calling removeStartChars("fooBar", "foo") results in "Bar".  Does nothing if
   * baseString does not start with chars.
   *
   * @param baseString Original string
   * @param chars      Characters to remove if found
   *
   * @return String Updated string
   */
  public static String removeStartChars( String baseString, String chars )
  {
    if( baseString.startsWith( chars ) )
    {
      return baseString.substring( chars.length() );
    }
    else
    {
      return baseString;
    }
  }

  /**
   * Removes "chars" from end of baseString if baseString ends with chars.  For example
   * calling removeEndChars("hello world", "world") results in "hello ".  Does nothing if
   * baseString does not end with chars.
   *
   * @deprecated use StringUtils.chomp() instead
   */
  public static String removeEndChars( String baseString, String chars )
  {
    if( baseString.endsWith( chars ) )
    {
      return baseString.substring( 0, baseString.length() - chars.length() );
    }
    else
    {
      return baseString;
    }
  }

  /**
   * Combination of removeStartChars and removeEndChars.
   */
  public static String removeStartAndEndChars( String baseString, String startChars, String endChars )
  {
    return removeEndChars( removeStartChars( baseString, startChars ), endChars );
  }

  /**
   * Interface used by {@link editor.search.StringUtil#substitute} for variable substitution
   */
  public interface VariableMap
  {
    public String getValue( String variableName );

  }

  public static final int ANT_STYLE = 0; // ${...}
  public static final int JAVADOC_LINK_STYLE = 0; // {@...}

  /**
   * Looks in the pattern string for any variables enclosed in ${}. Any such
   * variables are replaced by looking up their names in the variable map.
   * For example ${some.name} would be replaced by whatever
   * variableMap.getValue("some.name"} returns. Single quotes are stripped
   * out of the pattern, but substitution is disabled between the quotes.
   * To include a single quote in the pattern string use two single quotes
   * next to each other.
   *
   * @param pattern     the pattern string, must not be null
   * @param variableMap the variable map used to lookup variable values
   *
   * @return either the original string (if no ${} were found) or a new string
   * with the variable names replaced by values from the variable map.
   */
  public static String substitute( String pattern, VariableMap variableMap )
  {
    int quoteStart = -1;
    int variableStart = -1;
    StringBuffer resultBuffer = null;
    for( int i = 0, n = pattern.length(); i < n; i++ )
    {
      char ch = pattern.charAt( i );
      boolean withinQuotes = quoteStart >= 0;
      boolean withinVariable = variableStart >= 0;
      boolean isQuote = ch == '\'';
      boolean isVariableStart
        = !withinQuotes && ch == '{' && i > 0 && pattern.charAt( i - 1 ) == '$';
      if( (isQuote || isVariableStart) && resultBuffer == null )
      {
        resultBuffer = new StringBuffer( pattern );
        resultBuffer.setLength( i );
      }
      if( isVariableStart )
      {
        if( withinVariable )
        {
          throw new IllegalArgumentException( "Invalid pattern, ${ inside ${:" + pattern );
        }
        resultBuffer.setLength( resultBuffer.length() - 1 );
        variableStart = i + 1;
      }
      else if( isQuote )
      {
        if( withinVariable )
        {
          throw new IllegalArgumentException( "Invalid pattern, ' inside ${:" + pattern );
        }
        if( withinQuotes )
        {
          // Just ignore quotes, unless get '' which is equivalent to just '
          if( quoteStart == i - 1 )
          {
            resultBuffer.append( '\'' );
          }
          quoteStart = -1;
        }
        else
        {
          quoteStart = i;
        }
      }
      else if( withinVariable )
      {
        if( ch == '}' )
        {
          String variableName = pattern.substring( variableStart, i );
          String value = variableMap.getValue( variableName );
          if( value != null )
          {
            resultBuffer.append( value );
          }
          variableStart = -1;
        }
      }
      else
      {
        // Normal character
        if( resultBuffer != null )
        {
          resultBuffer.append( ch );
        }
      }
    }
    if( variableStart >= 0 )
    {
      throw new IllegalArgumentException( "Invalid pattern, non terminated ${ variable:" + pattern );
    }
    else if( quoteStart >= 0 )
    {
      throw new IllegalArgumentException( "Invalid pattern, missing closing quote:" + pattern );
    }
    return resultBuffer != null ? resultBuffer.toString() : pattern;
  }

  public static void escapeForJava( Writer out, String string ) throws IOException
  {
    if( string == null || string.length() == 0 )
    {
      out.write( string );
    }

    for( int i = 0, length = string.length(); i < length; i++ )
    {
      char ch = string.charAt( i );
      String escape = escapeForJava( ch );
      if( escape != null )
      {
        out.write( escape );
      }
      else
      {
        out.write( ch );
      }
    }
  }

  /**
   * Escape any special characters in the string, using the Java escape syntax.
   * For example any tabs become \t, newlines become \n etc.
   *
   * @return the escaped string. Returns the original string unchanged if it
   * contains no special characters.
   */
  public static String escapeForJava( String string )
  {
    return process( string, new Escaper()
    {
      public String eacape( char ch )
      {
        return escapeForJava( ch );
      }
    } );
  }

  public static String escapeForGosuStringLiteral( String string )
  {
    return process( string, new Escaper()
    {
      public String eacape( char ch )
      {
        return escapeForGosuStringLiteral( ch );
      }
    } );
  }

  private static String process( String string, Escaper escaper )
  {
    if( string == null || string.length() == 0 )
    {
      return string;
    }
    StringBuffer resultBuffer = null;
    for( int i = 0, length = string.length(); i < length; i++ )
    {
      char ch = string.charAt( i );
      String escape = escaper.eacape( ch );
      if( escape != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuffer( string );
          resultBuffer.setLength( i );
        }
        resultBuffer.append( escape );
      }
      else if( resultBuffer != null )
      {
        resultBuffer.append( ch );
      }
    }
    return (resultBuffer != null) ? resultBuffer.toString() : string;
  }

  /**
   * Like escapeForJava( String ), but escapes the stringbuffer in place.
   * Uses less memory.
   */
  public static void escapeForJava( StringBuffer sb )
  {
    if( sb == null || sb.length() == 0 )
    {
      return;
    }
    for( int i = 0; i < sb.length(); i++ )
    {
      String escape = escapeForJava( sb.charAt( i ) );
      if( escape != null )
      {
        sb.replace( i, i + 1, escape );
        i += escape.length() - 1;
      }
    }
  }

  /**
   * Replaces any backslashes in a String with two backslashes, in case a
   * replaceFirst() call is going to consume half of them.
   */
  public static String doubleBackslashes( String string )
  {
    if( string == null || string.length() == 0 )
    {
      return string;
    }
    StringBuffer resultBuffer = null;
    for( int i = 0, length = string.length(); i < length; i++ )
    {
      char ch = string.charAt( i );
      String escape = (ch == '\\') ? "\\\\" : null;
      if( escape != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuffer( string );
          resultBuffer.setLength( i );
        }
        resultBuffer.append( escape );
      }
      else if( resultBuffer != null )
      {
        resultBuffer.append( ch );
      }
    }
    return (resultBuffer != null) ? resultBuffer.toString() : string;
  }


  /**
   * Converts an escaped character code into a string literal expressing it, e.g. '\n' becomes "\\n".
   *
   * @param ch Escaped character code.
   *
   * @return The string expression of the character code, null if <code>ch</code> is not an escaped character.
   * Supports Unicode.
   */
  public static String escapeForJava( char ch )
  {
    String escape = escapeForGosuStringLiteral( ch );
    if( escape == null )
    {
      if( ch <= 31 || ch >= 127 )
      {
        escape = getUnicodeEscape( ch );
      }
    }
    return escape;
  }

  private static String escapeForGosuStringLiteral( char ch )
  {
    String escape = null;
    switch( ch )
    {
      case '\b':
        escape = "\\b";
        break;
      case '\t':
        escape = "\\t";
        break;
      case '\n':
        escape = "\\n";
        break;
      case '\f':
        escape = "\\f";
        break;
      case '\r':
        escape = "\\r";
        break;
      case '\"':
        escape = "\\\"";
        break;
      case '\'':
        escape = "\\'";
        break;
      case '\\':
        escape = "\\\\";
        break;
      default:
        break;
    }
    return escape;
  }

  /**
   * Escape a string by replacing all occurrences of special characters (such
   * as &gt; and &lt;) by their corresponding XML entities. Also converts
   * any illegal XML characters into sequences of characters: control
   * characters are converted to ^@, ^A etc. Large unicode characters that are
   * not valid XML characters are converted to U+xxxx where xxxx are hex digits.
   * This is more thorough than the Apache commons StringEscapeUtils.escapeXML
   * which only handles the four basic XML entities (gt, lt, quot and amp)
   *
   * @param string the string to be escaped
   *
   * @return the escaped string or, if the original string does not
   * contain any special characters, the original string.
   */
  public static String escapeForXML( String string )
  {
    if( string == null || string.length() == 0 )
    {
      return string;
    }
    StringBuffer resultBuffer = null;
    for( int i = 0, length = string.length(); i < length; i++ )
    {
      String entity = null;
      char ch = string.charAt( i );
      if( !isAllowedXMLCharacter( ch ) )
      {
        entity = replaceNonXMLCharacter( ch );
      }
      else if( ch > 127 )
      {
        entity = "&#" + (int)ch + ";";
      }
      else
      {
        switch( ch )
        {
          case '<':
            entity = "&lt;";
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
          default:
            break;
        }
      }
      if( entity != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuffer( string );
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

  /**
   * Check whether a given character is XML-processor-safe. This information
   * is taken from XML Spec at http://www.w3.org/TR/REC-xml/#NT-Char
   *
   * @param ch the character to be checked
   *
   * @return true if the character is XML-processor-safe, false otherwise.
   */
  public static boolean isAllowedXMLCharacter( char ch )
  {
    // Note: using the unicode form for \n, \r and \t seems to break the
    // compiler, even if you do it inside a comment!
    return ch == '\n' || ch == '\r' || ch == '\t'
           || ('\u0020' <= ch && ch <= '\uD7FF')
           || ('\uE000' <= ch && ch <= '\uFFFD');
  }

  /**
   */
  public static String replaceNonXMLCharacter( char ch )
  {
    if( ch < ' ' )
    {
      return "^" + (char)(ch + '@'); // Control character
    }
    else
    {
      return "U+" + Integer.toHexString( ch ).toUpperCase(); // Large Unicode character
    }
  }

  /**
   * Encode a list of strings as a single CSV encoded string
   *
   * @param fields a non null list of non null objects; their toString method
   *               will be called to convert them to strings (harmless if they are strings)
   *
   * @return the fields encoded as a CSV string
   *
   * @throws NullPointerException if fields or any member of fields is null
   */
  public static String encodeCSV( List fields )
  {
    return encodeCSV( fields.iterator() );
  }

  /**
   * Encode a list of strings as a single CSV encoded string
   *
   * @param i a non null iterator which should return a series of non null
   *          objects; their toString method will be called to convert them to strings
   *          (harmless if they are strings)
   *
   * @return the objects encoded as a CSV string
   *
   * @throws NullPointerException if i or any object returned by i is null
   */
  public static String encodeCSV( Iterator i )
  {
    StringBuilder resultBuffer = new StringBuilder();
    boolean first = true;
    while( i.hasNext() )
    {
      if( first )
      {
        first = false;
      }
      else
      {
        resultBuffer.append( ',' );
      }
      int fieldStart = resultBuffer.length();
      boolean isQuoted = false;
      String field = i.next().toString();
      for( int j = 0, n = field.length(); j < n; j++ )
      {
        char ch = field.charAt( j );
        if( (ch == ',' || ch == '"') && !isQuoted )
        {
          resultBuffer.insert( fieldStart, '"' );
          isQuoted = true;
        }
        resultBuffer.append( ch );
        if( ch == '"' )
        {
          resultBuffer.append( '"' );
        }
      }
      if( isQuoted )
      {
        resultBuffer.append( '"' );
      }
    }
    return resultBuffer.toString();
  }

  /**
   * Decodes a string encoded by {@link #encodeCSV(java.util.Iterator)} and
   * returns an array containing the individual string fields. An empty string
   * is ambiguous - could be a zero length array or an array containing a single
   * empty string. This implementation assumes it is an array containing a
   * single empty string
   *
   * @param csvFields the encoded fields
   *
   * @return an array of decoded fields
   *
   * @throws NullPointerException     if csvFields is null
   * @throws IllegalArgumentException if csvFields is badly encoded - for example if it contains strings that
   *                                  contain quotes or commas and they have not been encoded correctly
   */
  public static String[] decodeCSV( String csvFields )
  {
    List<String> resultList = new ArrayList<>();
    StringBuilder fieldBuffer = new StringBuilder();
    boolean inQuotedField = false;
    for( int i = 0, n = csvFields.length(); i < n; i++ )
    {
      char ch = csvFields.charAt( i );
      if( ch == '"' )
      {
        if( inQuotedField )
        {
          boolean atEnd = i == n - 1;
          char nextCh = atEnd ? 0 : csvFields.charAt( i + 1 );
          if( nextCh == '"' )
          {
            fieldBuffer.append( '"' );
            i++;
          }
          else if( atEnd || nextCh == ',' )
          {
            inQuotedField = false;
          }
          else
          {
            throw new IllegalArgumentException( "Misplaced double quote in CSV string: " + csvFields );
          }
        }
        else if( fieldBuffer.length() == 0 )
        {
          inQuotedField = true;
        }
        else
        {
          throw new IllegalArgumentException( "Misplaced double quote in CSV string: " + csvFields );
        }
      }
      else if( ch == ',' && !inQuotedField )
      {
        resultList.add( fieldBuffer.toString() );
        fieldBuffer.setLength( 0 );
      }
      else
      {
        fieldBuffer.append( ch );
      }
    }
    if( inQuotedField )
    {
      throw new IllegalArgumentException( "Missing quote at end of CSV string: " + csvFields );
    }
    resultList.add( fieldBuffer.toString() );
    return resultList.toArray( new String[resultList.size()] );
  }

  /**
   * Return a list of <code>SearchLocation</code>s representing the occurrences of the
   * specified pattern in the specified source string.
   *
   * @param strSource  A string to search.
   * @param strPattern A pattern to search for.
   *
   * @return A list of SearchLocations for each occurrence of the specified pattern.
   * Returns an empty list for zero occurrences.
   */
  public static List searchIgnoreCase( String strSource, String strPattern )
  {
    return search( strSource, strPattern, true, false );
  }

  public static List<SearchLocation> search( String strSource, String strPattern, boolean bIgnoreCase, boolean bWords )
  {
    return search( strSource, strPattern, bIgnoreCase, bWords, 0 );
  }

  public static List<SearchLocation> search( String strSource, String strPattern, boolean bIgnoreCase, boolean bWords, int iOffset )
  {
    return search( strSource, strPattern, bIgnoreCase, bWords, iOffset, false );
  }

  public static List<SearchLocation> search( String content, String pattern, boolean bIgnoreCase, boolean bWords, int iOffset, boolean backwards )
  {
    if( bIgnoreCase )
    {
      content = content.toLowerCase();
      pattern = pattern.toLowerCase();
    }

    List<SearchLocation> list = new ArrayList<>();

    int iIndex = backwards ? content.length() : 0;
    int iIndex2 = backwards ? content.lastIndexOf( pattern, iOffset - pattern.length() - 1 ) : content.indexOf( pattern, iOffset );
    if( iIndex2 < 0 )
    {
      return list;
    }

    SearchLocation slBuffer = new SearchLocation();

    while( true )
    {
      getLocation( content, iIndex, iIndex2, slBuffer );
      if( !bWords || isWholeWord( content, iIndex2, pattern ) )
      {
        SearchLocation sl = new SearchLocation();
        sl._iOffset = iIndex2;
        sl._iLine = slBuffer._iLine + 1;
        sl._iColumn = slBuffer._iColumn + 1;
        sl._iLineOffset = slBuffer._iLineOffset;
        sl._iLength = pattern.length();

        list.add( sl );
      }
      iIndex = iIndex2;
      iIndex2 = backwards ? content.lastIndexOf( pattern, iIndex -= 1 ) : content.indexOf( pattern, iIndex += pattern.length() );
      if( iIndex2 < 0 )
      {
        break;
      }
    }

    return list;
  }

  private static boolean isWholeWord( String content, int offset, String pattern )
  {
    if( offset > 0 && Character.isLetterOrDigit( content.charAt( offset-1 ) ) )
    {
      return false;
    }
    int followingCharOffset = offset + pattern.length();
    if( followingCharOffset < content.length() )
    {
      return !Character.isLetterOrDigit( content.charAt( followingCharOffset ) );
    }
    return true;
  }

  /**
   * Returns <code>true</code> if and only if <code>s</code> begins with
   * the string <code>prefix</code>, ignoring case. This method is not null
   * safe on the second argument.
   */
  public static boolean startsWithIgnoreCase( String s, String prefix )
  {
    if( s == null || s.length() < prefix.length() )
    {
      return false;
    }
    for( int i = 0; i < prefix.length(); i++ )
    {
      char c1Upper = Character.toUpperCase( s.charAt( i ) );
      char c2Upper = Character.toUpperCase( prefix.charAt( i ) );
      /* Unfortunately, conversion to uppercase does not work properly
       * for the Georgian alphabet, which has strange rules about case
       * conversion.  So we need to make one last check before
       * exiting. {@see java.lang.String#regionMatches}
       */
      if( c1Upper != c2Upper && Character.toLowerCase( c1Upper ) != Character.toLowerCase( c2Upper ) )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns <code>true</code> if and only if <code>s</code> ends with
   * the string <code>prefix</code>, ignoring case. This method is not null
   * safe on the second argument.
   */
  public static boolean endsWithIgnoreCase( String s, String suffix )
  {
    if( s == null || s.length() < suffix.length() )
    {
      return false;
    }
    for( int i = 0; i < suffix.length(); i++ )
    {
      char c1Upper = Character.toUpperCase( s.charAt( s.length() - 1 - i ) );
      char c2Upper = Character.toUpperCase( suffix.charAt( suffix.length() - 1 - i ) );
      /* Unfortunately, conversion to uppercase does not work properly
       * for the Georgian alphabet, which has strange rules about case
       * conversion.  So we need to make one last check before
       * exiting. {@see java.lang.String#regionMatches}
       */
      if( c1Upper != c2Upper && Character.toLowerCase( c1Upper ) != Character.toLowerCase( c2Upper ) )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Return true if string s contains the substring substr
   */
  public static boolean contains( String s, String substr )
  {
    return s.indexOf( substr ) >= 0;
  }

  /**
   * Return true if string s contains the substring substr (case-insensitive)
   */
  public static boolean containsCaseInsensitive( String s, String substr )
  {
    return !searchIgnoreCase( s, substr ).isEmpty();
  }

  /**
   */
  public static void getLocation( String strSource, int iFrom, int iTo, SearchLocation location )
  {
    if( strSource == null )
    {
      return;
    }

    if( iFrom > strSource.length() || iTo > strSource.length() )
    {
      throw new IllegalArgumentException( "String index out of bounds.  Source: " + ", From: " + iFrom + ", To:" + iTo );
    }

    int iLineCount = 0;
    int iLineStart = location._iLineOffset;
    for( int i = iFrom; i < iTo; i++ )
    {
      char c = strSource.charAt( i );
      if( c == '\n' )
      {
        iLineCount++;
        iLineStart = i + 1;
      }
    }

    location._iLine += iLineCount;
    location._iColumn = iTo - iLineStart;
    location._iLineOffset = iLineStart;
  }

  /**
   * Simple utility to format an List of objects as a delimited String.
   *
   * @param list      the list of items
   * @param delimiter the delimiter to use between items
   *
   * @return a delimited list of items, or the empty string if the list is empty
   */
  public static String formatObjectList( List list, String delimiter )
  {
    if( list.isEmpty() )
    {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < list.size(); i++ )
    {
      sb.append( list.get( i ) );
      if( i < list.size() - 1 )
      {
        sb.append( delimiter );
      }
    }

    return sb.toString();
  }

  /**
   */
  public static String stackTraceToString( Throwable t )
  {
    StringWriter sw = new StringWriter();
    t.printStackTrace( new PrintWriter( sw ) );
    return sw.toString();
  }

  /**
   * See TruncateFieldTest for examples of this method's usage
   *
   * @return The truncated field.
   */
  public static String truncateField( String fieldVal, String delimiter, int maxLength )
  {
    if( fieldVal != null )
    {
      int firstDelimiter = -1;
      if( delimiter != null )
      {
        firstDelimiter = fieldVal.indexOf( delimiter );
      }
      while( fieldVal.length() > maxLength )
      {
        if( firstDelimiter != -1 )
        {
          fieldVal = fieldVal.substring( firstDelimiter + delimiter.length() );
          firstDelimiter = fieldVal.indexOf( delimiter );
        }
        else
        {
          fieldVal = fieldVal.substring( fieldVal.length() - maxLength );
        }
      }
    }

    return fieldVal;
  }

  private static String getUnicodeEscape( char ch )
  {
    String prefix = "\\u";
    int length = prefix.length() + 4;
    String hex = Integer.toHexString( ch );
    StringBuilder resultBuffer = new StringBuilder( length );
    resultBuffer.append( prefix );
    for( int i = 0, n = length - (prefix.length() + hex.length()); i < n; i++ )
    {
      resultBuffer.append( '0' );
    }
    resultBuffer.append( hex );
    return resultBuffer.toString();
  }

  /**
   * Replaces all occurences of space with its escape-sequence "%20".
   *
   * @param strUrl - string to encode
   *
   * @return encoded string
   */
  public static String encodeSpaces( String strUrl )
  {
    StringBuffer sb = new StringBuffer();
    for( int i = 0; i < strUrl.length(); i++ )
    {
      char c = strUrl.charAt( i );
      if( c == ' ' )
      {
        escapeUrlChar( sb, c );
      }
      else
      {
        sb.append( c );
      }
    }
    return sb.toString();
  }

  /**
   * Replaces all occurrences of characters which need to be escaped in a URL.
   *
   * @param strUrl string to encode
   *
   * @return encoded string
   */
  public static String encodeURLCharacters( String strUrl )
  {
    StringBuffer sb = new StringBuffer();
    for( int i = 0; i < strUrl.length(); i++ )
    {
      char c = strUrl.charAt( i );
      escapeUrlChar( sb, c );
    }

    return sb.toString();
  }

  /**
   * Replaces all occurrences of characters which need to be escaped in a URL.
   *
   * @param strUrl string to encode
   *
   * @return encoded string
   *
   * @deprecated Please use {@link #encodeURLCharacters} instead
   */
  public static String encodeDocumentFilenameChars( String strUrl )
  {
    return encodeURLCharacters( strUrl );
  }

  private static void escapeUrlChar( StringBuffer sb, char c )
  {
    switch( c )
    {
      case ' ':
      case '%':
      case '#':
      case '`':
      case '~':
      case '!':
      case '@':
      case '$':
      case '^':
      case '&':
      case '(':
      case ')':
      case '{':
      case '}':
      case '+':
        sb.append( '%' ).append( Integer.toHexString( 0x100 | (int)c ).substring( 1 ).toUpperCase() );
        break;
      default:
        sb.append( c );
    }
  }

  /**
   * Compares two Strings, ignoring line separator differences. Acceptable line separators are<ul>
   * <li>&quot;\r\n&quot;</li>
   * <li>&quot;\n&quot;</li>
   * </ul>
   * A single &quot;\r&quot; is not considered to be a line separator.
   * <p/>
   * Examples:
   * <pre>
   * StringUtil.equalsIgnoreLineSeparatorDifferences(null, null) = true
   * StringUtil.equalsIgnoreLineSeparatorDifferences(null, *) = false
   * StringUtil.equalsIgnoreLineSeparatorDifferences(*, null) = false
   * StringUtil.equalsIgnoreLineSeparatorDifferences("abc", "abc") = true
   * StringUtil.equalsIgnoreLineSeparatorDifferences("abc", "def") = false
   * StringUtil.equalsIgnoreLineSeparatorDifferences("\r\n", "\n") = true
   * StringUtil.equalsIgnoreLineSeparatorDifferences("abc\r\ndef", "abc\ndef") = true
   * StringUtil.equalsIgnoreLineSeparatorDifferences("abc\rdef", "abc\ndef") = false
   * StringUtil.equalsIgnoreLineSeparatorDifferences("abc\r\ndef\nghi", "abc\ndef\r\nghi") = true
   * StringUtil.equalsIgnoreLineSeparatorDifferences("abc\r\r\n\ndef", "abc\r\n\r\ndef") = true
   * </pre>
   *
   * @return true if the strings are equivalent, otherwise this method returns false.
   */
  public static boolean equalsIgnoreLineSeparatorDifferences( String s1, String s2 )
  {
    if( s1 == null )
    {
      return s2 == null;
    }
    if( s2 == null )
    {
      return false;
    }
    int len1 = s1.length();
    int len2 = s2.length();
    int index1 = 0;
    int index2 = 0;
    for(; index1 < len1 && index2 < len2; index1++, index2++ )
    {
      char c1 = s1.charAt( index1 );
      char c2 = s2.charAt( index2 );
      if( c1 != c2 )
      {
        if( c1 == '\r' && index1 < (len1 - 1) && s1.charAt( index1 + 1 ) == '\n' )
        { // Have we encountered a "\r\n" sequence?
          index2--; // back up index2, so that we stay on the same char in s2 on the next iteration. In effect,
          // we've skipped the '\r' because the following char is '\n'.
        }
        else if( c2 == '\r' && index2 < (len2 - 1) && s2.charAt( index2 + 1 ) == '\n' )
        {
          index1--;
        }
        else
        {
          return false;
        }
      }
    }
    // We now know that one string is a prefix of the other. Make sure we've reached the end of each string to know
    // whether or not they're equal.
    return (index1 == len1 && index2 == len2);
  }

  /**
   * Takes a string 'glue' and array of strings, and returns a StringBuffer containing the strings joined with the glue
   * between each of them. The strings are joined in order   *
   *
   * @param glue    The glue string
   * @param strings The strings to join
   *
   * @return a StringBuffer
   */
  public static StringBuffer join( String glue, String[] strings )
  {
    return join( glue, strings, 0, strings.length - 1 );
  }

  /**
   * Takes a string 'glue' and array of strings, and returns a StringBuffer containing the strings
   * between the specified indices (inclusive) joined with the glue between each of them. The strings are joined in order
   *
   * @param glue    The glue string
   * @param strings The strings to join
   * @param first   The index of the first string to join
   * @param last    The index of the last string to join
   *
   * @return a StringBuffer
   */
  public static StringBuffer join( String glue, String[] strings, int first, int last )
  {
    StringBuffer buf = new StringBuffer();
    for( int i = first; i <= last; i++ )
    {
      String s = strings[i];
      if( i > first )
      {
        buf.append( glue );
      }
      buf.append( s );
    }
    return buf;
  }

  /**
   * Takes a string 'glue' and collection of CharSequences, and returns a StringBuffer containing the CharSequences
   * joined with the glue between each of them. They are joined in the order returned by the iterator of the colection
   *
   * @param glue          The glue string
   * @param charSequences The CharSequences to join
   *
   * @return a StringBuffer
   */
  public static <E> StringBuffer join( String glue, Collection<E> charSequences )
  {
    StringBuffer buf = new StringBuffer();
    int i = 0;
    for( Object charSequence : charSequences )
    {
      if( i > 0 )
      {
        buf.append( glue );
      }
      buf.append( charSequence );
      i++;
    }
    return buf;
  }

  /**
   * Prefixes the given string with the given fillChar so that the string is at least minLength long
   *
   * @param fillChar  The character to use as prefix
   * @param minLength The minimum length of the string
   * @param string    The original string
   *
   * @return A StringBuffer that is at least of length minLength
   */
  public static StringBuffer preFill( char fillChar, int minLength, String string )
  {
    if( string.length() >= minLength )
    {
      return new StringBuffer( string );
    }
    else
    {
      StringBuffer strbuf = new StringBuffer( minLength );
      for( int i = 0; i < minLength - string.length(); i++ )
      {
        strbuf.append( fillChar );
      }
      strbuf.append( string );
      return strbuf;
    }
  }

  /**
   * Suffixes the given string with the given fillChar so that the string is at least minLength long
   *
   * @param fillChar  The character to use as prefix
   * @param minLength The minimum length of the string
   * @param string    The original string
   *
   * @return A StringBuffer that is at least of length minLength
   */
  public static StringBuffer postFill( char fillChar, int minLength, String string )
  {
    if( string.length() >= minLength )
    {
      return new StringBuffer( string );
    }
    else
    {
      StringBuffer strbuf = new StringBuffer( minLength );
      strbuf.append( string );
      for( int i = 0; i < minLength - string.length(); i++ )
      {
        strbuf.append( fillChar );
      }
      return strbuf;
    }
  }

  /**
   * Returns an unmodifiable set containing the given strings
   */
  public static Set finalSetOfStrings( String[] members )
  {
    return Collections.unmodifiableSet( new HashSet( Arrays.asList( members ) ) );
  }

  /**
   * Returns the toString() of the object or null if the object is null.
   *
   * @return The object as a String or null if the object is null.
   */
  public static String asStringOrNull( Object object )
  {
    if( object == null )
    {
      return null;
    }
    else
    {
      return object.toString();
    }
  }

  /**
   * @param object the object
   *
   * @return Returns the toString() of the object or "null" string if the object is null.
   */
  public static String asStringOrNullString( Object object )
  {
    if( object == null )
    {
      return "null";
    }
    else
    {
      return object.toString();
    }
  }

  /**
   * Returns the toString() of the object or "" if the object is null.
   *
   * @return The object as a String or an empty string if the object is null.
   */
  public static String asStringOrEmpty( Object object )
  {
    if( object == null )
    {
      return "";
    }
    else
    {
      return object.toString();
    }
  }

  /**
   * Escape any special characters in the string, using the CSV escape syntax.
   * Here are the rules for CSV escape:
   * 1) If the string contains comma, then enclose the string with double quotes
   * 2) If the string contains double quotes, then firstly escape each double quote by
   * putting a double quote preceeding it. Secondly enclose the string with double quotes
   *
   * @return the escaped string. Returns the original string unchanged if it contains no sepcail char
   */
  public static String escapeForCSV( String str )
  {
    if( str == null || str.length() == 0 )
    {
      return str;
    }
    StringBuffer resultBuffer = null;
    for( int i = 0, length = str.length(); i < length; i++ )
    {
      char ch = str.charAt( i );
      String escape = escapeForCSV( ch );
      if( escape != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuffer( "\"" + str );
          resultBuffer.setLength( 1 + i );
        }
        resultBuffer.append( escape );
      }
      else if( resultBuffer != null )
      {
        resultBuffer.append( ch );
      }
    }
    if( resultBuffer != null )
    {
      resultBuffer.append( '"' );
    }
    return (resultBuffer != null) ? resultBuffer.toString() : str;
  }

  /**
   * Converts an escaped character code into a string literal expressing it, i.e. '"' becomes '""'
   *
   * @param ch Escaped character code
   *
   * @return The string expression of the character code, null if <code>ch</code> is not an escaped character
   */
  public static String escapeForCSV( char ch )
  {
    String escape = null;
    switch( ch )
    {
      case '"':
        escape = "\"\"";
        break;
      case ',':
        escape = ","; //We are returning the same char, but this will notify the caller that escape is needed!
        break;
    }
    return escape;
  }

  /**
   * Make a copy of String before interning to make sure the interned string doesn't have an
   * overly-large char buffer. Returns null for null arg.
   *
   * @return the interned String s
   */
  public static String intern( String s )
  {
    if( s == null )
    {
      return null;
    }
    else
    {
      return new String( s ).intern();
    }
  }

  private static final Map _internMap = new InternMap( 20000 );

  /**
   * Interns the given string, but unlike string.intern() the strings will be stored
   * on the heap rather than in perm space, allowing us to later clear out the map if necessary.
   *
   * @return If s is null, returns null. Otherwise, this method returns a String from a pool of strings.
   */
  public static String heapIntern( String s )
  {
    if( s == null )
    {
      return null;
    }

    if( _internMap.containsKey( s ) )
    {
      return (String)_internMap.get( s );
    }
    else
    {
      _internMap.put( s, s );
      return s;
    }
  }

  private static class InternMap extends ConcurrentHashMap
  {

    public InternMap( int initialCapacity )
    {
      super( initialCapacity );
    }
  }

  public static String stripNewLinesAndExtraneousWhiteSpace( String s )
  {
    if( s == null )
    {
      return null;
    }

    StringBuilder result = new StringBuilder();
    boolean hitNewLine = false;
    boolean addedSpace = false;
    for( int i = 0; i < s.length(); i++ )
    {
      char c = s.charAt( i );
      if( c == '\n' )
      {
        hitNewLine = true;
      }
      else if( c == ' ' )
      {
        if( hitNewLine )
        {
          if( !addedSpace )
          {
            result.append( c );
            addedSpace = true;
          }
        }
        else
        {
          result.append( c );
        }
      }
      else
      {
        hitNewLine = false;
        addedSpace = false;
        result.append( c );
      }
    }
    return result.toString().trim();
  }

  /**
   * Transforms a given name to a legal identifier suitable for Java or
   * Gosu. Strips all illegal characters like spaces, punctuation, etc.
   *
   * @param name A name that may not already be a legal identifier
   *
   * @return A legal identifier formed from all legal characters in name.
   */
  public static String makeIdentifier( CharSequence name )
  {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < name.length(); i++ )
    {
      char c = name.charAt( i );
      if( (sb.length() == 0 && (c == '_' || Character.isLetter( c ))) ||
          (sb.length() > 0 && (c == '_' || Character.isLetterOrDigit( c ))) )
      {
        sb.append( c );
      }
    }
    return sb.toString();
  }

  public static String capitalizeFirstChar( String name )
  {
    if( name == null || name.length() == 0 )
    {
      return name;
    }
    if( name.startsWith( "_" ) )
    {
      return capitalizeFirstChar( name.substring( 1 ) );
    }
    char chars[] = name.toCharArray();
    chars[0] = Character.toUpperCase( chars[0] );
    return new String( chars );
  }

  public static String toRGBString( Color color )
  {
    StringBuilder str = new StringBuilder();
    int red = color.getRed();
    str.append( red < 16 ? "0" : "" ).append( Integer.toHexString( red ) );
    int green = color.getGreen();
    str.append( green < 16 ? "0" : "" ).append( Integer.toHexString( green ) );
    int blue = color.getBlue();
    str.append( blue < 16 ? "0" : "" ).append( Integer.toHexString( blue ) );
    return str.toString();
  }

  public static String elide( String str, int maxLength )
  {
    if( str.length() <= maxLength )
    {
      return str;
    }
    return str.substring( 0, (maxLength - 3) / 2 ) + "..." + str.substring( str.length() - (maxLength - 3) / 2 );
  }

  /**
   * Returns the index of the specified character in the char sequence.
   *
   * @returns the index of the specified character in the char sequence.
   */
  public static int indexOf( CharSequence seq, char ch )
  {
    return indexOf( seq, ch, 0 );
  }

  /**
   * Returns the index of the specified character in the char sequence starting at the given start position.
   *
   * @returns the index of the specified character in the char sequence.
   */
  public static int indexOf( CharSequence seq, char ch, int startPos )
  {
    int max = seq.length();
    for( int i = startPos; i < max; i++ )
    {
      if( seq.charAt( i ) == ch )
      {
        return i;
      }
    }

    return -1;
  }


  /**
   * Returns the index of the specified character in the char sequence.
   *
   * @returns the index of the specified character in the char sequence.
   */
  public static int lastIndexOf( CharSequence seq, char ch )
  {
    return lastIndexOf( seq, ch, 0 );
  }

  /**
   * Returns the index of the specified character in the char sequence starting at the given start position.
   *
   * @returns the index of the specified character in the char sequence.
   */
  public static int lastIndexOf( CharSequence seq, char ch, int startPos )
  {
    for( int i = startPos - 1; i >= 0; i-- )
    {
      if( seq.charAt( i ) == ch )
      {
        return i;
      }
    }

    return -1;
  }

  private static interface Escaper
  {
    public String eacape( char ch );
  }

  public static String htmlEncode( String text )
  {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < text.length(); i++ )
    {
      char ch = text.charAt( i );
      switch( ch )
      {
        case '<':
          sb.append( "&lt;" );
          break;
        case '>':
          sb.append( "&gt;" );
          break;
        case '&':
          sb.append( "&amp;" );
          break;
        case '"':
          sb.append( "&quot;" );
          break;
        default:
          sb.append( ch );
      }
    }
    return sb.toString();
  }

  public static String xmlEncode( String text )
  {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < text.length(); i++ )
    {
      char ch = text.charAt( i );
      switch( ch )
      {
        case '<':
          sb.append( "&lt;" );
          break;
        case '>':
          sb.append( "&gt;" );
          break;
        case '&':
          sb.append( "&amp;" );
          break;
        case '"':
          sb.append( "&quot;" );
          break;
        case '\'':
          sb.append( "&apos;" );
          break;
        default:
          sb.append( ch );
      }
    }
    return sb.toString();
  }

  public static String wildcardToRegex( String wildcard )
  {
    StringBuilder s = new StringBuilder( wildcard.length() );
    s.append( '^' );
    for( int i = 0, is = wildcard.length(); i < is; i++ )
    {
      char c = wildcard.charAt( i );
      switch( c )
      {
        case '*':
          s.append( ".*" );
          break;
        case '?':
          s.append( "." );
          break;
        // escape special regexp-characters
        case '(':
        case ')':
        case '[':
        case ']':
        case '$':
        case '^':
        case '.':
        case '{':
        case '}':
        case '|':
        case '\\':
          s.append( "\\" );
          s.append( c );
          break;
        default:
          s.append( c );
          break;
      }
    }
    s.append( '$' );
    return s.toString();
  }

  /**
   * Pretty print function for formatting a duration.  E.g. 1010 milliseconds
   * is printed as "1sec 10ms"
   *
   * @param millis Milli to format
   *
   * @return A nicely formatted description of the duration
   */
  public static String formatDuration( long millis )
  {
    if( millis < 0 )
    {
      return "Negative";
    }

    int secs = (int)(millis / 1000);
    int mins = (secs / 60) % 60;
    int hrs = (secs / 3600);
    secs = secs % 60;
    millis = millis % 1000;

    StringBuilder buf = new StringBuilder();
    if( hrs > 0 )
    {
      buf.append( hrs ).append( ":" );
    }
    if( mins > 0 || hrs > 0 )
    {
      if( buf.length() > 0 && mins < 10 )
      {
        buf.append( "0" );
      }
      buf.append( mins ).append( ":" );
    }
    if( secs > 0 || mins > 0 || hrs > 0 )
    {
      if( buf.length() > 0 && secs < 10 )
      {
        buf.append( "0" );
      }
      buf.append( secs ).append( "." );
    }
    else
    {
      buf.append( "0." );
    }
    if( millis < 10 )
    {
      buf.append( "0" );
    }
    if( millis < 100 )
    {
      buf.append( "0" );
    }
    buf.append( millis );

    return buf.toString();
  }
}
