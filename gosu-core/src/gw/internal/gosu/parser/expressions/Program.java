/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.ParseTree;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.expressions.IProgram;
import gw.lang.reflect.IType;

import java.util.List;
import java.util.Map;

/**
 * An expression representing a Program:
 *
 * @see gw.lang.parser.IGosuParser
 */
public class Program extends Expression implements IProgram
{
  private Statement _mainStatement;
  private Map<String, IDynamicFunctionSymbol> _functions;
  private IType _declaredReturnType;

  /**
   * Constructs a Program given an ISymbolTable instance.
   */
  public Program()
  {
  }

  public Statement getMainStatement()
  {
    return _mainStatement;
  }

  public void setMainStatement( Statement mainStatement )
  {
    _mainStatement = mainStatement;
  }

  public Map getFunctions()
  {
    return _functions;
  }

  public void setFunctions( Map functions )
  {
    //noinspection unchecked
    _functions = functions;
  }

  public IType getDeclaredReturnType() {
    return _declaredReturnType;
  }

  public void setDeclaredReturnType(IType declaredReturnType) {
    _declaredReturnType = declaredReturnType;
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  public void addParseWarning( IParseIssue warning ) {
    if (_mainStatement != null) {
      super.addParseWarning(warning);
    }
  }

  @Override
  public ParseTree getLocation()
  {
    if( _mainStatement != null )
    {
      return _mainStatement.getLocation();
    }
    return super.getLocation();
  }

  @Override
  public String toString()
  {
    if( _mainStatement != null )
    {
      return _mainStatement.toString();
    }
    return "<empty>";
  }

  @Override
  public IType getReturnType()
  {
    if (_declaredReturnType != null) {
      return _declaredReturnType;
    }

    if( _mainStatement != null )
    {
      return _mainStatement.getReturnType();
    }
    return null;
  }

  public boolean hasContent()
  {
    Statement mainStatement = getMainStatement();
    return mainStatement != null && mainStatement.hasContent();
  }

  @Override
  public <E extends IParsedElement> boolean getContainedParsedElementsByType( Class<E> parsedElementType, List<E> listResults )
  {
    return _mainStatement.getContainedParsedElementsByType( parsedElementType, listResults );
  }

  @Override
  public IParsedElementWithAtLeastOneDeclaration findDeclaringStatement( IParsedElement element, String identifierName )
  {
    return _mainStatement.findDeclaringStatement( element, identifierName );
  }

  @Override
  public List<IParseIssue> getParseIssues() {
    if( _mainStatement != null )
    {
      return _mainStatement.getParseIssues();
    }
    return super.getParseIssues();
  }

  @Override
  public List<IParseIssue> getImmediateParseIssues() {
    if( _mainStatement != null )
    {
      return _mainStatement.getImmediateParseIssues();
    }
    return super.getImmediateParseIssues();
  }

  @Override
  public boolean hasParseExceptions()
  {
    if( _mainStatement != null )
    {
      return _mainStatement.hasParseExceptions();
    }
    return super.hasParseExceptions();
  }

  @Override
  public List<IParseIssue> getParseExceptions()
  {
    if( _mainStatement != null )
    {
      return _mainStatement.getParseExceptions();
    }
    return super.getParseExceptions();
  }

  @Override
  public List<IParseIssue> getParseWarnings()
  {
    if( _mainStatement != null )
    {
      return _mainStatement.getParseWarnings();
    }
    return super.getParseWarnings();
  }

  @Override
  public boolean hasParseWarnings()
  {
    if( _mainStatement != null )
    {
      return _mainStatement.hasParseWarnings();
    }
    return super.hasParseWarnings();
  }

}
