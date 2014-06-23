/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.util.List;

/**
 */
public class GosuEnumTest extends TestClass
{
  public void testEmptyEnum() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.enums.EmptyEnum" );
    assertTrue( gsClass.isValid() );
  }

  private IGosuClassInternal loadClass(String s) {
    return (IGosuClassInternal) TypeSystem.getByFullName(s);
  }

  public void testSimpleEnum() throws ClassNotFoundException
  {
    testEnum( "gw.internal.gosu.parser.classTests.gwtest.enums.SimpleEnum", 9 );
  }

  public void testComplexEnum() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = testEnum( "gw.internal.gosu.parser.classTests.gwtest.enums.ComplexEnum", 11 );
    List<IGosuObject> enumConstants = (List<IGosuObject>)gsClass.getTypeInfo().getProperty( "AllValues" ).getAccessor().getValue( null );

    assertPropertyValue( enumConstants.get( 0 ), "Age", 17 );
    assertPropertyValue( enumConstants.get( 1 ), "Age", 15 );
    assertPropertyValue( enumConstants.get( 2 ), "Age", 5 );
  }

  public void testHasInnerEnum() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = testEnum( "gw.internal.gosu.parser.classTests.gwtest.enums.HasInnerEnum.InnerEnum", 11 );
    List<IGosuObject> enumConstants = (List<IGosuObject>)gsClass.getTypeInfo().getProperty( "AllValues" ).getAccessor().getValue( null );

    assertPropertyValue( enumConstants.get( 0 ), "Age", 17 );
    assertPropertyValue( enumConstants.get( 1 ), "Age", 15 );
    assertPropertyValue( enumConstants.get( 2 ), "Age", 5 );
  }

  public void testInnerSimpleEnumWithCtorAndOuterWithCtor() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.enums.InnerSimpleEnumWithCtorAndOuterWithCtor.InnerEnum" );
    assertTrue( gsClass.isValid() );
    assertNotNull( gsClass.getTypeInfo().getConstructor( gsClass, new IType[0] ) );
    assertNotNull( gsClass.getEnclosingType().getTypeInfo().getConstructor() );
  }

  public void testErrantPrivateEnum() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.enums.ErrantPrivateEnum", Res.MSG_ILLEGAL_USE_OF_MODIFIER );
  }

  public void testErrantProtectedEnum() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.enums.ErrantProtectedEnum", Res.MSG_ILLEGAL_USE_OF_MODIFIER );
  }

  public void testErrantAbstractEnum() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.enums.ErrantAbstractEnum", Res.MSG_ILLEGAL_USE_OF_MODIFIER );
  }

  public void testErrant_IllegalCtorAccess() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.enums.Errant_IllegalCtorAccess", Res.MSG_ENUM_CONSTRUCTOR_NOT_ACCESSIBLE );
  }

  public void testErrantEnumWrongNumberOfCtorArgs() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.enums.ErrantEnumWrongNumberOfCtorArgs", Res.MSG_WRONG_NUMBER_OF_ARGS_TO_CONSTRUCTOR );
  }

  public void testQualifiedAssignment() throws ParseResultsException
  {
    String strName = (String)GosuTestUtil.eval( "uses gw.internal.gosu.parser.classTests.gwtest.enums.SimpleEnum\n" +
          "var simple : SimpleEnum = SimpleEnum.Dog\n" +
          "return simple.Name" );
    assertEquals( "Dog", strName );
  }

  public void testUnqualifiedAssignment() throws ParseResultsException
  {
    String strName = (String)GosuTestUtil.eval( "uses gw.internal.gosu.parser.classTests.gwtest.enums.SimpleEnum\n" +
          "var simple : SimpleEnum = Cat\n" +
          "return simple.Name" );
    assertEquals( "Cat", strName );
  }

  public void testToStringReturnsName() throws ParseResultsException
  {
    String strName = (String)GosuTestUtil.eval( "uses gw.internal.gosu.parser.classTests.gwtest.enums.SimpleEnum\n" +
          "var simple = SimpleEnum.Cat\n" +
          "return simple.toString()" );
    assertEquals( "Cat", strName );
  }

  public void testErrantNonPrivateEnumCtor() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.enums.ErrantNonPrivateEnumCtor", Res.MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE );
  }

  public void testEnumWithNoConstantsCanHaveAConstructor() throws ClassNotFoundException, ParseResultsException
  {
    assertNotNull( GosuTestUtil.evalGosu( "enum Foo{ private construct(){} } return true" ) );
  }

  public void testEnumWithBadConstrutorDoesNotCauseNPE() throws ClassNotFoundException, ParseResultsException
  {
    GosuTestUtil.assertCausesPRE( "enum Foo { publicconstruct(){} } return true" );
  }

  public void testEnumInInnerClassInProgram()
  {
    assertEquals( "HELLO",
      GosuTestUtil.eval( "class Foo\n" +
                         "{\n" +
                         "  enum Blah\n" +
                         "  {\n" +
                         "    HELLO, BYE\n" +
                         "  }\n" +
                         "}\n" +
                         "\n" +
                         "return Foo.Blah.HELLO.Name" ) );
  }

  public void testInnerEnumsCanInvokeValueOf()
  {
    assertEquals( "DOH", GosuTestUtil.eval("class Foo {\n" +
                                           "  enum Bar {\n" +
                                           "    DOH\n" +
                                           "  }\n" +
                                           "}\n" +
                                           "\n" +
                                           "return Foo.Bar.valueOf('DOH').toString()\n") );
  }

  public void testEnumInProgram()
  {
    assertEquals( "HELLO",
      GosuTestUtil.eval( "enum Blah\n" +
                         "{\n" +
                         "  HELLO, BYE\n" +
                         "}\n" +
                         "\n" +
                         "return Blah.HELLO.Name" ) );
  }  

  private IGosuClassInternal testEnum( String strEnum, int iProperties ) throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( strEnum );
    assertTrue( gsClass.isValid() );

    // Dog, Cat, Mouse + AllNames, Name, Ordinal
    assertEquals( gsClass.getTypeInfo().getDeclaredProperties().toString(),
                  iProperties,
                  gsClass.getTypeInfo().getDeclaredProperties().size() );

    List<IGosuObject> enumConstants = (List<IGosuObject>)gsClass.getTypeInfo().getProperty( "AllValues" ).getAccessor().getValue( null );

    // Gosu
    assertPropertyValue( enumConstants.get( 0 ), "Name", "Dog" );
    assertPropertyValue( enumConstants.get( 1 ), "Name", "Cat" );
    assertPropertyValue( enumConstants.get( 2 ), "Name", "Mouse" );
    // Java
    assertEquals( "Dog", ((IEnumValue)enumConstants.get( 0 )).getCode() );
    assertEquals( "Cat", ((IEnumValue)enumConstants.get( 1 )).getCode() );
    assertEquals( "Mouse", ((IEnumValue)enumConstants.get( 2 )).getCode() );

    // Gosu
    assertPropertyValue( enumConstants.get( 0 ), "Ordinal", 0 );
    assertPropertyValue( enumConstants.get( 1 ), "Ordinal", 1 );
    assertPropertyValue( enumConstants.get( 2 ), "Ordinal", 2 );
    // Java
    assertEquals( 0, ((IEnumValue)enumConstants.get( 0 )).getOrdinal() );
    assertEquals( 1, ((IEnumValue)enumConstants.get( 1 )).getOrdinal() );
    assertEquals( 2, ((IEnumValue)enumConstants.get( 2 )).getOrdinal() );

    assertValueOf( gsClass, enumConstants.get( 0 ), "Dog" );
    assertValueOf( gsClass, enumConstants.get( 1 ), "Cat" );
    assertValueOf( gsClass, enumConstants.get( 2 ), "Mouse" );

    return gsClass;
  }

  private void assertValueOf( IGosuClassInternal gsClass, IGosuObject enumConst, String strName )
  {
    IMethodInfo mi = gsClass.getTypeInfo().getMethod( "valueOf", TypeSystem.get( String.class ) );
    IGosuObject value = (IGosuObject)mi.getCallHandler().handleCall( null, strName );
    assertEquals( enumConst, value );
  }

  private void assertPropertyValue( Object enum0, String strProp, Object value )
  {
    assertEquals( value, TypeSystem.getFromObject( enum0 ).getTypeInfo().getProperty( strProp ).getAccessor().getValue( enum0 ) );
  }
}