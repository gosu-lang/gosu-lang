/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/**
 * Created by IntelliJ IDEA.
 * User: kprevas
 * Date: Oct 10, 2007
 * Time: 5:25:01 PM
 * To change this template use File | Settings | File Templates.
 */

package gw.internal.gosu.parser.classTests;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 * User: dbrewster
 * Date: Apr 5, 2007
 * Time: 2:51:54 PM
 */
public class GosuClassConstructorTest extends TestClass {

  public void testChainedThisConstructors() throws Exception {
    Object x = GosuTestUtil.eval("(new gw.internal.gosu.parser.classTests.gwtest.ctor.Ctor()).foo()");
    assertEquals(4, x);
  }

  public void testThisToSuper() throws Exception {
    Object x = GosuTestUtil.eval("(new gw.internal.gosu.parser.classTests.gwtest.ctor.CtorSub()).foo()");
    assertEquals(4, x);
  }

  public void testBlockInThis() throws Exception {
    Object x = GosuTestUtil.eval("(new gw.internal.gosu.parser.classTests.gwtest.ctor.CtorSub(1, 2)).foo()");
    assertEquals(0, x);
  }

  public void testBlockInSuper() throws Exception {
    Object x = GosuTestUtil.eval("(new gw.internal.gosu.parser.classTests.gwtest.ctor.CtorSub(1, 2, 3)).foo()");
    assertEquals(0, x);
  }
  
  public void testErrorsOnNonStaticFeaturesReferencedInSuper() {
    IGosuClass type = (IGosuClass)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.ctor.Errant_BadConstructors" );
    assertFalse( type.isValid() );
    ParseResultsException resultsException = type.getParseResultsException();
    // there should be three references to non-static features in Errant_BadConstructors, and each should cause an error
    assertEquals( 3, type.getParseResultsException().getParseExceptions().size() );
  }

}