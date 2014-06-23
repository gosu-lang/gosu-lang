/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.JavaTypes;

import java.util.List;
import java.util.ArrayList;

@UnstableAPI
public abstract class IRElementBuilder {

  // ------------------------ Protected Helper Methods

  protected static IRType getIRType( Class cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected static IRType getIRType( IType type ) {
    return GosuShop.getIRTypeResolver().getDescriptor( type );
  }

  protected static IRType getIRType( IJavaClassInfo cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected static List<IRType> getIRTypes( Class[] classes ) {
    List<IRType> results = new ArrayList<IRType>();
    for (Class cls : classes) {
      results.add( getIRType( cls ) );
    }
    return results;
  }
  protected static List<IRType> getIRTypes( IJavaClassInfo[] classes ) {
    List<IRType> results = new ArrayList<IRType>();
    for (IJavaClassInfo cls : classes) {
      results.add( getIRType( cls ) );
    }
    return results;
  }

  // ------------------------ Constructor Resolution

  protected static IJavaClassConstructor findConstructor( IJavaClassInfo cls, int numArgs ) {
    IJavaClassConstructor match = null;
    for (IJavaClassConstructor cons : cls.getDeclaredConstructors()) {
      if (cons.getParameterTypes().length == numArgs) {
        if (match != null) {
          throw new IllegalArgumentException("The call to create a new " + cls + " with " + numArgs + " arguments is ambiguous");
        } else {
          match = cons;
        }
      }
    }

    if (match == null) {
      throw new IllegalArgumentException("No constructor with " + numArgs + " arguments found on class " + cls);
    }

    return match;
  }

  // ------------------------ IJavaClassMethod Resolution

  protected static IJavaClassMethod findMethod( IJavaClassInfo cls, String name, int numArgs ) {
    IJavaClassMethod match = findUniqueMethodInList( cls.getDeclaredMethods(), cls, name, numArgs );

    if (match != null) {
      return match;
    }

    match = findDeclaredMethod( cls, name, numArgs );
    if (match != null) {
      return match;
    }

    if ( name.equals("toString") || name.equals("equals") || name.equals("hashCode")) {
      return findMethod( JavaTypes.OBJECT().getBackingClassInfo(), name, numArgs );
    }

    throw new IllegalArgumentException("No method named " + name + " with " + numArgs + " arguments found starting from class " + cls);
  }

  private static IJavaClassMethod findDeclaredMethod( IJavaClassInfo cls, String name, int numArgs ) {
    IJavaClassMethod match = findUniqueMethodInList( cls.getDeclaredMethods(), cls, name, numArgs );

    if (match != null) {
      return match;
    }

    if ( cls.getSuperclass() != null ) {
      return findDeclaredMethod( cls.getSuperclass(), name, numArgs );
    }

    return null;
  }

  private static IJavaClassMethod findUniqueMethodInList( IJavaClassMethod[] methods, IJavaClassInfo cls, String name, int numArgs ) {
    IJavaClassMethod match = null;
    for (IJavaClassMethod m : methods) {
      if (m.getName().equals(name) && m.getParameterTypes().length == numArgs) {
        if (match != null) {
          throw new IllegalArgumentException("The call to method " + name + " on class " + cls.getName() + " with " + numArgs + " arguments is ambiguous");
        }
        match = m;
      }
    }

    return match;
  }

  // ------------------------- Field Resolution

  protected static IJavaClassField findField( IJavaClassInfo cls, String name ) {
    IJavaClassField[] allFields = cls.getFields();

    for (IJavaClassField f : allFields) {
      if (f.getName().equals(name)) {
        return f;
      }
    }

    IJavaClassField match = findDeclaredField( cls, name );
    if ( match != null ) {
      return match;
    }

    throw new IllegalArgumentException("No field named " + name + " found starting from class " + cls);
  }

  private static IJavaClassField findDeclaredField( IJavaClassInfo cls, String name ) {
    IJavaClassField[] declaredFields = cls.getDeclaredFields();

    for (IJavaClassField f : declaredFields) {
      if (f.getName().equals(name)) {
        return f;
      }
    }

    if ( cls.getSuperclass() != null ) {
      return findDeclaredField( cls.getSuperclass(), name );
    }

    return null;
  }

}
