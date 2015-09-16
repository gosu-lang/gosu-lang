/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.test.TestClass;

/**
 */
public class AbstractModifierTest extends TestClass
{
  public void testAbstractClassWithNoMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.AbstractClassWithNoMembers" );
    assertTrue( gsClass.isValid() );
  }

  private IGosuClassInternal loadClass(String s) {
    return (IGosuClassInternal) TypeSystem.getByFullName(s);
  }

  public void testAbstractClassWithNoAbstractMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.AbstractClassWithNoAbstractMembers" );
    assertTrue( gsClass.isValid() );
  }

  public void testAbstractClassWithNoConcreteMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.AbstractClassWithNoConcreteMembers" );
    assertTrue( gsClass.isValid() );
    assertEquals( 1, gsClass.getTypeInfo().getDeclaredProperties().size() );
    assertTrue( gsClass.getTypeInfo().getProperty( "bar" ).isAbstract() );
    assertEquals( 2, gsClass.getTypeInfo().getDeclaredMethods().size() );
    assertTrue( gsClass.getTypeInfo().getMethod( "foo" ).isAbstract() );
  }

  public void testErrantAbstractClassWithImplsOnAbstractMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantAbstractClassWithImplsOnAbstractMembers" );
    assertFalse( gsClass.isValid() );
  }

  public void testErrantAbstractClassWithPrivateAbstractMember() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantAbstractClassWithPrivateAbstractMember" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_ILLEGAL_USE_OF_MODIFIER, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantAbstractClassWithFinalAbstractMember() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantAbstractClassWithFinalAbstractMember" );
    assertFalse( gsClass.isValid() );
    assertEquals( 2, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_ILLEGAL_USE_OF_MODIFIER, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantAbstractClassWithOverrideAbstractMember() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantAbstractClassWithOverrideAbstractMember" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_FUNCTION_NOT_OVERRIDE, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantNonAbstractClassWithAbstractMember() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantNonAbstractClassWithAbstractMember" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testInterfaceHasImplicitAbstractMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.InterfaceHasImplicitAbstractMembers" );
    assertTrue( gsClass.isValid() );
    assertEquals( 4, gsClass.getTypeInfo().getDeclaredMethods().size() );
    for( IMethodInfo mi : gsClass.getTypeInfo().getMethods() ) {
      if ( mi.getOwnersType() == gsClass ) {
        assertTrue(mi.getName() + " should be abstract", mi.isAbstract());
      }
    }
    assertEquals( 1, gsClass.getTypeInfo().getDeclaredProperties().size() );
    for( IPropertyInfo pi : gsClass.getTypeInfo().getProperties() )
    {
      if( !pi.getDisplayName().equals( "IntrinsicType" ) && pi.getOwnersType() == gsClass )
      {
        assertTrue( pi.getName() + " should be abstract", pi.isAbstract() );
      }
    }
  }

  public void testInterfaceHasExplicitAbstractMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.InterfaceHasExplicitAbstractMembers" );
    assertTrue( gsClass.isValid() );
    assertEquals( 4, gsClass.getTypeInfo().getDeclaredMethods().size() );
    for( IMethodInfo mi : gsClass.getTypeInfo().getMethods() )
    {
      if (mi.getOwnersType() == gsClass) {
        assertTrue( mi.getName() + " should be abstract", mi.isAbstract() );
      }
    }
    assertEquals( 1, gsClass.getTypeInfo().getDeclaredProperties().size() );
    for( IPropertyInfo pi : gsClass.getTypeInfo().getProperties() )
    {
      if( !pi.getDisplayName().equals( "IntrinsicType" ) && pi.getOwnersType() == gsClass )
      {
        assertTrue( pi.getName() + " should be abstract", pi.isAbstract() );
      }
    }
  }

  public void testNonAbstractClassExtendsBaseAbstractClass() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.NonAbstractClassExtendsBaseAbstractClass" );
    assertTrue( gsClass.isValid() );
    IMethodInfo mi = gsClass.getTypeInfo().getMethod( "foo" );
    assertFalse( mi.isAbstract() );
    Object gsInstance = ReflectUtil.construct( gsClass.getName() );
    String strValue = (String)mi.getCallHandler().handleCall( gsInstance );
    assertEquals( "foo", strValue );
    IPropertyInfo pi = gsClass.getTypeInfo().getProperty( "Bar" );
    assertFalse( pi.isAbstract() );
    assertEquals( 88, ((Number)pi.getAccessor().getValue( gsInstance )).intValue() );
  }

  public void testErrantNonAbstractSubClassDoesNotImplAbstractMembers() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantNonAbstractSubClassDoesNotImplAbstractMembers" );
    assertFalse( gsClass.isValid() );
    assertEquals( 2, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, gsClass.getParseResultsException().getParseExceptions().get( 1 ).getMessageKey() );
  }

  public void testAbstractClassDoesNotImplInterface() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.AbstractClassDoesNotImplInterface" );
    assertTrue( gsClass.isValid() );
  }

  public void testErrantNonAbstractSubClassDoesNotImplAbstractMembersDefinedInInterface() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantNonAbstractSubClassDoesNotImplAbstractMembersDefinedInInterface" );
    assertFalse( gsClass.isValid() );
    assertEquals( 2, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
    assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, gsClass.getParseResultsException().getParseExceptions().get( 1 ).getMessageKey() );
  }

  public void testNonAbstractSubClassImplsAbstractMembersDefinedInInterface() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.NonAbstractSubClassImplsAbstractMembersDefinedInInterface" );
    assertTrue( gsClass.isValid() );
  }
}
