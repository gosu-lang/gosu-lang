/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.ir.IRType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuClassUtil;

import gw.util.Array;

public class GosuClassIRType implements IRType {
  private IType _type;
  private Boolean _structural;

  public static GosuClassIRType get(IType type) {
    if (!(type instanceof IGosuClassInternal || type instanceof IGosuArrayClass)) {
      throw new IllegalArgumentException("Cannot create a GosuClassIRType from a type of type " + type.getClass());
    }
    return new GosuClassIRType(type);
  }

  private GosuClassIRType(IType type) {
    _type = type;
  }

  public IType getType() {
    return _type;
  }

  @Override
  public String getName() {
    return _type.getName();
  }

  @Override
  public String getRelativeName() {
    return _type.getRelativeName();
  }

  @Override
  public String getDescriptor() {
    if (isArray()) {
      return '[' + getComponentType().getDescriptor();
    } else {
      return 'L' + getSlashName( ) + ';';
    }
  }

  @Override
  public boolean isStructural() {
    return _structural == null
           ? _structural = (_type instanceof IGosuClass && ((IGosuClass)_type).isStructure())
           : _structural;
  }

  @Override
  public boolean isStructuralAndErased( IRType ownersType ) {
    return isStructural() && ownersType instanceof GosuClassIRType;
  }

  @Override
  public Class getJavaClass() {
    if ( isArray() ) {
      return Array.newInstance(getComponentType().getJavaClass(), 0).getClass();    
    } else {
      try
      {
        return GosuClassLoader.instance().loadClass( _type.getName() );
      }
      catch( ClassNotFoundException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  @Override
  public String getSlashName() {
    if( isArray() ) {
      return getComponentType().getSlashName() + "[]";
    }

    IType outerType = _type.getEnclosingType();
    if( outerType != null )
    {
      return IRTypeFactory.get( outerType).getSlashName( ) + "$" + GosuClassUtil.getNameNoPackage( _type.getName() );
    }

    IType type = _type;
    IJavaType jtype = ((IGosuClassInternal)_type).getJavaType();
    if( jtype != null && !jtype.getBackingClassInfo().isAnnotation() )
    {
      // The Gosu type here is a proxy to a java class; use the java class
      type = jtype;
    }
    return type.getName().replace( '.', '/' );
  }

  @Override
  public IRType getArrayType() {
    return IRTypeFactory.get(_type.getArrayType());
  }

  @Override
  public IRType getComponentType() {
    return IRTypeFactory.get(_type.getComponentType());
  }

  @Override
  public boolean isArray() {
    return _type.isArray();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof GosuClassIRType && _type.equals(((GosuClassIRType) obj)._type);
  }

  @Override
  public boolean isAssignableFrom(IRType otherType) {
    if (isArray() && otherType.isArray()) {
      return getComponentType().isAssignableFrom( otherType.getComponentType() );
    } else if (isArray() || otherType.isArray()) {
      return false;
    }

    if (otherType instanceof GosuClassIRType) {
      return _type.isAssignableFrom(((GosuClassIRType) otherType)._type);
    } else {
      return false;
    }
  }

  @Override
  public boolean isByte() {
    return false;
  }

  @Override
  public boolean isBoolean() {
    return false;
  }

  @Override
  public boolean isShort() {
    return false;
  }

  @Override
  public boolean isChar() {
    return false;
  }

  @Override
  public boolean isInt() {
    return false;
  }

  @Override
  public boolean isLong() {
    return false;
  }

  @Override
  public boolean isFloat() {
    return false;
  }

  @Override
  public boolean isDouble() {
    return false;
  }

  @Override
  public boolean isVoid() {
    return false;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  @Override
  public boolean isInterface() {
    return _type.isInterface();
  }

  @Override
  public String toString()
  {
    return getName();
  }
}
