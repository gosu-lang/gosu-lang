/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.util.GosuTestUtil;
import gw.lang.parser.exceptions.ParseResultsException;

/**
 */
public class ForeachStatementIteratorTest extends ByteCodeTestBase
{
  public void testHasForeachStringNoIndex() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachStringNoIndex" );
    String ret = (String)val;
    assertEquals( "abc", ret );
  }

  public void testHasForeachString() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachString" );
    String ret = (String)val;
    assertEquals( "a0b1c2", ret );
  }

  public void testHasForeachInt() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachInt" );
    String ret = (String)val;
    assertEquals( "506172", ret );
  }

  public void testHasForeachBreak() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachBreak" );
    String ret = (String)val;
    assertEquals( "01", ret );
  }

  public void testHasForeachContinue() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachContinue" );
    String ret = (String)val;
    assertEquals( "02", ret );
  }

  public void testHasForeachNeverEnterBody() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachNeverEnterBody" );
    String ret = (String)val;
    assertEquals( "", ret );
  }

  public void testHasForeachTerminal() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachTerminal" );
    String ret = (String)val;
    assertEquals( "pass", ret );
  }

  public void testHasForeachWithThrow() throws Exception
  {
    Object obj = newForeachClass_iterator();

    try
    {
      Object val = invokeMethod( obj,  "hasForeachWithThrow" );
    }
    catch( RuntimeException ite )
    {
      assertEquals( "yay", ite.getCause().getCause().getMessage() );
      return;
    }
    fail();
  }

  public void testHasForeachCharacters() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachCharacters" );
    String ret = (String)val;
    assertEquals( "abc", ret );
  }

  public void testHasForeachNull() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachNull" );
    String ret = (String)val;
    assertEquals( "pass", ret );
  }

  public void testHasForeachIntNumber() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachIntNumber" );
    String ret = (String)val;
    assertEquals( "0123456789", ret );
  }

  public void testHasForeachBigInteger() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachBigInteger" );
    String ret = (String)val;
    assertEquals( "0123456789", ret );
  }

  public void testHasForeachIterator() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachIterator" );
    String ret = (String)val;
    assertEquals( "abc", ret );
  }

  public void testHasForeachWithEval() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachWithEval" );
    String ret = (String)val;
    assertEquals( "a0b1c2", ret );
  }

  public void testHasForeachWithIntegerInterval() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachWithIntegerInterval" );
    String ret = (String)val;
    assertEquals( "012345", ret );
  }

  public void testHasForeachWithIntegerInterval_NoLoopVar() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachWithIntegerInterval_NoLoopVar" );
    Integer ret = (Integer)val;
    assertEquals( 6, ret.intValue() );
  }

  public void testHasForeachWithComplexIntegerInterval_NoLoopVar() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachWithComplexIntegerInterval_NoLoopVar" );
    Integer ret = (Integer)val;
    assertEquals( 3, ret.intValue() );
  }

  public void testHasForeachWithLongInterval() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "hasForeachWithLongInterval" );
    String ret = (String)val;
    assertEquals( "012345", ret );
  }

  public void testGenericIterator() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "testGenericIterator" );
    String ret = (String)val;
    assertEquals( "hibye", ret );
  }

  public void testStructuralIterable() throws Exception
  {
    Object obj = newForeachClass_iterator();

    Object val = invokeMethod( obj,  "testStructuralIterable" );
    String ret = (String)val;
    assertEquals( "abc", ret );
  }

  public void testIndexCannotHaveSameNameAsLoopVar() {
    try
    {
      Object val = GosuTestUtil.eval( "for(i in 1..10 index i){return i}" );
      fail( "Shouldn't have parsed!" );
    }
    catch( RuntimeException e )
    {
      assertTrue( e.getCause() instanceof ParseResultsException );
    }
  }

  private Object newForeachClass_iterator() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.HasForeachStatement_Iterator";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}