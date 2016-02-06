/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

/**
 * User: dbrewster
 * Date: Mar 16, 2007
 * Time: 3:20:12 PM
 */
public class DeprecatedTest extends TestClass {

  public void testClassWithNoDeprecatedFeaturesDoesntHaveDeprecatedFeatures() {
    IGosuClassInternal clazz = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.DeprecatedTestNoDeprecatedFeatures");
    assertFalse(clazz.getTypeInfo().isDeprecated());
    for (IAttributedFeatureInfo feature : clazz.getTypeInfo().getDeclaredProperties()) {
      assertFalse(feature.isDeprecated());
    }
    for (IAttributedFeatureInfo feature : clazz.getTypeInfo().getDeclaredConstructors()) {
      assertFalse(feature.isDeprecated());
    }
    for (IAttributedFeatureInfo feature : clazz.getTypeInfo().getDeclaredMethods()) {
      assertFalse(feature.isDeprecated());
    }
  }

  public void testClassWithDeprecatedClassHasAllFeaturesDeprecated() {
    IGosuClassInternal clazz = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.DeprecatedTestDeprecatedClass");
    assertTrue(clazz.getTypeInfo().isDeprecated());
    for (IAttributedFeatureInfo feature : clazz.getTypeInfo().getDeclaredProperties()) {
      assertTrue(feature.isDeprecated());
    }
    for (IAttributedFeatureInfo feature : clazz.getTypeInfo().getDeclaredConstructors()) {
      assertTrue(feature.isDeprecated());
    }
    for (IAttributedFeatureInfo feature : clazz.getTypeInfo().getDeclaredMethods()) {
      assertTrue(feature.isDeprecated());
    }
  }

  public void testClassWithDeprecatedFeaturesHasCorrectFeaturesDeprecated() {
    IGosuClassInternal clazz = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.DeprecatedTestDeprecatedFeatures");
    assertFalse(clazz.getTypeInfo().isDeprecated());
    assertTrue(clazz.getTypeInfo().getProperty("varFeature").isDeprecated());
    assertFalse(clazz.getTypeInfo().getProperty("varFeature2").isDeprecated());
    assertTrue(clazz.getTypeInfo().getProperty("varFeatureWithProperty").isDeprecated());
    assertFalse(clazz.getTypeInfo().getProperty("varFeatureWithProperty2").isDeprecated());
    assertTrue(clazz.getTypeInfo().getProperty("VarAsProperty").isDeprecated());
    assertFalse(clazz.getTypeInfo().getProperty("VarAsProperty2").isDeprecated());
    assertTrue(clazz.getTypeInfo().getProperty("varProperty").isDeprecated());
    assertFalse(clazz.getTypeInfo().getProperty("varProperty2").isDeprecated());
    assertTrue(clazz.getTypeInfo().getMethod("varFunction").isDeprecated());
    assertFalse(clazz.getTypeInfo().getMethod("varFunction2").isDeprecated());
    assertTrue(clazz.getTypeInfo().getMethod("varFunctionWithParam", JavaTypes.STRING()).isDeprecated());
    assertFalse(clazz.getTypeInfo().getMethod("varFunctionWithParam2", JavaTypes.STRING()).isDeprecated());
    assertTrue(clazz.getTypeInfo().getConstructor().isDeprecated());
    assertFalse(clazz.getTypeInfo().getConstructor(JavaTypes.STRING()).isDeprecated());
  }

}
