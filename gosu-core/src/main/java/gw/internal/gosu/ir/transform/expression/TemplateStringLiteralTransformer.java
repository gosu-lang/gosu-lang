/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.TemplateStringLiteral;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.expression.IRStringLiteralExpression;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.parser.expressions.IProgram;
import gw.util.Stack;

import java.util.Collections;

/**
 */
public class TemplateStringLiteralTransformer extends AbstractExpressionTransformer<TemplateStringLiteral>
{
  private static final ThreadLocal<Stack<IRSymbol>> SYMBOL_STACK = new ThreadLocal<>();

  public static IRExpression compile( TopLevelTransformationContext cc, TemplateStringLiteral expr )
  {
    TemplateStringLiteralTransformer compiler = new TemplateStringLiteralTransformer( cc, expr );
    return compiler.compile();
  }

  private TemplateStringLiteralTransformer( TopLevelTransformationContext cc, TemplateStringLiteral expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    TemplateStringLiteral stringLiteral = _expr();
    IProgram iProgram = stringLiteral.getProgram();

    if( iProgram != null )
    {
      IRCompositeExpression template = new IRCompositeExpression();
      IRType sbType = getDescriptor( StringBuilder.class );
      IRSymbol symbol = _cc().makeAndIndexTempSymbol( sbType );
      try
      {
        getThreadLocalStack().push( symbol );

        // instantiate and store a string builder
        IRAssignmentStatement sbAssignment = buildAssignment( symbol, buildNewExpression( sbType, Collections.<IRType>emptyList(), Collections.emptyList() ) );
        template.addElement( sbAssignment );
        sbAssignment.setImplicit( true );

        // invoke the body
        IRStatement templateBody = _cc().compile( iProgram.getMainStatement() );
        template.addElement( templateBody );
        templateBody.setImplicit( true );

        // invoke toString on the string builder
        IRMethodCallExpression toString = buildMethodCall( sbType, "toString", false, IRTypeConstants.STRING(), Collections.emptyList(), identifier( symbol ), Collections.emptyList() );
        template.addElement( toString );
        template.setImplicit( true );

        return template;
      }
      finally
      {
        getThreadLocalStack().pop();
      }
    }
    else
    {
      return new IRStringLiteralExpression( "Unsupported Template");
    }
  }

  private static Stack<IRSymbol> getThreadLocalStack()
  {
    Stack<IRSymbol> symbolStack = SYMBOL_STACK.get();
    if( symbolStack == null )
    {
      symbolStack = new Stack<>();
      SYMBOL_STACK.set( symbolStack );
    }
    return symbolStack;
  }

  public static IRSymbol getCurrentTemplateSymbol()
  {
    Stack<IRSymbol> threadLocalStack = getThreadLocalStack();
    return threadLocalStack.isEmpty() ? null : threadLocalStack.peek();
  }
}
