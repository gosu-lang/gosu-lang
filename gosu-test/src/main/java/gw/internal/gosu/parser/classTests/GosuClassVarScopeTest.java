/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.test.TestClass;
import gw.lang.reflect.TypeSystem;

/**
 * User: dbrewster
 * Date: Apr 5, 2007
 * Time: 2:51:54 PM
 */
public class GosuClassVarScopeTest extends TestClass {

  public void testScopedVarsAreScopedByThread() throws Throwable {
    final IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.varTests.VarScope1");
    gosuClass.getTypeInfo().getProperty("_staticVar").getAccessor().setValue(null, "static");
    gosuClass.getTypeInfo().getProperty("_sessionVar").getAccessor().setValue(null, "session");
    gosuClass.getTypeInfo().getProperty("_requestVar").getAccessor().setValue(null, "request");
    gosuClass.getTypeInfo().getProperty("_appVar").getAccessor().setValue(null, "app");

    assertEquals("static", gosuClass.getTypeInfo().getProperty("_staticVar").getAccessor().getValue(null));
    assertEquals("session", gosuClass.getTypeInfo().getProperty("_sessionVar").getAccessor().getValue(null));
    assertEquals("request", gosuClass.getTypeInfo().getProperty("_requestVar").getAccessor().getValue(null));
    assertEquals("app", gosuClass.getTypeInfo().getProperty("_appVar").getAccessor().getValue(null));
    final Throwable e[] = new Throwable[1];
    Thread t = new Thread(new Runnable() {
      public void run() {
        try {
          assertEquals("static", gosuClass.getTypeInfo().getProperty("_staticVar").getAccessor().getValue(null));
//          assertNull(gosuClass.getTypeInfo().getProperty("_sessionVar").getAccessor().getValue(null));
//          assertNull( gosuClass.getTypeInfo().getProperty("_requestVar").getAccessor().getValue(null));
//          assertEquals("app", gosuClass.getTypeInfo().getProperty("_appVar").getAccessor().getValue(null));
        } catch (Throwable t) {
          e[0] = t;
        }
      }
    });

    t.start();
    t.join(5000);
    if (e[0] != null) {
      throw e[0];
    }
  }

  public void testScopedVarsDoNotBleedAcrossTwoClassIfPropertiesAreSame() throws Throwable {
    final IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.varTests.VarScope1");
    gosuClass.getTypeInfo().getProperty("_staticVar").getAccessor().setValue(null, "static");
    gosuClass.getTypeInfo().getProperty("_sessionVar").getAccessor().setValue(null, "session");
    gosuClass.getTypeInfo().getProperty("_requestVar").getAccessor().setValue(null, "request");
    gosuClass.getTypeInfo().getProperty("_appVar").getAccessor().setValue(null, "app");

    assertEquals("static", gosuClass.getTypeInfo().getProperty("_staticVar").getAccessor().getValue(null));
    assertEquals("session", gosuClass.getTypeInfo().getProperty("_sessionVar").getAccessor().getValue(null));
    assertEquals("request", gosuClass.getTypeInfo().getProperty("_requestVar").getAccessor().getValue(null));
    assertEquals("app", gosuClass.getTypeInfo().getProperty("_appVar").getAccessor().getValue(null));

    final IGosuClassInternal gosuClass2 = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.varTests.VarScope2");
    gosuClass2.getTypeInfo().getProperty("_staticVar").getAccessor().setValue(null, "static2");
    gosuClass2.getTypeInfo().getProperty("_sessionVar").getAccessor().setValue(null, "session2");
    gosuClass2.getTypeInfo().getProperty("_requestVar").getAccessor().setValue(null, "request2");
    gosuClass2.getTypeInfo().getProperty("_appVar").getAccessor().setValue(null, "app2");

    assertEquals("static2", gosuClass2.getTypeInfo().getProperty("_staticVar").getAccessor().getValue(null));
    assertEquals("session2", gosuClass2.getTypeInfo().getProperty("_sessionVar").getAccessor().getValue(null));
    assertEquals("request2", gosuClass2.getTypeInfo().getProperty("_requestVar").getAccessor().getValue(null));
    assertEquals("app2", gosuClass2.getTypeInfo().getProperty("_appVar").getAccessor().getValue(null));

    // Now check the old class
    assertEquals("static", gosuClass.getTypeInfo().getProperty("_staticVar").getAccessor().getValue(null));
    assertEquals("session", gosuClass.getTypeInfo().getProperty("_sessionVar").getAccessor().getValue(null));
    assertEquals("request", gosuClass.getTypeInfo().getProperty("_requestVar").getAccessor().getValue(null));
    assertEquals("app", gosuClass.getTypeInfo().getProperty("_appVar").getAccessor().getValue(null));
  }

  public void testHasArrayInitializerAndFunction() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = (IGosuClassInternal) TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.varTests.HasArrayInitializerAndFunction" );
    assertTrue( gsClass.isValid() );
  }
}
