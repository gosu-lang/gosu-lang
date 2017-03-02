package editor.search;

import java.util.ArrayList;
import java.util.List;

public class StringUtil
{
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

    if( content.isEmpty() )
    {
      return list;
    }

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
    if( offset > 0 && Character.isLetterOrDigit( content.charAt( offset - 1 ) ) )
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


  public static int getLineOffset( String content, int line )
  {
    int lineCsr = 1;
    int offset = 0;
    for(; offset < content.length(); offset++ )
    {
      if( lineCsr == line )
      {
        return offset;
      }
      char c = content.charAt( offset );
      if( c == '\n' )
      {
        lineCsr++;
      }
    }
    return offset;
  }
}
