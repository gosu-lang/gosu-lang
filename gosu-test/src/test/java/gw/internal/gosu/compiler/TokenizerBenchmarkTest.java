/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;


import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;

import java.util.Set;

/**
 */
public class TokenizerBenchmarkTest extends ByteCodeTestBase
{
  public void testTokenizerBenchmark() throws Exception
  {
//    IGosuClassInternal type = (IGosuClassInternal)TypeSystem.getByFullName( "gw.lang.enhancements.CoreIterableEnhancement" );
//    type.isValid();
//    ISourceFileHandle fsh = type.getSourceFileHandle();
//    ISourceCodeTokenizer tokenizer = fsh.getSource().getTokenizer();
//
//    long t = System.currentTimeMillis();
//    for( int i = 0; i < 100000; i++ )
//    {
//      while( !tokenizer.isEOF() )
//      {
//        tokenizer.nextToken();
//      }
//      tokenizer.reset();
//    }
//
//    long delta = System.currentTimeMillis() - t;
//    System.out.println( "Time: " + delta / 1000.0 );
  }

  public void testTokenizerWithParsingBenchmark() throws Exception
  {
    ITypeLoader loader = TypeSystem.getTypeLoader( GosuClassTypeLoader.class );
    Set<? extends CharSequence> allTypeNames = loader.getAllTypeNames();

    long t = System.currentTimeMillis();

    System.out.println( "TOTAL: " + allTypeNames.size() );
    for( CharSequence n : allTypeNames )
    {
      if( n.toString().contains( "Errant" ) )
      {
        continue;
      }
      IType type = TypeSystem.getByFullName( n.toString() );
      type.isValid();
    }

    long delta = System.currentTimeMillis() - t;
    System.out.println( "Time: " + delta / 1000.0 );
  }
}
