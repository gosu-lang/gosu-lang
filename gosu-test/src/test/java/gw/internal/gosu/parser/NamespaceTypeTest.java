/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class NamespaceTypeTest extends TestClass
{
  public void testNamespaceTypeHasValidArrayType()
  {
    NamespaceType namespaceType = new NamespaceType( "foo.bar" );
    assertNotNull( namespaceType.getArrayType() );
  }

  public void testNamespaceTypeResolves() {
    INamespaceType namespaceType = TypeSystem.getNamespace( "gw.internal.gosu.parser" );
    assertNotNull( namespaceType );
  }

  public void testNamespaceTypeDoesNotParse() {
    assertCausesError( "gw.internal.gosu.parser" );
    assertCausesError( "gw.internal.gosu.parser[]" );
    assertCausesError( "uses gw.internal.gosu\n" +
                       "return parser" );
    assertCausesError( "uses gw.internal.gosu\n" +
                       "return parser[]" );
  }

  private void assertCausesError( String script )
  {
    try
    {
      GosuTestUtil.eval( script );
      fail( "Script didn't cause an exception: \n" + script );
    }
    catch( RuntimeException e )
    {
      Throwable throwable = e.getCause();
      if( !(throwable instanceof ParseResultsException) )
      {
        throw e;
      }
    }
  }

}
