/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;


import gw.internal.gosu.parser.expressions.ImplicitTypeAsExpression;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IExternalSymbolMap;

/**
 * The root class for all Expressions represented in a parse tree as specified
 * in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public abstract class Expression extends ParsedElement implements IExpression
{
  protected IType _type;

  /**
   * Returns this Expression's IType.
   */
  public IType getType()
  {
    IType type = getTypeImpl();
    if (TypeSystem.isDeleted(type)) {
      type = TypeSystem.getErrorType();
    }
    return type;
  }

  protected IType getTypeImpl() {
    return _type;
  }

  /**
   * Sets this Expression's IType.
   */
  public void setType( IType type )
  {
    _type = type;
  }

  @Override
  public boolean isNullSafe()
  {
    return false;
  }

  @Override
  public Object evaluate()
  {
    if( getGosuProgram() == null )
    {
      throw new IllegalStateException( "Expression was not compiled to bytecode" );
    }

    return getGosuProgram().evaluate( null );
  }

  @Override
  public Object evaluate( IExternalSymbolMap externalSymbols )
  {
    if( !isCompileTimeConstant() )
    {
      if( getGosuProgram() == null )
      {
        throw new IllegalStateException( "Expression was not compiled to bytecode" );
      }

      return getGosuProgram().evaluate( externalSymbols );
    }
    else
    {
      return evaluate();
    }
  }

  public IType getReturnType()
  {
    return getType();
  }

  /**
   * Context type is the type this literal value evaluates as in the context
   * of a containing expression e.g., given the expression, n == "42", the
   * literal "42" is always converted to a Number. This feature is most useful
   * for source code tools that provide source-sensitive help (e.g., rule composer).
   */
  public IType getContextType()
  {
    if( getParent() instanceof ImplicitTypeAsExpression )
    {
      return ((Expression)getParent()).getType();
    }
    return getType();
  }

  /**
   * Subclasses should return a String representing the parsed expression.
   */
  public abstract String toString();

}
