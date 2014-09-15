/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.config.CommonServices;
import gw.internal.gosu.parser.ArrayExpansionPropertyInfo;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.Expression;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.ISymbol;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IQueryPathExpression;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.util.GosuExceptionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a query path expression in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class QueryPathExpression extends Expression implements IQueryPathExpression
{
  private Expression _delegate;  // A MemberAccess -or- an Identifier with a name of an entity type

  public QueryPathExpression()
  {
    super();
  }

  public Expression getDelegate()
  {
    return _delegate;
  }
  public void setDelegate( Expression delegate )
  {
    _delegate = delegate;
  }

  public Object evaluate()
  {
    throw new EvaluationException( "Query path expressions do not evaluate directly." );
  }

  public void assembleQueryPart( IQueryExpressionEvaluator evaluator )
  {
    evaluator.addQueryPathExpression( this );
  }

  @Override
  public String toString()
  {
    return _delegate.toString();
  }

  public IType getRootType()
  {
    Identifier identifier = getRootIdentifier();
    return identifier != null ? identifier.getType() : ErrorType.getInstance();
  }

  public String getRootName()
  {
    Identifier rootId = getRootIdentifier();
    return rootId == null ? "" : rootId.getSymbol().getName();
  }

  private Identifier getRootIdentifier()
  {
    Identifier rootId = null;
    if( getDelegate() instanceof Identifier )
    {
      rootId = (Identifier)getDelegate();
    }
    else if( getDelegate() instanceof MemberAccess )
    {
      Expression csr = ((MemberAccess)getDelegate()).getRootExpression();
      while( csr instanceof MemberAccess )
      {
        csr = ((MemberAccess)csr).getRootExpression();
      }
      rootId = csr instanceof Identifier ? (Identifier)csr : null;
    }
    return rootId;
  }

  public List<String> getAccessPath()
  {
    if( getDelegate() instanceof Identifier )
    {
      return Collections.emptyList();
    }

    List<String> accessPath = new ArrayList<String>( 2 );
    insertIntoAccessPath( getDelegate(), accessPath );
    return accessPath;
  }

  private void insertIntoAccessPath( Expression expr, List<String> accessPath )
  {
    if( expr instanceof MemberAccess )
    {
      MemberAccess ma = (MemberAccess)expr;
      accessPath.add( 0, ma.getMemberName() );
      insertIntoAccessPath( ma.getRootExpression(), accessPath );
    }
  }

  public ISymbol getSymbol()
  {
    Identifier identifier = getRootIdentifier();
    return identifier == null ? null : identifier.getSymbol();
  }

  public IPropertyInfo getPropertyInfo() {
    if (getDelegate() instanceof Identifier)
    {
      return CommonServices.getEntityAccess().getEntityIdProperty( getRootType() );
    }
    try
    {
      if (getDelegate() instanceof MemberAccess) {
        IPropertyInfo pi = BeanAccess.getPropertyInfo(
          getRootType(), ((MemberAccess)getDelegate()).getMemberName(), CommonServices.getEntityAccess().getQueryExpressionFeatureFilter(), null, null );
        if( pi instanceof ArrayExpansionPropertyInfo)
        {
          pi = ((ArrayExpansionPropertyInfo)pi).getDelegate();
        }
        return pi;
      }
      return null;
    }
    catch( ParseException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  public String getMemberName()
  {
    if( getDelegate() instanceof MemberAccess )
    {
      return ((MemberAccess)getDelegate()).getMemberName();
    }
    else
    {
      return "";
    }
  }  
}
