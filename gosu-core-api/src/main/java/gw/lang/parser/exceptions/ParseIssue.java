/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IProgram;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParserState;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.IParserPart;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.IType;
import gw.config.CommonServices;

public abstract class ParseIssue extends Exception implements IParseIssue
{
  private static final String SOURCE_DELIMITER = "\nat line ";
  private static final int CONTEXT_LINES = 3;

  private Integer _lineNumber;
  private Integer _lineOffset;
  private Integer _tokenColumn;
  private Integer _tokenStart;
  private Integer _tokenEnd;
  private ResourceKey _messageKey;
  private Object[] _messageArgs;

  private ISymbolTable _symbolTable;
  private IParsedElement _parentElement;
  private String _stateSource;
  private IGosuClass _parentClass;

  private void debug() {
//    if (toString().contains("PXLOGGER")) {
//      int uuuu = 0;
//    }
  }

  protected ParseIssue( IParserState parserState, ResourceKey key, Object... msgArgs )
  {
    super( "" );
    _messageKey = key;
    _messageArgs = normalizeMessageArgs(msgArgs);
    initFieldsFromParserState( parserState );
    debug();
  }

  protected ParseIssue( Integer lineNumber, Integer lineOffset, Integer tokenColumn,
                                Integer tokenStart, Integer tokenEnd,
                                ISymbolTable symbolTable, ResourceKey key, Object... msgArgs )
  {
    super( "" );
    _messageKey = key;
    _messageArgs = normalizeMessageArgs(msgArgs);
    _symbolTable = symbolTable;
    _lineNumber = lineNumber;
    _lineOffset = lineOffset == null ? 1 : Math.max( 1, lineOffset );
    _tokenColumn = tokenColumn;
    _tokenStart = tokenStart;
    _tokenEnd = tokenEnd;
    debug();
  }

  protected ParseIssue( IParserState state, Throwable t )
  {
    super( t );
    initFieldsFromParserState( state );
    debug();
  }

  /**
   * Don't fill in stack trace since parse issues are not really "exceptional"
   * in terms of the parser's Java implementation; we don't care much about the
   * Java stack trace when these are thrown. Rather parse issues provide a means
   * to tag parsed elements with issues discovered during parsing, such as
   * syntax warnings and errors. Hence, the ParseIssue interface.
   * <p/>
   * Note this method is otherwise very costly from a performance standpoint.
   * <p/>
   * todo: consider extracting the bulk of this class into a separate non-exception class and reference that class here for when we need to use it as a real exception
   */
  public Throwable fillInStackTrace()
  {
    return this;
  }

  private void initFieldsFromParserState( IParserState parserState )
  {
    if( parserState != null )
    {
      if( parserState instanceof IFullParserState )
      {
        IFullParserState fullParserState = (IFullParserState)parserState;
        _symbolTable = fullParserState.getSymbolTable();
      }
      _lineNumber = parserState.getLineNumber();
      _lineOffset = Math.max( 1, parserState.getLineOffset() );
      _tokenColumn = parserState.getTokenColumn();
      _tokenStart = parserState.getTokenStart();
      _tokenEnd = parserState.getTokenEnd();
      // Copy the value because the source is a string built from a StringBuffer which will have the whole array allocated.
      String parserSource = parserState.getSource();
      setStateSource( parserSource );
    }
  }

  /**
   * Normalize all non string & non number args to string types to prevent
   * race conditions wrt/ the TypeSystem lock when the message is formatted.
   */
  private Object [] normalizeMessageArgs(Object [] args) {
    Object [] result = args;
    if(args != null && args.length > 0) {
      result = new Object[args.length];
      for(int i = 0; i < args.length; i++) {
        if(args[i] == null || args[i] instanceof CharSequence || args[i] instanceof Number) {
          result[i] = args[i];
        } else {
          result[i] = args[i].toString();
        }
      }
    }
    return result;
  }

  protected static String formatError( ResourceKey key, Object... msgArgs )
  {
    if( key != null )
    {
      return CommonServices.getGosuLocalizationService().localize( key, msgArgs );
    }
    else
    {
      return "";
    }
  }

  public Integer getLineNumber()
  {
    return _lineNumber;
  }

  public Integer getLineOffset()
  {
    return _lineOffset;
  }

  public void addLineOffset( int offset )
  {
    _lineOffset += offset;
  }

  public Integer getTokenColumn()
  {
    return _tokenColumn;
  }

  public Integer getTokenEnd()
  {
    return _tokenEnd;
  }

  public Integer getTokenStart()
  {
    return _tokenStart;
  }

  public String getContextString()
  {
    if( getStateSource() != null )
    {
      return makeContextString( _lineNumber - _lineOffset + 1, getStateSource(), getLineReportingOffset() );
    }
    else
    {
      return null;
    }
  }
  
  public String getContextStringNoLineNumbers() {
    if( getStateSource() != null )
    {
      return makeContextString( _lineNumber - _lineOffset + 1, getStateSource(), getLineReportingOffset(), false );
    }
    else
    {
      return null;
    }
  }

  public String getStateSource()
  {
    if( _stateSource == null )
    {
      return _parentClass != null ? _parentClass.getSourceFileHandle().getSource().getSource() : null;
    }
    return _stateSource;
  }

  public void setStateSource( String parserSource )
  {
    _stateSource = parserSource;
  }

  private String getMyMessage() {
    if (_messageKey != null) {
      return formatError(_messageKey, _messageArgs);
    } else if (getCause() != null){
      return getCause().getMessage();
    } else {
      return "";
    }
  }

