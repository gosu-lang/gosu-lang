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
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRCaseClause;
import gw.lang.ir.statement.IRSwitchStatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IRSwitchStatementCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRSwitchStatement statement, IRBytecodeContext context ) {
    if( statement.areLabelsConstant() ) {
      compileWithTableSwitch( statement, context );
      return;
    }

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

  private static ConstantCase[] generateConstantCases( IRSwitchStatement switchStmt )
  {
    List<IRCaseClause> caseClauses = switchStmt.getCases();
    ConstantCase[] cases = new ConstantCase[caseClauses.size()];
    int i = 0;
    while( i < caseClauses.size() )
    {
      cases[i] = new ConstantCase();
      if( i > 0 && caseClauses.get( i - 1 ).getStatements().size() == 0 )
      {
        cases[i].label = cases[i - 1].label;
      }
      else
      {
        cases[i].label = new Label();
      }
      cases[i].value = caseClauses.get( i ).getConstValue();
      cases[i].body = caseClauses.get( i ).getStatements();
      i++;
    }
    return cases;
  }

  private final static class ConstantCase implements Comparable<ConstantCase>
  {
    int value;
    Label label;
    List<IRStatement> body;

    @Override
    public int compareTo( ConstantCase o )
    {
      return Integer.compare( value, o.value );
    }
  }

  private static void compileWithTableSwitch( IRSwitchStatement switchStmt, IRBytecodeContext context )
  {
    MethodVisitor mv = context.getMv();
    Label defaultL = new Label();
    Label endSwitch = new Label();
    ConstantCase[] cases = generateConstantCases( switchStmt );
    ConstantCase[] sortedCases = Arrays.copyOf( cases, cases.length );
    Arrays.sort( sortedCases );
    IRStatement init = switchStmt.getInit();
    context.compile( init );
    context.compile( new IRIdentifier( ((IRAssignmentStatement)init).getSymbol() ) );
    mv.visitTableSwitchInsn( sortedCases[0].value, sortedCases[cases.length - 1].value, defaultL, collectLabels( defaultL, sortedCases ) );
    emitCaseBodies( endSwitch, cases, context );
    context.visitLabel( defaultL );
    List<IRStatement> defaultStmts = switchStmt.getDefaultStatements();
    if( defaultStmts.size() != 0 )
    {
      compileCaseBody( endSwitch, defaultStmts, context );
    }
    context.visitLabel( endSwitch );
  }

  private static void emitCaseBodies( Label endSwitch, ConstantCase[] cases, IRBytecodeContext context )
  {
    int i = 0;
    Set<Label> visited = new HashSet<>();
    while( i < cases.length )
    {
      if( !visited.contains( cases[i].label ) )
      {
        context.visitLabel( cases[i].label );
        visited.add( cases[i].label );
      }
      if( cases[i].body.size() != 0 )
      {
        compileCaseBody( endSwitch, cases[i].body, context );
      }
      i++;
    }
  }

  private static Label[] collectLabels( Label defaultL, ConstantCase[] cases )
  {
    int min = cases[0].value;
    int max = cases[cases.length - 1].value;
    Label[] all = new Label[max - min + 1];
    int i = 0;
    int j = 0;
    int lastMin = cases[0].value;
    while( j < cases.length )
    {
      while( lastMin < cases[j].value )
      {
        all[i] = defaultL;
        i++;
        lastMin++;
      }
      all[i] = cases[j].label;
      i++;
      lastMin++;
      j++;
    }
    return all;
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
