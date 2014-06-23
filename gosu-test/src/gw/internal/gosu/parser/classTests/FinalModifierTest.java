/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.Symbol;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.resources.Res;
import gw.test.TestClass;

/**
 */
public class FinalModifierTest extends TestClass
{
  public void testFinalClass() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.FinalClass" );
    assertTrue( gsClass.isFinal() );
    assertTrue( gsClass.isValid() );
    assertTrue( gsClass.isFinal() );
  }

  private IGosuClassInternal loadClass(String s) {
    return (IGosuClassInternal) TypeSystem.getByFullName(s);
  }

  public void testInnerFinalClass() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasInnerFinalClass.InnerFinalClass" );
    assertTrue( gsClass.isFinal() );
    assertTrue( gsClass.isValid() );
    assertTrue( gsClass.isFinal() );
  }

  public void testErrantExtendsFinalClass() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantExtendsFinalClass" );
    assertFalse( gsClass.isValid() );
    assertEquals( "expected 1 exception, got " + gsClass.getParseResultsException().getParseExceptions(), 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_EXTEND_FINAL_TYPE, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantInnerExtendsInnerFinalClass() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasErrantInnerExtendsInnerFinalClass.ErrantInnerExtendsInnerFinalClass" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_EXTEND_FINAL_TYPE, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testHasFinalFunction() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasFinalFunction" );
    assertTrue( gsClass.isValid() );
    IMethodInfo mi = gsClass.getTypeInfo().getMethod( "finalFunction" );
    assertTrue( mi.isFinal() );
    String strResult = (String)mi.getCallHandler().handleCall( ReflectUtil.construct( gsClass.getName() ) );
    assertEquals( "imfinal", strResult );
  }

  public void testErrantSubclassExtendsFinalMethod() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantSubclassOverridesFinalMethod" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_OVERRIDE_FINAL, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testHasFinalPropertyGetter() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasFinalPropertyGetter" );
    assertTrue( gsClass.isValid() );
    IPropertyInfo pi = gsClass.getTypeInfo().getProperty( "FinalProperty" );
    assertTrue( pi.isFinal() );
    String strResult = (String)pi.getAccessor().getValue( ReflectUtil.construct( gsClass.getName() ) );
    assertEquals( "imfinalproperty", strResult );
  }

  public void testErrantSubclassExtendsFinalProperty() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantSubclassOverridesFinalProperty" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_OVERRIDE_FINAL, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantSubclassExtendsFinalPropertySetter() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantSubclassOverridesFinalPropertySetter" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_OVERRIDE_FINAL, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testHasFinalPropertyGetterAndSetter() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasFinalPropertyGetterAndSetter" );
    assertTrue( gsClass.isValid() );
    IPropertyInfo pi = gsClass.getTypeInfo().getProperty( "FinalProperty" );
    assertTrue( pi.isFinal() );
    Object gsInstance = ReflectUtil.construct( gsClass.getName() );
    pi.getAccessor().setValue( gsInstance, "goober" );
    String strResult = (String)pi.getAccessor().getValue( gsInstance );
    assertEquals( "goober", strResult );
  }

  public void testHasFinalVar() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasFinalVar" );
    assertFalse( gsClass.isValid() );
    VarStatement varStmt = gsClass.getMemberField( (String)"_finalVar" );
    assertTrue( varStmt.isFinal() );
    assertTrue(((Symbol) varStmt.getSymbol()).isFinal() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_PROPERTY_NOT_WRITABLE, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testHasStaticFinalVar() throws ClassNotFoundException
  {
    IGosuClassInternal gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasStaticFinalVar" );
    assertFalse( gsClass.isValid() );
    VarStatement varStmt = gsClass.getStaticField( (String)"STATIC_FINAL_VAR" );
    assertTrue( varStmt.isFinal() );
    assertTrue(((Symbol) varStmt.getSymbol()).isFinal() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_PROPERTY_NOT_WRITABLE, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testHasFinalLocalVar() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasFinalLocalVar" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantSubClassCannotMutatePublicFinalVar() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantSubClassCannotMutatePublicFinalVar" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_PROPERTY_NOT_WRITABLE, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testHasFinalParam() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.HasFinalParam" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_PROPERTY_NOT_WRITABLE, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantOverridesFinalJavaMethod() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantOverridesFinalJavaMethod" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_OVERRIDE_FINAL, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testErrantOverridesFinalJavaPropertyGetter() throws ClassNotFoundException
  {
    IGosuClass gsClass = loadClass( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantOverridesFinalJavaPropertyGetter" );
    assertFalse( gsClass.isValid() );
    assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    assertEquals( Res.MSG_CANNOT_OVERRIDE_FINAL, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }
}
