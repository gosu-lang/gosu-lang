/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.IRType;
import gw.lang.ir.statement.IREvalStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IREvalStatementCompiler extends AbstractBytecodeCompiler
{
  public static void compile( IREvalStatement statement, IRBytecodeContext context )
  {

    IRBytecodeCompiler.compileIRExpression( statement.getExpression(), context );

    IRType returnType = statement.getExpression().getType();
    if( !returnType.isVoid() )
    {
      context.getMv().visitInsn( getIns( Opcodes.POP, returnType ) );
    }
  }
}
