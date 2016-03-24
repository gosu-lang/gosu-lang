/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.ConditionContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IRConditionalOrExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRConditionalOrExpression expression, IRBytecodeContext context) {
    MethodVisitor mv = context.getMv();

    // Push LHS
    IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );
    IRExpression rhs = expression.getRhs();
    ConditionContext lhsCondCxt = expression.getLhs().getConditionContext();
    mv.visitJumpInsn( lhsCondCxt.getOperator(), lhsCondCxt.generateTrueLabel() );
    lhsCondCxt.fixLabels( false, mv );
    // Push RHS
    IRBytecodeCompiler.compileIRExpression( rhs, context );
    ConditionContext rhsCondCxt = rhs.getConditionContext();
    lhsCondCxt.mergeLabels( true, rhsCondCxt );
    lhsCondCxt.setFalseLabels( rhsCondCxt.getLabels( false ) );
    lhsCondCxt.setOperator( rhsCondCxt.getOperator() );
    expression.getConditionContext().update( lhsCondCxt );
    lhsCondCxt.clearLabels();
    rhsCondCxt.clearLabels();
    if( isNotPartOfBooleanExpr( expression ) )
    {
      compileConditionAssignment( expression, mv );
    }
  }
}