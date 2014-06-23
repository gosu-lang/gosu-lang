/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IUsingStatement;
import gw.lang.parser.statements.ITerminalStatement;

import java.util.Collections;
import java.util.List;


/**
 * Represents the using-statement as specified in the Gosu grammar:
 * <pre>
 * <i>using-statement</i>
 *   <b>using</b> <b>(</b> &lt;expression&gt; | &lt;var-statement-list&gt; <b>)</b> &lt;statement&gt; [ <b>finally</b> &lt;statement&gt; ]
 * <i>var-statement-list</i>
 *   &lt;var-statement&gt; [, var-statement-list]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class UsingStatement extends Statement implements IUsingStatement
{
  private Expression _expression;
  private List<IVarStatement> _varStmts;
  private Statement _statement;
  private Statement _finallyStatement;

  public UsingStatement()
  {
    _varStmts = Collections.emptyList();
  }

  /**
   * @return The single expression (mutually exclusive with getVarStatements)
   */
  public Expression getExpression()
  {
    return _expression;
  }
  public void setExpression( IExpression expression )
  {
    _expression = (Expression)expression;
  }

  /**
   * @return The statement to execute
   */
  public Statement getStatement()
  {
    return _statement;
  }
  public void setStatement( IStatement statement )
  {
    _statement = (Statement)statement;
  }

  /**
   * @return The var-statements (mutually exclusive with getExpression)
   */
  public List<IVarStatement> getVarStatements()
  {
    return _varStmts;
  }

  public boolean hasVarStatements()
  {
    return _varStmts != null && !_varStmts.isEmpty();
  }

  public void setVarStatements( List<IVarStatement> varStmts )
  {
    _varStmts = varStmts;
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
    
    throw new IllegalStateException( "Can't execute this parsed element directly" );
  }

  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return getStatement() == null ? null : getStatement().getLeastSignificantTerminalStatement( bAbsolute );
  }

  public String toString()
  {
    return "using( " +
           (getExpression() == null
           ? varStatementsToString()
           : getExpression().toString()) + " )\n" +

           getStatement() == null
           ? ""
           : getStatement().toString() +

           (getFinallyStatement() == null
           ? ""
           : ("\nfinally\n" + getFinallyStatement().toString()));
  }

  private String varStatementsToString()
  {
    String s = "";
    for( IVarStatement varStmt : getVarStatements() )
    {
      if( s.length() == 0 )
      {
        s += varStmt.toString();
      }
      else
      {
        s += ", ";
      }
    }
    return s;
  }

}
