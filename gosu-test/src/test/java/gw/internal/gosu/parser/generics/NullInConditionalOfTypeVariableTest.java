/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

/**
 * In {@code cond ? item : null} with an item of type T, the parser casts the {@code null} to T. The cast of
 * {@code null} compiles to a checkcast and does not read T at runtime, so the function need not be reified. The
 * parser used to report "must be declared with the reified modifier" for it, although it accepted
 * {@code return null} and {@code var x : T = null}.
 */
public class NullInConditionalOfTypeVariableTest extends TestClass
{
  private static final String TYPE = "gw.internal.gosu.parser.generics.gwtest.NullInConditionalOfTypeVar";

  public void testFunctionThatIsNotReifiedHasNullInAConditionalOfItsTypeVariable()
  {
    IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullName( TYPE );
    // isValid() first: it is what compiles the class and produces the feedback
    boolean valid = gsClass.isValid();
    assertTrue( gsClass.getParseResultsException() == null ? "" : gsClass.getParseResultsException().getFeedback(), valid );

    Object instance = ReflectUtil.construct( TYPE );
    assertEquals( "abc", ReflectUtil.invokeMethod( instance, "itemOrNull", "abc", true ) );
    assertNull( ReflectUtil.invokeMethod( instance, "itemOrNull", "abc", false ) );
    assertNull( ReflectUtil.invokeMethod( instance, "nullOrItem", "abc", true ) );
    assertEquals( "abc", ReflectUtil.invokeMethod( instance, "nullOrItem", "abc", false ) );
  }
}
