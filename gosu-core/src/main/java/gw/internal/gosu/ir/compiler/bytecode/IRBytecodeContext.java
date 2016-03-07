/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.gosu.compiler.NamedLabel;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.statement.IRTerminalStatement;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.util.GosuExceptionUtil;
import gw.util.Stack;

import java.util.ArrayList;
import java.util.List;

public class IRBytecodeContext {
  private MethodVisitor _mv;
  private Stack<IRCompilerScope> _scopes;
  private List<IRCompilerLocalVar> _allLocalVars;
  private Stack<IRFinallyCodePartitioner> _finallyStatements;
  private int _tempVarCount;
  private Stack<Label> _breakLabels;
  private Stack<Label> _continueLabels;
  private Label _lastVisitedLabel;
  private int _lastLineNumber;

  public IRBytecodeContext(MethodVisitor mv) {
    _mv = mv;
    _scopes = new Stack<IRCompilerScope>();
    pushScope();
    _allLocalVars = new ArrayList<IRCompilerLocalVar>();
    _finallyStatements = new Stack<IRFinallyCodePartitioner>();
    _breakLabels = new Stack<Label>();
    _continueLabels = new Stack<Label>();
    _lastLineNumber = -1;
  }

  public MethodVisitor getMv() {
    return _mv;
  }

  public void visitLabel(Label label) {
    _lastVisitedLabel = label;
    _mv.visitLabel( label );

    for( IRCompilerLocalVar lv : _allLocalVars )
    {
      if( lv.getStartLabel() == null )
      {
        lv.setStartLabel( label );
      }
      if( isOutOfScope( lv ) && lv.getEndLabel() == null )
      {
        lv.setEndLabel( label );
      }
    }
  }

  public int getLocalCount()
  {
    return _allLocalVars.size();
  }

  public int getMaxScopeSize()
  {
    int iMax = 0;
    for( IRCompilerLocalVar local : _allLocalVars )
    {
      iMax = Math.max( local.getScope().getLocalVars().size(), iMax );
    }
    return iMax;
  }

  private boolean isOutOfScope( IRCompilerLocalVar lv )
  {
    return !lv.getScope().isActive();
  }

  public void visitLocalVars()
  {

    for( IRCompilerLocalVar lv : _allLocalVars )
    {
      if( !lv.isTemp() )
      {
        try {
          if (!lv.getStartLabel().equals(lv.getEndLabel())) {
            _mv.visitLocalVariable(
                    lv.getName(), lv.getType().getDescriptor(), null,
                    lv.getStartLabel(), lv.getEndLabel(), lv.getIndex());
          }
        }
        catch( Exception e )
        {
          throw GosuExceptionUtil.forceThrow( e, lv.getName() );
        }
      }
    }
  }

  public void pushScope() {
    _scopes.push(new IRCompilerScope(_scopes.isEmpty() ? null : _scopes.peek()));
  }

  public void popScope() {
    IRCompilerScope oldScope = _scopes.pop();
    oldScope.scopeRemoved();
  }

  public void indexThis(IRType type) {
    Label label = new Label();
    visitLabel( label );
    IRCompilerLocalVar thisVar = getLocalVar( new IRSymbol( "this", type, false ) );
    thisVar.setStartLabel( label );
  }

  public void indexSymbols(List<IRSymbol> symbols) {
    for (IRSymbol symbol : symbols) {
      getLocalVar( symbol );
    }
  }

  public IRCompilerLocalVar getLocalVar(IRSymbol symbol) {
    IRCompilerLocalVar localVar = _scopes.peek().findLocalVar( symbol );
    if (localVar == null) {
      localVar = _scopes.peek().createLocalVar(symbol);
//## note: We don't assign the start label here because local vars are not in scope until after their declaration
//##       The start label is assigned during in visitLabel() above.
//
//      if (_lastVisitedLabel != null) {
//        localVar.setStartLabel(_lastVisitedLabel);
//      }
      _allLocalVars.add( localVar );
    }
    return localVar;
  }

  public IRCompilerLocalVar makeTempVar(IRType type) {
    return getLocalVar( new IRSymbol( "$$compilertemp$$" + (_tempVarCount++), type, true) );
  }

  public IRFinallyCodePartitioner pushFinallyStatement( IRTryCatchFinallyStatement tryCatchFinallyStmt )
  {
    IRFinallyCodePartitioner partition = new IRFinallyCodePartitioner( this, tryCatchFinallyStmt );
    _finallyStatements.push( partition );

    return partition;
  }

  public void popFinallyStatement( IRFinallyCodePartitioner partition )
  {
    IRFinallyCodePartitioner popped = _finallyStatements.pop();
    if( popped != partition )
    {
      throw new IllegalStateException(
        "Finally statements out of order. " +
        "Expected '" + partition + "', but got '" + popped );
    }
  }
  public boolean hasFinallyStatements()
  {
    return !_finallyStatements.isEmpty();
  }

  public Stack<IRFinallyCodePartitioner> getFinallyParitioners()
  {
    return _finallyStatements;
  }

  public IRFinallyCodePartitioner peekFinallyPartitioner() {
    return _finallyStatements.peek();
  }

  public void inlineFinallyStatements( IRTerminalStatement stmt )
  {
    if( !hasFinallyStatements() )
    {
      return;
    }

    Stack<IRFinallyCodePartitioner> partitions = getFinallyParitioners();
    List<IRFinallyCodePartitioner> inlinedFinallys = new ArrayList<IRFinallyCodePartitioner>();
    for( int i = partitions.size() - 1; i >= 0; i-- )
    {
      IRFinallyCodePartitioner partition = partitions.get( i );
      if( !partition.appliesTo( stmt ) )
      {
        // once a try block does not apply to a point, no enclosing try blocks do
        break;
      }
      partition.inlineFinally();
      inlinedFinallys.add( partition );
    }

    // stop the finally coverage for all nested finally's at the very end of the inlined finally statements
    NamedLabel endLabel = new NamedLabel( "EndFinally" );
    visitLabel( endLabel );
    for( IRFinallyCodePartitioner inlinedFinally : inlinedFinallys )
    {
      inlinedFinally.endInlineFinally( endLabel );
    }
  }

  public void compile( IRElement element ) {
    IRBytecodeCompiler.compileIRElement( element, this );
  }

  public void pushBreakLabel( Label label ) {
    _breakLabels.push( label );
  }

  public void popBreakLabel() {
    _breakLabels.pop();
  }

  public void pushContinueLabel( Label label ) {
    _continueLabels.push( label );
  }

  public void popContinueLabel() {
    _continueLabels.pop();
  }

  public Label getCurrentBreakLabel() {
    return _breakLabels.peek();
  }

  public Label getCurrentContinueLabel() {
    return _continueLabels.peek();
  }

  public int setLineNumber( int lineNumber )
  {
    int lastLineNumber = _lastLineNumber;
    if( lineNumber > 0 && lineNumber != lastLineNumber )
    {
      MethodVisitor mv = getMv();
      Label label = new Label();
      visitLabel( label );
      mv.visitLineNumber( lineNumber, label );
      _lastLineNumber = lineNumber;
    }
    return lastLineNumber;
  }
}
