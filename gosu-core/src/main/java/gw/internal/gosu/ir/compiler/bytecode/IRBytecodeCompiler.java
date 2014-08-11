/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.gosu.ir.compiler.bytecode.expression.IRArithmeticExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRArrayLengthExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRArrayLoadExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRBooleanLiteralCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRCastExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRCharacterLiteralCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRClassLiteralCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRCompositeExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRConditionalAndExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRConditionalOrExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IREqualityExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRFieldGetExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRIdentifierCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRInstanceOfExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRLazyTypeMethodCallExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRMethodCallExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRNegationExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRNewArrayExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRNewExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRNewMultiDimensionalArrayExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRNotExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRNullLiteralCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRNumericLiteralCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRPrimitiveTypeConversionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRRelationalExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRStringLiteralExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRTernaryExpressionCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRArrayStoreStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRAssignmentStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRBreakStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRContinueStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRDoWhileStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IREvalStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRFieldSetStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRForEachStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRIfStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRMethodCallStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRMonitorLockAcquireCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRMonitorLockReleaseCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRNewStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRReturnStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRStatementListCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRSwitchStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRSyntheticStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRThrowStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRTryCatchFinallyStatementCompiler;
import gw.internal.gosu.ir.compiler.bytecode.statement.IRWhileStatementCompiler;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.expression.IRArrayLengthExpression;
import gw.lang.ir.expression.IRArrayLoadExpression;
import gw.lang.ir.expression.IRBooleanLiteral;
import gw.lang.ir.expression.IRCastExpression;
import gw.lang.ir.expression.IRCharacterLiteral;
import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.expression.IRInstanceOfExpression;
import gw.lang.ir.expression.IRLazyTypeMethodCallExpression;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.expression.IRNegationExpression;
import gw.lang.ir.expression.IRNewArrayExpression;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.ir.expression.IRNewMultiDimensionalArrayExpression;
import gw.lang.ir.expression.IRNoOpExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.lang.ir.expression.IRPrimitiveTypeConversion;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.expression.IRStringLiteralExpression;
import gw.lang.ir.expression.IRTernaryExpression;
import gw.lang.ir.statement.IRArrayStoreStatement;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRBreakStatement;
import gw.lang.ir.statement.IRContinueStatement;
import gw.lang.ir.statement.IRDoWhileStatement;
import gw.lang.ir.statement.IREvalStatement;
import gw.lang.ir.statement.IRFieldSetStatement;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.statement.IRIfStatement;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.lang.ir.statement.IRMonitorLockAcquireStatement;
import gw.lang.ir.statement.IRMonitorLockReleaseStatement;
import gw.lang.ir.statement.IRNewStatement;
import gw.lang.ir.statement.IRNoOpStatement;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRSwitchStatement;
import gw.lang.ir.statement.IRSyntheticStatement;
import gw.lang.ir.statement.IRThrowStatement;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.ir.statement.IRWhileStatement;

public class IRBytecodeCompiler {
  public static void compileIRElement(IRElement element, IRBytecodeContext context) {
    if (element instanceof IRStatement) {
      compileIRStatement((IRStatement) element, context);
    } else {
      compileIRExpression((IRExpression) element, context);
    }
  }

  public static void compileIRStatement(IRStatement statement, IRBytecodeContext context) {
    if (statement == null) {
      return;
    }

    context.setLineNumber( statement.getLineNumber() );
    if (statement instanceof IRAssignmentStatement) {
      IRAssignmentStatementCompiler.compile((IRAssignmentStatement) statement, context);
    } else if (statement instanceof IRFieldSetStatement) {
      IRFieldSetStatementCompiler.compile((IRFieldSetStatement) statement, context);
    } else if (statement instanceof IRIfStatement) {
      IRIfStatementCompiler.compile((IRIfStatement) statement, context);
    } else if (statement instanceof IRMethodCallStatement) {
      IRMethodCallStatementCompiler.compile((IRMethodCallStatement) statement, context);
    } else if (statement instanceof IRNewStatement ) {
      IRNewStatementCompiler.compile( (IRNewStatement)statement, context );
    } else if (statement instanceof IRNoOpStatement) {
      // Do nothing
    } else if (statement instanceof IRReturnStatement) {
      IRReturnStatementCompiler.compile((IRReturnStatement) statement, context);
    } else if (statement instanceof IRStatementList) {
      IRStatementListCompiler.compile((IRStatementList) statement, context);
    } else if (statement instanceof IRArrayStoreStatement) {
      IRArrayStoreStatementCompiler.compile((IRArrayStoreStatement) statement, context);
    } else if (statement instanceof IRThrowStatement) {
      IRThrowStatementCompiler.compile((IRThrowStatement) statement, context);
    } else if (statement instanceof IRForEachStatement ) {
      IRForEachStatementCompiler.compile((IRForEachStatement) statement, context);
    } else if (statement instanceof IRWhileStatement ) {
      IRWhileStatementCompiler.compile((IRWhileStatement) statement, context);
    } else if (statement instanceof IRDoWhileStatement ) {
      IRDoWhileStatementCompiler.compile((IRDoWhileStatement) statement, context);
    } else if (statement instanceof IRBreakStatement ) {
      IRBreakStatementCompiler.compile((IRBreakStatement) statement, context);
    } else if (statement instanceof IRContinueStatement) {
      IRContinueStatementCompiler.compile((IRContinueStatement) statement, context);
    } else if (statement instanceof IRTryCatchFinallyStatement) {
      IRTryCatchFinallyStatementCompiler.compile((IRTryCatchFinallyStatement) statement, context);
    } else if (statement instanceof IRMonitorLockAcquireStatement ) {
      IRMonitorLockAcquireCompiler.compile((IRMonitorLockAcquireStatement) statement, context);
    } else if (statement instanceof IRMonitorLockReleaseStatement ) {
      IRMonitorLockReleaseCompiler.compile((IRMonitorLockReleaseStatement) statement, context);
    } else if (statement instanceof IRSyntheticStatement) {
      IRSyntheticStatementCompiler.compile((IRSyntheticStatement) statement, context);
    } else if (statement instanceof IRSwitchStatement) {
      IRSwitchStatementCompiler.compile((IRSwitchStatement) statement, context);
    } else if (statement instanceof IREvalStatement ) {
      IREvalStatementCompiler.compile((IREvalStatement) statement, context);
    } else {
      throw new IllegalArgumentException("Unrecognized statement of type " + statement.getClass());
    }
  }

