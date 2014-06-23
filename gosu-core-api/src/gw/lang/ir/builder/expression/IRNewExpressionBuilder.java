/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IJavaClassIRType;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.UnstableAPI;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;
import java.util.ArrayList;

@UnstableAPI
public class IRNewExpressionBuilder extends IRExpressionBuilder {

  private BuilderImpl _builderImpl;

  public IRNewExpressionBuilder(IRType ownersType, List<IRExpressionBuilder> args) {
    _builderImpl = new TypeAndArgsBuilder(ownersType, args);
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return _builderImpl.buildImpl(context);
  }

  private static interface BuilderImpl {
    IRNewExpression buildImpl(IRBuilderContext context);
  }

  private static final class TypeAndArgsBuilder implements BuilderImpl {
    private IRType _type;
    private List<IRExpressionBuilder> _args;

    private TypeAndArgsBuilder(IRType type, List<IRExpressionBuilder> args) {
      _type = type;
      _args = args;
    }

    @Override
    public IRNewExpression buildImpl(IRBuilderContext context) {
      if ( _type instanceof IJavaClassIRType) {
        IJavaClassConstructor cons = findConstructor( ((IJavaClassIRType)_type).getJavaClassInfo(), _args.size() );
        IJavaClassInfo[] paramTypes = cons.getParameterTypes();
        List<IRExpression> args = new ArrayList<IRExpression>();
        for (int i = 0; i < _args.size(); i++) {
          args.add( IRArgConverter.castOrConvertIfNecessary( getIRType(paramTypes[i]), _args.get(i).build( context ) ) );
        }

        return new IRNewExpression( _type, getIRTypes(paramTypes), args );
      } else {
        throw new IllegalArgumentException("Cannot reference a method only by name on a root expression that's not an IJavaClassIRType");
      }

    }

  }
}
