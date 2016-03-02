/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.Pair;
import gw.lang.GosuShop;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.Keyword;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarningForDeprecatedMember;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IBlockLiteralExpression;
import gw.lang.parser.expressions.ICompoundTypeLiteral;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IBeanMethodCallStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.gs.IGosuEnhancement;

import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.GapContent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * This is quite primitive in that it simply provides support for lexically
 * analyzing the Gosu source.
 */
public class GosuDocument extends PlainDocument
{
  /**
   * Key to be used in AttributeSet's holding a value of Token.
   */
  static final Object CommentAttribute = new AttributeKey();

  private ParseResultsException _pe;
  private List<IParseTree> _locations;
  private int _locationsOffset;
  private char[] _charArray;
  private ITokenizerInstructor _instructor;

  public GosuDocument()
  {
    super( new GapContent( 1024 ) );
    _charArray = new char[1];
  }

  /**
   * @return A new lexical analyzer for this document.
   */
  public Scanner createScanner()
  {
    return new Scanner( new DocumentReader( 0, getLength() ), _instructor );
  }

  public void setParseResultsException( ParseResultsException pe )
  {
    _pe = pe;
  }

  public ParseResultsException getParseResultsException()
  {
    return _pe;
  }

  /**
   * The parsed Locations for the document.
   */
  public void setLocations( final List<IParseTree> locations )
  {
    _locations = locations;
  }

  public List<IParseTree> getLocations()
  {
    return _locations;
  }

  public void setLocationsOffset( int locationsOffset )
  {
    _locationsOffset = locationsOffset;
  }

  public boolean hasErrorStartingAt( int iPos )
  {
    if( _pe == null )
    {
      return false;
    }

    List<IParseIssue> pes = _pe.getParseExceptions();
    for( IParseIssue pe : pes )
    {
      if( pe.getTokenStart() == iPos )
      {
        return true;
      }
    }
    return false;
  }

  public Pair<Boolean, ParseWarningForDeprecatedMember> getParseWarningStartingAt( int iPos )
  {
    if( _pe == null )
    {
      return new Pair<Boolean, ParseWarningForDeprecatedMember>( false, null );
    }

    List<IParseIssue> pes = _pe.getParseWarnings();
    boolean hasParseWarning = false;
    ParseWarningForDeprecatedMember parseWarning = null;
    for( IParseIssue pe : pes )
    {
      if( (pe.getTokenStart() != null) && (pe.getTokenStart() == iPos) )
      {
        hasParseWarning = true;
        if( pe instanceof ParseWarningForDeprecatedMember )
        {
          parseWarning = (ParseWarningForDeprecatedMember)pe;
          break;
        }
      }
    }
    return new Pair<Boolean, ParseWarningForDeprecatedMember>( hasParseWarning, parseWarning );
  }

  /**
   * Returns a style code for the absolute position in the document or null if
   * no code is mapped.
   */
  public Integer getStyleCodeAtPosition( int iPosition )
  {
    if( _locations == null || _locations.isEmpty() )
    {
      return null;
    }
    IParseTree l;
    try
    {
      l = IParseTree.Search.getDeepestLocation( _locations, iPosition - _locationsOffset, true );
    }
    catch( Throwable t )
    {
      // Ok, what we are guarding against here is primarly a InnerClassNotFoundException. These can happen here in the UI
      // Thread when the intellisense thread is midway though parsing. The UI thread gets an old parsedelement which has
      // an old anonymous gosu class type in it and fails to load due the how anonymous class names are encoded with
      // offsets into the enclosing class file.
      return null;
    }
    if( l == null )
    {
      return null;
    }
    if( !l.contains( iPosition - _locationsOffset ) )
    {
      return null;
    }

    IParsedElement parsedElem = l.getParsedElement();

    try
    {
      return getStyleCodeForParsedElement( iPosition, parsedElem );
    }
    catch( Throwable t )
    {
      // Ok, what we are guarding against here is primarly a InnerClassNotFoundException. These can happen here in the UI
      // Thread when the intellisense thread is midway though parsing. The UI thread gets an old parsedelement which has
      // an old anonymous gosu class type in it and fails to load due the how anonymous class names are encoded with
      // offsets into the enclosing class file. 
      return null;
    }
  }

