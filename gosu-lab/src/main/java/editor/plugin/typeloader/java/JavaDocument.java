package editor.plugin.typeloader.java;

import editor.Scheme;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class JavaDocument extends DefaultStyledDocument
{
  private Element _root;
  private MutableAttributeSet _word;
  private MutableAttributeSet _keyword;
  private MutableAttributeSet _error;
  private MutableAttributeSet _warning;
  private MutableAttributeSet _comment;
  private MutableAttributeSet _stringLiteral;
  private boolean _multiLineComment;
  private Set<String> _keywords;
  private DiagnosticCollector<JavaFileObject> _errorHandler;

  public JavaDocument()
  {
    _root = getDefaultRootElement();
    putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );

    _word = new SimpleAttributeSet();
    StyleConstants.setForeground( _word, Scheme.active().getCodeWindowText() );

    _error = new SimpleAttributeSet();
    StyleConstants.setForeground( _error, Scheme.active().getCodeError() );

    _warning = new SimpleAttributeSet();
    StyleConstants.setForeground( _warning, Scheme.active().getCodeWarning() );

    _keyword = new SimpleAttributeSet();
    StyleConstants.setForeground( _keyword, Scheme.active().getCodeKeyword() );
    StyleConstants.setBold( _keyword, true );

    _comment = new SimpleAttributeSet();
    StyleConstants.setForeground( _comment, Scheme.active().getCodeComment() );

    _stringLiteral = new SimpleAttributeSet();
    StyleConstants.setForeground( _stringLiteral, Scheme.active().getCodeStringLiteral() );

    _keywords = new HashSet<>();
    _keywords.add( "abstract" );
    _keywords.add( "assert" );
    _keywords.add( "boolean" );
    _keywords.add( "break" );
    _keywords.add( "byte" );
    _keywords.add( "case" );
    _keywords.add( "catch" );
    _keywords.add( "char" );
    _keywords.add( "class" );
    _keywords.add( "continue" );
    _keywords.add( "default" );
    _keywords.add( "do" );
    _keywords.add( "double" );
    _keywords.add( "else" );
    _keywords.add( "enum" );
    _keywords.add( "extends" );
    _keywords.add( "final" );
    _keywords.add( "finally" );
    _keywords.add( "float" );
    _keywords.add( "for" );
    _keywords.add( "if" );
    _keywords.add( "implements" );
    _keywords.add( "import" );
    _keywords.add( "instanceof" );
    _keywords.add( "int" );
    _keywords.add( "interface" );
    _keywords.add( "long" );
    _keywords.add( "native" );
    _keywords.add( "new" );
    _keywords.add( "package" );
    _keywords.add( "private" );
    _keywords.add( "protected" );
    _keywords.add( "public" );
    _keywords.add( "return" );
    _keywords.add( "short" );
    _keywords.add( "static" );
    _keywords.add( "strictfp" );
    _keywords.add( "super" );
    _keywords.add( "switch" );
    _keywords.add( "synchronized" );
    _keywords.add( "this" );
    _keywords.add( "throws" );
    _keywords.add( "transient" );
    _keywords.add( "try" );
    _keywords.add( "void" );
    _keywords.add( "volatile" );
    _keywords.add( "while" );
    _keywords.add( "true" );
    _keywords.add( "false" );
  }

  /**
   * Override to apply syntax highlighting after the document has been updated
   */
  @Override
  public void insertString( int offset, String str, AttributeSet a ) throws BadLocationException
  {
    switch( str )
    {
      case "(":
        str = addParenthesis();
        break;
      case "\n":
        str = addWhiteSpace( offset );
        break;
      case "\"":
        str = addMatchingQuotationMark();
        break;
      case "{":
        str = addMatchingBrace( offset );
        break;
    }
    super.insertString( offset, str, a );
    processChangedLines( offset, str.length() );
  }

  /*
   * Override to apply syntax highlighting after the document has been updated
   */
  @Override
  public void remove( int offset, int length ) throws BadLocationException
  {
    super.remove( offset, length );
    processChangedLines( offset, 0 );
  }

  /*
   * Determine how many lines have been changed,
   * then apply highlighting to each line
   */
  private void processChangedLines( int offset, int length ) throws BadLocationException
  {
    String content = getText( 0, getLength() );
    // The lines affected by the latest document update
    int startLine = _root.getElementIndex( offset );
    int endLine = _root.getElementIndex( offset + length );
    // Make sure all comment lines prior to the start line are commented
    // and determine if the start line is still in a multi line comment
    setMultiLineComment( commentLinesBefore( content, startLine ) );
    // Do the actual highlighting
    for( int i = startLine; i <= endLine; i++ )
    {
      applyHighlighting( content, i );
    }
    // Resolve highlighting to the next end multi line delimiter
    if( isMultiLineComment() )
    {
      commentLinesAfter( content, endLine );
    }
    else
    {
      highlightLinesAfter( content, endLine );
    }
  }

  /*
   * Highlight lines when a multi line comment is still 'open'
   * (ie. matching end delimiter has not yet been encountered)
   */
  private boolean commentLinesBefore( String content, int line )
  {
    int offset = _root.getElement( line ).getStartOffset();
    // Start of comment not found, nothing to do
    int startDelimiter = lastIndexOf( content, getStartDelimiter(), offset - 2 );
    if( startDelimiter < 0 )
    {
      return false;
    }
    // Matching start/end of comment found, nothing to do
    int endDelimiter = indexOf( content, getEndDelimiter(), startDelimiter );
    if( endDelimiter < offset & endDelimiter != -1 )
    {
      return false;
    }
    // End of comment not found, highlight the lines
    setCharacterAttributes( startDelimiter, offset - startDelimiter + 1, _comment, false );
    return true;
  }

  /*
   * Highlight comment lines to matching end delimiter
   */
  private void commentLinesAfter( String content, int line )
  {
    int offset = _root.getElement( line ).getEndOffset();
    // End of comment not found, nothing to do
    int endDelimiter = indexOf( content, getEndDelimiter(), offset );
    if( endDelimiter < 0 )
    {
      return;
    }
    // Matching start/end of comment found, comment the lines
    int startDelimiter = lastIndexOf( content, getStartDelimiter(), endDelimiter );
    if( startDelimiter < 0 || startDelimiter <= offset )
    {
      setCharacterAttributes( offset, endDelimiter - offset + 1, _comment, false );
    }
  }

  /*
   * Highlight lines to start or end delimiter
   */
  private void highlightLinesAfter( String content, int line )
    throws BadLocationException
  {
    int offset = _root.getElement( line ).getEndOffset();
    // Start/End delimiter not found, nothing to do
    int startDelimiter = indexOf( content, getStartDelimiter(), offset );
    int endDelimiter = indexOf( content, getEndDelimiter(), offset );
    if( startDelimiter < 0 )
    {
      startDelimiter = content.length();
    }
    if( endDelimiter < 0 )
    {
      endDelimiter = content.length();
    }
    int delimiter = Math.min( startDelimiter, endDelimiter );
    if( delimiter < offset )
    {
      return;
    }
    // Start/End delimiter found, reapply highlighting
    int endLine = _root.getElementIndex( delimiter );
    for( int i = line + 1; i < endLine; i++ )
    {
      Element branch = _root.getElement( i );
      Element leaf = getCharacterElement( branch.getStartOffset() );
      AttributeSet as = leaf.getAttributes();
      if( as.isEqual( _comment ) )
      {
        applyHighlighting( content, i );
      }
    }
  }

  /*
   * Parse the line to determine the appropriate highlighting
   */
  private void applyHighlighting( String content, int line ) throws BadLocationException
  {
    int startOffset = _root.getElement( line ).getStartOffset();
    int endOffset = _root.getElement( line ).getEndOffset() - 1;
    int lineLength = endOffset - startOffset;
    int contentLength = content.length();
    if( endOffset >= contentLength )
    {
      endOffset = contentLength - 1;
    }
    // check for multi line comments
    // (always set the comment attribute for the entire line)
    if( endingMultiLineComment( content, startOffset, endOffset ) || isMultiLineComment() || startingMultiLineComment( content, startOffset, endOffset ) )
    {
      setCharacterAttributes( startOffset, endOffset - startOffset + 1, _comment, false );
      return;
    }
    // set normal attributes for the line
    setCharacterAttributes( startOffset, lineLength, _word, true );
    // check for single line comment
    int index = content.indexOf( getSingleLineDelimiter(), startOffset );
    if( (index > -1) && (index < endOffset) )
    {
      setCharacterAttributes( index, endOffset - index + 1, _comment, false );
      endOffset = index - 1;
    }
    // check for tokens
    checkForTokens( content, startOffset, endOffset );
  }

  /*
   * Does this line contain the start delimiter
   */
  private boolean startingMultiLineComment( String content, int startOffset, int endOffset ) throws BadLocationException
  {
    int index = indexOf( content, getStartDelimiter(), startOffset );
    if( (index < 0) || (index > endOffset) )
    {
      return false;
    }
    else
    {
      setMultiLineComment( true );
      return true;
    }
  }

  /*
   * Does this line contain the end delimiter
   */
  private boolean endingMultiLineComment( String content, int startOffset, int endOffset ) throws BadLocationException
  {
    int index = indexOf( content, getEndDelimiter(), startOffset );
    if( (index < 0) || (index > endOffset) )
    {
      return false;
    }
    else
    {
      setMultiLineComment( false );
      return true;
    }
  }

  private boolean isMultiLineComment()
  {
    return _multiLineComment;
  }
  private void setMultiLineComment( boolean value )
  {
    _multiLineComment = value;
  }

  /*
   * Parse the line for tokens to highlight
   */
  private void checkForTokens( String content, int startOffset, int endOffset )
  {
    while( startOffset <= endOffset )
    {
      // skip the delimiters to find the start of a new token
      while( isDelimiter( content.substring( startOffset, startOffset + 1 ) ) )
      {
        if( isError( startOffset ) )
        {
          setCharacterAttributes( startOffset, 1, _error, true );
        }
        else if( isWarning( startOffset ) )
        {
          setCharacterAttributes( startOffset, 1, _warning, true );
        }

        if( startOffset < endOffset )
        {
          startOffset++;
        }
        else
        {
          return;
        }
      }
      // Extract and process the entire token
      if( isQuoteDelimiter( content.substring( startOffset, startOffset + 1 ) ) )
      {
        startOffset = getQuoteToken( content, startOffset, endOffset );
      }
      else if( isCharQuoteDelimiter( content.substring( startOffset, startOffset + 1 ) ) )
      {
        startOffset = getQuoteToken( content, startOffset, endOffset );
      }
      else
      {
        startOffset = getOtherToken( content, startOffset, endOffset );
      }
    }
  }

  /*
   * Parse the line to get the quotes and highlight it
   */
  private int getQuoteToken( String content, int startOffset, int endOffset )
  {
    String quoteDelimiter = content.substring( startOffset, startOffset + 1 );
    int index;
    int endOfQuote = startOffset;
    int endOfToken = startOffset + 1;
    while( endOfToken <= endOffset )
    {
      if( isDelimiter( content.substring( endOfToken, endOfToken + 1 ) ) )
      {
        break;
      }
      endOfToken++;
    }
    index = content.indexOf( quoteDelimiter, endOfQuote + 1 );
    if( (index < 0) || (index > endOffset) )
    {
      endOfQuote = endOffset;
    }
    else
    {
      endOfQuote = index;
    }

    if( isError( startOffset, endOfQuote - startOffset + 1 ) )
    {
      setCharacterAttributes( startOffset, endOfQuote - startOffset + 1, _error, true );
      return endOfQuote + 1;
    }
    if( isWarning( startOffset, endOfQuote - startOffset + 1 ) )
    {
      setCharacterAttributes( startOffset, endOfQuote - startOffset + 1, _warning, true );
      return endOfQuote + 1;
    }

    setCharacterAttributes( startOffset, endOfQuote - startOffset + 1, _stringLiteral, false );
    return endOfQuote + 1;
  }

  private int getOtherToken( String content, int startOffset, int endOffset )
  {
    int endOfToken = startOffset + 1;
    while( endOfToken <= endOffset )
    {
      if( isDelimiter( content.substring( endOfToken, endOfToken + 1 ) ) )
      {
        break;
      }
      endOfToken++;
    }
    String token = content.substring( startOffset, endOfToken );

    if( isError( startOffset, endOfToken - startOffset ) )
    {
      setCharacterAttributes( startOffset, endOfToken - startOffset, _error, true );
      return endOfToken + 1;
    }
    if( isWarning( startOffset, endOfToken - startOffset ) )
    {
      setCharacterAttributes( startOffset, endOfToken - startOffset, _warning, true );
      return endOfToken + 1;
    }

    if( isKeyword( token ) )
    {
      setCharacterAttributes( startOffset, endOfToken - startOffset, _keyword, false );
    }
    return endOfToken + 1;
  }

  /*
   * This updates the colored text and prepares for undo event
   */
  @Override
  protected void fireInsertUpdate( DocumentEvent evt )
  {

    super.fireInsertUpdate( evt );

    try
    {
      processChangedLines( evt.getOffset(), evt.getLength() );
    }
    catch( Exception ex )
    {
      System.out.println( "" + ex );
    }
  }

  /*
   * This updates the colored text and does the undo operation
   */
  @Override
  protected void fireRemoveUpdate( DocumentEvent evt )
  {

    super.fireRemoveUpdate( evt );

    try
    {
      processChangedLines( evt.getOffset(), evt.getLength() );
    }
    catch( BadLocationException ex )
    {
      System.out.println( "" + ex );
    }
  }

  /*
   * Assume the needle will the found at the start/end of the line
   */
  private int indexOf( String content, String needle, int offset )
  {
    int index;
    while( (index = content.indexOf( needle, offset )) != -1 )
    {
      String text = getLine( content, index ).trim();
      if( text.startsWith( needle ) || text.endsWith( needle ) )
      {
        break;
      }
      else
      {
        offset = index + 1;
      }
    }
    return index;
  }

  /*
   * Assume the needle will the found at the start/end of the line
   */
  private int lastIndexOf( String content, String needle, int offset )
  {
    int index;
    while( (index = content.lastIndexOf( needle, offset )) != -1 )
    {
      String text = getLine( content, index ).trim();
      if( text.startsWith( needle ) || text.endsWith( needle ) )
      {
        break;
      }
      else
      {
        offset = index - 1;
      }
    }
    return index;
  }

  private String getLine( String content, int offset )
  {
    int line = _root.getElementIndex( offset );
    Element lineElement = _root.getElement( line );
    int start = lineElement.getStartOffset();
    int end = lineElement.getEndOffset();
    return content.substring( start, end - 1 );
  }

  /*
   * Override for other languages
   */
  protected boolean isDelimiter( String character )
  {
    String operands = ".;:()\\[]+-/%<=>!&|^~*";
    return Character.isWhitespace( character.charAt( 0 ) ) || operands.contains( character );
  }

  /*
   * Override for other languages
   */
  protected boolean isQuoteDelimiter( String character )
  {
    String quoteDelimiters = "\"";
    return quoteDelimiters.contains( character );
  }

  protected boolean isCharQuoteDelimiter( String character )
  {
    String quoteDelimiters = "\'";
    return quoteDelimiters.contains( character );
  }

  /*
   * Override for other languages
   */
  protected boolean isKeyword( String token )
  {
    return _keywords.contains( token );
  }

  /*
   * Override for other languages
   */
  protected String getStartDelimiter()
  {
    return "/*";
  }

  /*
   * Override for other languages
   */
  protected String getEndDelimiter()
  {
    return "*/";
  }

  /*
   * Override for other languages
   */
  protected String getSingleLineDelimiter()
  {
    return "//";
  }

  /*
   * Override for other languages
   */
  protected String addMatchingQuotationMark() throws BadLocationException
  {
    return "\"\"";
  }

  /*
   * Overide bracket matching for other languages
   */
  protected String addMatchingBrace( int offset ) throws BadLocationException
  {
    StringBuilder whiteSpace = new StringBuilder();
    int line = _root.getElementIndex( offset );
    int i = _root.getElement( line ).getStartOffset();
    while( true )
    {
      String temp = getText( i, 1 );
      if( temp.equals( " " ) || temp.equals( "\t" ) )
      {
        whiteSpace.append( temp );
        i++;
      }
      else
      {
        break;
      }
    }
    return "{\n" + whiteSpace.toString() + "    " + "\n" + whiteSpace.toString() + "}";
  }

  /*
   * Overide bracket matching for other languages
   */
  protected String addWhiteSpace( int offset ) throws BadLocationException
  {
    StringBuilder whiteSpace = new StringBuilder();
    int line = _root.getElementIndex( offset );
    int i = _root.getElement( line ).getStartOffset();
    while( true )
    {
      String temp = getText( i, 1 );
      if( temp.equals( " " ) || temp.equals( "\t" ) )
      {
        whiteSpace.append( temp );
        i++;
      }
      else
      {
        break;
      }
    }
    return "\n" + whiteSpace;
  }

  protected String addParenthesis() throws BadLocationException
  {
    return "()";
  }

  public DiagnosticCollector<JavaFileObject> getErrorHandler()
  {
    return _errorHandler;
  }
  public void setErrorHandler( DiagnosticCollector<JavaFileObject> errorHandler )
  {
    DiagnosticCollector<JavaFileObject> oldErrorHandler = _errorHandler;
    _errorHandler = errorHandler;
    processIssues( oldErrorHandler );
    processIssues( _errorHandler );
  }

  private void processIssues( DiagnosticCollector<JavaFileObject> errorHandler )
  {
    if( errorHandler == null )
    {
      return;
    }

    for( Diagnostic issue: errorHandler.getDiagnostics() )
    {
      try
      {
        processChangedLines( (int)issue.getStartPosition(), (int)issue.getEndPosition() - (int)issue.getStartPosition() );
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private boolean isError( int iPos )
  {
    return isError( iPos, 1 );
  }
  private boolean isError( int iPos, int iLength )
  {
    if( _errorHandler == null )
    {
      return false;
    }

    for( Diagnostic issue: _errorHandler.getDiagnostics() )
    {
      if( issue.getKind() == Diagnostic.Kind.ERROR &&
          (iPos >= issue.getStartPosition() && iPos <= issue.getEndPosition() ||
           iPos+iLength >= issue.getStartPosition() && iPos+iLength <= issue.getEndPosition()) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isWarning( int iPos )
  {
    return isWarning( iPos, 1 );
  }
  private boolean isWarning( int iPos, int iLength )
  {
    if( _errorHandler == null )
    {
      return false;
    }

    for( Diagnostic issue: _errorHandler.getDiagnostics() )
    {
      if( (issue.getKind() == Diagnostic.Kind.WARNING ||
           issue.getKind() == Diagnostic.Kind.MANDATORY_WARNING) &&
          (iPos >= issue.getStartPosition() && iPos <= issue.getEndPosition() ||
           iPos+iLength >= issue.getStartPosition() && iPos+iLength <= issue.getEndPosition()) )
      {
        return true;
      }
    }
    return false;
  }

  public String findErrorMessage( int iPos )
  {
    if( _errorHandler == null )
    {
      return null;
    }

    Element root = getDefaultRootElement();
    int index = root.getElementIndex( iPos );
    if( index < 0 )
    {
      return null;
    }

    Element lineElement = root.getElement( index );
    if( lineElement == null )
    {
      return null;
    }

    int tokenIndex = lineElement.getElementIndex( iPos );
    if( tokenIndex < 0 )
    {
      return null;
    }

    Element tokenElem = lineElement.getElement( tokenIndex );
    if( tokenElem == null )
    {
      return null;
    }

    if( tokenElem.getAttributes().containsAttributes( _error ) ||
        tokenElem.getAttributes().containsAttributes( _warning ) )
    {
      for( Diagnostic issue: _errorHandler.getDiagnostics() )
      {
        if( issue.getStartPosition() >= tokenElem.getStartOffset() && issue.getStartPosition() >= tokenElem.getEndOffset() ||
            issue.getEndPosition() >= tokenElem.getStartOffset() && issue.getEndPosition() >= tokenElem.getEndOffset() )
        {
          return issue.getMessage( Locale.getDefault() );
        }
      }
    }

    return null;
  }
}
