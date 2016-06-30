/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;
import gw.lang.reflect.IType;

@UnstableAPI
public class SyntheticIRType implements IRType {     

  private Class _superClass;
  private String _name;
  private String _relativeName;

  public SyntheticIRType(Class superClass, String name, String relativeName) {
    _superClass = superClass;
    _name = name;
    _relativeName = relativeName;
  }

  public Class getSuperClass() {
    return _superClass;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public String getRelativeName() {
    return _relativeName;
  }

  @Override
  public String getDescriptor() {
    return 'L' + getSlashName( ) + ';';
  }

  @Override
  public Class getJavaClass() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getSlashName() {
    return _name.replace( '.', '/' );
  }

  @Override
  public IType getType()
  {
    return null;
  }

  @Override
  public IRType getArrayType() {
    return new SyntheticIRArrayType(this);
  }

  @Override
  public IRType getComponentType() {
    return null;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public boolean isAssignableFrom(IRType otherType) {
    return otherType instanceof SyntheticIRType && otherType.getName().equals(_name);
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
    return false;
  }

  @Override
  public boolean isStructural() {
    return false;
  }

  @Override
  public boolean isStructuralAndErased( IRType ownersType ) {
    return false;
  }
}
