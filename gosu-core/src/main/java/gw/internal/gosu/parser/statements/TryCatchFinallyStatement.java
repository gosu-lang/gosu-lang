/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.statements.ITryCatchFinallyStatement;
import gw.lang.parser.statements.ITerminalStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * Represents a try-catch-finally-statement as specified in the Gosu grammar:
 * <pre>
 * <i>try-catch-finally-statement</i>
 *   <b>try</b> &lt;statement&gt; [ <b>catch</b> <b>(</b> &lt;identifier&gt; <b>)</b> &lt;statement&gt; ] [ <b>finally</b> &lt;statement&gt; ]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class TryCatchFinallyStatement extends Statement implements ITryCatchFinallyStatement
{
  private Statement _tryStatement;
  private List<CatchClause> _catchStatements;
  private Statement _finallyStatement;
  private Map<String,Integer> _offsetByName;


  public TryCatchFinallyStatement()
  {
    _offsetByName = new HashMap<String,Integer>();
  }

  public Statement getTryStatement()
  {
    return _tryStatement;
  }

  public void setTryStatement( Statement tryStatement )
  {
    _tryStatement = tryStatement;
  }

  public List<CatchClause> getCatchStatements()
  {
    return _catchStatements;
  }

  public void addCatchClause( CatchClause catchClause )
  {
    if( _catchStatements == null )
    {
      _catchStatements = new ArrayList<CatchClause>();
    }
    _catchStatements.add( catchClause );
  }

  public Statement getFinallyStatement()
  {
    return _finallyStatement;
  }

  public void setFinallyStatement( Statement finallyStatement )
  {
    _finallyStatement = finallyStatement;
  }

  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }
    
    throw new CannotExecuteGosuException();
    }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    boolean[] bTry = {false};
    ITerminalStatement termRet = _tryStatement == null ? null : _tryStatement.getLeastSignificantTerminalStatement( bTry );
    bAbsolute[0] = bTry[0];
    if( _catchStatements != null ) {
      for( CatchClause catchClause : _catchStatements ) {
        boolean[] bCatch = {false};
        ITerminalStatement catchTerm = catchClause.getLeastSignificantTerminalStatement( bCatch );
        termRet = getLeastSignificant( termRet, catchTerm );
        bAbsolute[0] = bAbsolute[0] && bCatch[0];
      }
    }
    bAbsolute[0] = termRet != null && bAbsolute[0];
    return termRet;
  }

  /**
   */
  @Override
  public String toString()
  {
    return "try\n" + _tryStatement.toString() +
           (_catchStatements == null ? "" : printCatches()) +
           (_finallyStatement == null ? "" : ("finally\n" + _finallyStatement.toString()));
  }

  private String printCatches() {
    StringBuilder sb = new StringBuilder();
    for( CatchClause c : getCatchStatements() )
    {
      sb.append( c.toString() );
    }
    return sb.toString();
  }

}
