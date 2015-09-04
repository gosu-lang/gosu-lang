/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.internal.gosu.parser.SourceCodeTokenizer;
import gw.internal.gosu.parser.Token;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.SourceCodeReader;
import gw.util.Stack;

/**
 * An ITokenizerInstructor for Gosu templates. Implemented as a finite state
 * machine where states correspond to template directives.
 */
public class TemplateTokenizerInstructor implements ITokenizerInstructor
{
  static final int IGNORE = 0;
  static final int IGNORE_COMMENT = 50;
  static final int COMMENT_END_PENDING = 75;
  static final int COMMENT_END_PENDING2 = 76;

  static final int ANALYZE_START_PENDING = 100;
  static final int ANALYZE_PENDING = 200;
  static final int ANALYZE_SEPARATELY_PENDING = 300;
  static final int ANALYZE_DIRECTIVE_PENDING = 350;

  static final int ANALYZE = 400;
  static final int ANALYZE_SEPARATELY = 500;
  static final int ANALYZE_DIRECTIVE = 550;

  static final int ANALYZE_END_PENDING = 600;
  static final int ANALYZE_SEPARATELY_END_PENDING = 700;
  static final int ANALYZE_DIRECTIVE_END_PENDING = 750;

  static final String[] DELIMITERS =
  {
    TemplateGenerator.SCRIPTLET_BEGIN,
    TemplateGenerator.SCRIPTLET_BEGIN + TemplateGenerator.EXPRESSION_SUFFIX,
    TemplateGenerator.SCRIPTLET_BEGIN + TemplateGenerator.DIRECTIVE_SUFFIX,
    TemplateGenerator.SCRIPTLET_BEGIN + TemplateGenerator.DECLARATION_SUFFIX,
    TemplateGenerator.SCRIPTLET_END,
    TemplateGenerator.ALTERNATE_EXPRESSION_BEGIN,
    TemplateGenerator.ALTERNATE_EXPRESSION_END,
    TemplateGenerator.COMMENT_BEGIN,
    TemplateGenerator.COMMENT_END,
  };

  ISourceCodeTokenizer _tokenizer;
  int _iState;
  boolean _bAltTag;
  boolean _bTag;
  boolean _bEndTagPending;
  boolean _bStartTagBuffer;
  int _iLines;
  StringBuffer _sbStartTag;
  char _lastC;


  public TemplateTokenizerInstructor(ISourceCodeTokenizer tokenizer)
  {
    _tokenizer = tokenizer;

    _iState = IGNORE;
    _sbStartTag = new StringBuffer();
  }

  /**
   * Reset state
   */
  public void reset()
  {
    _iState = IGNORE;
    _sbStartTag = new StringBuffer();
    _bTag = false;
    _bEndTagPending = false;
    _bStartTagBuffer = false;
    _iLines = 0;
    _bAltTag = false;
  }

  public ITokenizerInstructor createNewInstance(ISourceCodeTokenizer tokenizer) {
    return new TemplateTokenizerInstructor( tokenizer );
  }

  @Override
  public boolean isAtIgnoredPos()
  {
    boolean bStartPending = true;
    switch( getState() )
    {
      case IGNORE:
      case IGNORE_COMMENT:
        return true;

      case COMMENT_END_PENDING:
      case COMMENT_END_PENDING2:
      case ANALYZE_END_PENDING:
      case ANALYZE_SEPARATELY_END_PENDING:
      case ANALYZE_DIRECTIVE_END_PENDING:
        bStartPending = false;
        // fall through...

      case ANALYZE_START_PENDING:
      case ANALYZE_PENDING:
      case ANALYZE_SEPARATELY_PENDING:
      case ANALYZE_DIRECTIVE_PENDING:
      {
        SourceCodeReader reader = _tokenizer.getReader();
        String strSource = _tokenizer.getSource();
        for( String delim : DELIMITERS )
        {
          int iLen = delim.length();
          for( int i = 0; i < iLen; i++ )
          {
            int iOffset = reader.getPosition() - i - 1;
            if( strSource.length()-i >= iLen )
            {
              if( reader.getSource().startsWith( delim, iOffset ) )
              {
                return true;
              }
            }
          }
        }
        // The pending state will ultimately revert back to either ignore or analyze,
        // thus if we're currently in an End Pending state we'll continue to analyze,
        // otherwise, if we're in a Start Pending state we'll continue to ignore.
        return bStartPending;
      }
    }
    return false;
  }

  @Override
  public boolean isAnalyzingDirective()
  {
    switch( getState() )
    {
      case ANALYZE_DIRECTIVE:
      case ANALYZE_DIRECTIVE_END_PENDING:
        return true;
    }
    return false;
  }

  @Override
  public boolean isAnalyzingSeparately()
  {
    switch( getState() )
    {
      case ANALYZE_SEPARATELY:
      case ANALYZE_SEPARATELY_END_PENDING:
        return true;
    }
    return false;
  }

