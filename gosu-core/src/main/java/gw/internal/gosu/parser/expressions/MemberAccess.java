/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.RuntimeExceptionWithNoStacktrace;
import gw.lang.parser.MemberAccessKind;
import gw.internal.gosu.parser.optimizer.SinglePropertyMemberAccessRuntime;


import gw.lang.parser.IExpressionRuntime;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IProgramInstance;
import gw.lang.reflect.java.ICompileTimeConstantValue;
import gw.internal.gosu.util.StringPool;

/**
 * Represents a member access expression in the Gosu grammar:
 * <pre>
 * <i>member-access</i>
 *   &lt;root-expression&gt;.&lt;member&gt;
 *   &lt;root-expression&gt;[member-name]
 * <p/>
 * <i>root-expression</i>
 *   &lt;bean-reference&gt;
 *   &lt;type-literal&gt;
 * <p/>
 * <i>member</i>
 *   &lt;member-access&gt;
 *   &lt;identifier&gt;
 * <p/>
 * <i>bean-reference</i>
 *   &lt;primary-expression&gt;
 * <p/>
 * <i>member-name</i>
 *   &lt;expression&gt;
 * </pre>
 *
 * @see gw.lang.parser.IGosuParser
 */
public class MemberAccess extends Expression implements IFieldAccessExpression, IHasOperatorLineNumber
{
  /**
   * The root expression in the path (instead of a root bean symbol)
   */
  private Expression _rootExpression;
  /**
   * The member name.
   */
  private String _strMemberName;

  /**
   * Start offset of array list (without leading '.')
   */
  private int _startOffset;

  /**
   * An expression for accessing a member by name dynamically
   */
  private Expression _memberExpression;

  private IExpressionRuntime _expressionRuntime;
  
  private MemberAccessKind _kind;
  private int _opLineNum;


  public Expression getRootExpression()
  {
    return _rootExpression;
  }

  public Object evaluateRootExpr()
  {
    IProgramInstance instance;
    try
    {
      instance = (IProgramInstance)getGosuProgram().getBackingClass().newInstance();
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    return instance.evaluateRootExpr(null);
  }
  
  public void setRootExpression( Expression rootExpression )
  {
    _rootExpression = rootExpression;
  }

  public String getMemberName()
  {
    return _strMemberName;
  }
  public void setMemberName( String strMemberName )
  {
    assert strMemberName != null;
      _strMemberName = StringPool.get(strMemberName);
  }

  public int getStartOffset()
  {
    return _startOffset;
  }
  public void setStartOffset( int startOffset )
  {
    _startOffset = startOffset;
  }

  public Expression getMemberExpression()
  {
    return _memberExpression;
  }
  public void setMemberExpression( Expression memberExpression )
  {
    _memberExpression = memberExpression;
  }

  public IType getRootType()
  {
    IType rootType = getRootExpression().getType();
    rootType = IGosuClass.ProxyUtil.isProxy(rootType) && rootType instanceof IGosuClass ? ((IGosuClass) rootType).getJavaType() : rootType;
    return rootType;
  }

  public IType getAssignableType()
  {
    try
    {
      IPropertyInfo pi = getPropertyInfo();
      if( pi != null )
      {
        return pi.getAssignableFeatureType();
      }
    }
    catch( RuntimeExceptionWithNoStacktrace e )
    {
      // eat
    }
    return getType();
  }

  public IPropertyInfo getPropertyInfo()
  {
    if( _memberExpression != null || _strMemberName == null )
    {
      return null;
    }
    try
    {
      return BeanAccess.getPropertyInfoDirectly( getRootType(), _strMemberName );
    }
    catch( ParseException e )
    {
      throw new RuntimeExceptionWithNoStacktrace( e );
    }
  }

  public IPropertyInfo getPropertyInfoWithoutThrowing()
  {
    if( _memberExpression != null || _strMemberName == null )
    {
      return null;
    }
    return BeanAccess.getPropertyInfoDirectly_NoException( getRootType(), _strMemberName );
  }

  public boolean isCompileTimeConstant()
  {
    try
    {
      if( getRootExpression().isCompileTimeConstant() )
      {
        IPropertyInfo pi;
        pi = getCompileTimePropertyInfo();
        if( pi == null )
        {
          return false;
        }
        while( pi instanceof IPropertyInfoDelegate )
        {
          pi = ((IPropertyInfoDelegate)pi).getSource();
        }
        if( pi.isStatic() && pi instanceof ICompileTimeConstantValue )
        {
          return ((ICompileTimeConstantValue)pi).isCompileTimeConstantValue();
        }
      }
    }
    catch( Throwable e ) { /* Can happen if this expression does not parse / errant property name */  }
    return false;
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    IPropertyInfo pi = getCompileTimePropertyInfo();
    while( pi instanceof IPropertyInfoDelegate )
    {
      pi = ((IPropertyInfoDelegate)pi).getSource();
    }

    return ((ICompileTimeConstantValue)pi).doCompileTimeEvaluation();
  }

  public IPropertyInfo getCompileTimePropertyInfo()
  {
    if( _memberExpression != null || _strMemberName == null )
    {
      return null;
    }
    try
    {
      IType rootType = getRootType();
      if (rootType instanceof IMetaType) {
        rootType = ((IMetaType) rootType).getType();
      }
      return BeanAccess.getPropertyInfoDirectly(rootType, _strMemberName);
    }
    catch( ParseException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void setExpressionRuntime(IExpressionRuntime expressionRuntime)
  {
    _expressionRuntime = expressionRuntime;
  }

  public IExpressionRuntime getExpressionRuntime()
  {
    if( _expressionRuntime == null )
    {
      if( SinglePropertyMemberAccessRuntime.isConvertible( this ) )
      {
        _expressionRuntime = new SinglePropertyMemberAccessRuntime( this );
      }
    }
    return _expressionRuntime;
  }

  public MemberAccessKind getMemberAccessKind()
  {
    return _kind;
  }
  public void setMemberAccessKind( MemberAccessKind kind )
  {
    _kind = kind;
  }

  @Override
  public boolean isNullSafe()
  {
    return getMemberAccessKind() == MemberAccessKind.NULL_SAFE;
  }

  @Override
  public String toString()
  {
    return getRootExpression().toString() + "." + _strMemberName;
  }

  @Override
  public int getOperatorLineNumber()
  {
    return _opLineNum;
  }

  @Override
  public void setOperatorLineNumber( int operatorLineNumber )
  {
    _opLineNum = operatorLineNumber;
  }
}
