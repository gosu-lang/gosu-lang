/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.compiler.NamedLabel;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRCompilerLocalVar;
import gw.internal.gosu.ir.compiler.bytecode.IRFinallyCodePartitioner;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.ir.statement.IRCatchClause;
import gw.lang.ir.IRType;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

import java.util.Collections;
import java.util.List;

public class IRTryCatchFinallyStatementCompiler extends AbstractBytecodeCompiler
{

  private IRFinallyCodePartitioner _finallyPartitioner;
  private IRTryCatchFinallyStatement _stmt;
  private IRBytecodeContext _context;

  private IRTryCatchFinallyStatement _stmt() {
    return _stmt;
  }

  public static void compile( IRTryCatchFinallyStatement stmt, IRBytecodeContext context )
  {
    IRTryCatchFinallyStatementCompiler compiler = new IRTryCatchFinallyStatementCompiler( stmt, context );
    compiler.compile();
  }

  private IRTryCatchFinallyStatementCompiler( IRTryCatchFinallyStatement stmt, IRBytecodeContext context )
  {
    _stmt = stmt;
    _context = context;
  }

  private void compile()
  {
    MethodVisitor mv = _context.getMv();

    Label tryStart = new NamedLabel( "TryStart" );
    Label tryEnd = new NamedLabel( "TryEnd" );
    Label tryCatchFinallyEnd = new NamedLabel( "TryCatchFinallyEnd" );
    List<IRCatchClause> catchStmts = _stmt().getCatchStatements();

    _finallyPartitioner = pushFinallyStmt();
    try
    {
      compileTryStatement( mv, tryStart, tryEnd, tryCatchFinallyEnd );

      if( catchStmts != null && !catchStmts.isEmpty() )
      {
        compileCatchStatements( mv, tryStart, tryEnd, tryCatchFinallyEnd, catchStmts );
      }
      if( hasFinally() )
      {
        compileFinallyStatement( mv, tryStart );
      }
      mv.visitLabel( tryCatchFinallyEnd );
    }
    finally
    {
      popFinallyStmt( _finallyPartitioner );
    }
  }

  private void compileTryStatement( MethodVisitor mv, Label tryStart, Label tryEnd, Label tryCatchEnd )
  {
    mv.visitLabel( tryStart );
    IRBytecodeCompiler.compileIRStatement( _stmt().getTryBody(), _context );
    inlineLocalFinallyStmt( _stmt().getTryBody(), tryCatchEnd );
    mv.visitLabel( tryEnd );
  }

  private void compileCatchStatements( MethodVisitor mv, Label tryStart, Label tryEnd, Label tryCatchEnd, List<IRCatchClause> catchStmts )
  {
    for( int i = 0; i < catchStmts.size(); i++ )
    {
      IRCatchClause catchStmt = catchStmts.get( i );
      _context.pushScope( ); // scope for the exception param
      try
      {
        IRType type = catchStmt.getIdentifier().getType();
        Label catchStart = new NamedLabel( "CatchStart for " + (type == null ? "(no type)" : type.getName()) );
        _context.visitLabel( catchStart );

        declareCatchExtents( mv, tryStart, tryEnd, catchStart, type );

        assignExceptionParam( mv, catchStmt.getIdentifier() );
        IRBytecodeCompiler.compileIRStatement( catchStmt.getBody(), _context );

        inlineLocalFinallyStmt( catchStmt.getBody(), tryCatchEnd );
      }
      finally
      {
        _context.popScope();
      }
    }
  }

  private void declareCatchExtents( MethodVisitor mv, Label tryStart, Label coverageEnd, Label handlerStart, IRType type )
  {
    Label start = tryStart;
    Label end;
    List<Label> starts = _finallyPartitioner == null ? Collections.<Label>emptyList() : _finallyPartitioner.getFinallyStarts();
    List<Label> ends = _finallyPartitioner == null ? Collections.<Label>emptyList() : _finallyPartitioner.getFinallyEnds();
    for( int i = 0; i < starts.size(); i++ )
    {
      end = starts.get( i );
      if( end.getOffset() > coverageEnd.getOffset() )
      {
        break;
      }
      insertTryCatchBlock( mv, handlerStart, type, start, end );
      start = ends.get( i );
    }
    end = coverageEnd;
    insertTryCatchBlock( mv, handlerStart, type, start, end );
  }

  private void insertTryCatchBlock( MethodVisitor mv, Label handlerStart, IRType type, Label start, Label end )
  {
    if( start.getOffset() > end.getOffset() )
    {
      throw new IllegalStateException( "start label must precede the end label" );
    } else if (start.getOffset() != end.getOffset()) { // If start == end, then we want to ignore it and not generate any exception table entry
      mv.visitTryCatchBlock( start, end, handlerStart, type.getSlashName() );
    }
  }

  private void assignExceptionParam( MethodVisitor mv, IRSymbol exceptionSym )
  {
    IRCompilerLocalVar exceptionVar = _context.getLocalVar(exceptionSym);
    mv.visitVarInsn( Opcodes.ASTORE, exceptionVar.getIndex() );
  }

  private void compileFinallyStatement( MethodVisitor mv, Label tryStart )
  {
    Label finallyStart = new NamedLabel( "Finally Start ");
    mv.visitLabel( finallyStart );
    declareCatchExtents( mv, tryStart, finallyStart, finallyStart, JavaClassIRType.get( Throwable.class ));
    int iExceptionIndex = _context.getLocalVar( new IRSymbol("**temp**throwable**", JavaClassIRType.get( Throwable.class ), true ) ).getIndex();
    mv.visitVarInsn( Opcodes.ASTORE, iExceptionIndex );

    IRBytecodeCompiler.compileIRStatement( _stmt().getFinallyBody(), _context );

    if( _stmt().getFinallyBody().getLeastSignificantTerminalStatement() == null )
    {
      mv.visitVarInsn( Opcodes.ALOAD, iExceptionIndex );
      mv.visitInsn( Opcodes.ATHROW );
    }
  }

  private void inlineLocalFinallyStmt( IRStatement tryOrCatchStmt, Label labelEnd )
  {
    MethodVisitor mv = _context.getMv();

    if( tryOrCatchStmt.getLeastSignificantTerminalStatement() == null )
    {
      if( hasFinally() )
      {
        _finallyPartitioner.inlineFinally();
        NamedLabel endLabel = new NamedLabel( "EndFinally" + _finallyPartitioner.getFinallyEnds().size() );
        _context.visitLabel( endLabel );
        _finallyPartitioner.endInlineFinally( endLabel );
      }

      // Also jump to end of finally
      mv.visitJumpInsn( Opcodes.GOTO, labelEnd );
    }
  }

  private void popFinallyStmt( IRFinallyCodePartitioner partition )
  {
    if( hasFinally() )
    {
      _context.popFinallyStatement( partition );
    }
  }

  private IRFinallyCodePartitioner pushFinallyStmt()
  {
    if( hasFinally() )
    {
      return _context.pushFinallyStatement( _stmt() );
    }
    else
    {
      return null;
    }
  }

  private boolean hasFinally()
  {
    return _stmt().getFinallyBody() != null;
  }

}
