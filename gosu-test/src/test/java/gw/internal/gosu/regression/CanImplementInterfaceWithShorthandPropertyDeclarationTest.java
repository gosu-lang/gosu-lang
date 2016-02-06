/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class CanImplementInterfaceWithShorthandPropertyDeclarationTest extends TestClass
{
  public void testIt() {
    JavaInterfaceWithGetter eval = (JavaInterfaceWithGetter)GosuTestUtil.eval( "new gw.internal.gosu.regression.ImplementsJavaInterfaceWithShorthandProperty()" );
    assertEquals( "yes", eval.getStringProp() );
  }
}
