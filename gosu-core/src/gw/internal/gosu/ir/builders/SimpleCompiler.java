/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.builders;

import gw.lang.ir.IRClass;
import gw.internal.gosu.ir.compiler.bytecode.IRClassCompiler;
import gw.internal.gosu.ir.compiler.java.IRJavaCompiler;
import gw.internal.gosu.ir.compiler.verifier.IRTreeVerifier;

public class SimpleCompiler implements gw.lang.ir.IRClassCompiler {

  public static SimpleCompiler INSTANCE = new SimpleCompiler();

  private SimpleCompiler() {
  }


  public byte[] compile( IRClass irClass, boolean debug ) {
    IRTreeVerifier verifier = new IRTreeVerifier();
    verifier.verifyClassStatement(irClass);
    if (!verifier.getErrors().isEmpty()) {
      verifier.printErrors();
    }

    if( debug )
    {
      IRJavaCompiler javaCompiler = new IRJavaCompiler();
      javaCompiler.compileClassStatement( irClass );
      System.out.println( javaCompiler.getOutput() );
    }

    return IRClassCompiler.compileClass( irClass, debug );
  }

  @Override
  public String compileToJava(IRClass irClass) {
    IRJavaCompiler javaCompiler = new IRJavaCompiler();
    javaCompiler.compileClassStatement( irClass );
    return javaCompiler.getOutput().toString();
  }
}