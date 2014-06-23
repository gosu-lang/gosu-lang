/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.builder.expression.IRCastExpressionBuilder;
import gw.lang.ir.builder.expression.IREqualityExpressionBuilder;
import gw.lang.ir.builder.expression.IRFieldGetExpressionBuilder;
import gw.lang.ir.builder.expression.IRMethodCallExpressionBuilder;
import gw.lang.ir.builder.expression.IRNullLiteralBuilder;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@UnstableAPI
public abstract class IRExpressionBuilder extends IRElementBuilder {

  private IRExpression _expression;

  protected IRExpressionBuilder() {
  }

  public IRExpression build(IRBuilderContext context) {
    if (_expression == null) {
      _expression = buildImpl(context);
    }
    return _expression;
  }

  public IRType getType(IRBuilderContext context) {
    return build(context).getType();
  }

  protected abstract IRExpression buildImpl(IRBuilderContext context);

  // ------------------------ Casts -------------------------------------------

  public IRCastExpressionBuilder cast( Class type ) {
    return cast( getIRType( type ) ); 
  }

  public IRCastExpressionBuilder cast( IJavaClassInfo type ) {
    return cast( getIRType( type ) );
  }

  public IRCastExpressionBuilder cast( IRType type ) {
    return new IRCastExpressionBuilder( this, type );
  }

  // ------------------------ Equality Expressions ----------------------------

  public IREqualityExpressionBuilder equals(IRExpressionBuilder rhs) {
    return new IREqualityExpressionBuilder(this, rhs, true);
  }

  public IREqualityExpressionBuilder notEquals(IRExpressionBuilder rhs) {
    return new IREqualityExpressionBuilder(this, rhs, false);
  }

  public IREqualityExpressionBuilder isNull() {
    return new IREqualityExpressionBuilder(this, new IRNullLiteralBuilder(), true);
  }

  public IREqualityExpressionBuilder isNotNull() {
    return new IREqualityExpressionBuilder(this, new IRNullLiteralBuilder(), false);
  }

  // ------------------------ Field Get Expressions ---------------------------

  public IRFieldGetExpressionBuilder field(String name) {
    return new IRFieldGetExpressionBuilder(this, name);  
  }

  // ------------------------ Method Calls ------------------------------------

  public IRMethodCallExpressionBuilder call(String name, IRExpressionBuilder... args) {
    return call(name, Arrays.asList(args));  
  }

  public IRMethodCallExpressionBuilder call(String name, List<IRExpressionBuilder> args) {
    return new IRMethodCallExpressionBuilder( this, name, args );
  }

  public IRMethodCallExpressionBuilder call(IJavaClassMethod method, List<IRExpressionBuilder> args) {
    return new IRMethodCallExpressionBuilder( this, method, args );
  }

  public IRExpressionBuilder call(Method method, List<IRExpressionBuilder> args) {
    return new IRMethodCallExpressionBuilder(this, method, args);
  }
}
