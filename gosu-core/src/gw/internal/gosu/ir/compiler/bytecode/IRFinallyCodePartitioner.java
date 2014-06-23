/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.gosu.compiler.NamedLabel;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRBreakStatement;
import gw.lang.ir.statement.IRContinueStatement;
import gw.lang.ir.statement.IRLoopStatement;
import gw.lang.ir.statement.IRTerminalStatement;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.ir.statement.IRSwitchStatement;
import gw.internal.ext.org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;

public class IRFinallyCodePartitioner
{
  private IRBytecodeContext _context;
  private IRTryCatchFinallyStatement _tryCatchFinallyStmt;
  private List<Label> _finallyStarts;
  private List<Label> _finallyEnds;

  public IRFinallyCodePartitioner( IRBytecodeContext context, IRTryCatchFinallyStatement tryCatchFinallyStmt )
  {
    _context = context;
    _tryCatchFinallyStmt = tryCatchFinallyStmt;
    _finallyStarts = new ArrayList<Label>();
    _finallyEnds = new ArrayList<Label>();
  }

  public String toString()
  {
    return _tryCatchFinallyStmt.getFinallyBody().toString();
  }

  public void inlineFinally()
  {
    Label startLabel = new NamedLabel( "FinallyStart" + _finallyStarts.size() );
    _finallyStarts.add( startLabel );
    _context.visitLabel( startLabel );
    IRBytecodeCompiler.compileIRStatement( _tryCatchFinallyStmt.getFinallyBody(), _context );
  }

  public List<Label> getFinallyStarts() {
    return _finallyStarts;
  }

  public List<Label> getFinallyEnds() {
    return _finallyEnds;
  }

  public boolean appliesTo( IRTerminalStatement elt )
  {
    return applies( _tryCatchFinallyStmt, elt );
  }

  public static boolean applies( IRStatement statement, IRTerminalStatement terminal )
  {
    if( isContainedControlFlow( terminal, statement ) )
    {
      return false;
    }
    return true;
  }

  private static boolean isContainedControlFlow( IRTerminalStatement elt, IRStatement stmt )
  {
    if( elt instanceof IRBreakStatement || elt instanceof IRContinueStatement)
    {
      return elementIsEnclosedBy( (IRElement) elt, IRLoopStatement.class, stmt ) ||
             elementIsEnclosedBy( (IRElement) elt, IRSwitchStatement.class, stmt );
    }
    else
    {
      return false;
    }
  }

  private static boolean elementIsEnclosedBy( IRElement elt, Class enclosedType, IRStatement stmt )
  {
    if( elt == null || elt == stmt )
    {
      return false;
    }
    else if( enclosedType.isAssignableFrom( elt.getClass() ) )
    {
      return true;
    }
    else
    {
      return elementIsEnclosedBy( elt.getParent(), enclosedType, stmt );
    }
  }

  public void endInlineFinally( Label endLabel )
  {
    _finallyEnds.add( endLabel );
  }

}