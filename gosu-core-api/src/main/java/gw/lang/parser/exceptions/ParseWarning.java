/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.IParserState;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.reflect.IType;

public class ParseWarning extends ParseIssue implements IWarningSuppressor
{
  public ParseWarning( IParserState state, ResourceKey msgKey, Object... msgArgs )
  {
    super( state, msgKey, msgArgs );
  }

  public ParseWarning( ParseWarning e )
  {
    super( e.getLineNumber(), e.getLineOffset(), e.getTokenColumn(), e.getTokenStart(), e.getTokenEnd(),
           e.getSymbolTable(), e.getMessageKey(), e.getMessageArgs() );
    setSource( e.getSource() );
  }

  public ParseWarning( Integer lineNumber, Integer lineOffset, Integer tokenColumn, Integer tokenStart, Integer tokenEnd, ISymbolTable symbolTable, ResourceKey key, Object... msgArgs )
  {
    super( lineNumber, lineOffset, tokenColumn, tokenStart, tokenEnd, symbolTable, key, msgArgs );
  }

  public IType getExpectedType()
  {
    return null;
  }

  public void setExpectedType( IType argType )
  {
    throw new UnsupportedOperationException( "Parse warnings to do not maintain 'expected types'" );
  }

  public boolean isDeprecationWarning()
  {
    return getMessageKey().equals( Res.MSG_DEPRECATED_MEMBER );
  }

  @Override
  public boolean isSuppressed( String warningCode )
  {
    if( "all".equals( warningCode ) )
    {
      return true;
    }

    if( "deprecation".equals( warningCode ) && isDeprecationWarning() )
    {
      return true;
    }

    return false;
  }
}
