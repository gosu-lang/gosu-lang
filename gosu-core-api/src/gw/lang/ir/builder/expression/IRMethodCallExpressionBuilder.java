/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.UnstableAPI;
import gw.lang.ir.IJavaClassIRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class IRMethodCallExpressionBuilder extends IRExpressionBuilder {

  private BuilderImpl _builderImpl;

  public static IRMethodCallExpressionBuilder callSuperInit(List<IRExpressionBuilder> args) {
    return new IRMethodCallExpressionBuilder(new SuperInitBuilder(args));
  }

  private IRMethodCallExpressionBuilder(BuilderImpl builderImpl) {
    _builderImpl = builderImpl;
  }

  public IRMethodCallExpressionBuilder(IRExpressionBuilder root, String name, List<IRExpressionBuilder> args) {
    _builderImpl = new RootAndNameAndArgsBuilder(root, name, args);
  }

  public IRMethodCallExpressionBuilder(IRExpressionBuilder root, IJavaClassMethod method, List<IRExpressionBuilder> args) {
    _builderImpl = new MethodAndArgsBuilder(root, method, args);
  }

  public IRMethodCallExpressionBuilder(IRExpressionBuilder root, Method method, List<IRExpressionBuilder> args) {
    _builderImpl = new JavaMethodAndArgsBuilder(root, method, args);
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return _builderImpl.buildImpl(context);
  }

  private static IRMethodCallExpression buildCall(IRBuilderContext context, String name, IRExpression root, List<IRExpressionBuilder> argBuilders, MethodInfo methodInfo) {
    List<IRType> parameterTypes = methodInfo.getParamTypes();

    // Convert arguments as needed
    List<IRExpression> args = new ArrayList<IRExpression>();
    for (int i = 0; i < argBuilders.size(); i++) {
      args.add(IRArgConverter.castOrConvertIfNecessary( parameterTypes.get(i), argBuilders.get(i).build(context)));
    }

    IRMethodCallExpression methodCall = new IRMethodCallExpression(name, methodInfo.getOwningType(), methodInfo.isInterface(),
            methodInfo.getReturnType(), parameterTypes, root, args);
    methodCall.setSpecial(methodInfo.isSpecial());
    return methodCall;
  }

  private static interface BuilderImpl {
    IRMethodCallExpression buildImpl(IRBuilderContext context);
  }

  private static final class RootAndNameAndArgsBuilder implements BuilderImpl {
    private IRExpressionBuilder _root;
    private String _name;
    private List<IRExpressionBuilder> _args;

    private RootAndNameAndArgsBuilder(IRExpressionBuilder root, String name, List<IRExpressionBuilder> args) {
      _root = root;
      _name = name;
      _args = args;
    }

    @Override
    public IRMethodCallExpression buildImpl(IRBuilderContext context) {
      IRExpression root = _root.build(context);
      IRType rootType = root.getType();

      MethodInfo methodInfo = findMethod(_name, _args.size(), rootType, context);
      return buildCall( context, _name, root, _args, methodInfo );
    }
  }

  private static final class MethodAndArgsBuilder implements BuilderImpl {
    private IRExpressionBuilder _root;
    private IJavaClassMethod _method;
    private List<IRExpressionBuilder> _args;

    private MethodAndArgsBuilder(IRExpressionBuilder root, IJavaClassMethod method, List<IRExpressionBuilder> args) {
      _root = root;
      _method = method;
      _args = args;
    }

    @Override
    public IRMethodCallExpression buildImpl(IRBuilderContext context) {
      IRExpression root = _root == null ? null : _root.build(context);

      MethodInfo methodInfo = new MethodInfo( _method );
      return buildCall( context, _method.getName(), root, _args, methodInfo );
    }
  }

  private static final class JavaMethodAndArgsBuilder implements BuilderImpl {
    private IRExpressionBuilder _root;
    private Method _method;
    private List<IRExpressionBuilder> _args;

    private JavaMethodAndArgsBuilder(IRExpressionBuilder root, Method method, List<IRExpressionBuilder> args) {
      _root = root;
      _method = method;
      _args = args;
    }

    @Override
    public IRMethodCallExpression buildImpl(IRBuilderContext context) {
      IRExpression root = _root == null ? null : _root.build(context);

      MethodInfo methodInfo = new MethodInfo( _method );
      return buildCall( context, _method.getName(), root, _args, methodInfo );
    }
  }

  private static final class SuperInitBuilder implements BuilderImpl {

    private List<IRExpressionBuilder> _args;

    private SuperInitBuilder(List<IRExpressionBuilder> args) {
      _args = args;
    }

    @Override
    public IRMethodCallExpression buildImpl(IRBuilderContext context) {
      IRType type = context.currentClassSuperType();
      if (type instanceof IJavaClassIRType) {
        IJavaClassConstructor cons = findConstructor(((IJavaClassIRType)type).getJavaClassInfo(), _args.size());
        MethodInfo methodInfo = new MethodInfo(type, getIRTypes(cons.getParameterTypes()), IRTypeConstants.pVOID(), true);
        return buildCall(context, "<init>", new IRIdentifier(new IRSymbol("this", context.owningType(), false)), _args, methodInfo);
      } else {
        throw new IllegalArgumentException();
      }
    }
  }

  private static class MethodInfo {
    private IRType _owningType;
    private List<IRType> _paramTypes;
    private IRType _returnType;
    private boolean _special;

    private MethodInfo(IJavaClassMethod method) {
      this(getIRType(method.getEnclosingClass()), getIRTypes(method.getParameterTypes()), getIRType(method.getReturnClassInfo()), false);
    }

    private MethodInfo(Method method) {
      this(getIRType(method.getDeclaringClass()), getIRTypes(method.getParameterTypes()), getIRType(method.getReturnType()), false);
    }

    private MethodInfo(IRType owningType, List<IRType> paramTypes, IRType returnType, boolean special) {
      _owningType = owningType;
      _paramTypes = paramTypes;
      _returnType = returnType;
      _special = special;
    }

    public IRType getOwningType() {
      return _owningType;
    }

    public List<IRType> getParamTypes() {
      return _paramTypes;
    }

    public IRType getReturnType() {
      return _returnType;
    }

    public boolean isInterface() {
      return _owningType.isInterface();
    }

    public boolean isSpecial() {
      return _special;
    }
  }

  private static MethodInfo findMethod(String name, int numArgs, IRType rootType, IRBuilderContext context) {
    if (rootType instanceof IJavaClassIRType) {
      IJavaClassMethod method = findMethod(((IJavaClassIRType)rootType).getJavaClassInfo(), name, numArgs);
      return new MethodInfo(method);
    } else if (rootType.equals(context.owningType())) {
      IRMethodStatement methodDecl = context.findMethod(name, numArgs);
      if (methodDecl != null) {
        List<IRType> paramTypes = new ArrayList<IRType>();
        for (IRSymbol paramSymbol : methodDecl.getParameters()) {
          paramTypes.add(paramSymbol.getType());
        }
        return new MethodInfo(context.owningType(), paramTypes, methodDecl.getReturnType(), false);
      } else {
        return findMethod(name, numArgs, context.currentClassSuperType(), context);
      }
    } else {
      throw new IllegalArgumentException("Cannot reference a method only by name on a root expression that's not an IJavaClassIRType");
    }
  }
}
