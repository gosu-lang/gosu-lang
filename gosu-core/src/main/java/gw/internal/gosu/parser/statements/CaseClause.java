/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;

import java.util.List;

import gw.lang.parser.statements.ICaseClause;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public final class CaseClause extends Expression implements ICaseClause
{
  private Expression _expression;
  private List<Statement> _statements;

  public CaseClause( Expression e, List<Statement> statements )
  {
    _expression = e;
    _statements = statements;
    setType( JavaTypes.pVOID() );
  }

  public Expression getExpression()
  {
    return _expression;
  }

  public void setExpression( Expression expression )
  {
    _expression = expression;
  }

  public List<Statement> getStatements()
  {
    return _statements;
  }

  public void setStatements( List<Statement> statements )
  {
    _statements = statements;
  }

  @Override
  public String toString()
  {
    return "";
  }

}
