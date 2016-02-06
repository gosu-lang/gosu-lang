/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

public class AAFirstTest extends TestClass
{
  private static boolean _started;

  @Override
  public void beforeTestClass()
  {
    _started = true;
  }

  public static boolean hasStarted()
  {
    return _started;
  }
  
  public void testNothing() {} //No tests, this test is just to ensure ordering of tests is correct
    

}
