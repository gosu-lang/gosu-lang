/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class MethodChainingTest extends TestClass
{
  int _i = 0;

  public static MethodChainingTest start() {
    return new MethodChainingTest();
  }

  public MethodChainingTest chainedCall()
  {
    _i++;
    return this;
  }

  public void testMethodChaining() {
    doTest( 1, 50 );
    doTest( 2, 50 );
    doTest( 5, 50 );
    doTest( 7, 50 );
    doTest( 13, 50 );
  }

  public void doTest( int varEvery, int iterations )
  {
    for( int i = 0; i < iterations; i++ )
    {
      int previousX = 0;
      StringBuilder sb = new StringBuilder( "var x0 = gw.internal.gosu.parser.annotation.MethodChainingTest.start()" );
      for( int j = 0; j < i; j++ )
      {
        if( j != 0 && j % varEvery == 0 )
        {
          sb.append( "\nvar x" + j + " = x" + previousX );
          previousX = j;
        }
        sb.append( ".chainedCall()" );
      }
      sb.append( "\nreturn x" + previousX );
      System.out.println( "Evaling " + sb.toString() );
      MethodChainingTest o = (MethodChainingTest)GosuTestUtil.eval( sb.toString() );
      assertEquals( i, o._i );
    }
  }
}
