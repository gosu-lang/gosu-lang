/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.util.List;

/**
 */
public class SwitchStatementTest extends ByteCodeTestBase
{
  //
  // Single case
  //
  
  public void testMatchSingleCaseWithBreakWithDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testMatchSingleCaseWithBreakWithDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testMatchSingleCaseWithBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testMatchSingleCaseWithBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testMatchSingleCaseNoBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testMatchSingleCaseNoBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testNotMatchSingleCaseWithBreakWithDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testNotMatchSingleCaseWithBreakWithDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( -1, (int) ret.get( 0 ) );
  }

  public void testNotMatchSingleCaseWithBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testNotMatchSingleCaseWithBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 0, ret.size() );
  }

  public void testNotMatchSingleCaseNoBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testNotMatchSingleCaseNoBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 0, ret.size() );
  }


  //
  // Multi case
  //
  
  public void testMatchMultiCaseWithBreakWithDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testMatchMultiCaseWithBreakWithDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testMatchMultiCaseWithBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testMatchMultiCaseWithBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testMatchMultiCaseNoBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testMatchMultiCaseNoBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testNotMatchMultiCaseWithBreakWithDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testNotMatchMultiCaseWithBreakWithDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( -1, (int) ret.get( 0 ) );
  }

  public void testNotMatchMultiCaseWithBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testNotMatchMultiCaseWithBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 0, ret.size() );
  }

  public void testNotMatchMultiCaseNoBreakNoDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testNotMatchMultiCaseNoBreakNoDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 0, ret.size() );
  }

  
  //
  // Int Types
  //

  public void testByteSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testByteSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testCharSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testCharSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testShortSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testShortSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testIntSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testIntSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testIntSwitchByteCase() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testIntSwitchByteCase" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testByteSwitchIntCase() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testByteSwitchIntCase" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testIntSwitchStringCase() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testIntSwitchStringCase" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testStringSwitchIntCase() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testStringSwitchIntCase" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  //
  // Other types
  //

  public void testEnumSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testEnumSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testStringSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testStringSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testBooleanSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testBooleanSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }
  
  public void testIntegerSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testIntegerSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  public void testBigDecimalSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testBigDecimalSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }


  //
  // Fall through
  //

  public void testFallThroughFromTopWithBreakBeforeDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testFallThroughFromTopWithBreakBeforeDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 3, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
    assertEquals( (Integer) 9, ret.get( 1 ) );
    assertEquals( (Integer) 10, ret.get( 2 ) );
  }

  public void testFallThroughFromMiddleWithBreakBeforeDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testFallThroughFromMiddleWithBreakBeforeDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 3, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
    assertEquals( (Integer) 9, ret.get( 1 ) );
    assertEquals( (Integer) 10, ret.get( 2 ) );
  }

  public void testFallThroughAndIncludeDefault() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testFallThroughAndIncludeDefault" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 4, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
    assertEquals( (Integer) 9, ret.get( 1 ) );
    assertEquals( (Integer) 10, ret.get( 2 ) );
    assertEquals( -1, (int) ret.get( 3 ) );
  }

  public void testFallThroughNoBody() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testFallThroughNoBody" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 9, ret.get( 0 ) );
  }


  //
  // Misc.
  //
  
  public void testSwitch() throws Exception
  {
    Object obj = newSwitchClass();

    Object val = invokeMethod( obj,  "testSwitch" );
    List<Integer> ret = (List<Integer>)val;
    assertEquals( 1, ret.size() );
    assertEquals( (Integer) 8, ret.get( 0 ) );
  }

  
  private Object newSwitchClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.HasSwitchStatement";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}