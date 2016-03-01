/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.TypeSystem;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.IExpression;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;
import gw.util.GosuTestUtil;
import junit.framework.Assert;

/**
 */
public class ArrayCompatibilityTest extends TestClass
{
  public void testGosuArrayCoercesToJavaArray() throws ParseResultsException
  {
    IExpression expr = GosuTestUtil.compileExpression( "uses gw.internal.gosu.parser.classTests.gwtest.base.*\n" +
            "\n" +
            "return (new Test[] { new Test(), new Test()}) as Object[]" );
    Object value = expr.evaluate();
    assertTrue( value instanceof IGosuObject[] );
    Assert.assertEquals( 2, TypeSystem.getFromObject( value ).getArrayLength( value ) );
    IGosuClass gsClass = (IGosuClassInternal)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.base.Test" );
    for( IGosuObject o : ((IGosuObject[])value) )
    {
      assertEquals( gsClass, o.getIntrinsicType() );
    }
  }

  public void testJavaArrayCoercesToGosuArray() throws ParseResultsException
  {
    IExpression expr = GosuTestUtil.compileExpression( "uses gw.internal.gosu.parser.classTests.gwtest.base.* \n" +
            "\n" +
            "return ((new Test[] {new Test(), new Test()}) as Object[]) as Test[]" );
    Object value = expr.evaluate();
    assertEquals( 2, TypeSystem.getFromObject( value ).getArrayLength( value ) );
    IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.base.Test" );
    for( IGosuObject o : (IGosuObject[])value )
    {
      assertEquals( gsClass, o.getIntrinsicType() );
    }
  }
}
