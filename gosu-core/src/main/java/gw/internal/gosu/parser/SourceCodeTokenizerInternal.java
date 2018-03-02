/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.internal.gosu.template.TemplateGenerator;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.Keyword;
import gw.lang.parser.SourceCodeReader;
import gw.util.Stack;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class is adapted from java.io.SourceCodeTokenizer.  It adds the notion
 * of operator to the mix.  You can define your own operators or use the
 * default set of operators, which are taken from the Java Language Spec.
 * It also captures state information for use by our parser e.g., current token
 * location, line number and column.
 */
final public class SourceCodeTokenizerInternal
{
  protected static final int CT_WHITESPACE = 1;
  protected static final int CT_DIGIT = 2;
  protected static final int CT_ALPHA = 4;
  protected static final int CT_QUOTE = 8;
  protected static final int CT_COMMENT = 16;
  protected static final int CT_OPERATOR = 32;
  protected static final int CT_BITSHIFT_OPERATOR = 64;
  protected static final int CT_CHARQUOTE = 128;

  private static final Set<String> DEFAULT_OPERATORS =
    Collections.unmodifiableSet( new HashSet<>( Arrays.asList( getDefaultOperators() ) ) );
  private static final Set<String> BITSHIFT_OPERATORS =
          Collections.unmodifiableSet( new HashSet<>( Arrays.asList( getBitshiftOperators() ) ) );

  private SourceCodeReader _reader;

  int _peekc;
  private boolean _bForceLower;

  private boolean _bEOLIsSignificantP;

  private int _ctype[];

  private Set<String> _operators;

  private int _iPos;
  private int _iLineNum;
  private int _iColumn;
  private int _iTokenStart;
  private int _iTokenColumn;

  private boolean _bWhitespaceSignificant;
  private boolean _bCommentsSignificant;

  public int _iType;
  public Keyword _keyword;
  public String _strValue;
  public int _iInvalidCharPos;
  public boolean _bUnterminatedString;
  public boolean _bUnterminatedComment;

  private ITokenizerInstructor _instructor;
  private DocCommentBlock _lastComment;
  private boolean _bParseDotsAsOperators;
  private int _iLineOffset;

  private Stack<Token> _tokens;
  private Token _eof;
  private boolean _supportsKeywords = true;

  SourceCodeTokenizerInternal( boolean initForCopy )
  {
    if( !initForCopy )
    {
      _iLineOffset = -1;
      _iLineNum = 1;
      _bEOLIsSignificantP = false;
      _ctype = new int[256];
      _iType = ISourceCodeTokenizer.TT_NOTHING;
      _bParseDotsAsOperators = true;
      _tokens = new Stack<>(); // assigned as needed

      wordChars( 'a', 'z' );
      wordChars( 'A', 'Z' );
      wordChars( 128 + 32, 255 );
      wordChars( '$', '$' );
      whitespaceChars( 0, ' ' );
      //commentChar( '/' );
      quoteChar( '"' );
      quoteChar( '\'' );
      charQuoteChar( '\'' );
      parseNumbers();

      _operators = DEFAULT_OPERATORS;
      setOperatorChars( _operators );
      setBitshiftOperatorChars( BITSHIFT_OPERATORS );
    }
  }

  public SourceCodeTokenizerInternal( CharSequence sourceCode )
  {
    this( new SourceCodeReader( sourceCode ), null );
  }

  public SourceCodeTokenizerInternal( Reader reader )
  {
    this( SourceCodeReader.makeSourceCodeReader( reader ), null );
  }

  public SourceCodeTokenizerInternal( SourceCodeReader reader )
  {
    this( reader, null );
  }

  public SourceCodeTokenizerInternal( SourceCodeReader reader, ITokenizerInstructor instructor )
  {
    this( false );
    _reader = reader;
    _instructor = instructor;
  }

  public Token copy()
  {
    throw new IllegalStateException( "should not call this from internal" );
  }

  public void reset()
  {
    reset( _reader, true );
  }

  public void reset( Reader reader )
  {
    reset( SourceCodeReader.makeSourceCodeReader( reader ), false );
  }

  public void reset( SourceCodeReader reader )
  {
    reset( reader, false );
  }

