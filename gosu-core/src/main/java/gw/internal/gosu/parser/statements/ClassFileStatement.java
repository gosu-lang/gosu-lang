/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.PositionToken;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.Token;
import gw.lang.parser.IParseTree;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.ITerminalStatement;

import java.util.List;

/**
 */
public class ClassFileStatement extends Statement implements IClassFileStatement
{
  public ClassStatement getClassStatement()
  {
    for( IParseTree parseTree : getLocation().getChildren() )
    {
      if( parseTree.getParsedElement() instanceof ClassStatement )
      {
        return (ClassStatement)parseTree.getParsedElement();
      }
    }
    return null;
  }

  public IGosuClassInternal getGosuClass()
  {
    if(getLocation() == null) {
      return null;
    }
    if(getLocation().getScriptPartId() == null) {
      return null;
    }
    return (IGosuClassInternal)getLocation().getScriptPartId().getContainingType();
  }
  
  public Object execute()
  {
    return null;
  }

  @Override
  public String toString()
  {
    IGosuClassInternal gosuClass = getGosuClass();
    return gosuClass == null ? "no class" : gosuClass.toString();
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public void assignTokens( List<Token> tokens )
  {
    super.assignTokens( tokens );

    // Add remaining tokens e.g., trailing whitespace, trailing commnets
    for( int i = 0; i < tokens.size(); i++ )
    {
      IToken token = tokens.get( i );
      if( token.getType() != ISourceCodeTokenizer.TT_EOF && !(token instanceof PositionToken) )
      {
        addToken( token, getLocation().getLastChild() );
      }
      tokens.remove( i-- );
    }
  }
}
