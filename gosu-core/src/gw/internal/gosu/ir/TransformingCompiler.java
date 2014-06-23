/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir;

import gw.internal.gosu.compiler.DebugFlag;
import gw.internal.gosu.ir.compiler.bytecode.IRClassCompiler;
import gw.internal.gosu.ir.compiler.java.IRJavaCompiler;
import gw.internal.gosu.ir.compiler.verifier.IRTreeVerifier;
import gw.internal.gosu.ir.transform.GosuClassTransformer;
import gw.internal.gosu.ir.transform.GosuFragmentTransformer;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.fragments.GosuFragment;
import gw.lang.ir.IRClass;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.reflect.gs.ICompilableType;

public class TransformingCompiler
{
  public static byte[] compileClass( ICompilableType gsClass, boolean debug )
  {
    IRClass irClass = compileType( gsClass );
    return _compileClass( gsClass, debug, irClass );
  }

  private static byte[] _compileClass( ICompilableType gsClass, boolean debug, IRClass irClass )
  {
    if( BytecodeOptions.isTreeVerifcationEnabled() )
    {
      IRTreeVerifier verifier = new IRTreeVerifier();
      verifier.verifyClassStatement( irClass );
      if( !verifier.getErrors().isEmpty() )
      {
        verifier.printErrors();
      }
    }

    if( debug || DebugFlag.getDebugFlags().contains( DebugFlag.TRACE ) ||
        BytecodeOptions.shouldDebug( gsClass.getName() ) )
    {
      IRJavaCompiler javaCompiler = new IRJavaCompiler();
      javaCompiler.compileClassStatement( irClass );
      System.out.println( javaCompiler.getOutput() );
    }

    return IRClassCompiler.compileClass( irClass, debug );
  }

  private static IRClass compileType( ICompilableType gsClass )
  {
    if( gsClass instanceof IGosuClassInternal )
    {
      return GosuClassTransformer.compile( (IGosuClassInternal)gsClass );
    }
    else if( gsClass instanceof GosuFragment )
    {
      return GosuFragmentTransformer.transform( (GosuFragment)gsClass );
    }
    else
    {
      throw new IllegalArgumentException( "Don't know how to transform " + gsClass.getClass() );
    }
  }
}