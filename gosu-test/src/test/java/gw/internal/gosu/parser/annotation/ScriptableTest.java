/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.lang.parser.ScriptabilityModifiers;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

/**
 * User: dbrewster
 * Date: Mar 20, 2007
 * Time: 3:54:03 PM
 */
public class ScriptableTest extends TestClass {

  public void testUnspecifiedScriptabilityDefaultsToScriptableAll() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.scriptable.NoScriptable");
    for (IAttributedFeatureInfo feature : gosuClass.getTypeInfo().getProperties()) {
      assertTrue(feature.isVisible(ScriptabilityModifiers.SCRIPTABLE));
    }
    for (IAttributedFeatureInfo feature : gosuClass.getTypeInfo().getConstructors()) {
      assertTrue(feature.isVisible(ScriptabilityModifiers.SCRIPTABLE));
    }
    for (IAttributedFeatureInfo feature : gosuClass.getTypeInfo().getMethods()) {
      assertTrue(feature.isVisible(ScriptabilityModifiers.SCRIPTABLE));
    }
  }

//  public void testInternalAPIHidesMembers() {
//    IType scriptableTestExampleType = TypeSystem.get(ScriptableTestExample.class);
//
//    IPropertyInfo exampleProperty = scriptableTestExampleType.getTypeInfo().getProperty("ExampleProperty");
//    assertTrue(exampleProperty.isVisible(ScriptabilityModifiers.SCRIPTABLE));
//    IPropertyInfo internalProperty = scriptableTestExampleType.getTypeInfo().getProperty("InternalProperty");
//    assertFalse(internalProperty.isVisible(ScriptabilityModifiers.SCRIPTABLE));
//
//    IMethodInfo exampleMethod = scriptableTestExampleType.getTypeInfo().getMethod("exampleMethod");
//    assertTrue(exampleMethod.isVisible(ScriptabilityModifiers.SCRIPTABLE));
//    IMethodInfo internalMethod = scriptableTestExampleType.getTypeInfo().getMethod("internalMethod");
//    assertFalse(internalMethod.isVisible(ScriptabilityModifiers.SCRIPTABLE));
//  }
}
