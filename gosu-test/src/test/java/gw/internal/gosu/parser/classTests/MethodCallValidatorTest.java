/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.config.CommonServices;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.IParseIssue;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;

import java.util.List;

/**
 */
public class MethodCallValidatorTest extends TestClass
{
  public void testCatchesInvalidCallAndPassesValidCall()
  {
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() )
    {
      // no validation stuff in os gosu
      return;
    }
    IGosuClassInternal gsClass = (IGosuClassInternal) TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.annotations.UsesHasMethodCallValidator" );
    assertFalse( gsClass.isValid() );
    List<IParseIssue> exceptions = gsClass.getParseResultsException().getParseExceptions();
    assertEquals( 1, exceptions.size() );
    assertEquals( 16, exceptions.get( 0 ).getLine() );
    assertEquals( "Must pass single argument that is a block expression with no arguments", exceptions.get( 0 ).getMessageArgs()[0] );
  }

  public void testCatchesInvalidCallAndPassesValidCallForGosu()
  {
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() )
    {
      // no validation stuff in os gosu
      return;
    }

    IGosuClassInternal gsClass = (IGosuClassInternal) TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.annotations.GosuMethodCallValidator" );
    assertFalse( gsClass.isValid() );
    List<IParseIssue> exceptions = gsClass.getParseResultsException().getParseExceptions();
    assertEquals( 1, exceptions.size() );
    assertEquals( 16, exceptions.get( 0 ).getLine() );
    assertEquals( "Go Gosu", exceptions.get( 0 ).getMessageArgs()[0] );
  }
}
