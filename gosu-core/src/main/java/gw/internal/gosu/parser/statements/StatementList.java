/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;


import gw.lang.parser.IStackProvider;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.ITerminalStatement;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.reflect.TypeSystem;
import gw.internal.gosu.parser.expressions.EvalExpression;

import java.util.List;


/**
 * Represents a statement-list as specified in the Gosu grammar:
 * <pre>
 * <i>statement-list</i>
 *   &lt;statement&gt;
 *   &lt;statement-list&gt; &lt;statement&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class StatementList extends Statement implements IStatementList
{
  protected Statement[] _statements;
  protected IStackProvider _stackProvider;

  /**
   * Constructs a StatementList given an ISymbolTable instance.
   */
  public StatementList( IStackProvider stackProvider )
  {
    _stackProvider = stackProvider;
  }

  @Override
  public void clearParseTreeInformation()
  {
    TypeSystem.lock();
    try
    {
      super.clearParseTreeInformation();
      if( _statements != null )
      {
        for( int i = 0; i < _statements.length; i++ )
        {
          Statement statement = _statements[i];
          statement.clearParseTreeInformation();
        }
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  /**
   * @return A list of Statements representing this statement-list.
   */
  public Statement[] getStatements()
  {
    return _statements;
  }

  /**
   * @param statements A list of Statements representing this statement-list.
   */
  public void setStatements( List<Statement> statements )
  {
    if( statements.size() == 0 )
    {
      _statements = null;
    }
    else
    {
      _statements = statements.toArray( new Statement[statements.size()] );
      tryToEliminateTheScope();
    }
  }

  public int indexOf( Statement stmt )
  {
    for( int i = 0; i < _statements.length; i++ )
    {
      if( stmt == _statements[i] )
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * A statement-list needs to push a new scope on the symbol table to provide
   * a for local variable scoping. Since this is a relatively expensive
   * operation we avoid pushing the scope if we know none of the statements
   * declare variables.
   */
  private void tryToEliminateTheScope()
  {
    for( int i = 0; i < _statements.length; i++ )
    {
      Statement statement = _statements[i];
      if( statement instanceof VarStatement ||
          (!(statement instanceof StatementList) &&
           statement.getContainedParsedElementsByType( EvalExpression.class, null )) )
      {
        return;
      }
    }
    setNoScope();
  }

  public void setNoScope()
  {
    _stackProvider = null;
  }

  /**
   * for testing
   */
  public boolean hasScope()
  {
    return _stackProvider != null;
  }

  /**
   * Execute the list of statements.
   */
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
    bAbsolute[0] = false;
    return getLeastSignificantTerminalStatementAfter( null, bAbsolute );
  }

  public ITerminalStatement getLeastSignificantTerminalStatementAfter( Statement fromStmt, boolean[] bAbsolute )
  {
    if( _statements == null )
    {
      return null;
    }

    ITerminalStatement ret = null;
    for( int i = fromStmt == null ? 0 : indexOf( fromStmt )+1; i < _statements.length; i++ )
    {
      boolean[] bCsr = {false};
      ITerminalStatement terminalStmt = _statements[i].getLeastSignificantTerminalStatement( bCsr );
      if( terminalStmt != null )
      {
        ret = getLeastSignificant( ret, terminalStmt );
        if( ret == terminalStmt ) {
          bAbsolute[0] = bCsr[0];
        }
      }
    }
    return ret;
  }

  @Override
  public String toString()
  {
    String strStatements = "{\n";

    if( _statements != null )
    {
      int iSize = _statements.length;
      for( int i = 0; i < iSize; i++ )
      {
        Statement stmt = _statements[i];
        strStatements += "  " + stmt.toString() + "\n";
      }
    }

    return strStatements + "}\n";
  }

  public Statement getSelfOrSingleStatement()
  {
    if( _statements != null && _statements.length == 1 && !hasScope() )
    {
      _statements[0].addExceptionsFrom( this );
      return _statements[0];
    }
    else
    {

      return this;
    }
  }

}