  /**
   * Given a IParsedElement return a special Style. Or return null if no special
   * style exists for the IParsedElement.
   */
  private Integer getStyleCodeForParsedElement( int iPosition, IParsedElement parsedElem )
  {
    if( parsedElem instanceof IBeanMethodCallStatement )
    {
      // Always inspect the IBeanMethodCallExpression rather than the corresponding statement
      parsedElem = ((IBeanMethodCallStatement)parsedElem).getBeanMethodCall();
    }

    //
    // Check for deprecated method descriptor
    //
    if( parsedElem instanceof IBeanMethodCallExpression )
    {
      // Highlight starting at the method name
      if( iPosition >= ((IBeanMethodCallExpression)parsedElem).getStartOffset() )
      {
        IMethodInfo md = ((IBeanMethodCallExpression)parsedElem).getMethodDescriptor();
        if( md != null )
        {
          if( md.getOwnersType() instanceof IGosuEnhancement )
          {
            return GosuStyleContext.ENHANCEMENT_METHOD_CALL_KEY;
          }
          else
          {
            return GosuStyleContext.METHOD_CALL_KEY;
          }
          // See CC-40285
//          return md.isDeprecated()
//                 ? GosuStyleContext.DEPRECATED_KEY
//                 : GosuStyleContext.METHOD_CALL_KEY;
        }
      }
      return null;
    }

    if( (parsedElem instanceof IExpression &&
         ((IExpression)parsedElem).getType() instanceof INamespaceType) &&
        !parsedElem.hasParseExceptions() )
    {
      return GosuStyleContext.NESTED_TYPE_LITERAL_KEY;
    }

    //
    // Check for deprecated property descriptor
    //
    if( parsedElem instanceof IFieldAccessExpression )
    {
      IFieldAccessExpression ma = (IFieldAccessExpression)parsedElem;
      try
      {
        if( iPosition < ma.getStartOffset() )
        {
          // Only highlight the property name
          return null;
        }

        if( !GosuEditorKit.getStylePreferences().areStylesEquivalent( GosuStyleContext.STYLE_EnhancementProperty, GosuStyleContext.STYLE_Property ) )
        {
          IPropertyInfo pi = ((IFieldAccessExpression)parsedElem).getPropertyInfo();
          if( pi != null && pi.getOwnersType() instanceof IGosuEnhancement )
          {
            return GosuStyleContext.ENHANCEMENT_PROPERTY_KEY;
          }
          else
          {
            return GosuStyleContext.PROPERTY_KEY;
          }
        }
        else
        {
          return GosuStyleContext.PROPERTY_KEY;
        }

        // See CC-40285
//        if( ma.getMemberName() != null )
//        {
//          IPropertyInfo pi = ((IFieldAccessExpression)parsedElem).getPropertyInfo();
//          if( pi != null && pi.isDeprecated() )
//          {
//            return GosuStyleContext.DEPRECATED_KEY;
//          }
//        }
      }
      catch( Exception e )
      {
        // Eat it.
      }
    }

    //
    // Check for ITypeLiteralExpression
    //
    if( parsedElem instanceof ITypeLiteralExpression )
    {
      ITypeLiteralExpression tl = (ITypeLiteralExpression)parsedElem;
      if( !(tl.getType().getType() instanceof IErrorType) )
      {
        IParsedElement parent = tl.getParent();
        if( parent instanceof IUsesStatement )
        {
          return GosuStyleContext.USES_KEY;
        }
        else
        {
          int segLength = Math.min( 5, getContent().length() - iPosition );
          Segment txt = new Segment( _charArray, iPosition, segLength );
          try
          {
            getContent().getChars( iPosition, segLength, txt );
            if( Character.isJavaIdentifierPart( txt.first() ) )
            {
              if( parent instanceof ITypeLiteralExpression &&
                  !(parent instanceof ICompoundTypeLiteral) )
              {
                if( parent instanceof IBlockLiteralExpression )
                {
                  return GosuStyleContext.TYPE_LITERAL_KEY;
                }
                else
                {
                  return GosuStyleContext.NESTED_TYPE_LITERAL_KEY;
                }
              }
              else if( parsedElem instanceof IBlockLiteralExpression &&
                       !"block".equals( txt.toString() ) )
              {
                return GosuStyleContext.NESTED_TYPE_LITERAL_KEY;
              }
              else
              {
                return GosuStyleContext.TYPE_LITERAL_KEY;
              }
            }
          }
          catch( BadLocationException e )
          {
            //ignore
          }
        }
      }
      else
      {
        return GosuStyleContext.PARSE_ERROR_KEY;
      }
    }

    if( parsedElem instanceof INamespaceStatement )
    {
      return GosuStyleContext.PACKAGE_KEY;
    }

    return null;
  }

