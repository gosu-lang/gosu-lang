/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 */
public abstract class AbstractComplexGenericsTest extends TestClass
{
  private String _strClassName;
  private String _strSuperClassName;

  public AbstractComplexGenericsTest( String strClassName, String strSuperClassName)
  {
    _strClassName = strClassName;
    _strSuperClassName = strSuperClassName;
  }

  public void testParsesSimpleParameterizedType() throws ParseResultsException
  {
    exec( "var typeWithString : " + _strClassName + "<java.lang.Integer, String>;" );
  }

  private void exec(String s) throws ParseResultsException {
    GosuTestUtil.evalGosu(s);
  }

  public void testParsesTypeWithWildCard() throws ParseResultsException
  {
    exec( "var typeWithWildcard : " + _strClassName + "<String, ?>;" );
  }

  public void testParsesTypeWithBoundedWildcard() throws ParseResultsException
  {
    exec( "var typeWithBoundedWildcard : " + _strClassName + "<String, ? extends String>;" );
  }

  public void testParsesTypeWithBoundedWildcardWithWildcard() throws ParseResultsException
  {
    exec( "var typeWithBoundedWildcardWithWildcard : " + _strClassName + "<String, ? extends " + _strClassName + "<String, ?>>;" );
  }

  public void testParsesTypeWithBoundedWildcardWithBoundedWildcard() throws ParseResultsException
  {
    exec( "var typeWithBoundedWildcardWithBoundedWildcard : " + _strClassName + "<String, ? extends " + _strClassName + "<String, ? extends String>>;" );
  }

  public void testTooManyTypeParams()
  {
    try
    {
      exec( "var shouldFail = " + _strClassName + "<String,String,String>;" );
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
      "var typeWithString : " + _strClassName + "<java.lang.Integer, String>;" +
      "var typeWithString2 : " + _strClassName + "<java.lang.Integer, String> = typeWithString;" );
  }

  public void testNotSameTypesAreNotAssignable() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithString : " + _strClassName + "<java.lang.Integer, String>;" +
        "var typeWithNumber : " + _strClassName + "<java.lang.Integer, Number> = typeWithString;" );
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
      "var typeWithString : " + _strClassName + "<java.lang.Integer, String>;" +
      "var typeWithWildcard : " + _strClassName + "<java.lang.Integer, ?> = typeWithString;" );
  }

  public void testUnboundedWildcardTypeIsNotAssignableIncompatibleType() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithString : java.util.Map<java.lang.Integer, String>" +
        "var typeWithWildcard : " + _strClassName + "<java.lang.Integer, ?> = typeWithString;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }

  public void testUnboundedWildcardTypeIsNotAssignableIncompatibleParameterization() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithString : " + _strSuperClassName + "<String>" +
        "var typeWithWildcard : " + _strClassName + "<java.lang.Integer, ?> = typeWithString;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }

  public void testUnboundedWildcardTypeIsAssignableCompatibleParameterization() throws ParseResultsException
  {
    exec(
      "var typeWithString : " + _strClassName + "<java.lang.Integer, String>" +
      "var typeWithWildcard : " + _strSuperClassName + "<?> = typeWithString;" );
  }

  public void testBoundedWildcardTypeIsAssignableCompatibleParameterization() throws ParseResultsException
  {
    exec(
      "var typeWithString : " + _strClassName + "<java.lang.Integer, String>" +
      "var typeWithWildcard : " + _strSuperClassName + "<? extends java.lang.CharSequence> = typeWithString;" );
  }

  public void testBoundedWildcardTypeIsNotAssignableInCompatibleParameterization() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithString : " + _strClassName + "<java.lang.Integer, String>" +
        "var typeWithWildcard : " + _strSuperClassName + "<? extends java.util.List> = typeWithString;" );
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
      "var typeWithWildcard : " + _strClassName + "<java.lang.Integer, ?>;" +
      "var typeWithWildcard2 : " + _strClassName + "<java.lang.Integer, ?> = typeWithWildcard;" );
  }

  public void testUnboundedWildcardTypeIsAssignableFromBoundedWildcardType() throws ParseResultsException
  {
    exec(
      "var typeWithWildcard : " + _strClassName + "<java.lang.Integer, ? extends java.lang.CharSequence>;" +
      "var typeWithWildcard2 : " + _strClassName + "<java.lang.Integer, ?> = typeWithWildcard;" );
  }

  public void testBoundedWildcardTypeIsAssignableFromBoundaryType() throws ParseResultsException
  {
    exec(
      "var typeWithList : " + _strClassName + "<java.lang.Integer, java.util.List>;" +
      "var typeWithBoundedWildcard : " + _strClassName + "<java.lang.Integer, ? extends java.util.List> = typeWithList;" );
  }

  public void testBoundedWildcardTypeIsAssignableFromImplOfBoundaryType() throws ParseResultsException
  {
    exec(
      "var typeWithArrayList : " + _strClassName + "<java.lang.Integer, java.util.ArrayList>;" +
      "var typeWithBoundedWildcard : " + _strClassName + "<java.lang.Integer, ? extends java.util.List> = typeWithArrayList;" );
  }

  public void testBoundedWildcardTypeIsNotAssignableFromIncompatibleBoundaryType() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithArrayList : " + _strClassName + "<java.lang.Integer, java.util.List>;" +
        "var typeWithBoundedWildcard : " + _strClassName + "<java.lang.Integer, ? extends java.util.ArrayList> = typeWithArrayList;" );
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
      "var typeWithStringBuffer : " + _strClassName + "<java.lang.Integer, java.lang.StringBuffer>;" +
      "var typeWithBoundedWildcard : " + _strClassName + "<java.lang.Integer, ? extends java.lang.Object> = typeWithStringBuffer;" );
  }

  public void testBoundedWildcardTypeIsNotAssignableFromDisparateType() throws ParseResultsException
  {
    try
    {
      exec(
        "var typeWithStringBuffer : " + _strClassName + "<java.lang.Integer, java.lang.StringBuffer>;" +
        "var typeWithBoundedWildcard : " + _strClassName + "<java.lang.Integer, ? extends java.lang.String> = typeWithStringBuffer;" );
    }
    catch( ParseResultsException pe )
    {
      return;
    }
    fail( "Should not have parsed" );
  }
}
