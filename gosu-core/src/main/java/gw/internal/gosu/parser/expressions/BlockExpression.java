/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ISymbol;
import gw.lang.parser.IScope;
import gw.lang.parser.StandardScope;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.expressions.IBlockExpression;
import gw.internal.gosu.parser.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A block expression, representing an anonymous function/closure
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class BlockExpression extends Expression implements IBlockExpression
{
  private List<ISymbol> _args;
  private IParsedElement _blockBody;
  private Map<String, ICapturedSymbol> _capturedSymbols;
  private StandardScope _scope;
  private IType _blockReturnType;
  private IBlockClassInternal _blockClass;

  /**
   * Constructs an block expression.
   */
  public BlockExpression()
  {
    _capturedSymbols = Collections.emptyMap();
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
  public String toString()
  {
    String returnString = "\\ ";
    for( int i = 0; _args != null && i < _args.size(); i++ )
    {
      ISymbol iSymbol = _args.get( i );
      returnString += iSymbol.getName() + " : " + iSymbol.getType();
      if( i < _args.size() - 2 )
      {
        returnString += ", ";
      }
    }
    returnString += " -> ";
    if( _blockBody == null )
    {
      returnString += "<empty body>";
    }
    else
    {
      returnString += _blockBody.toString();
    }
    return returnString;
  }

  public void setArgs( List<ISymbol> args )
  {
    _args = args;
  }

  public void setBody( IParsedElement blockBody )
  {
    if( blockBody instanceof Statement || blockBody instanceof Expression )
    {
      _blockBody = blockBody;
    }
    else
    {
      throw new IllegalArgumentException( "Cannot set block body of type " + blockBody.getClass() );
    }
  }

  public IFunctionType getType() {
    return (IFunctionType) super.getType();
  }

  @Override
  public IFunctionType getTypeImpl()
  {
    if( _type == null )
    {
      ArrayList<IType> argTypes = new ArrayList<IType>();
      ArrayList<String> argNames = new ArrayList<String>();
      ArrayList<IExpression> defValues = new ArrayList<IExpression>();
      if( _args != null )
      {
        for( int i = 0; i < _args.size(); i++ )
        {
          ISymbol symbol = _args.get( i );
          argTypes.add( symbol.getType() );
          argNames.add( symbol.getName() );
          defValues.add( symbol.getDefaultValueExpression() );
        }
      }
      argNames.trimToSize();
      argTypes.trimToSize();
      IType returnType = _blockBody == null ? JavaTypes.OBJECT() : getBlockReturnType();
      setType( new BlockType( returnType, argTypes.toArray( new IType[argTypes.size()] ), argNames, defValues ) );
    }
    return (IFunctionType)super.getTypeImpl();
  }

  public IType getBlockReturnType()
  {
    return _blockReturnType;
  }

  public void setBlockReturnType( IType blockReturnType )
  {
    _blockReturnType = blockReturnType;
  }

  public IParsedElement getBody() {
    return _blockBody;
  }

  public List<ISymbol> getArgs()
  {
    return _args;
  }

  public ICapturedSymbol getCapturedSymbol( String strName )
  {
    return _capturedSymbols.get( strName );
  }

  public void addCapturedSymbol( ICapturedSymbol sym )
  {
    if( _capturedSymbols.isEmpty() )
    {
      _capturedSymbols = new HashMap<String, ICapturedSymbol>( 2 );
    }
    _capturedSymbols.put( (String)sym.getName(), sym );
  }

  public boolean isWithinScope( ISymbol sym, ISymbolTable symbolTable )
  {
    return getArgs().contains( sym ) || symbolTable.isSymbolWithinScope( sym, _scope );
  }

  @Override
  public String getFunctionName()
  {
    return getType().getName();
  }

  /**
   * The scope of the block, available only at compile time
   */
  public void setScope( StandardScope blockScope )
  {
    _scope = blockScope;
  }

  /**
   * The scope of the block, available only at compile time
   */
  public IScope getScope()
  {
    return _scope;
  }

  // Don't clear parse tree information.  Ugh, this is bad.

  @Override
  public boolean shouldClearParseInfo() {
    return false;
  }

  public void setBlockGosuClass( IBlockClassInternal blockClass )
  {
    _blockClass = blockClass;
  }

  public IBlockClass getBlockGosuClass()
  {
    return _blockClass;
  }

  @Override
  public IGosuClass getGosuClass()
  {
    return getBlockGosuClass();
  }

  public Map<String, ICapturedSymbol> getCapturedSymbols()
  {
    return _capturedSymbols;
  }

  public void updateGosuClass()
  {
    if( _blockClass != null )
    {
      _blockClass.update();
    }
  }
}
