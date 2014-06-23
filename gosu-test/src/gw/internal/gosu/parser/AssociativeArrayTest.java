/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

/**
 */
public class AssociativeArrayTest extends TestClass
{
  public void testAssociativeArrayAssignment() throws ParseResultsException
  {
    Object ret = GosuTestUtil.eval(
      "var s = java.util.Calendar.getInstance();\n" +
      "s[\"Time\"] = new java.util.Date();\n" +
      "return s.Time" );

    Assert.assertTrue( ret instanceof Date );
    Calendar cal = Calendar.getInstance();
    cal.setTime( (Date)ret );
    Assert.assertEquals( cal.get( Calendar.YEAR ), new Date().getYear() + 1900 );
  }
}
