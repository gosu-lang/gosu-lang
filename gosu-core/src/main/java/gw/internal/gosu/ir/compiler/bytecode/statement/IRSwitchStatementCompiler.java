/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.statement.IRCaseClause;
import gw.lang.ir.statement.IRSwitchStatement;

import java.util.ArrayList;
import java.util.List;

public class IRSwitchStatementCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRSwitchStatement statement, IRBytecodeContext context ) {
    // Note, we don't use the lookupswitch or tableswitch instructions here.
    // Given the differences in Gosu's switch vs. Java's and the very
    // small gain in perf from the switch bytecode instructions (when they
    // can be applied), it doesn't seem worth the trouble... for now.
    MethodVisitor mv = context.getMv();

    context.compile( statement.getInit() );

    Label endSwitchLabel = new Label();

    int iCaseMatchedIndex = context.makeTempVar(IRTypeConstants.pBOOLEAN()).getIndex();
    mv.visitInsn( Opcodes.ICONST_0 );
    mv.visitVarInsn( Opcodes.ISTORE, iCaseMatchedIndex );

    // Generate the branch instructions corresponding with cases
    List<Label> caseBodyLabels = new ArrayList<Label>();
    List<IRCaseClause> cases = statement.getCases();
    for( IRCaseClause caseClause : cases ) {
      Label caseBodyLabel = new Label();
      caseBodyLabels.add( caseBodyLabel );
      // Check to see if the case matches
      context.compile( caseClause.getCondition() );
      // If matches, Jump to the code corresponding with the case
      mv.visitJumpInsn( Opcodes.IFNE, caseBodyLabel );
    }

    // Jump directly to the default label if no cases matches
    Label defaultLabel = new Label();
    mv.visitJumpInsn( Opcodes.GOTO, defaultLabel );

    // Generate and label the case bodies
    for( int i = 0; i < cases.size(); i++ ) {
      IRCaseClause caseClause = cases.get( i );
      Label caseBodyLabel = caseBodyLabels.get( i );

      context.visitLabel( caseBodyLabel );
      compileCaseBody( endSwitchLabel, caseClause.getStatements(), context );
    }

    // Generate and label the default body
    context.visitLabel( defaultLabel );
    compileCaseBody( endSwitchLabel, statement.getDefaultStatements(), context );
    context.visitLabel( endSwitchLabel );
  }

  private static void compileCaseBody( Label endSwitchLabel, List<IRStatement> statements, IRBytecodeContext context ) {
    // Push a scope so that locals don't bleed through to the next case
    context.pushBreakLabel( endSwitchLabel );
    context.pushScope();
    try {
      for( IRStatement caseStatement : statements ) {
        context.compile( caseStatement );
      }
    }
    finally {
      context.popScope();
      context.popBreakLabel();
    }
  }
}
