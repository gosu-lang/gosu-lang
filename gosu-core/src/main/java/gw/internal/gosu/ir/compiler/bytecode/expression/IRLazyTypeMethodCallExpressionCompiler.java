/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.ext.org.objectweb.asm.Handle;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Type;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.reflect.IType;
import gw.lang.reflect.LazyTypeResolver;
import gw.lang.ir.expression.IRLazyTypeMethodCallExpression;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class IRLazyTypeMethodCallExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRLazyTypeMethodCallExpression expression, IRBytecodeContext context ) {
    try {
      MethodType mt = MethodType.methodType( CallSite.class,MethodHandles.Lookup.class, String.class, MethodType.class, MethodType.class, MethodHandle.class, MethodType.class );
      Handle bootstrap = new Handle( Opcodes.H_INVOKESTATIC, LambdaMetafactory.class.getName().replace( '.', '/' ), "metafactory",
                                     mt.toMethodDescriptorString() );
      Type resolveDesc = Type.getType( LazyTypeResolver.ITypeResolver.class.getDeclaredMethod( "resolve" ) );
      context.getMv().visitInvokeDynamicInsn( "resolve",
                                              Type.getMethodDescriptor( Type.getType( LazyTypeResolver.ITypeResolver.class ), getAnonCtorParams( expression ) ),
                                              bootstrap,
                                                resolveDesc,
                                                new Handle( expression.isStatic() ? Opcodes.H_INVOKESTATIC : Opcodes.H_INVOKESPECIAL,
                                                            expression.getOwnersType().getSlashName(), expression.getName(), makeDescriptor( expression.getFunctionTypeParamCount() ) ),
                                                resolveDesc );
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private static Type[] getAnonCtorParams( IRLazyTypeMethodCallExpression expression ) {
    boolean bStatic = expression.isStatic();
    Type[] params = new Type[(bStatic ? 0 : 1) + expression.getFunctionTypeParamCount()];
    int i = 0;
    if( !bStatic ) {
      params[0] = Type.getType( expression.getOwnersType().getDescriptor() );
      i = 1;
    }
    Type typeResClass = Type.getType( LazyTypeResolver.class );
    for( ; i < params.length; i++ ) {
      params[i] = typeResClass;
    }
    return params;
  }

  private static String makeDescriptor( int iFunctionTypeParamCount ) {
    Type[] params = new Type[iFunctionTypeParamCount];
    Type typeResClass = Type.getType( LazyTypeResolver.class );
    for( int i = 0; i < params.length; i++ ) {
      params[i] = typeResClass;
    }
    return Type.getMethodDescriptor( Type.getType( IType.class ), params );
  }
}
