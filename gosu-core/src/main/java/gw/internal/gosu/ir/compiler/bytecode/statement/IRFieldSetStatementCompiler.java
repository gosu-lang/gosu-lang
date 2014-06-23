/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRFieldSetStatement;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRFieldSetStatementCompiler extends AbstractBytecodeCompiler {
  public static void compile(IRFieldSetStatement statement, IRBytecodeContext context) {

    int opCode;
    if ( statement.getLhs() != null ) {
      IRBytecodeCompiler.compileIRExpression( statement.getLhs(), context );
      opCode = Opcodes.PUTFIELD;
    } else {
      opCode = Opcodes.PUTSTATIC;
    }
    
    IRBytecodeCompiler.compileIRExpression( statement.getRhs(), context );

    MethodVisitor mv = context.getMv();
    mv.visitFieldInsn( opCode,
                       statement.getOwnersType().getSlashName(),
                       statement.getName(),
                       statement.getFieldType().getDescriptor() );
  }
}
