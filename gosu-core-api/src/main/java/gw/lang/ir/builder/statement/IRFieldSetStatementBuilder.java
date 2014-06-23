/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IJavaClassIRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRType;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.ir.statement.IRFieldSetStatement;
import gw.lang.reflect.java.IJavaClassField;

@UnstableAPI
public class IRFieldSetStatementBuilder extends IRStatementBuilder {

  private BuilderImpl _builder;

  public IRFieldSetStatementBuilder(IRExpressionBuilder root, String name, IRExpressionBuilder value) {
    _builder = new RootAndNameBuilder(root, name, value);
  }

  @Override
  protected IRStatement buildImpl(IRBuilderContext context) {
    return _builder.buildImpl(context);
  }

  private static interface BuilderImpl {
    IRFieldSetStatement buildImpl(IRBuilderContext context);
  }

  private static final class RootAndNameBuilder implements BuilderImpl {
    private IRExpressionBuilder _root;
    private String _name;
    private IRExpressionBuilder _value;

    private RootAndNameBuilder(IRExpressionBuilder root, String name, IRExpressionBuilder value) {
      _root = root;
      _name = name;
      _value = value;
    }

    @Override
    public IRFieldSetStatement buildImpl(IRBuilderContext context) {
      IRExpression root = _root.build(context);
      IRType rootType = root.getType();

      if ( rootType instanceof IJavaClassIRType) {
        IJavaClassField field = findField( ((IJavaClassIRType)rootType).getJavaClassInfo(), _name );
        IRExpression value = IRArgConverter.castOrConvertIfNecessary( getIRType(field.getType()), _value.build(context));
        return new IRFieldSetStatement(root, value, _name, getIRType(field.getType()), getIRType(field.getEnclosingClass()));
      } else if ( rootType.equals( context.owningType() ) ) {
        IRFieldDecl field = context.findField(_name);
        IRExpression value = IRArgConverter.castOrConvertIfNecessary( field.getType(), _value.build(context) );
        return new IRFieldSetStatement(root, value, _name, field.getType(), context.owningType() );
      } else {
        throw new IllegalArgumentException("Cannot reference a field only by name on a root expression that's not an IJavaClassIRType");
      }
    }
  }

}
