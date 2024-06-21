/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.*;
import gw.lang.parser.MemberAccessKind;


import gw.lang.parser.IExpressionRuntime;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.internal.gosu.util.StringPool;

/**
 * An expression representing a bean method call:
 * <pre>
 * <i>bean-method-call-expression</i>
 *   &lt;member-access&gt; <b>(</b> [&lt;argument-list&gt;] <b>)</b>
 * <p/>
 * <i>member-access</i>
 *   &lt;root-expression&gt;.&lt;member&gt;
 *   &lt;root-expression&gt;*.&lt;member&gt;
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
public final class BeanMethodCallExpression extends Expression implements IBeanMethodCallExpression, IHasOperatorLineNumber {
  private Expression _rootExpression;
  private IType[] _argTypes;
  private String _accessPath;
  private Expression[] _args;
  private IMethodInfo _md;
  private IFunctionType _funcType;
  private MemberAccessKind _kind;
  private int[] _namedArgOrder;
  private int _iArgPos;

  /**
   * Start offset of array list (without leading '.')
   */
  protected int _startOffset;
  private static final IType[] EMPTY_ARG_TYPES = new IType[0];
  private IExpressionRuntime _expressionRuntime;
  private int _opLineNum;

  public IFunctionType getFunctionType() {
    return _funcType;
  }

  public void setFunctionType(IFunctionType funcType) {
    _funcType = funcType;
  }

  public Expression getRootExpression() {
    return _rootExpression;
  }

  public void setRootExpression(Expression rootExpression) {
    _rootExpression = rootExpression;
  }

  /**
   * @return An array of IIntrinsicITyperguments of the method call.
   */
  public IType[] getArgTypes() {
    return _argTypes;
  }

  /**
   * @param argTypes An array of IIntrinsicType for the arguments of the method call.
   */
  public void setArgTypes(IType[] argTypes) {
    _argTypes = argTypes.length == 0 ? EMPTY_ARG_TYPES : argTypes;
  }

  /**
   * @return A list of Strings representing the member access path. Note the
   *         member access path for the expression Root.foo.bar() is {foo, bar}.
   */
  public String getMemberName() {
    return _accessPath;
  }

  /**
   * @param accessPath A list of Strings representing the member access path.
   */
  public void setAccessPath(String accessPath) {
    assert accessPath != null;
      _accessPath = StringPool.get(accessPath);
  }

  public String getAccessPath() {
    return _accessPath;
  }

  public int getStartOffset() {
    return _startOffset;
  }

  public void setExpressionRuntime(IExpressionRuntime expressionRuntime) {
    _expressionRuntime = expressionRuntime;
  }

  @Override
  public IPropertyInfo getPropertyInfo()
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public IExpressionRuntime getExpressionRuntime() {
    return _expressionRuntime;
  }

  public void setStartOffset(int startOffset) {
    _startOffset = startOffset;
  }

  /**
   * @return An array of expressions for corresponding to the arguments in the
   *         expression.
   */
  public Expression[] getArgs() {
    return _args;
  }

  /**
   * @param args An array of expressions for corresponding to the arguments in
   *             the expression.
   */
  public void setArgs(Expression[] args) {
    _args = args == null || args.length == 0 ? null : args;
  }

  public int[] getNamedArgOrder()
  {
    return _namedArgOrder;
  }
  public void setNamedArgOrder( int[] namedArgOrder )
  {
    _namedArgOrder = namedArgOrder;
  }

  public void setMethodDescriptor(IMethodInfo md) {
    _md = md;

    if (md != null) {
      IType type = JavaTypes.IGOSU_OBJECT();
      if (_md.getOwnersType() == IGosuClassInternal.Util.getGosuClassFrom(type)) {
        _md = type.getTypeInfo().getMethod(_md.getDisplayName(), ((FunctionType) ((IGosuMethodInfo) _md).getDfs().getType()).getParameterTypes());
      }
    }
  }

  public IMethodInfo getMethodDescriptor() {
    return _md;
  }

  public IMethodInfo getGenericMethodDescriptor() {
    if (_md instanceof GosuMethodInfo) {
      ReducedDynamicFunctionSymbol dfs = ((GosuMethodInfo) _md).getDfs();
      if (dfs instanceof ReducedParameterizedDynamicFunctionSymbol) {
        return (IMethodInfo) ((ReducedParameterizedDynamicFunctionSymbol) dfs).getBackingDfs().getMethodOrConstructorInfo();
      }
    }
    return _md;
  }

  /**
   */
  public IType getRootType() {
    IType rootType = getRootExpression().getType();
    rootType = IGosuClass.ProxyUtil.isProxy(rootType) && rootType instanceof IGosuClass ? ((IGosuClass) rootType).getJavaType() : rootType;
    return rootType;
  }

  public MemberAccessKind getMemberAccessKind()
  {
    return _kind;
  }
  public void setMemberAccessKind( MemberAccessKind kind )
  {
    if( kind == MemberAccessKind.NORMAL &&
        _md != null &&
        GosuClassProxyFactory.isPropertyGetter( _md ) )
    {
       // getter call null-safety treatment must behave the same way as property member access
      kind = MemberAccessKind.NULL_SAFE;
    }
    _kind = kind;
  }

  @Override
  public boolean isNullSafe()
  {
    return getMemberAccessKind() == MemberAccessKind.NULL_SAFE || isExpansion();
  }

  public boolean isExpansion() {
    return _kind == MemberAccessKind.EXPANSION;
  }

  /**
   * Evaluates the bean method call.
   *
   * @return The value of the expression.
   */
  public Object evaluate() {
    if (!isCompileTimeConstant() ) {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  public String toString() {
    String strOut = getRootExpression().toString();
    if (_accessPath != null) {
      strOut += "." + _accessPath;
    }
    strOut += "(";

    if (_args != null && _args.length > 0) {
      strOut += " ";
      for (int i = 0; i < _args.length; i++) {
        if (i != 0) {
          strOut += ", ";
        }
        strOut += _args[i].toString();
      }
      strOut += " ";
    }

    return strOut += ")";
  }

  public int getArgPosition() {
    return _iArgPos;
  }

  public void setArgPosition(int iArgPos) {
    _iArgPos = iArgPos;
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
