package gw.lang.enhancements

uses gw.util.*
uses java.util.regex.Pattern
uses java.lang.Integer
uses java.lang.Short
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.math.BigDecimal
uses java.util.Date
uses java.text.MessageFormat

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreStringEnhancement : java.lang.String
{  
  @ShortCircuitingProperty
  property get length() : int
  {
    return this.length() 
  }
  
  @ShortCircuitingProperty
  property get size() : int
  {
    return this.length()
  }
  
  /**
   * Note this property leverages Gosu's null short-circuit feature so that
   * you can use it even when this string is null.
   *
   * @return True if this string is not null and has a length > 0
   */
  property get HasContent() : boolean
  {
    return this.length() > 0
  }

  /**
   * Note this property leverages Gosu's null short-circuit feature so that
   * you can use it even when this string is null.
   *
   * @return True if this string is not null and contains at least one non-whitespace character
   */
  property get NotBlank() : boolean {
    return not GosuStringUtil.isBlank(this)
  }

  /**
   * Returns a RegExpMatch if this entire string matches the given
   * regular expression and null otherwise.
   */
  function match( regExp : String ) : RegExpMatch {
    return GosuStringUtil.match( this, regExp )
  }

  /*
   * Finds the index for all disjoint (non-overlapping) occurances of the substringToLookFor in the string.
   */
  function findDistinctIndicesOf(stringToLookFor : String) : List<java.lang.Integer> {
    return GosuStringUtil.findDistinctIndexesOf(this, stringToLookFor)
  }

  /**
   * <p>Checks if String contains a search String irrespective of case.</p>
   *
   * @param searchStr  the String to find, may be null
   * @return true if the String contains the search String irrespective of
   * case or false if not or <code>null</code> string input
   */
  function containsIgnoreCase(searchStr : String) : boolean {
    return GosuStringUtil.containsIgnoreCase(this, searchStr)
  }

  /**
   * <p>Removes all occurrences of a substring from within the source string.</p>
   *
   * An empty ("") source string will return the empty string.
   * A <code>null</code> remove string will return the source string.
   * An empty ("") remove string will return the source string.</p>
   *
   * @param substring  the String to search for and remove, may be null
   * @return the substring with the string removed if found,
   *  <code>null</code> if null String input
   */
  function remove(substring : String) : String {
    return GosuStringUtil.remove(this, substring)
  }

  /**
   * <p>Removes one newline from end of a String if it's there,
   * otherwise leave it alone.  A newline is &quot;<code>\n</code>&quot;,
   * &quot;<code>\r</code>&quot;, or &quot;<code>\r\n</code>&quot;.</p>
   *
   * @return String without newline
   */
  function chomp() : String {
    return GosuStringUtil.chomp(this)
  }

  /**
   * <p>Removes <code>separator</code> from the end of
   * this string if it's there, otherwise leave it alone.</p>
   *
   */
  function chomp( separator : String ) : String {
    return GosuStringUtil.chomp(this, separator)
  }

  /**
   * <p>Remove the last character from a String.</p>
   *
   * <p>If the String ends in <code>\r\n</code>, then remove both
   * of them.</p>
   *
   * @return String without last character
   */
  function chop() : String {
    return GosuStringUtil.chop(this)
  }

  /**
   * <p>Repeat a String <code>times</code> times to form a
   * new String.</p>
   *
   * @param times  number of times to repeat str, negative treated as zero
   * @return a new String consisting of the original String repeated
   */
  function repeat(times : int) : String {
    return GosuStringUtil.repeat(this, times)
  }

  /**
   * <p>Right pad a String with spaces (' ').</p>
   *
   * <p>The String is padded to the size of <code>size</code>.</p>
   *
   * @param size  the size to pad to
   * @return right padded String or original String if no padding is necessary
   */
  function rightPad(newSize : int) : String {
    return GosuStringUtil.rightPad(this, newSize)
  }

  /**
   * <p>Left pad a String with spaces (' ').</p>
   *
   * <p>The String is padded to the size of <code>size</code>.</p>
   *
   * @param size  the size to pad to
   * @return left padded String or original String if no padding is necessary
   */
  function leftPad(newSize : int) : String {
    return GosuStringUtil.leftPad(this, newSize)
  }

  /**
   * <p>Centers a String in a larger String of size <code>size</code>
   * using the space character (' ').<p>
   *
   * <p>If the size is less than the String length, the String is returned.
   * A negative size is treated as zero.</p>
   *
   * @param size  the int size of new String, negative treated as zero
   * @return centered String
   */
  function center(newSize : int) : String {
    return GosuStringUtil.center(this, newSize)
  }

  /**
   * <p>Capitalizes a String changing the first letter to title case as
   * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
   *
   * @return the capitalized String
   */
  function capitalize() : String {
    return GosuStringUtil.capitalize(this)
  }

  /**
   * <p>Uncapitalizes a String changing the first letter to lower case as
   * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
   *
   * @return the uncapitalized String
   */
  function uncapitalize() : String {
    return GosuStringUtil.uncapitalize(this)
  }

  /**
   * <p>Swaps the case of a String changing upper and title case to
   * lower case, and lower case to upper case.</p>
   *
   * <ul>
   *  <li>Upper case character converts to Lower case</li>
   *  <li>Title case character converts to Lower case</li>
   *  <li>Lower case character converts to Upper case</li>
   * </ul>
   *
   * @return the changed String
   */
  function swapCase() : String {
    return GosuStringUtil.swapCase(this)
  }

  /**
   * <p>Counts how many times the substring appears in the larger String.</p>
   *
   * <p>A <code>null</code> or empty ("") String input returns <code>0</code>.</p>
   *
   * @param sub  the substring to count, may be null
   * @return the number of occurrences, 0 if <code>sub</code> is <code>null</code>
   */
  function countMatches(substring : String) : int {
    return GosuStringUtil.countMatches(this, substring)
  }

  /**
   * <p>Counts how many times a regexp appears in the larger String.</p>
   *
   * <p>A <code>null</code> or empty ("") String input returns <code>0</code>.</p>
   *
   * @param regexp  the regexp to count
   * @return the number of occurrences
   */
  function countRegexpMatches(regexp : String) : int {
    return GosuStringUtil.countRegexpMatches(this, regexp)
  }

  /**
   * <p>Checks if the String contains only unicode letters.</p>
   *
   * <p><code>null</code> will return <code>false</code>.
   * An empty String ("") will return <code>true</code>.</p>
   *
   * @return <code>true</code> if only contains letters, and is non-null
   */
  property get Alpha() : boolean {
    return GosuStringUtil.isAlpha(this)
  }

  /**
   * <p>Checks if the String contains only unicode letters and
   * space (' ').</p>
   *
   * <p><code>null</code> will return <code>false</code>
   * An empty String ("") will return <code>true</code>.</p>
   *
   * @return <code>true</code> if only contains letters and space,
   *  and is non-null
   */
  property get AlphaSpace() : boolean {
    return GosuStringUtil.isAlphaSpace(this)
  }

  /**
   * <p>Checks if the String contains only unicode letters or digits.</p>
   *
   * <p><code>null</code> will return <code>false</code>.
   * An empty String ("") will return <code>true</code>.</p>
   *
   * @return <code>true</code> if only contains letters or digits,
   *  and is non-null
   */
  property get Alphanumeric() : boolean {
    return GosuStringUtil.isAlphanumeric(this)
  }

  /**
   * <p>Checks if the String contains only unicode letters, digits
   * or space (<code>' '</code>).</p>
   *
   * <p><code>null</code> will return <code>false</code>.
   * An empty String ("") will return <code>true</code>.</p>
   *
   * @return <code>true</code> if only contains letters, digits or space,
   *  and is non-null
   */
  property get AlphanumericSpace() : boolean {
    return GosuStringUtil.isAlphanumericSpace(this)
  }

  /**
   * <p>Checks if the String contains only unicode digits.
   * A decimal point is not a unicode digit and returns false.</p>
   *
   * <p><code>null</code> will return <code>false</code>.
   * An empty String ("") will return <code>true</code>.</p>
   *
   * @return <code>true</code> if only contains digits, and is non-null
   */
  property get Numeric() : boolean {
    return GosuStringUtil.isNumeric(this)
  }

  /**
   * <p>Checks if the String contains only unicode digits or space
   * (<code>' '</code>).
   * A decimal point is not a unicode digit and returns false.</p>
   *
   * <p><code>null</code> will return <code>false</code>.
   * An empty String ("") will return <code>true</code>.</p>
   *
   * @return <code>true</code> if only contains digits or space,
   *  and is non-null
   */
  property get NumericSpace() : boolean {
    return GosuStringUtil.isNumericSpace(this)
  }

  /**
   * <p>Checks if the String contains only whitespace.</p>
   *
   * <p><code>null</code> will return <code>false</code>.
   * An empty String ("") will return <code>true</code>.</p>
   *
   * @return <code>true</code> if only contains whitespace, and is non-null
   */
  property get Whitespace() : boolean {
    return GosuStringUtil.isWhitespace(this)
  }

  /**
   * <p>Reverses a String as per {@link StringBuffer#reverse()}.</p>
   *
   * @return the reversed String
   */
  function reverse() : String {
    return GosuStringUtil.reverse(this)
  }

  /**
   * <p>Abbreviates a String using ellipses. This will turn
   * "Now is the time for all good men" into "Now is the time for..."</p>
   *
   * <p>Specifically:
   * <ul>
   *   <li>If <code>str</code> is less than <code>maxWidth</code> characters
   *       long, return it.</li>
   *   <li>Else abbreviate it to <code>(substring(str, 0, max-3) + "...")</code>.</li>
   *   <li>If <code>maxWidth</code> is less than <code>4</code>, throw an
   *       <code>IllegalArgumentException</code>.</li>
   *   <li>In no case will it return a String of length greater than
   *       <code>maxWidth</code>.</li>
   * </ul>
   * </p>
   *
   * @param width  maximum length of result String, must be at least 4
   * @return abbreviated String
   * @throws IllegalArgumentException if the width is too small
   */
  function elide(width : int) : String {
    return GosuStringUtil.abbreviate(this, width)
  }

  /**
   * <p>Find the Levenshtein distance between two Strings.</p>
   *
   * <p>This is the number of changes needed to change one String into
   * another, where each change is a single character modification (deletion,
   * insertion or substitution).</p>
   *
   * @param otherStr  the second String, must not be null
   * @return result distance
   * @throws IllegalArgumentException if otherStr is <code>null</code>
   */
  function getDistanceFrom(otherStr : String) : int {
    return GosuStringUtil.getLevenshteinDistance(this, otherStr)
  }

  /**
   * <p>Case insensitive check if a String starts with a specified prefix.</p>
   *
   * @see java.lang.String#startsWith(String)
   * @param prefix the prefix to find, may be null
   * @return <code>true</code> if the String starts with the prefix, case insensitive
   */
  function startsWithIgnoreCase(substring : String) : boolean {
    return GosuStringUtil.startsWithIgnoreCase(this, substring)
  }

  /**
   * <p>Case insensitive check if a String ends with a specified suffix.</p>
   *
   * @see java.lang.String#endsWith(String)
   * @return <code>true</code> if the String ends with the suffix, case insensitive
   */
  function endsWithIgnoreCase(substring : String) : boolean {
    return GosuStringUtil.endsWithIgnoreCase(this, substring)
  }

  function formatMessage(args : Object[]) : String {
    return MessageFormat.format(this, args)
  }

  function toRegEx() : Pattern {
    return Pattern.compile( this )
  }

  function toBoolean() : boolean {
    return Boolean.parseBoolean( this )
  }

  function toShort() : short {
    return Short.parseShort( this )
  }

  function toInt() : int {
    return Integer.parseInt( this )
  }

  function toLong() : long {
    return Long.parseLong( this )
  }

  function toFloat() : float {
    return Float.parseFloat( this )
  }

  function toDouble() : double {
    return Double.parseDouble( this )
  }

  function toBigInteger() : BigInteger {
    return new BigInteger( this )
  }

  function toBigDecimal() : BigDecimal {
    return new BigDecimal( this )
  }

  function toDate() : Date {
    return new Date( this )
  }

  function toSHA1String() : String {
    return GosuStringUtil.getSHA1String( this )
  }
}
