/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.coercion;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.Res;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 */
public class CrossCastTest extends TestClass
{
  public void testCrossCastClassToInterface() throws ParseResultsException
  {
    try
    {
      GosuTestUtil.compileExpression( "new java.awt.Button() as java.lang.Runnable" );
    }
    catch( ParseResultsException e )
    {
      fail( "Should have accepted cast from Button to Runnable" );
    }
  }
  
  public void testErrantCrossCastPrimitiveToInterface() throws ParseResultsException
  {
    try
    {
      GosuTestUtil.compileExpression( "0 as int as java.lang.Runnable" );
    }
    catch( ParseResultsException e )
    {
      assertEquals( 1, e.getParseExceptions().size() );
      assertEquals( Res.MSG_TYPE_MISMATCH, e.getParseExceptions().get( 0 ).getMessageKey() );
    }
  }

  public void testErrantCrossCastFinalToInterface() throws ParseResultsException
  {
    try
    {
      GosuTestUtil.compileExpression( "new java.lang.StringBuilder() as java.lang.Runnable" );
    }
    catch( ParseResultsException e )
    {
      assertEquals( 1, e.getParseExceptions().size() );
      assertEquals( Res.MSG_TYPE_MISMATCH, e.getParseExceptions().get( 0 ).getMessageKey() );
    }
  }
}