  /**
   * Fetch a reasonable location to start scanning
   * given the desired start location.  This allows
   * for adjustments needed to accomodate multiline
   * comments.
   */
  public int getScannerStart( int p )
  {
    Element elem = getDefaultRootElement();
    int lineNum = elem.getElementIndex( p );
    Element line = elem.getElement( lineNum );
    AttributeSet a = line.getAttributes();
    while( a.isDefined( CommentAttribute ) && lineNum > 0 )
    {
      lineNum -= 1;
      line = elem.getElement( lineNum );
      a = line.getAttributes();
    }
    return line.getStartOffset();
  }

  // --- AbstractDocument methods ----------------------------

  /**
   * Updates document structure as a result of text insertion.  This
   * will happen within a write lock.  The superclass behavior of
   * updating the line map is executed followed by marking any comment
   * areas that should backtracked before scanning.
   *
   * @param chng the change event
   * @param attr the set of attributes
   */
  @Override
  protected void insertUpdate( DefaultDocumentEvent chng, AttributeSet attr )
  {
    super.insertUpdate( chng, attr );

    // Update comment marks
    Element root = getDefaultRootElement();
    DocumentEvent.ElementChange ec = chng.getChange( root );
    if( ec != null )
    {
      Element[] added = ec.getChildrenAdded();
      boolean inComment = false;
      for( Element elem : added )
      {
        int p0 = elem.getStartOffset();
        int p1 = elem.getEndOffset();
        String s;
        try
        {
          s = getText( p0, p1 - p0 );
        }
        catch( BadLocationException bl )
        {
          s = "";
        }
        if( inComment )
        {
          MutableAttributeSet a = (MutableAttributeSet)elem.getAttributes();
          a.addAttribute( CommentAttribute, CommentAttribute );
          int index = s.indexOf( "*/" );
          if( index >= 0 )
          {
            // found an end of comment, turn off marks
            inComment = false;
          }
        }
        else
        {
          // scan for multiline comment
          int index = s.indexOf( "/*" );
          if( index >= 0 )
          {
            // found a start of comment, see if it spans lines
            index = s.indexOf( "*/", index );
            if( index < 0 )
            {
              // it spans lines
              inComment = true;
            }
          }
        }
      }
    }
  }

  /**
   * Updates any document structure as a result of text removal.
   * This will happen within a write lock.  The superclass behavior of
   * updating the line map is executed followed by placing a lexical
   * update command on the analyzer queue.
   *
   * @param chng the change event
   */
  @Override
  protected void removeUpdate( DefaultDocumentEvent chng )
  {
    super.removeUpdate( chng );

    // update comment marks
  }

  public void setTokenizerInstructor( ITokenizerInstructor tokenizerInstructor )
  {
    _instructor = tokenizerInstructor;
  }


  //------------------------------------------------------------------------------
  //------------------------------------------------------------------------------
  static class AttributeKey
  {
    private AttributeKey()
    {
    }

    @Override
    public String toString()
    {
      return "comment";
    }
  }


