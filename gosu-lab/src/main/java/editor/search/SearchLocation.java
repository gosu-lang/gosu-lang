package editor.search;

import java.util.regex.Matcher;

/**
 * Location of a substring used for search results.
 */
public final class SearchLocation
{
  /**
   * The zero-based Offset from the beginning of the string.
   */
  public int _iOffset;

  /**
   * The length of the matching string.
   */
  public int _iLength;

  /**
   * The one-based line number
   */
  public int _iLine;

  /**
   * The one-based column number
   */
  public int _iColumn;

  /**
   * The offset of the line
   */
  public int _iLineOffset;

  /**
   * The regexp pattern matcher if applicable.
   */
  public Matcher _matcher;

  public String getReplacementText( String dialogReplaceText )
  {
    if( _matcher == null )
    {
      return dialogReplaceText;
    }
    else
    {
      StringBuffer replaceText = new StringBuffer();
      Matcher matcher = _matcher.region( _iOffset, _iOffset + _iLength );
      matcher.find();
      matcher.appendReplacement( replaceText, dialogReplaceText );
      return replaceText.substring( _iOffset );
    }
  }
}
