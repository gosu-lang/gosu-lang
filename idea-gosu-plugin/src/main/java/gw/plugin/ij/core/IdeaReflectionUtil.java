/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import java.lang.reflect.Field;

public class IdeaReflectionUtil {

  public static Field getField(String name, String obfuscatedName, Class<?> clazz) {
    try {
      return clazz.getDeclaredField(name);
    } catch (NoSuchFieldException e1) {
      if (obfuscatedName == null) {
        throw new RuntimeException("No obfuscated field name provided.");
      }
      try {
        return clazz.getDeclaredField(obfuscatedName);
      } catch (NoSuchFieldException e2) {
      }
    }
    throw new RuntimeException("Cannot find field: " + name);
  }

  public static Object getFieldValue(String name, String obfuscatedName, Class<?> clazz, Object object) {
    Field field = getField(name, obfuscatedName, clazz);
    try {
      field.setAccessible( true );
      return field.get(object);
    } catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public static void setFieldValue(String name, String obfuscatedName, Class<?> clazz, Object object, Object value) {
    Field field = getField(name, obfuscatedName, clazz);
    try {
      field.setAccessible( true );
      field.set( object, value );
    } catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

}