  //------------------------------------------------------------------------------
  //------------------------------------------------------------------------------
  public class Scanner
  {
    int _p0;
    ISourceCodeTokenizer _tokenizer;

    //------------------------------------------------------------------------------
    Scanner( Reader reader, ITokenizerInstructor instructor )
    {
      _tokenizer = GosuShop.createSourceCodeTokenizer( reader );

      // Initialize the tokenizer
      _tokenizer.setWhitespaceSignificant( true );
      _tokenizer.setCommentsSignificant( true );
      _tokenizer.setParseDotsAsOperators( true );
      _tokenizer.wordChars( '_', '_' );

      if( instructor != null )
      {
        _tokenizer.setInstructor( instructor.createNewInstance( _tokenizer ) );
      }
    }

    //------------------------------------------------------------------------------
    public boolean isReservedWord( String strWord )
    {
      return Keyword.isReservedKeyword( strWord );
    }

    /**
     * Sets the range of the scanner.  This should be called
     * to reinitialize the scanner to the desired range of
     * coverage.
     */
    public void setRange( int p0, int p1 ) throws IOException
    {
      _tokenizer.reset( new DocumentReader( p0, p1 ) );
      _tokenizer.nextToken();

      _p0 = p0;
    }

    /**
     * This fetches the starting location of the current
     * token in the document.
     */
    public final int getStartOffset()
    {
      int begOffs = _tokenizer.getTokenStart();
      return _p0 + begOffs;
    }

    /**
     * This fetches the ending location of the current
     * token in the document.
     */
    public final int getEndOffset()
    {
      int endOffs = _tokenizer.getTokenEnd();
      return _p0 + endOffs;
    }

    public int getType()
    {
      return _tokenizer.getType();
    }

    public void nextToken() throws IOException
    {
      _tokenizer.nextToken();
    }
  }

  //------------------------------------------------------------------------------
  //------------------------------------------------------------------------------

  /**
   * Class to provide Reader functionality from a portion of a Document.
   */
  class DocumentReader extends Reader
  {
    Segment segment;
    int p0;    // start position
    int p1;    // end position
    int pos;   // pos in document
    int index; // index into array of the segment

    //------------------------------------------------------------------------------
    public DocumentReader( int p0, int p1 )
    {
      this.segment = new Segment();
      this.p0 = p0;
      this.p1 = Math.min( getLength(), p1 );
      pos = p0;
      try
      {
        loadSegment();
      }
      catch( IOException ioe )
      {
        throw new Error( "unexpected: " + ioe );
      }
    }

    //------------------------------------------------------------------------------
    @Override
    public int read() throws IOException
    {
      if( index >= segment.offset + segment.count )
      {
        if( pos >= p1 )
        {
          // no more data
          return -1;
        }
        loadSegment();
      }
      return segment.array[index++];
    }

    //------------------------------------------------------------------------------
    @Override
    public void close() throws IOException
    {
    }

    @Override
    public void reset() throws IOException
    {
      pos = p0;
    }

    //------------------------------------------------------------------------------
    @Override
    public int read( char[] cbuf, int off, int len ) throws IOException
    {
      if( (off < 0) || (off > cbuf.length) || (len < 0) ||
          ((off + len) > cbuf.length) || ((off + len) < 0) )
      {
        throw new IndexOutOfBoundsException();
      }
      else if( len == 0 )
      {
        return 0;
      }
      int length = segment.offset + segment.count;
      if( index >= length )
      {
        if( pos >= p1 )
        {
          // no more data
          return -1;
        }
        loadSegment();
        length = segment.offset + segment.count;
      }
      int n = Math.min( length - index, len );
      System.arraycopy( segment.array, index, cbuf, 0, n );
      index += n;
      return n;
    }

    //------------------------------------------------------------------------------
    void loadSegment() throws IOException
    {
      try
      {
        int n = Math.min( 1024, p1 - pos );
        getText( pos, n, segment );
        pos += n;
        index = segment.offset;
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
  }
}

