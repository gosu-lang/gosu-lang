/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.lang.ir.ConditionContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IRConditionalAndExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRConditionalAndExpression expression, IRBytecodeContext context) {
    MethodVisitor mv = context.getMv();

    // Push LHS
    IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );
    IRExpression rhs = expression.getRhs();
    ConditionContext lhsCondCxt = expression.getLhs().getConditionContext();
    mv.visitJumpInsn( negateOpcode( lhsCondCxt.getOperator() ), lhsCondCxt.generateFalseLabel() );
    lhsCondCxt.fixLabels( true, mv );
    // Push RHS
    IRBytecodeCompiler.compileIRExpression( rhs, context );
    ConditionContext rhsCondCxt = rhs.getConditionContext();
    lhsCondCxt.mergeLabels( false, rhsCondCxt );
    lhsCondCxt.setTrueLabels( rhsCondCxt.getLabels( true ) );
    lhsCondCxt.setOperator( rhsCondCxt.getOperator() );
    expression.getConditionContext().update( lhsCondCxt );
    if( isNotPartOfBooleanExpr( expression ) )
    {
      compileConditionAssignment( expression, mv );
    }
  }
}
