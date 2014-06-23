/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/**
 */
package gw.internal.gosu.parser;

import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.IToken;

public class StandardParserState extends LightweightParserState implements IFullParserState
{
  private ISymbolTable _symTable;
  private IToken _token;
  private boolean _bKeepSymbolTableInIssues;

  public StandardParserState( ISymbolTable symTable, SourceCodeTokenizer tokenizer, int offsetShift, int lineShift, boolean bKeepSymbolTableInIssues )
  {
    _symTable = symTable;
    if( tokenizer != null )
    {
      // Restore state so that token positions are accurate for point of error
      _token = tokenizer.getPriorToken().copy();
    }
    init( tokenizer.getSource(),
          _token.getLine() + lineShift,
          _token.getLineOffset(),
          _token.getTokenColumn(),
          _token.getTokenStart() + offsetShift,
          _token.getTokenEnd() + offsetShift );
    _bKeepSymbolTableInIssues = bKeepSymbolTableInIssues;
  }

  public StandardParserState( IParsedElement elt, String src, boolean bKeepSymbolTableInIssues )
  {
    _symTable = null;
    init( src, elt.getLineNum(), 1, elt.getColumn(), elt.getLocation().getOffset(), elt.getLocation().getExtent() );
    _bKeepSymbolTableInIssues = bKeepSymbolTableInIssues;
  }

  public ISymbolTable getSymbolTable()
  {
    return _symTable;
  }

  public void setSymbolTable( ISymbolTable table )
  {
    _symTable = table;
  }


  public void collapseToken()
  {
    if( _token != null )
    {
      /**
       * Move the token start value to the end of the token so as to make the token
       * width zero. This is only useful (so far) for the case where the parser needs
       * to move the error position after the the current token, but before advancing
       * to the next token.
       */
      _token.collapse();
    }
  }

  public boolean isKeepSymbolTableInIssues()
  {
    return _bKeepSymbolTableInIssues;
  }

  public void setKeepSymbolTableInIssues( boolean keepSymbolTableInIssues )
  {
    _bKeepSymbolTableInIssues = keepSymbolTableInIssues;
  }
}
