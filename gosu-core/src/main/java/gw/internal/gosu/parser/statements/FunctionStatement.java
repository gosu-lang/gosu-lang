/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IParameterDeclaration;
import gw.lang.parser.expressions.IParameterListClause;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.util.GosuObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 */
public class FunctionStatement extends Statement implements IFunctionStatement
{
  private DynamicFunctionSymbol _dfs;
  private int _iNameOffset;

  public FunctionStatement()
  {
    _iNameOffset = -1;
  }

  public void setDynamicFunctionSymbol( DynamicFunctionSymbol dfs )
  {
    _dfs = dfs;
  }

  public DynamicFunctionSymbol getDynamicFunctionSymbol()
  {
    return _dfs;
  }

  public Object execute()
  {
    // No-Op
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public boolean isNoOp()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return _dfs == null ? "no dfs" : _dfs.toString();
  }

  @Override
  public String getFunctionName()
  {
    DynamicFunctionSymbol dfs = getDynamicFunctionSymbol();
    return dfs == null ? null : dfs.getDisplayName();
  }

  @Override
  public int getNameOffset( String identifierName )
  {
    return _iNameOffset;
  }

  @Override
  public void setNameOffset( int iOffset, String identifierName )
  {
    _iNameOffset = iOffset;
  }

  public boolean declares( String identifierName )
  {
    if( _dfs == null )
    {
      return false;
    }

    if( GosuObjectUtil.equals( _dfs.getName(), identifierName ) )
    {
      return true;
    }
    else
    {
      if( _dfs.getName().charAt( 0 ) == '@' )
      {
        boolean b = _dfs.getName().contains( "(" );
        assert b;
        int indexOfParen = _dfs.getName().length() - 1;
        while( indexOfParen > 0 )
        {
          if( _dfs.getName().charAt( indexOfParen ) == '(' )
          {
            break;
          }
          indexOfParen--;
        }
        return _dfs.getName().subSequence( 1, indexOfParen ).equals( identifierName );
      }
      else
      {
        return false;
      }
    }
  }

  public String[] getDeclarations()
  {
    if( _dfs == null )
    {
      return new String[0];
    }
    else
    {
      return new String[]{_dfs.getDisplayName().replace( "@", "" )};
    }
  }

  public static IMethodInfo getGenericMethodInfo( IMethodInfo mi )
  {
    IMethodInfo genericMethodInfo = null;
    IType type = mi.getOwnersType();
    IType superType = type.getSupertype();
    while( (superType != null) && (genericMethodInfo == null) )
    {
      if( superType.isParameterizedType() )
      {
        type = TypeLord.getPureGenericType( superType );
        List<? extends IMethodInfo> methodInfos = type.getTypeInfo().getMethods();
        for( IMethodInfo methodInfo : methodInfos )
        {
          if( (methodInfo.getDisplayName().equals( mi.getDisplayName() )) &&
              (methodInfo.getParameters().length == mi.getParameters().length) )
          {
            genericMethodInfo = methodInfo;
          }
        }
      }
      superType = superType.getSupertype();
    }
    if( genericMethodInfo == null )
    {
      genericMethodInfo = mi;
    }
    return genericMethodInfo;
  }

  @Override
  public List<IParameterDeclaration> getParameters()
  {
    List<IParameterDeclaration> params = new ArrayList<IParameterDeclaration>();
    List<IParseTree> children = getLocation().getChildren();
    for( IParseTree parseTree : children )
    {
      IParsedElement pe = parseTree.getParsedElement();
      if( pe instanceof IParameterListClause )
      {
        for( IParseTree parseTree2 : parseTree.getChildren() )
        {
          pe = parseTree2.getParsedElement();
          if( pe instanceof IParameterDeclaration )
          {
            params.add( (IParameterDeclaration)pe );
          }
        }
        break;
      }
    }
    return params;
  }

  public List<IGosuAnnotation> getAnnotations()
  {
    return getDynamicFunctionSymbol() == null
           ? Collections.<IGosuAnnotation>emptyList()
           : getDynamicFunctionSymbol().getAnnotations();
  }

  @Override
  protected List getExcludedReturnTypeElements()
  {
    return Arrays.asList( BlockExpression.class );
  }
}
