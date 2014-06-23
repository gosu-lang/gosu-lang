/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringUtil
{
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


}
