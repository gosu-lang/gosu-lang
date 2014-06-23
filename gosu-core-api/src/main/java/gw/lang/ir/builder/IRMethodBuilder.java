/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.builder.expression.IRMethodCallExpressionBuilder;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UnstableAPI
public class IRMethodBuilder extends IRFeatureBuilder<IRMethodBuilder> {
  private IRClassBuilder _classBuilder;

  private String _name;
  private IRType _returnType;
  private List<IRSymbol> _parameters = new ArrayList<IRSymbol>();

  public IRMethodBuilder(IRClassBuilder classBuilder) {
    _classBuilder = classBuilder;
  }

  public IRClassBuilder getClassBuilder() {
    return _classBuilder;
  }

  public List<IRSymbol> getParameters() {
    return _parameters;
  }

  public IRType getReturnType() {
    return _returnType;
  }

  public IRMethodBuilder name(String name) {
    _name = name;
    return this;
  }

  public IRMethodBuilder returns(IRType returnType) {
    _returnType = returnType;
    return this;
  }

  public IRMethodBuilder returns(IType returnType) {
    return returns(getIRType(returnType));
  }

  public IRMethodBuilder returns(Class returnType) {
    return returns(getIRType(returnType));
  }

  public IRMethodBuilder returns(IJavaClassInfo returnType) {
    return returns(getIRType(returnType));
  }

  public IRMethodBuilder parameters(Object... parameters) {
    if (parameters.length % 2 == 1) {
      throw new IllegalArgumentException("Parameters array must be an even number of arguments consisting of alternating names and types");
    }

    for (int i = 0; i < parameters.length; i+=2) {
      String name = (String) parameters[i];
      Object typeObj = parameters[i + 1];
      IRType type;
      if (typeObj instanceof Class) {
        type = getIRType((Class) typeObj);
      } else if (typeObj instanceof IType) {
        type = getIRType((IType) typeObj);
      } else if (typeObj instanceof IRType) {
        type = (IRType) typeObj;
      } else {
        throw new IllegalArgumentException("Argument " + typeObj.getClass() + " is not convertible to an IRType");
      }
      parameter(name, type);
    }

    return this;
  }

  public IRMethodBuilder copyParameters(IJavaClassMethod method) {
    IJavaClassInfo[] paramTypes = method.getParameterTypes();
    for (int i = 0; i < paramTypes.length; i++) {
      parameter("arg" + i, getIRType(paramTypes[i]));
    }

    return this;
  }

  public IRMethodBuilder copyParameters(IMethodInfo method) {
    IParameterInfo[] paramTypes = method.getParameters();
    for (int i = 0; i < paramTypes.length; i++) {
      parameter( "arg" + i, getIRType( paramTypes[i].getFeatureType() ) );
    }

    return this;
  }

  public IRMethodBuilder copyParameters(Method method) {
    Class[] paramTypes = method.getParameterTypes();
    for (int i = 0; i < paramTypes.length; i++) {
      parameter("arg" + i, getIRType(paramTypes[i]));
    }

    return this;
  }

  public IRMethodBuilder copyParameters(IJavaClassConstructor cons) {
    IJavaClassInfo[] paramTypes = cons.getParameterTypes();
    for (int i = 0; i < paramTypes.length; i++) {
      parameter("arg" + i, getIRType(paramTypes[i]));
    }

    return this;
  }

  public IRMethodBuilder copyParameters(Constructor cons) {
    Class[] paramTypes = cons.getParameterTypes();
    for (int i = 0; i < paramTypes.length; i++) {
      parameter("arg" + i, getIRType(paramTypes[i]));
    }

    return this;
  }

  public IRMethodBuilder parameter(String name, IRType type) {
    _parameters.add(new IRSymbol(name, type, false));
    return this;
  }

  public IRMethodBuilder parameter(String name, Class cls) {
    return parameter(name, getIRType(cls));
  }

  public IRMethodBuilder parameter(String name, IType type) {
    return parameter(name, getIRType(type));
  }

  public IRMethodStatement body(IRElementBuilder... elements) {
    return body(Arrays.asList(elements));   
  }

  public IRMethodStatement body(List<IRElementBuilder> elements) {
    IRBuilderContext context = new IRBuilderContext(this);

    List<IRStatement> statements = new ArrayList<IRStatement>();
    for (IRElementBuilder element : elements) {
      if (element instanceof IRStatementBuilder) {
        statements.add(((IRStatementBuilder) element).build( context ));
      } else if (element instanceof IRMethodCallExpressionBuilder) {
        statements.add(new IRMethodCallStatement(((IRMethodCallExpressionBuilder) element).build( context )));
      } else {
        throw new IllegalArgumentException("Element " + element.getClass() + " is not a statement or method call");
      }
    }
    return build(new IRStatementList(true, statements));
  }

  private IRMethodStatement build(IRStatement body) {
    IRMethodStatement method = new IRMethodStatement(body, _name, _modifiers, _returnType, _parameters);
    _classBuilder.withMethod( method );
    return method;
  }

}