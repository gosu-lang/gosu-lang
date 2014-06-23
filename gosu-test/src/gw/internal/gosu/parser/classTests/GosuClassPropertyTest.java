/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;
import gw.util.GosuTestUtil;
import org.junit.Assert;

import java.util.List;

public class GosuClassPropertyTest extends TestClass
{
  public void testPropertySetterWithoutGetter() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = (IGosuClassInternal) TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.properties.PropertySetterWithoutGetter" );
    assertTrue( gsClass.isValid() );
  }

  public void testErrantPropertySetterWithZeroParameters() throws ClassNotFoundException
  {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.properties.ErrantPropertySetterWithZeroParameters");
    Assert.assertFalse( gsClass.isValid() );
    Assert.assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    Assert.assertEquals(Res.MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantPropertySetterWithTwoParameters() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.properties.ErrantPropertySetterWithTwoParameters", Res.MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER );
  }

  public void testErrantPropertySetterWithThreeParameters() throws ClassNotFoundException
  {
    GosuTestUtil.assertHasErrors( "gw.internal.gosu.parser.classTests.gwtest.properties.ErrantPropertySetterWithThreeParameters", Res.MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER, Res.MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER );
  }

}