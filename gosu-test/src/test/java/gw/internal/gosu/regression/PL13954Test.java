/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class PL13954Test extends TestClass
{
  
  public void testCanInstantiateClassInQuestion()
  {
    assertNotNull( GosuTestUtil.eval( "new gw.internal.gosu.regression.PL13954ExtendsBaseGenericClass<String>()" ) );
  }
  
}