  /**
   */
  public void getInstructionFor( int iC )
  {
    char c = (char)iC;
    char lastC = _lastC;
    _lastC = c;

    countLines( c );

    switch( getState() )
    {
      case IGNORE:
      {
        if( c == '<' && lastC != '\\' )
        {
          setState( ANALYZE_START_PENDING );
          startTagBuffer();
        }
        else if( c == '$' && lastC != '\\' )
        {
          setState( ANALYZE_START_PENDING );
          startTagBuffer();
          _bAltTag = true;
        }
        break;
      }

      case ANALYZE_START_PENDING:
      {
        if( !_bAltTag && c == '%' )
        {
          setState( ANALYZE_PENDING );
        }
        else if( _bAltTag && c == '{' )
        {
          setState( ANALYZE_SEPARATELY_PENDING );
        }
        else
        {
          _bAltTag = false;
          setState( IGNORE );
        }
        break;
      }

      case ANALYZE_PENDING:
      {
        if( c == '=' )
        {
          setState( ANALYZE_SEPARATELY_PENDING );
          break;
        }
        else if( c == '!' )
        {
          break;
        }
        else if( c == '-' ) // <%-- Comment
        {
          if( lastC == '-' )
          {
            setState( IGNORE_COMMENT );
            break;
          }
          setState( ANALYZE_PENDING );
          break;
        }
        else if( c == '@' ) // <%@ Page Directive
        {
          setState( ANALYZE_DIRECTIVE_PENDING );
          break;
        }
        setState( ANALYZE );
        break;
      }

      case ANALYZE_SEPARATELY_PENDING:
      {
        setState( ANALYZE_SEPARATELY );
        if( !_bAltTag && c == '%' )
        {
          setState( ANALYZE_SEPARATELY_END_PENDING );
          break;
        }
        else if( _bAltTag && c == '}' )
        {
          if( isAnalyzingSeparatelyWaitingForCloseBrace() )
          {
            setState( ANALYZE_SEPARATELY_END_PENDING );
            break;
          }
        }
        break;
      }
      
      case ANALYZE_DIRECTIVE_PENDING:
      {
        setState( ANALYZE_DIRECTIVE );
        if( c == '%' )
        {
          setState( ANALYZE_DIRECTIVE_END_PENDING );
          break;
        }
        break;
      }

      case IGNORE_COMMENT:
      {
        if( c == '-' && lastC == '-' )
        {
          setState( COMMENT_END_PENDING );
        }
        break;
      }

      case COMMENT_END_PENDING:
      {
        if( c == '%' )
        {
          setState( COMMENT_END_PENDING2 );
          break;
        }
        setState( IGNORE_COMMENT );
        break;
      }
      case COMMENT_END_PENDING2:
      {
        if( c == '>' )
        {
          setState( IGNORE );
          break;
        }
        setState( IGNORE_COMMENT );
        break;
      }

      case ANALYZE:
      {
        if( c == '%' )
        {
          setState( ANALYZE_END_PENDING );
          break;
        }
        break;
      }

      case ANALYZE_SEPARATELY:
      {
        if( !_bAltTag && c == '%' )
        {
          setState( ANALYZE_SEPARATELY_END_PENDING );
          break;
        }
        else if( _bAltTag && c == '}' )
        {
          if( isAnalyzingSeparatelyWaitingForCloseBrace() )
          {
            setState( ANALYZE_SEPARATELY_END_PENDING );
            break;
          }
        }
        break;
      }
      
      case ANALYZE_DIRECTIVE:
      {
        if( c == '%' )
        {
          setState( ANALYZE_DIRECTIVE_END_PENDING );
          break;
        }
        break;
      }
      
      case ANALYZE_END_PENDING:
      {
        if( c == '>' )
        {
          setState( IGNORE );
          break;
        }
        setState( ANALYZE );
        break;
      }

      case ANALYZE_SEPARATELY_END_PENDING:
      {
        if( _bAltTag )
        {
          _bAltTag = false;
          if( c == '<' )
          {
            setState( ANALYZE_START_PENDING );
            startTagBuffer();
          }
          else if( c == '$' )
          {
            setState( ANALYZE_START_PENDING );
            startTagBuffer();
            _bAltTag = true;
          }
          else
          {
            setState( IGNORE );
          }
          break;
        }
        else if( c == '>' )
        {
          setState( IGNORE );
          break;
        }
        setState( ANALYZE_SEPARATELY );
        break;
      }
      
      case ANALYZE_DIRECTIVE_END_PENDING:
      {
        if( c == '>' )
        {
          setState( IGNORE );
          break;
        }
        setState( ANALYZE_DIRECTIVE );
        break;
      }

      default:
        throw new RuntimeException( "Bad template tokenizer instructor state" );
    }
  }

  private boolean isAnalyzingSeparatelyWaitingForCloseBrace()
  {
    //noinspection unchecked
    Stack<Token> tokens = (Stack<Token>)_tokenizer.getTokens();
    if( tokens.isEmpty() )
    {
      return true;
    }

    int iStmtBlock = ((SourceCodeTokenizer)_tokenizer).getInternal().getType() == '}' ? 1 : 0;
    for( int i = tokens.size()-1; i >= 0; i-- )
    {
      IToken token = tokens.get( i );
      if( token.getType() == '}' )
      {
        iStmtBlock++;
      }
      if( token.getType() == '{' )
      {
        if( iStmtBlock == 0 )
        {
          if( token.isAnalyzingSeparately() )
          {
            return false;
          }
        }
        else
        {
          iStmtBlock--;
        }
      }

    }
    return true;
  }

  private void setState( int iState )
  {
    _iState = iState;
  }

  private int getState()
  {
    return _iState;
  }

  private void countLines( char c )
  {
    if( c == '\n' )
    {
      _iLines++;
    }
  }

  private void startTagBuffer()
  {
    _bStartTagBuffer = true;
  }

  @Override
  public void setTokenizer( ISourceCodeTokenizer tokenizer )
  {
    _tokenizer = tokenizer;
  }
}

