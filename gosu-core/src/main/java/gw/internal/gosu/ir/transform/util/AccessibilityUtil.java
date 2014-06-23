/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.util;

import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.parser.ISymbol;
import gw.internal.gosu.parser.Symbol;

import java.lang.reflect.Method;

public class AccessibilityUtil {

  public static IRelativeTypeInfo.Accessibility forMethod(Method method) {
    if (Modifier.isPublic(method.getModifiers())) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else if (Modifier.isProtected(method.getModifiers())) {
      return IRelativeTypeInfo.Accessibility.PROTECTED;
    } else if (Modifier.isPrivate(method.getModifiers())) {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    } else {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
  }

  public static IRelativeTypeInfo.Accessibility forMethod(IJavaClassMethod method) {
    if (Modifier.isPublic(method.getModifiers())) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else if (Modifier.isProtected(method.getModifiers())) {
      return IRelativeTypeInfo.Accessibility.PROTECTED;
    } else if (Modifier.isPrivate(method.getModifiers())) {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    } else {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
  }

  public static IRelativeTypeInfo.Accessibility forFeatureInfo(IAttributedFeatureInfo feature) {
    if (feature.isPublic()) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else if (feature.isProtected()) {
      return IRelativeTypeInfo.Accessibility.PROTECTED;
    } else if (feature.isPrivate()) {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    } else {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
  }

  public static IRelativeTypeInfo.Accessibility forSymbol(IReducedSymbol symbol) {
    if (symbol.isPublic()) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else if (symbol.isProtected()) {
      return IRelativeTypeInfo.Accessibility.PROTECTED;
    } else if (symbol.isPrivate()) {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    } else {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
  }

  public static IRelativeTypeInfo.Accessibility forCapturedVar() {
    if (BytecodeOptions.isSingleServingLoader()) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    }
  }

  public static IRelativeTypeInfo.Accessibility forTypeParameter() {
    if (BytecodeOptions.isSingleServingLoader()) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
  }

  public static IRelativeTypeInfo.Accessibility forOuter() {
    if (BytecodeOptions.isSingleServingLoader()) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
  }

  public static IRelativeTypeInfo.Accessibility forOuterAccess() {
    if (BytecodeOptions.isSingleServingLoader()) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
  }

  public static IRelativeTypeInfo.Accessibility forType( IType type ) {
    int modifiers = type.getModifiers();
    if ( Modifier.isPublic(modifiers) ) {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    } else if ( Modifier.isInternal( modifiers ) ) {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    } else if ( Modifier.isPrivate( modifiers ) ) {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    } else {
      if ( type instanceof IJavaType ) {
        // Java types default to internal access if no modifiers are present
        return IRelativeTypeInfo.Accessibility.INTERNAL;
      } else {
        // All other types should be considered to default to public access
        return IRelativeTypeInfo.Accessibility.PUBLIC;
      }
    }
  }
}
