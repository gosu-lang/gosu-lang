/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;

import java.util.Set;

public class DependencyCollector {
  public static IDependencyCollector createExpressionProcessor(IExpression expression) {
    if (expression instanceof IMethodCallExpression) {
      return new MethodCallDependencyCollector();
    } else if (expression instanceof IBeanMethodCallExpression) {
      return new BeanMethodCallDependencyCollector();
    } else if (expression instanceof IFieldAccessExpression) {
      return new FieldAccessDependencyCollector();
    } else {
      return null;
    }
  }

  public static void collect(IParseTree parseTree, DependencySink sink) {
    final IParsedElement element = parseTree.getParsedElement();
    if (element == null) {
      return;
    }

    if (element instanceof IExpression) {
      final IExpression expression = (IExpression) element;
      final IDependencyCollector collector = createExpressionProcessor(expression);
      if (collector != null) {
        collector.collect(expression, sink);
      }

      final IType type = expression.getType();
      if (type != null) {
        sink.addType(type);
      }
    }

    if (element instanceof IUsesStatement) {
      UsesStatementsDependencyCollector.collect((IUsesStatement) element, sink);
    }

    if (element instanceof IClassStatement) {
      final IClassStatement classStatement = (IClassStatement) element;

      IGosuClass gosuClass = classStatement.getGosuClass();
      final IType supertype = gosuClass.getSupertype();
      if (supertype != null) {
        Set<? extends IType> types = supertype.getAllTypesInHierarchy();
        for (IType type : types) {
          type = IGosuClass.ProxyUtil.getProxiedType(type);
          if (!type.equals(JavaTypes.IGOSU_OBJECT())) {
            sink.addType(type);
          }
        }
      }
      EnhancedTypeDependencyCollector.collect(classStatement, sink);
    }

    for (IParseTree child : parseTree.getChildren()) {
      collect(child, sink);
    }
  }
}
