/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.statements.CatchClause;
import gw.internal.gosu.parser.statements.TryCatchFinallyStatement;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.statements.IForEachStatement;
import gw.lang.parser.statements.IFunctionStatement;

import java.util.Collections;
import java.util.List;

public class LocalVarDeclaration extends Expression implements ILocalVarDeclaration
{
  private final String _strLocalVarName;

  public LocalVarDeclaration( String strLocalVarName )
  {
    _strLocalVarName = strLocalVarName;
  }

  public CharSequence getLocalVarName()
  {
    return _strLocalVarName;
  }

  public Object evaluate()
  {
    return null; // Nothing to do
  }

  @Override
  public String toString()
  {
    return getLocalVarName().toString();
  }

  public int getNameOffset( String identifierName )
  {
    return getLocation().getOffset();
  }
  public void setNameOffset( int iOffset, String identifierName )
  {
    // Can't set the name offset w/o also setting the location, so this is a no-op 
  }

  public boolean declares( String identifierName )
  {
    return identifierName != null && identifierName.equals( _strLocalVarName );
  }

  public TypeLiteral getTypeLiteral()
  {
    for( IParseTree parseTree : getLocation().getChildren() )
    {
      if( parseTree.getParsedElement() instanceof TypeLiteral )
      {
        return (TypeLiteral)parseTree.getParsedElement();
      }
    }

    return null;
  }

  public ISymbol getSymbol() {
    return findSymbol( getParent() );
  }

  private ISymbol findSymbol( IParsedElement elem ) {
    if( elem == null ) {
      return null;
    }

    if( elem instanceof IFunctionStatement ) {
      IDynamicFunctionSymbol dfs = ((IFunctionStatement)elem).getDynamicFunctionSymbol();
      List<ISymbol> args = dfs.getArgs();
      if( args != null ) {
        for( ISymbol symbol: args ) {
          if( _strLocalVarName.equals( symbol.getName() ) ) {
            return symbol;
          }
        }
      }
    }
    else if( elem instanceof IBlockExpression ) {
      List<ISymbol> args = ((IBlockExpression)elem).getArgs();
      if( args != null ) {
        for(ISymbol symbol : args ) {
          if( _strLocalVarName.equals(symbol.getName())) {
            return symbol;
          }
        }
      }
    }
    else if( elem instanceof IForEachStatement ) {
      ISymbol symbol = ((IForEachStatement)elem).getIdentifier();
      if( symbol != null && _strLocalVarName.equals(symbol.getName())) {
        return symbol;
      }
      symbol = ((IForEachStatement)elem).getIndexIdentifier();
      if( symbol != null && _strLocalVarName.equals(symbol.getName())) {
        return symbol;
      }
    }
    else if( elem instanceof TryCatchFinallyStatement ) {
      List<CatchClause> catchStatements = ((TryCatchFinallyStatement)elem).getCatchStatements();
      if( catchStatements != null ) {
        for(CatchClause catchClause : catchStatements ) {
          if( _strLocalVarName.equals(catchClause.getSymbol().getName())) {
            return catchClause.getSymbol();
          }
        }
      }
    }

    return findSymbol( elem.getParent() );
  }

  @Override
  public String[] getDeclarations() {
    return new String[]{_strLocalVarName};
  }

  @Override
  public List<IGosuAnnotation> getAnnotations()
  {
    return getSymbol() == null
           ? Collections.<IGosuAnnotation>emptyList()
           : getSymbol().getAnnotations();
  }
}
