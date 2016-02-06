/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

public class ZBTest extends TestClass {
  public static int ORDER_CALLED = 0;
  
  public void testIt() {
    ORDER_CALLED = ZZLastTest.incrementOrder();
  }
}