  public static void compileIRExpression(IRExpression expression, IRBytecodeContext context) {
    if (expression == null) {
      return;
    }

    int previousLineNumber = context.setLineNumber( expression.getLineNumber() );
    try
    {
      if (expression instanceof IRArithmeticExpression) {
        IRArithmeticExpressionCompiler.compile((IRArithmeticExpression) expression, context);
      } else if (expression instanceof IRArrayLoadExpression) {
        IRArrayLoadExpressionCompiler.compile((IRArrayLoadExpression) expression, context);
      } else if (expression instanceof IRBooleanLiteral) {
        IRBooleanLiteralCompiler.compile((IRBooleanLiteral) expression, context);
      } else if (expression instanceof IRCompositeExpression) {
        IRCompositeExpressionCompiler.compile((IRCompositeExpression) expression, context);
      } else if (expression instanceof IREqualityExpression) {
        IREqualityExpressionCompiler.compile((IREqualityExpression) expression, context);
      } else if (expression instanceof IRFieldGetExpression) {
        IRFieldGetExpressionCompiler.compile((IRFieldGetExpression) expression, context);
      } else if (expression instanceof IRIdentifier) {
        IRIdentifierCompiler.compile((IRIdentifier) expression, context);
      } else if (expression instanceof IRMethodCallExpression) {
        IRMethodCallExpressionCompiler.compile((IRMethodCallExpression) expression, context);
      } else if (expression instanceof IRLazyTypeMethodCallExpression) {
        IRLazyTypeMethodCallExpressionCompiler.compile( (IRLazyTypeMethodCallExpression)expression, context );
      } else if (expression instanceof IRNullLiteral) {
        IRNullLiteralCompiler.compile((IRNullLiteral) expression, context);
      } else if (expression instanceof IRPrimitiveTypeConversion) {
        IRPrimitiveTypeConversionCompiler.compile((IRPrimitiveTypeConversion) expression, context);
      } else if (expression instanceof IRTernaryExpression) {
        IRTernaryExpressionCompiler.compile((IRTernaryExpression) expression, context);
      } else if (expression instanceof IRNumericLiteral) {
        IRNumericLiteralCompiler.compile((IRNumericLiteral) expression, context);
      } else if (expression instanceof IRCharacterLiteral ) {
        IRCharacterLiteralCompiler.compile((IRCharacterLiteral) expression, context);
      } else if (expression instanceof IRStringLiteralExpression) {
        IRStringLiteralExpressionCompiler.compile((IRStringLiteralExpression) expression, context);
      } else if (expression instanceof IRNewArrayExpression) {
        IRNewArrayExpressionCompiler.compile((IRNewArrayExpression) expression, context);
      } else if (expression instanceof IRArrayLengthExpression) {
        IRArrayLengthExpressionCompiler.compile((IRArrayLengthExpression) expression, context);
      } else if (expression instanceof IRCastExpression) {
        IRCastExpressionCompiler.compile((IRCastExpression) expression, context);
      } else if (expression instanceof IRNewExpression) {
        IRNewExpressionCompiler.compile((IRNewExpression) expression, context);
      } else if (expression instanceof IRRelationalExpression) {
        IRRelationalExpressionCompiler.compile((IRRelationalExpression) expression, context);
      } else if (expression instanceof IRClassLiteral) {
        IRClassLiteralCompiler.compile((IRClassLiteral) expression, context);
      } else if (expression instanceof IRNegationExpression) {
        IRNegationExpressionCompiler.compile((IRNegationExpression) expression, context);
      } else if (expression instanceof IRNotExpression) {
        IRNotExpressionCompiler.compile((IRNotExpression) expression, context);
      } else if (expression instanceof IRConditionalAndExpression) {
        IRConditionalAndExpressionCompiler.compile((IRConditionalAndExpression) expression, context);
      } else if (expression instanceof IRConditionalOrExpression) {
        IRConditionalOrExpressionCompiler.compile((IRConditionalOrExpression) expression, context);
      } else if (expression instanceof IRInstanceOfExpression) {
        IRInstanceOfExpressionCompiler.compile((IRInstanceOfExpression) expression, context);
      } else if (expression instanceof IRNewMultiDimensionalArrayExpression) {
        IRNewMultiDimensionalArrayExpressionCompiler.compile((IRNewMultiDimensionalArrayExpression) expression, context);
      } else if (expression instanceof IRNoOpExpression) {
        // Do nothing
      } else {
        throw new IllegalArgumentException("Unrecognized expression of type " + expression.getClass());
      }
    }
    finally
    {
      context.setLineNumber( previousLineNumber );
    }
  }
}
