/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.ConditionContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

import java.util.List;

public class IRNotExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRNotExpression expression, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();
    if ( expression.getType().isBoolean() ) {
      IRExpression root = expression.getRoot();
      IRBytecodeCompiler.compileIRElement( expression.getRoot(), context );
      ConditionContext condCxt = root.getConditionContext();
      condCxt.setOperator( negateOpcode( condCxt.getOperator() ) );
      List<Label> tLabels = condCxt.getLabels( true );
      List<Label> fLabels = condCxt.getLabels( false );
      condCxt.setTrueLabels( fLabels );
      condCxt.setFalseLabels( tLabels );
      expression.getConditionContext().update( condCxt );
      condCxt.clearLabels();
      if( isNotPartOfBooleanExpr( expression ) )
      {
        compileConditionAssignment( expression, mv );
      }

    } else {
      IRBytecodeCompiler.compileIRElement( expression.getRoot(), context );
      if ( expression.getType().isInt() ) {
        mv.visitInsn( Opcodes.ICONST_M1 );
        mv.visitInsn( Opcodes.IXOR );
      } else if ( expression.getType().isLong() ) {
        mv.visitLdcInsn( Long.valueOf( -1 ) );
        mv.visitInsn( Opcodes.LXOR );
      } else {
        throw new IllegalStateException( "Cannot compile a not expression that operates on type " + expression.getType().getName() );
      }
    }
  }
}
