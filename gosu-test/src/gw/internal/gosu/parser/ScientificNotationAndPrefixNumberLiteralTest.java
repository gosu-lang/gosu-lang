/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;


import gw.lang.parser.exceptions.ParseResultsException;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.math.BigInteger;


public class ScientificNotationAndPrefixNumberLiteralTest extends TestClass {

  public void testBinaryLiteral()
  {
    Object a = GosuTestUtil.eval("var a = 0b1010 \n return a");
    assertTrue( a instanceof Integer);
    int num = ((Integer) a).intValue();
    assertEquals(num, 10);

    a = GosuTestUtil.eval("var a = 0b1010s \n return a");
    assertTrue( a instanceof Short);
    short sNum = ((Short) a).shortValue();
    assertEquals(sNum, 10);

    a = GosuTestUtil.eval("var a = 0b1010S \n return a");
    assertTrue( a instanceof Short);
    sNum = ((Short) a).shortValue();
    assertEquals(sNum, 10);

    a = GosuTestUtil.eval("var a = 0b1010l \n return a");
    assertTrue( a instanceof Long);
    long lNum = ((Long) a).longValue();
    assertEquals(lNum, 10);

    a = GosuTestUtil.eval("var a = 0b1010L \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 10);

    a = GosuTestUtil.eval("var a = 0b1010b \n return a");
    assertTrue( a instanceof Byte);
    byte bNum = ((Byte) a).byteValue();
    assertEquals(bNum, 10);

    a = GosuTestUtil.eval("var a = 0b1010B \n return a");
    assertTrue( a instanceof Byte);
    bNum = ((Byte) a).byteValue();
    assertEquals(bNum, 10);

    a = GosuTestUtil.eval("var a = 0b1010bi \n return a");
    assertTrue( a instanceof BigInteger);
    BigInteger ten = new BigInteger("10");
    BigInteger biNum = (BigInteger) a;
    assertEquals(biNum, ten);

    a = GosuTestUtil.eval("var a = 0b1010BI \n return a");
    assertTrue( a instanceof BigInteger);
    biNum = (BigInteger) a;
    assertEquals(biNum, ten);

    a = GosuTestUtil.eval("var a : float = 0b1010 \n return a");
    assertTrue( a instanceof Float);
    float fNum = ((Float) a).floatValue();
    assertEquals(fNum, 10.0f);

    try
    {
      GosuTestUtil.eval("var a = 0b1010f \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }

    try
    {
      GosuTestUtil.eval("var a = 0b1010d \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }

    try
    {
      GosuTestUtil.eval("var a = 0b1010bd \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }

    a = GosuTestUtil.eval("var a : int = 0b1010 \n return a");
    assertTrue( a instanceof Integer);
    num = ((Integer) a).intValue();
    assertEquals(num, 10);

    a = GosuTestUtil.eval("var a : short = 0b1010 \n return a");
    assertTrue( a instanceof Short);
    sNum = ((Short) a).shortValue();
    assertEquals(sNum, 10);

    a = GosuTestUtil.eval("var a : short = 0b1010S \n return a");
    assertTrue( a instanceof Short);
    sNum = ((Short) a).shortValue();
    assertEquals(sNum, 10);

    a = GosuTestUtil.eval("var a : long = 0b1010 \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 10);

    a = GosuTestUtil.eval("var a : long = 0b1010L \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 10);

    a = GosuTestUtil.eval("var a : byte = 0b1010 \n return a");
    assertTrue( a instanceof Byte);
    bNum = ((Byte) a).byteValue();
    assertEquals(bNum, 10);

    a = GosuTestUtil.eval("var a : byte = 0b1010B \n return a");
    assertTrue( a instanceof Byte);
    bNum = ((Byte) a).byteValue();
    assertEquals(bNum, 10);

  }

  public void testNumberLiteral()
  {
    Object a = GosuTestUtil.eval("var a  = 123 \n return a");
    assertTrue( a instanceof Integer);
    int num = ((Integer) a).intValue();
    assertEquals(num, 123);

    a = GosuTestUtil.eval("var a = 123l \n return a");
    assertTrue( a instanceof Long);
    long lNum = ((Long) a).longValue();
    assertEquals(lNum, 123);

    a = GosuTestUtil.eval("var a : long = 123 \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 123);

    a = GosuTestUtil.eval("var a : long = 9223372036854775807l \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 9223372036854775807L);

    a = GosuTestUtil.eval("var a : byte \n a = 0b1 \n return a");
    assertTrue( a instanceof Byte);
    byte bNum = ((Byte) a).byteValue();
    assertEquals(bNum, 1);
  }

  public void testHexLiteral()
  {
    Object a = GosuTestUtil.eval("var a = 0xCAFE \n return a");
    assertTrue( a instanceof Integer);
    int num = ((Integer) a).intValue();
    assertEquals(num, 0xCAFE);

    a = GosuTestUtil.eval("var a = 0xCAFEBABEl \n return a");
    assertTrue( a instanceof Long);
    long lNum = ((Long) a).longValue();
    assertEquals(lNum, 0xCAFEBABEl);

    a = GosuTestUtil.eval("var a : long = 0xCAFEBABEl \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 0xCAFEBABEl);

    a = GosuTestUtil.eval("var a : long = 0xCAFEBABECAFEL \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 0xCAFEBABECAFEL);

    a = GosuTestUtil.eval("var a = 0xCAFEBABECAFEL \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, 0xCAFEBABECAFEL);

    a = GosuTestUtil.eval("var a = 0x0CAFs \n return a");
    assertTrue( a instanceof Short);
    short sNum = ((Short) a).shortValue();
    assertEquals(sNum, 0x0CAF);

    a = GosuTestUtil.eval("var a : short = 0x0CAF \n return a");
    assertTrue( a instanceof Short);
    sNum = ((Short) a).shortValue();
    assertEquals(sNum, 0x0CAF);

    a = GosuTestUtil.eval("var a : short = 0x0CAFS \n return a");
    assertTrue( a instanceof Short);
    sNum = ((Short) a).shortValue();
    assertEquals(sNum, 0x0CAF);

    a = GosuTestUtil.eval("var a = -0xCAFE \n return a");
    assertTrue( a instanceof Integer);
    num = ((Integer) a).intValue();
    assertEquals(num, -0xCAFE);

    a = GosuTestUtil.eval("var a = -0xCAFEBABEl \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, -0xCAFEBABEl);

    a = GosuTestUtil.eval("var a : long = +0xCAFEBABEl \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, +0xCAFEBABEl);

    a = GosuTestUtil.eval("var a : long = -0xCAFEBABECAFEL \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, -0xCAFEBABECAFEL);

    a = GosuTestUtil.eval("var a = -0xCAFEBABECAFEL \n return a");
    assertTrue( a instanceof Long);
    lNum = ((Long) a).longValue();
    assertEquals(lNum, -0xCAFEBABECAFEL);

    a = GosuTestUtil.eval("var a = 0x7F \n return a");
    assertTrue( a instanceof Integer);
    num = ((Integer) a).intValue();
    assertEquals(num, 0x7F);

    a = GosuTestUtil.eval("var a = -0x7F \n return a");
    assertTrue( a instanceof Integer);
    num = ((Integer) a).intValue();
    assertEquals(num, -0x7F);

    a = GosuTestUtil.eval("var a = +0x7F \n return a");
    assertTrue( a instanceof Integer);
    num = ((Integer) a).intValue();
    assertEquals(num, +0x7F);

    a = GosuTestUtil.eval("var a : byte \n a = 0x7F \n return a");
    assertTrue( a instanceof Byte);
    byte bNum = ((Byte) a).byteValue();
    assertEquals(bNum, +0x7F);

    try
    {
      GosuTestUtil.eval("var a = 0xFbI \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }
  }

  public void testNotationWithSimilarToken()
  {
    Object a = GosuTestUtil.eval("var e1 = 0 \n var a : float = 1.0 \n e1 = 2 \n return a");
    assertTrue( a instanceof Float);
    float fNum = ((Float) a).floatValue();
    assertEquals(fNum, 1.0f);
  }

  public void testSimpleNotation()
  {
    Object a = GosuTestUtil.eval("var a : float = 1e2 \n return a");
    assertTrue( a instanceof Float);
    float fNum = ((Float) a).floatValue();
    assertEquals(fNum, 100.0f);

    a = GosuTestUtil.eval("var a : float = 1.0e2 \n return a");
    assertTrue( a instanceof Float);
    fNum = ((Float) a).floatValue();
    assertEquals(fNum, 100.0f);

    a = GosuTestUtil.eval("var a : float = 1.e2 \n return a");
    assertTrue( a instanceof Float);
    fNum = ((Float) a).floatValue();
    assertEquals(fNum, 100.0f);

    a = GosuTestUtil.eval("var a : double = 1e2 \n return a");
    assertTrue( a instanceof Double);
    double dNum = ((Double) a).doubleValue();
    assertEquals(dNum, 100.0d);
  }

  public void testNotationWithSigns()
  {
    Object a = GosuTestUtil.eval("var a : double = 1e+2 \n return a");
    assertTrue( a instanceof Double);
    double num = ((Double) a).doubleValue();
    assertEquals(num, 100.0);

    a = GosuTestUtil.eval("var a : double = 1e-2 \n return a");
    assertTrue( a instanceof Double);
    num = ((Double) a).doubleValue();
    assertEquals(num, 0.01);

    a = GosuTestUtil.eval("var a : double = -1e-2 \n return a");
    assertTrue( a instanceof Double);
    num = ((Double) a).doubleValue();
    assertEquals(num,-0.01);
  }

  public void testNotationWithFunctionName()
  {
    Object a =   GosuTestUtil.eval("function e1at() : double {return 1} \n var a : double = 1e+1 + e1at() \n return a");
    assertTrue( a instanceof Double);
    double num = ((Double) a).doubleValue();
    assertEquals(num, 11.0);
  }

  public void testWrongSign()
  {
    try
    {
      GosuTestUtil.eval("var a : double = 1e*2 \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }
  }

  public void testInvalidNumber()
  {
    try
    {
      GosuTestUtil.eval("var a : double = 1eat \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }
    try
    {
      GosuTestUtil.eval("var a : double = 0.0XA \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }
  }

  public void testScientificNotationInExpression()
  {
    Object a = GosuTestUtil.eval("var a : double = 1e2 * 3 - 150.8 \n return a");
    assertTrue( a instanceof Double);
    double num = ((Double) a).doubleValue();
    assertEquals(num, 149.2);
  }

  public void testScientificNotationWithSuffix()
  {
    Object a = GosuTestUtil.eval("var a = 1e2f \n return a");
    assertTrue( a instanceof Float);
    float fNum = ((Float) a).floatValue();
    assertEquals(fNum, 100.0f);

    a = GosuTestUtil.eval("var a : float = 1e+2F \n return a");
    assertTrue(a instanceof Float);
    fNum = ((Float) a).floatValue();
    assertEquals(fNum, 100.0f);

    a = GosuTestUtil.eval("var a = 1e2d \n return a");
    assertTrue( a instanceof Double);
    double dNum = ((Double) a).doubleValue();
    assertEquals(dNum, 100.0);

    a = GosuTestUtil.eval("var a : double = 1e+2D \n return a");
    assertTrue(a instanceof Double);
    dNum = ((Double) a).doubleValue();
    assertEquals(dNum, 100.0d);


  }

  public void testScientificNotationWithWrongSuffix()
  {
    try
    {
      GosuTestUtil.eval("var a : float = 1fe+2f \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }

    try
    {
      GosuTestUtil.eval("var a : float = 1.0fe+2f \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }

    try
    {
      GosuTestUtil.eval("var a : float = 1.fe+2f \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }

    try
    {
      GosuTestUtil.eval("var a : double = 1fe+2d \n return a");
      fail();
    }
    catch(RuntimeException e)
    {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof ParseResultsException);
    }
  }
}
