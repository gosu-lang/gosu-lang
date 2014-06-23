/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.lang.parser.resources.Res;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 */
public class OverrideModifierTest extends TestClass
{
  public void testErrantClassWithOverrideModifier() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantClassWithOverrideModifier", Res.MSG_ILLEGAL_USE_OF_MODIFIER );
  }

  public void testErrantInterfaceWithOverrideModifier() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantInterfaceWithOverrideModifier", Res.MSG_ILLEGAL_USE_OF_MODIFIER );
  }

  public void testErrantEnumWithOverrideModifier() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantEnumWithOverrideModifier", Res.MSG_ILLEGAL_USE_OF_MODIFIER );
  }

  public void testErrantClassWithOverrideModifierOnConstructor() throws ClassNotFoundException
  {
    GosuTestUtil.assertHasErrors( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantClassWithOverrideModifierOnConstructor", Res.MSG_ILLEGAL_USE_OF_MODIFIER, Res.MSG_FUNCTION_NOT_OVERRIDE );
  }

  public void testErrantClassWithOverrideModifierOnVar() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantClassWithOverrideModifierOnVar", Res.MSG_ILLEGAL_USE_OF_MODIFIER );
  }

  public void testErrantClassDeclaresOverrideOnNonOverridenFunction() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantClassDeclaresOverrideOnNonOverridenFunction", Res.MSG_FUNCTION_NOT_OVERRIDE );
  }

  public void testErrantClassDeclaresOverrideOnNonOverridenProperty() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantClassDeclaresOverrideOnNonOverridenProperty", Res.MSG_FUNCTION_NOT_OVERRIDE );
  }
  
  public void testClassDeclaresOverrideOnOverridenFunction() throws ClassNotFoundException
  {
    assertTrue( TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ClassDeclaresOnOverridenFunction" ).isValid() );
  }

  public void testClassDeclaresOverrideOnOverridenProperty() throws ClassNotFoundException
  {
    assertTrue( TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ClassDeclaresOnOverridenProperty" ).isValid() );
  }
  
  public void testErrantClassDoesNotDeclareOverrideOnOverriddenFunction() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneWarning( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantClassDoesNotDeclareOverrideOnOverriddenFunction", Res.MSG_MISSING_OVERRIDE_MODIFIER );
  }

  public void testErrantClassDoesNotDeclareOverrideOnOverriddenProperty() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneWarning( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantClassDoesNotDeclareOverrideOnOverriddenProperty", Res.MSG_MISSING_OVERRIDE_MODIFIER );
  }

  public void testErrantClassOverrideNotAllowedWhenNonStaticMethodMasksStaticMethod() throws ClassNotFoundException
  {
    GosuTestUtil.assertOneError( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantOverridesStaticMethod", Res.MSG_FUNCTION_NOT_OVERRIDE );
  }

  public void testErrantClassOverrideNotAllowedWhenNonStaticMethodMasksEnhancementMethod() throws ClassNotFoundException
  {
    GosuTestUtil.assertHasErrors( "gw.internal.gosu.parser.classTests.gwtest.modifiers.ErrantOverridesEnhancementMethod", Res.MSG_FUNCTION_NOT_OVERRIDE, Res.MSG_CANNOT_OVERRIDE_FUNCTION_FROM_ENHANCEMENT );
  }
}
