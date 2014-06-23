/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.parser.expressions.IProgram;

public final class ParseResult implements IParseResult
{
  private IExpression _expr;
  private IGosuProgram _program;


  public ParseResult()
  {
  }

  public ParseResult( IExpression expr )
  {
    _expr = expr;
  }

  public ParseResult( IGosuProgram program )
  {
    _program = program;
  }


  @Override
  public IExpression getExpression()
  {
    return _expr == null ? _program.getExpression() : _expr;
  }

  @Override
  public IStatement getStatement()
  {
    return _expr instanceof IProgram
           ? ((IProgram)_expr).getMainStatement()
           : _program != null
              ? _program.getStatement()
              : null;
  }

  @Override
  public IParsedElement getParsedElement() {
    if (_expr != null) {
      return _expr;
    } else if (_program != null) {
      if (_program.getExpression() != null) {
        return _program.getExpression();
      } else if (_program.getStatement() != null) {
        return _program.getStatement();
      }
    }

    return null;
  }

  @Override
  public IExpression getRawExpression() {
    return _expr;
  }

  @Override
  public IGosuProgram getProgram()
  {
    return _program;
  }

  @Override
  public boolean isLiteral()
  {
    return getExpression() instanceof ILiteralExpression;
  }

  @Override
  public boolean isProgram()
  {
    return _expr instanceof IProgram || getStatement() != null;
  }

  @Override
  public IType getType()
  {
    if( _expr != null )
    {
      if (_expr.getType() != null) {
        return _expr.getType();
      } else if (_expr instanceof IProgram) {
        return _expr.getReturnType(); 
      } else {
        return null;
      }
    }
    return _program.getReturnType();
  }

  @Override
  public Object evaluate()
  {
    return _program != null ? _program.evaluate( null ) : _expr.evaluate();
  }
}