  public String getPlainMessage()
  {
    return getMyMessage();
  }

  public String getConsoleMessage()
  {
    StringBuilder retVal = new StringBuilder();
    retVal.append( getMyMessage() );
    if( _lineNumber != null )
    {
      retVal.append( " [line:" ).append( _lineNumber - _lineOffset - getLineReportingOffset() + 1 ).append( " col:" ).append( _tokenColumn );
      retVal.append( "]" );

      if( getStateSource() != null )
      {
        retVal.append( " in\n" );
        retVal.append( getContextString() );
      }
    }
    return (retVal.toString());

  }

  public static String makeContextString( int lineOfError, String source, int lineReportingOffset )
  {
    return makeContextString(lineOfError, source, lineReportingOffset, true);
  }
  
  private static String makeContextString( int lineOfError, String source, int lineReportingOffset, boolean showLineNumbers )
  {
    int offset = (CONTEXT_LINES - 1) / 2;
    int minLine = Math.max( lineOfError - offset, 1 );
    int maxLine = lineOfError + offset;

    int padding = maxLine <= 0 ? 1 : ((int)Math.log10( maxLine ) + 1);

    StringBuilder sb = new StringBuilder();
    int pos = 0;
    int currentLine = 1;
    while( pos < source.length() && currentLine < minLine )
    {
      char c = source.charAt( pos );
      if( c == '\n' )
      {
        ++currentLine;
      }
      ++pos;
    }

    if( showLineNumbers )
    {
      sb.append( String.format( "line %1$" + padding + "s: ", currentLine - lineReportingOffset ) );
    }

    while( pos < source.length() && currentLine <= maxLine )
    {
      char c = source.charAt( pos );
      if( c == '\n' )
      {
        ++currentLine;
        if( currentLine <= maxLine )
        {
          sb.append( '\n' );
          if( showLineNumbers )
          {
            sb.append( String.format( "line %1$" + padding + "s: ", currentLine - lineReportingOffset ) );
          }
        }
      }
      else
      {
        sb.append( c );
      }
      ++pos;
    }
    return sb.toString();
  }

  public String getUIMessage()
  {
    String strMessage = getMyMessage();
    if( strMessage != null )
    {
      int iIndex = strMessage.indexOf( SOURCE_DELIMITER );
      if( iIndex >= 0 )
      {
        strMessage = strMessage.substring( 0, iIndex );
      }
    }
    return strMessage;
  }

  public int getLine()
  {
    Integer lineNumber = getLineNumber();
    return lineNumber == null ? -1 : lineNumber;
  }

  public int getColumn()
  {
    return getTokenColumn();
  }

  /**
   * Warning:  Only valid if called from the parser thread.  Otherwise we null it out.
   */
  public IParsedElement getSource()
  {
    return _parentElement;
  }

  public void setSource( IParsedElement sourceOfError )
  {
    _parentElement = sourceOfError;
  }

  /**
   * Warning:  Only valid if called from the parser thread.  Otherwise we null it out.
   */
  public ISymbolTable getSymbolTable()
  {
    return _symbolTable;
  }

  public boolean appliesToPosition( int iPos )
  {
    Integer tokenStart = getTokenStart();
    Integer tokenEnd = getTokenEnd();

    return iPos >= tokenStart && iPos <= tokenEnd;
  }

  public ResourceKey getMessageKey()
  {
    return _messageKey;
  }

  public void resolve( IParserPart parserBase )
  {
    if( parserBase != null )
    {
      IScriptPartId scriptPart = parserBase.getOwner().getScriptPart();
      IType parentType = scriptPart == null ? null : scriptPart.getContainingType();
      if( parentType instanceof IGosuClass )
      {
        _parentClass = (IGosuClass)parentType;
        _stateSource = null;
      }
    }
    if( _parentElement != null )
    {
      resetPositions();
      if( parserBase != null && !parserBase.getOwner().isEditorParser() )
      {
        _parentElement = null;
        _symbolTable = null;
      }
    }
  }

  public void resetPositions()
  {
    if( _parentElement == null )
    {
      return;
    }

    boolean bForce = _parentElement instanceof IExpression && !(_parentElement instanceof IProgram);

    if( bForce || _lineNumber == null )
    {
      _lineNumber = _parentElement.getLineNum();
    }
    if( bForce || _lineOffset == null )
    {
      _lineOffset = _parentElement.getLocation().getOffset() - _parentElement.getLocation().getColumn();
    }
    if( bForce || _tokenColumn == null )
    {
      _tokenColumn = _parentElement.getColumn();
    }
    if( bForce || _tokenStart == null )
    {
      _tokenStart = _parentElement.getLocation().getOffset();
    }
    if( bForce || _tokenEnd == null )
    {
      _tokenEnd = _parentElement.getLocation().getExtent() + 1;
    }
  }

  public void adjustOffset(int offset, int lineNumOffset, int columnOffset) {
    _tokenStart += offset;
    _tokenEnd += offset;
    _lineNumber += lineNumOffset;
    _tokenColumn += columnOffset;
  }

  public void setMessage( ResourceKey key, Object... args )
  {
    _messageKey = key;
    _messageArgs = normalizeMessageArgs(args);
  }

  public Object[] getMessageArgs()
  {
    return _messageArgs;
  }

  public int getLineReportingOffset()
  {
    return 0;
  }

  public IType getExpectedType()
  {
    return null;
  }
}
