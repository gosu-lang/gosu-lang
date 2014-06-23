/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.ext.org.objectweb.asm.Type;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRClassLiteralCompiler {
  public static void compile(IRClassLiteral irClassLiteral, IRBytecodeContext context) {
    IRType type = irClassLiteral.getLiteralType();

    // If it's a primitive, we need to reference the TYPE constant off of the boxed version of the type
    if (type.isPrimitive()) {
      IRType boxedType;
      if (type.isBoolean()) {
        boxedType = JavaClassIRType.get( Boolean.class );
      } else if (type.isByte()) {
        boxedType = JavaClassIRType.get( Byte.class );
      } else if (type.isChar()) {
        boxedType = JavaClassIRType.get( Character.class );
      } else if (type.isDouble()) {
        boxedType = JavaClassIRType.get( Double.class );
      } else if (type.isFloat()) {
        boxedType = JavaClassIRType.get( Float.class );
      } else if (type.isInt()) {
        boxedType = JavaClassIRType.get( Integer.class );
      } else if (type.isLong()) {
        boxedType = JavaClassIRType.get( Long.class );
      } else if (type.isShort()) {
        boxedType = JavaClassIRType.get( Short.class );
      } else if (type.isVoid()) {
        boxedType = JavaClassIRType.get( Void.class );
      } else {
        throw new IllegalStateException("Unexpected primitive type " + type.getName());
      }

      context.getMv().visitFieldInsn( Opcodes.GETSTATIC, boxedType.getSlashName(), "TYPE", "Ljava/lang/Class;" );
    } else {
      context.getMv().visitLdcInsn( Type.getType( irClassLiteral.getLiteralType().getDescriptor() ) );
    }
  }
}
