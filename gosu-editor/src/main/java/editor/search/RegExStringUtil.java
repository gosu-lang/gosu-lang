/**
 */
package editor.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExStringUtil
{
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
  public static List<SearchLocation> searchIgnoreCase( String strSource, String strPattern )
  {
    return search( strSource, strPattern, true );
  }

  public static List<SearchLocation> search( String strSource, String strPattern, boolean bIgnoreCase )
  {
    return search( strSource, strPattern, bIgnoreCase, 0 );
  }

  public static List<SearchLocation> search( String strSource, String strPattern, boolean bIgnoreCase, int iOffset )
  {
    return search( strSource, strPattern, bIgnoreCase, iOffset, false );
  }

  public static List<SearchLocation> search( String strSource, String strPattern, boolean bIgnoreCase, int iOffset, boolean backwards )
  {
    int iFlags = bIgnoreCase ? Pattern.CASE_INSENSITIVE : 0;

    List<SearchLocation> list = new ArrayList<SearchLocation>();

    int iIndex = 0;
    Pattern pattern = Pattern.compile( strPattern, iFlags );
    Matcher matcher = pattern.matcher( strSource );

    boolean bFound = backwards ? matcher.find() : matcher.find( iOffset );
    if( !bFound )
    {
      return list;
    }

    int iIndex2 = matcher.start();

    SearchLocation slBuffer = new SearchLocation();

    while( !backwards || matcher.end() < iOffset )
    {
      SearchLocation sl = new SearchLocation();
      sl._iOffset = iIndex2;
      StringUtil.getLocation( strSource, iIndex, iIndex2, slBuffer );
      sl._iLine = slBuffer._iLine + 1;
      sl._iColumn = slBuffer._iColumn + 1;
      sl._iLineOffset = slBuffer._iLineOffset;
      sl._iLength = matcher.end() - matcher.start();
      sl._matcher = matcher;
      list.add( sl );

      iIndex = iIndex2;
      bFound = matcher.find();
      if( !bFound )
      {
        break;
      }
      iIndex2 = matcher.start();
    }
    if( backwards )
    {
      Collections.reverse( list );
    }

    return list;
  }

}