  void reset( SourceCodeReader reader, boolean bResetReader )
  {
    _reader = reader;
    if( bResetReader && (_reader != null) )
    {
      try
      {
        reader.setPosition( 0 );
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }

    _peekc = 0;
    _bForceLower = false;

    _iType = ISourceCodeTokenizer.TT_NOTHING;
    _strValue = null;
    _iInvalidCharPos = -1;

    _iPos = 0;
    _iLineNum = _iLineOffset > -1 ? _iLineOffset : 1;
    _iColumn = 0;
    _iTokenStart = 0;
    _iTokenColumn = 0;
    _bUnterminatedString = false;
    _bUnterminatedComment = false;
    _lastComment = null;
    _tokens = new Stack<>();
    if( _instructor != null )
    {
      _instructor.reset();
    }
  }

  public SourceCodeReader getReader()
  {
    return _reader;
  }

  public String getSource()
  {
    return _reader == null ? "" : _reader.getSource();
  }

  public ITokenizerInstructor getInstructor()
  {
    return _instructor;
  }

  public void setInstructor( ITokenizerInstructor instructor )
  {
    if( _instructor != instructor )
    {
      reset();
      _instructor = instructor;
    }
  }

  public boolean isWhitespaceSignificant()
  {
    return _bWhitespaceSignificant;
  }

  public void setWhitespaceSignificant( boolean bWhitespaceSignificant )
  {
    _bWhitespaceSignificant = bWhitespaceSignificant;
  }

  public boolean isCommentsSignificant()
  {
    return _bCommentsSignificant;
  }

  public void setCommentsSignificant( boolean bCommentsSignificant )
  {
    _bCommentsSignificant = bCommentsSignificant;
  }

  public int getLineNumber()
  {
    return _iLineNum;
  }

  public int getLineOffset()
  {
    return _iLineOffset;
  }

  protected void incrementLineNumber()
  {
    _iColumn = 0;
    _iLineNum++;
  }

  public int getTokenColumn()
  {
    return _iTokenColumn;
  }

  public void wordChars( int iLow, int iHigh )
  {
    if( iLow < 0 )
    {
      iLow = 0;
    }

    if( iHigh >= _ctype.length )
    {
      iHigh = _ctype.length - 1;
    }

    while( iLow <= iHigh )
    {
      _ctype[iLow++] |= CT_ALPHA;
    }
  }

  public void whitespaceChars( int iLow, int iHigh )
  {
    if( iLow < 0 )
    {
      iLow = 0;
    }

    if( iHigh >= _ctype.length )
    {
      iHigh = _ctype.length - 1;
    }

    while( iLow <= iHigh )
    {
      _ctype[iLow++] = CT_WHITESPACE;
    }
  }

  public void ordinaryChars( int iLow, int iHigh )
  {
    if( iLow < 0 )
    {
      iLow = 0;
    }

    if( iHigh >= _ctype.length )
    {
      iHigh = _ctype.length - 1;
    }

    while( iLow <= iHigh )
    {
      _ctype[iLow++] = 0;
    }
  }

  public void ordinaryChar( int ch )
  {
    if( ch >= 0 && ch < _ctype.length )
    {
      _ctype[ch] = 0;
    }
  }

  public static String[] getDefaultOperators()
  {
    return new String[]
      {
        "=", ">", "<",

        "!", "~", "?", ":", "?:",

        "==", "<=", /*">=",*/ "!=", "<>",

        "&&", "||",
        
        "++", "--",

        "===", "!==", "*.",

        // Arithmetic operators
        "+", "-", "*", "/", "&", "|", "^", "%",
        // Null-safe arithmetic operators
        "?+", "?-", "?*", "?/", "?%",
        // Unchecked overflow arithmetic operators for integers
        "!+", "!-", "!*",

        // Compound operators
        "+=", "-=", "*=", "/=", "&=", "&&=", "|=", "||=", "^=", "%=",

        // Block operators
        "\\", "->",

        // Member-access operators
        ".", "?.",

        // Null-safe array access
        "?[",

        // Interval operators
        "..", "|..", "..|", "|..|",

        // Feature Literals
        "#",
      };
  }

  public static List<String> getDefaultBindingOperators()
  {
    return Arrays.asList(
        ":",

        "-",

        "/",

        "|",

        "\\",

        "#" );
  }

  public static String[] getBitshiftOperators()
  {
    return new String[]
      {
        "<<",
        "<<=",
        //## these interfere w generics e.g. List<List<String>>
        //">>", ">>>",
        //">>=", ">>>="
      };
  }

  public void operators( String[] astrOperators )
  {
    if( astrOperators == null )
    {
      return;
    }

    for( String astrOperator : astrOperators )
    {
      if( (astrOperator == null) || (astrOperator.length() == 0) )
      {
        continue;
      }

      // Now append the operator to the list...
      if( _operators == null || _operators == DEFAULT_OPERATORS )
      {
        _operators = new HashSet<>();
      }

      _operators.add( astrOperator );
    }

    setOperatorChars( _operators );
  }

  private void setOperatorChars( Set<String> operators )
  {
    for( String operator : operators )
    {
      // First set the first character of each operator as a CT_OPERATOR character...
      char c = operator.charAt( 0 );
      operatorChars( c, c );
    }
  }

  public void operatorChars( int iLow, int iHigh )
  {
    if( iLow < 0 )
    {
      iLow = 0;
    }

    if( iHigh >= _ctype.length )
    {
      iHigh = _ctype.length - 1;
    }

    while( iLow <= iHigh )
    {
      _ctype[iLow++] |= CT_OPERATOR;
    }
  }

  private void setBitshiftOperatorChars( Set<String> operators )
  {
    for( String operator : operators )
    {
      // First set the first character of each operator as a CT_OPERATOR character...
      char c = operator.charAt( 0 );
      bitshiftOperatorChars( c, c );
    }
  }

  public void bitshiftOperatorChars( int iLow, int iHigh )
  {
    if( iLow < 0 )
    {
      iLow = 0;
    }

    if( iHigh >= _ctype.length )
    {
      iHigh = _ctype.length - 1;
    }

    while( iLow <= iHigh )
    {
      _ctype[iLow++] |= CT_BITSHIFT_OPERATOR;
    }
  }

  public boolean isOperator( String strOperator )
  {
    return (_operators != null && _operators.contains( strOperator )) ||
           BITSHIFT_OPERATORS.contains( strOperator );
  }

  public void commentChar( int ch )
  {
    if( ch >= 0 && ch < _ctype.length )
    {
      _ctype[ch] = CT_COMMENT;
    }
  }

  public void quoteChar( int ch )
  {
    if( ch >= 0 && ch < _ctype.length )
    {
      _ctype[ch] = CT_QUOTE;
    }
  }

  public void charQuoteChar( int ch )
  {
    if( ch >= 0 && ch < _ctype.length )
    {
      _ctype[ch] = CT_CHARQUOTE;
    }
  }

  public void parseNumbers()
  {
    for( int i = '0'; i <= '9'; i++ )
    {
      _ctype[i] |= CT_DIGIT;
    }
  }

  public void eolIsSignificant( boolean bFlag )
  {
    _bEOLIsSignificantP = bFlag;
  }

  public void lowerCaseMode( boolean bLowerCaseMode )
  {
    _bForceLower = bLowerCaseMode;
  }

  public boolean isUnterminatedString()
  {
    return _bUnterminatedString;
  }

  public boolean isUnterminatedComment()
  {
    return _bUnterminatedComment;
  }

  public void setParseDotsAsOperators( boolean parseDotsAsOperators )
  {
    _bParseDotsAsOperators = parseDotsAsOperators;
  }
  public boolean isParseDotsAsOperators()
  {
    return _bParseDotsAsOperators;
  }

  public DocCommentBlock popLastComment()
  {
    DocCommentBlock block = _lastComment;
    _lastComment = null;
    return block;
  }

  public int getTokenStart()
  {
    return _iTokenStart - 1;
  }

  public int getTokenEnd()
  {
    return _iPos - 1;
  }

  private int read() throws IOException
  {
    int c = readOne();
    if( _instructor == null )
    {
      return c;
    }
    return readWithInstructions( c );
  }

  private int readOne() throws IOException
  {
    if( _reader != null )
    {
      int iRet = _reader.read();
      _iPos++;
      _iColumn++;
      return iRet;
    }

    throw new IllegalStateException();
  }

  protected int readWithInstructions( int c ) throws IOException
  {
    _instructor.getInstructionFor( c );
    return c;
  }

  private void pushWhitespaceToken( StringBuilder sbWhitespace )
  {
    if( sbWhitespace.length() == 0 )
    {
      return;
    }

    int iSaveType = _iType;
    String strValueSave = _strValue;
    _strValue = sbWhitespace.toString();
    _iType = ISourceCodeTokenizer.TT_WHITESPACE;
    pushToken();
    _strValue = strValueSave;
    _iType = iSaveType;
  }

  private void pushToken()
  {
    if( _iType == ISourceCodeTokenizer.TT_EOF )
    {
      initEofToken();
      return;
    }

    Token token;
    switch( _iType )
    {
      case '\'':
      case '"':
        token = new StringToken();
        break;

      default:
        token = new Token();
    }
    pushToken( initToken( token ) );
  }

  private void initEofToken()
  {
    _eof = _eof == null ? initToken( new Token() ) : _eof;
  }

  Token initToken( Token token )
  {
    return token.init( _iType,
                       _iInvalidCharPos,
                       getTokenStart(),
                       getTokenEnd(),
                       getTokenColumn(),
                       getLineNumber(),
                       getLineOffset(),
                       isUnterminatedString(),
                       _strValue,
                       _keyword,
                       isAnalyzingSeparately(),
                       isAnalyzingDirective(),
                       getReader(),
                       popLastComment() );
  }

  private void pushToken( Token token )
  {
    if( _iType == ISourceCodeTokenizer.TT_EOF )
    {
      return;
    }

    if( _tokens.size() == 0 || _tokens.peek().getTokenEnd() < token.getTokenEnd() )
    {
      _tokens.push( token );
    }
  }

  public boolean isAnalyzingSeparately()
  {
    return _instructor != null && _instructor.isAnalyzingSeparately();
  }

  public boolean isAnalyzingDirective()
  {
    return _instructor != null && _instructor.isAnalyzingDirective();
  }

  final public Stack<Token> getTokens()
  {
    return _tokens;
  }

  void rip()
  {
    if( _iType == ISourceCodeTokenizer.TT_EOF )
    {
      initEofToken();
      return;
    }

    do
    {
      if( _iType == ISourceCodeTokenizer.TT_EOF )
      {
        _eof = initToken( new Token() );
        return;
      }

      try
      {
        nextToken();
      }
      catch( IOException e )
      {
        return;
      }
    } while( true );
  }

  public int nextToken() throws IOException
  {
    int token = _nextTokenImpl();
    pushToken();
    return token;
  }

  private int _nextTokenImpl() throws IOException
  {
    char _buf[] = new char[20];

    _bUnterminatedComment = false;
    _bUnterminatedString = false;

    _iTokenStart = _iPos;
    _iTokenColumn = _iColumn;

    int ct[] = _ctype;
    int c;
    _strValue = null;
    _keyword = null;
    _iInvalidCharPos = -1;

    if( _iType == ISourceCodeTokenizer.TT_NOTHING )
    {
      c = read();
      if( c >= 0 )    // _iType is surely overwritten below to its correct value.
      {
        _iType = c; // For now we just make sure it isn't ISourceCodeTokenizer.TT_NOTHING
      }
    }
    else
    {
      c = _peekc;
    }

    if( c < 0 )
    {
      return _iType = ISourceCodeTokenizer.TT_EOF;
    }

    if( _iType == ISourceCodeTokenizer.TT_EOF )
    {
      return _iType;
    }

    _iTokenStart = _iPos;
    _iTokenColumn = _iColumn;

    int iCharType = c < 256 ? ct[c] : CT_ALPHA;

    //
    // Handle WHITESPACE
    //
    if( (iCharType & CT_WHITESPACE) != 0 || isAtIgnorePos() )
    {
      StringBuilder sbWhitespace = new StringBuilder();

      while( (iCharType & CT_WHITESPACE) != 0 || isAtIgnorePos() )
      {
        if( c >= 0 )
        {
          sbWhitespace.append( (char)c );
        }

        if( c == '\r' )
        {
          c = read();
          if( c == '\n' )
          {
            if( c >= 0 )
            {
              sbWhitespace.append( (char)c );
            }
            incrementLineNumber();
            c = read();
          }
          if( _bEOLIsSignificantP )
          {
            _peekc = c;
            return _iType = ISourceCodeTokenizer.TT_EOL;
          }
        }
        else
        {
          if( c == '\n' )
          {
            incrementLineNumber();
            if( _bEOLIsSignificantP )
            {
              _peekc = read();
              return _iType = ISourceCodeTokenizer.TT_EOL;
            }
          }
          c = read();
        }
        if( c < 0 )
        {
          pushWhitespaceToken( sbWhitespace );
          _peekc = c;
          return _iType = ISourceCodeTokenizer.TT_EOF;
        }
        iCharType = c < 256 ? ct[c] : CT_ALPHA;
      }

      pushWhitespaceToken( sbWhitespace );
    }

    _iTokenStart = _iPos;
    _iTokenColumn = _iColumn;

    //
    // Handle DIGITS
    //
    if( (iCharType & CT_DIGIT) != 0 )
    {
      StringBuilder v = new StringBuilder();
      boolean hex = false;
      while( true )
      {
        if( '0' <= c && c <= '9' )
        {
          v.append( (char)c );
        }
        else if( !hex && (c == 'b' || c == 'B') )
        {
          if( Character.isJavaIdentifierPart( _reader.peek() ) &&
              Character.isJavaIdentifierPart( _reader.peek( 2 ) ) &&
              v.charAt( 0 ) != '0' )
          {
            // Only consume bd/bi if it is *not* part of a larger word eg., bit,
            // this is mostly for the benefit of the BindingExpression use-case.
            break;
          }

          v.append( (char)c );
          int next = read();
          if( (c == 'b' && next == 'd' ) || (c == 'B' && next == 'D' ) ||
              (c == 'b' && next == 'i' ) || (c == 'B' && next == 'I' ) )
          {
            v.append( (char) next );
            c = read();
            break;
          }

          c = next;
          if( v.length() == 2 && v.charAt( 0 ) == '0' && '0' <= c && c <= '9' )
          {
            v.append( (char)c );
          }
          else
          {
            break;
          }
        }
        else if( (c == 'x' || c == 'X') && v.length() == 1 && v.charAt( 0 ) == '0' )
        {
          hex = true;
          v.append( (char)c );
        }
        else if( hex && ('A' <= c && c <= 'F' || 'a' <= c && c <= 'f') )
        {
          v.append( (char)c );
        }
        else if( c == 'l' || c == 'L' ||
                 c == 'f' || c == 'F' ||
                 c == 'd' || c == 'D'||
                 c == 'r' || c == 'R' )
        {
          if( !Character.isJavaIdentifierPart( _reader.peek() ) )
          {
            // Only consume the number designation char if it is *not* part of a larger word,
            // this is mostly for the benefit of the BindingExpression use-case.
            v.append( (char)c );
            c = read();
          }
          break;
        }
        else
        {
          break;
        }
        c = read();
      }
      _peekc = c;
      _strValue = v.toString();
      _iType = ISourceCodeTokenizer.TT_INTEGER;
      return _iType;
    }

    //
    // Handle ALPHAS
    //
    if( (iCharType & CT_ALPHA) != 0 )
    {
      int iStart = _iPos-1;
      do
      {
        c = read();
        iCharType = c < 0 ? CT_WHITESPACE : c < 256 ? ct[c] : CT_ALPHA;
      }
      while( (iCharType & (CT_ALPHA | CT_DIGIT)) != 0 && !stopOnDot( c ) );

      _peekc = c;
      _strValue = _reader.subsequence( iStart, _iPos-1 ).toString();
      if( _bForceLower )
      {
        _strValue = _strValue.toLowerCase();
      }
      _keyword = isSupportsKeywords() ? Keyword.get( _strValue ) : null;
      return _iType = isReserved() ? ISourceCodeTokenizer.TT_KEYWORD : ISourceCodeTokenizer.TT_WORD;
    }

    //
    // Handle COMMENTS
    //
    if( (iCharType & CT_COMMENT) != 0 )
    {
      while( (c = read()) != '\n' && c != '\r' && c >= 0 )
      {
        // no-op
      }
      _peekc = c;

      if( c == ISourceCodeTokenizer.TT_EOF )
      {
        return _iType = ISourceCodeTokenizer.TT_EOF;
      }
      else
      {
        return nextToken();
      }
    }

    _iTokenStart = _iPos;
    _iTokenColumn = _iColumn;

    //
    // Handle QUOTED strings
    //
    if( (iCharType & CT_QUOTE) != 0 ||
        (iCharType & CT_CHARQUOTE) != 0 )
    {
      _iType = c;
      int i = 0;
      boolean bEscapedScriptlet = false;
      // Invariants (because \Octal needs a lookahead):
      //      (i)  c contains char value
      //      (ii) _peekc contains the lookahead
      _peekc = read();
      while( _peekc >= 0 && _peekc != _iType && _peekc != '\n' && _peekc != '\r' )
      {
        if( _peekc == '\\' )
        {
          c = read();
          int first = c;   // to allow \377, but not \477
          if( c >= '0' && c <= '7' )
          {
            c = c - '0';
            int c2 = read();
            if( '0' <= c2 && c2 <= '7' )
            {
              c = (c << 3) + (c2 - '0');
              c2 = read();
              if( '0' <= c2 && c2 <= '7' && first <= '3' )
              {
                c = (c << 3) + (c2 - '0');
                _peekc = read();
              }
              else
              {
                _peekc = c2;
              }
            }
            else
            {
              _peekc = c2;
            }
          }
          else
          {
            boolean bValidChar = true;
            switch( c )
            {
              case'a':
                c = 0x7;
                break;
              case'b':
                c = '\b';
                break;
              case'f':
                c = 0xC;
                break;
              case'n':
                c = '\n';
                break;
              case'r':
                c = '\r';
                break;
              case't':
                c = '\t';
                break;
              case'v':
                c = 0xB;
                break;
              case '\"':
              case '\'':
              case '\\':
                break;
              case 'u':
              {
                // try to parse 4 hex digits in the UTF-16 range \u0000 - \uffff
                c = read();
                if( isHexDigit( c ) )
                {
                  StringBuilder sb = new StringBuilder();
                  sb.append( (char)c );
                  c = read();
                  if( isHexDigit( c ) )
                  {
                    sb.append( (char)c );
                    c = read();
                    if( isHexDigit( c ) )
                    {
                      sb.append( (char)c );
                      c = read();
                      if( isHexDigit( c ) )
                      {
                        sb.append( (char)c );
                        c = Integer.parseInt( sb.toString(), 16 );
                      }
                      else
                      {
                        bValidChar = false;
                        _peekc = c;
                      }
                    }
                    else
                    {
                      bValidChar = false;
                      _peekc = c;
                    }
                  }
                  else
                  {
                    bValidChar = false;
                    _peekc = c;
                  }
                }
                else
                {
                  bValidChar = false;
                  _peekc = c;
                }
                break;
              }
              case'<':
                //## For escaping string template scripts
                bEscapedScriptlet = true;
                c = TemplateGenerator.ESCAPED_SCRIPTLET_BEGIN_CHAR;
                break;
              case'$':
                //## For escaping string template scripts
                bEscapedScriptlet = true;
                c = TemplateGenerator.ESCAPED_ALTERNATE_EXPRESSION_BEGIN_CHAR;
                break;

              default:
                if( !Character.isWhitespace( c ) )
                {
                  // Illegal escape char
                  bValidChar = false;
                   _peekc = read();
                }
                else if( c == '\n' )
                {
                  incrementLineNumber();
                }
                break;
            }
            if( bValidChar )
            {
              _peekc = read();
            }
            else
            {
              if( _iInvalidCharPos < 0 )
              {
                _iInvalidCharPos = i;
              }
              c = -1;
            }
          }
        }
        else
        {
          c = _peekc;
          _peekc = read();
          if( (c == TemplateGenerator.SCRIPTLET_BEGIN.charAt(0) &&
                  _peekc == TemplateGenerator.SCRIPTLET_BEGIN.charAt(1)) ||
                  (c == TemplateGenerator.ALTERNATE_EXPRESSION_BEGIN.charAt(0) &&
                  _peekc == TemplateGenerator.ALTERNATE_EXPRESSION_BEGIN.charAt(1)) )
          {
            boolean alternateStyle = (c == TemplateGenerator.ALTERNATE_EXPRESSION_BEGIN.charAt(0) &&
                  _peekc == TemplateGenerator.ALTERNATE_EXPRESSION_BEGIN.charAt(1));
            int numOpenCurlies = 0;
            while( _peekc >= 0 && _peekc != '\n' && _peekc != '\r' &&
                    !(!alternateStyle && c == TemplateGenerator.SCRIPTLET_END.charAt(0) &&
                      _peekc == TemplateGenerator.SCRIPTLET_END.charAt(1)) &&
                    !(alternateStyle && _peekc == TemplateGenerator.ALTERNATE_EXPRESSION_END.charAt(0) && numOpenCurlies <= 1) )
            {
              if(_peekc == '{') {
                numOpenCurlies++;
              } else if (_peekc == '}') {
                numOpenCurlies--;
              }
              if( i >= _buf.length )
              {
                char nb[] = new char[_buf.length * 2];
                System.arraycopy( _buf, 0, nb, 0, _buf.length );
                _buf = nb;
              }
              _buf[i++] = (char)c;
              c = _peekc;
              _peekc = read();
            }
          }
        }

        if( i >= _buf.length )
        {
          char nb[] = new char[_buf.length * 2];
          System.arraycopy( _buf, 0, nb, 0, _buf.length );
          _buf = nb;
        }
        _buf[i++] = (char)c;
      }
      if( _peekc == _iType )  // Keep \n or \r intact in _peekc
      {
        _peekc = read();
      }
      else
      {
        _bUnterminatedString = true;
      }
      _strValue = String.copyValueOf( _buf, 0, i );
      if( bEscapedScriptlet )
      {
        _strValue = TemplateGenerator.ESCAPED_SCRIPTLET_MARKER + _strValue;
      }
      return _iType;
    }

    //
    // Handle # pragma
    //
    if( _iPos == 1 && c == '#' )
    {
      int iCommnetPos = _iPos - 1;
      while( (c = read()) != '\n' && c != '\r' && c >= 0 )
      {
        //no-op
      }
      _peekc = c;
      String strSaveValue = _strValue;
      _strValue = _reader.getSource().substring( iCommnetPos, _iPos-1 );
      int iSaveType = _iType;
      _iType = ISourceCodeTokenizer.TT_COMMENT;
      pushToken();
      _iType = iSaveType;
      _strValue = strSaveValue;

      if( c == ISourceCodeTokenizer.TT_EOF )
      {
        return _iType = ISourceCodeTokenizer.TT_EOF;
      }
      else
      {
        return ISourceCodeTokenizer.TT_COMMENT;
      }
    }

    //
    // Handle SLASH signalled COMMENTS
    //
    boolean bSlashConsumed = false;
    int srcLen = _reader.getSource().length();

    if( c == '/' )
    {
      int iCommnetPos = _iPos - 1;
      c = read();
      if( c == '*' )
      {
        _lastComment = new DocCommentBlock();
        consumeBlockComment();
        String strSaveValue = _strValue;
        int endIndex =  Math.min( srcLen, _iPos-1 );
        _strValue = _reader.getSource().substring( iCommnetPos, endIndex );
        _lastComment.setRawComment( _strValue );
        int iSaveType = _iType;
        _iType = ISourceCodeTokenizer.TT_COMMENT;
        pushToken();
        _iType = iSaveType;
        _strValue = strSaveValue;

        if( _bUnterminatedComment )
        {
          return _iType = ISourceCodeTokenizer.TT_EOF;
        }
        else
        {
          return nextToken();
        }
      }
      else if( c == '/' )
      {
        while( (c = read()) != '\n' && c != '\r' && c >= 0 )
        {
          //no-op
        }
        _peekc = c;
        String strSaveValue = _strValue;
        _strValue = _reader.getSource().substring( iCommnetPos, _iPos-1 );
        int iSaveType = _iType;
        _iType = ISourceCodeTokenizer.TT_COMMENT;
        pushToken();
        _iType = iSaveType;
        _strValue = strSaveValue;

        if( c == ISourceCodeTokenizer.TT_EOF )
        {
          return _iType = ISourceCodeTokenizer.TT_EOF;
        }
        else
        {
          return ISourceCodeTokenizer.TT_COMMENT;
        }
      }
      else
      {
        if( (iCharType & CT_OPERATOR) == 0 )
        {
          _peekc = c;
          return _iType = '/';
        }
        else
        {
          bSlashConsumed = true;
        }
      }
    }

    if( !bSlashConsumed )
    {
      _iTokenStart = _iPos;
      _iTokenColumn = _iColumn;
    }

    //
    // Handle OPERATORS
    //
    if( (iCharType & CT_OPERATOR) != 0 || (iCharType & CT_BITSHIFT_OPERATOR) != 0 )
    {
      int i = 0;
      while( true )
      {
        if( i >= _buf.length )
        {
          char nb[] = new char[_buf.length * 2];
          System.arraycopy( _buf, 0, nb, 0, _buf.length );
          _buf = nb;
        }
        _buf[i++] = bSlashConsumed ? '/' : (char)c;
        String strOpTest = new String( _buf, 0, i );
        if( !isOperator( strOpTest ) && !isLeftOpenIntervalOp( strOpTest ) )
        {
          i--;
          break;
        }
        c = bSlashConsumed ? c : read();
        //iCharType = c < 0 ? CT_WHITESPACE : c < 256 ? ct[c] : CT_ALPHA;

        bSlashConsumed = false;
      }

      _peekc = c;
      _strValue = String.copyValueOf( _buf, 0, i );
      if( _strValue.equals( "." ) )
      {
        return _iType = '.';
      }

      return _iType = ISourceCodeTokenizer.TT_OPERATOR;
    }

    _iTokenStart = _iPos;
    _iTokenColumn = _iColumn;

    _iType = c;
    _peekc = read();
    return _iType = c;
  }

  private void consumeBlockComment() throws IOException
  {
    //consume *
    int c = read();
    int prev = -1;
    while( c >= 0 && !(prev == '*' && c =='/') )
    {
      if( prev == '/' && c == '*' )
      {
        consumeBlockComment();
        prev = -1;
        c = _peekc;
      }
      else
      {
        if( c == '\n' )
        {
          incrementLineNumber();
        }
        prev = c;
        c = read();
      }
    }
    _bUnterminatedComment = c == ISourceCodeTokenizer.TT_EOF;
    if( !_bUnterminatedComment )
    {
      _peekc = read();
    }
  }

  private boolean isReserved() {
    return isSupportsKeywords() && Keyword.isKeyword( _strValue );
  }

  private boolean isAtIgnorePos()
  {
    return _instructor != null && _instructor.isAtIgnoredPos();
  }

  private boolean isHexDigit( int c )
  {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
  }

  private boolean isLeftOpenIntervalOp( String strOpTest )
  {
    return strOpTest.equals( "|." ) && _reader.peek() == '.';
  }

  private boolean stopOnDot( int c )
  {
    return _bParseDotsAsOperators && c == '.';
  }

  @Override
  public String toString()
  {
    String ret;
    switch( _iType )
    {
      case ISourceCodeTokenizer.TT_EOF:
        ret = "EOF";
        break;

      case ISourceCodeTokenizer.TT_EOL:
        ret = "EOL";
        break;

      case ISourceCodeTokenizer.TT_NOTHING:
        ret = "NOTHING";
        break;

      default:
      {
        ret = getTokens().peek().getText();
        break;
      }
    }

    return "Token[" + ret + "], line " + getLineNumber();
  }

  public int getType()
  {
    return _iType;
  }

  public boolean isEOF()
  {
    return _iType == ISourceCodeTokenizer.TT_EOF;
  }
  public boolean isNOTHING()
  {
    return _iType == ISourceCodeTokenizer.TT_NOTHING;
  }

  void goToPosition( int iOffset ) throws IOException
  {
    if( getTokenStart() > iOffset )
    {
      reset( _reader, true );
      goToPosition( iOffset );
    }
    else
    {
      while( getTokenStart() < iOffset )
      {
        popLastComment();
        nextToken();
        if( isEOF() || isNOTHING() )
        {
          throw new IOException( "Unexpected EOF" );
        }
      }
    }
  }

  void setTokens( Stack<Token> tokens )
  {
    _tokens = tokens;
  }

  public Token getEofToken()
  {
    return _eof;
  }

  public boolean isSupportsKeywords()
  {
    return _supportsKeywords;
  }
  public void setSupportsKeywords( boolean supportsKeywords )
  {
    _supportsKeywords = supportsKeywords;
  }
}
