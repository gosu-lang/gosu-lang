/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 */
public abstract class AbstractSimpleGenericsTest extends TestClass
{
  private String _strClassName;

  public AbstractSimpleGenericsTest( String strClassName)
  {
    _strClassName = strClassName;
  }

  private void exec(String s) throws ParseResultsException {
    GosuTestUtil.evalGosu(s);
  }

  public void testParsesSimpleParameterizedType() throws ParseResultsException
  {
    exec( "var typeWithString : " + _strClassName + "<String>;" );
  }

  public void testParsesTypeWithWildCard() throws ParseResultsException
  {
    exec( "var typeWithWildcard : " + _strClassName + "<Object>;" );
  }

  public void testParsesTypeWithBoundedWildcard() throws ParseResultsException
  {
    exec( "var typeWithBoundedWildcard : " + _strClassName + "<String>;" );
  }

  public void testParsesTypeWithBoundedWildcardWithWildcard() throws ParseResultsException
  {
    exec( "var typeWithBoundedWildcardWithWildcard : " + _strClassName + "<" + _strClassName + "<Object>>;" );
  }

  public void testParsesTypeWithBoundedWildcardWithBoundedWildcard() throws ParseResultsException
  {
    exec( "var typeWithBoundedWildcardWithBoundedWildcard : " + _strClassName + "<" + _strClassName + "<String>>;" );
  }

  public void testNonGenericTypeNotParameterizable()
  {
    try
    {
      exec( "var shouldFail = Number<String>;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }

  public void testTooManyTypeParams()
  {
    try
    {
      exec( "var shouldFail = " + _strClassName + "<String,String>;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }

  public void testSameTypesAreAssignable() throws ParseResultsException
  {
    exec(
      "var typeWithString : " + _strClassName + "<String>;" +
      "var typeWithString2 : " + _strClassName + "<String> = typeWithString;" );
  }

  public void testNotSameTypesAreNotAssignable() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithString : " + _strClassName + "<String>;" +
        "var typeWithNumber : " + _strClassName + "<Number> = typeWithString;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }

  public void testUnboundedWildcardTypeIsAssignableFromAny() throws ParseResultsException
  {
    exec(
      "var typeWithString : " + _strClassName + "<String>;" +
      "var typeWithWildcard : " + _strClassName + "<Object> = typeWithString;" );
  }

  public void testUnboundedWildcardTypeIsNotAssignableIncompatibleType() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithString : java.util.List<String>" +
        "var typeWithWildcard : " + _strClassName + "<Object> = typeWithString;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }

  public void testUnboundedWildcardTypeIsAssignableFromUnboundedWildcardType() throws ParseResultsException
  {
    exec(
      "var typeWithWildcard : " + _strClassName + "<Object>;" +
      "var typeWithWildcard2 : " + _strClassName + "<Object> = typeWithWildcard;" );
  }

  public void testUnboundedWildcardTypeIsAssignableFromBoundedWildcardType() throws ParseResultsException
  {
    exec(
      "var typeWithWildcard : " + _strClassName + "<java.lang.CharSequence>;" +
      "var typeWithWildcard2 : " + _strClassName + "<Object> = typeWithWildcard;" );
  }

  public void testBoundedWildcardTypeIsAssignableFromBoundaryType() throws ParseResultsException
  {
    exec(
      "var typeWithList : " + _strClassName + "<java.util.List>;" +
      "var typeWithBoundedWildcard : " + _strClassName + "<java.util.List<Object>> = typeWithList;" );
  }

  public void testBoundedWildcardTypeIsAssignableFromImplOfBoundaryType() throws ParseResultsException
  {
    exec(
      "var typeWithArrayList : " + _strClassName + "<java.util.ArrayList>;" +
      "var typeWithBoundedWildcard : " + _strClassName + "<java.util.List<Object>> = typeWithArrayList;" );
  }

  public void testBoundedWildcardTypeIsNotAssignableFromIncompatibleBoundaryType() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithArrayList : " + _strClassName + "<java.util.List>;" +
        "var typeWithBoundedWildcard : " + _strClassName + "<java.util.ArrayList> = typeWithArrayList;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }

  public void testBoundedWildcardTypeIsAssignableFromSubtypeOfBoundaryType() throws ParseResultsException
  {
    exec(
      "var typeWithStringBuffer : " + _strClassName + "<java.lang.StringBuffer>;" +
      "var typeWithBoundedWildcard : " + _strClassName + "<java.lang.Object> = typeWithStringBuffer;" );
  }

  public void testBoundedWildcardTypeIsNotAssignableFromDisparateType() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithStringBuffer : " + _strClassName + "<java.lang.StringBuffer>;" +
        "var typeWithBoundedWildcard : " + _strClassName + "<java.lang.String> = typeWithStringBuffer;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }
}
