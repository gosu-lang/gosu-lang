/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import com.intellij.util.PlatformIcons;
import gw.lang.IModuleAware;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Set;

/**
 */
public class TypeInPackageType implements IType, IModuleAware {
  public final TypeName _typeName;
  private String _strRelativeName;
  private String _strParent;
  @Nullable
  private TypeInPackageTypeInfo _typeInfo;
  private boolean _bDiscarded;

  public TypeInPackageType(TypeName typeName) {
    _typeName = typeName;
    assignRelativePackageAndParent();
  }

  private void assignRelativePackageAndParent() {
    String fqn = _typeName.name;
    int iLastDot = fqn.lastIndexOf('.');
    if (iLastDot >= 0) {
      _strRelativeName = fqn.substring(iLastDot + 1);
      _strParent = fqn.substring(0, iLastDot);
    } else {
      _strRelativeName = fqn;
      _strParent = "";
    }
  }

  public String getName() {
    return _typeName.name;
  }

  public String getDisplayName() {
    return getName();
  }

  public String getRelativeName() {
    return _strRelativeName;
  }

  public String getNamespace() {
    return _strParent;
  }

  @Nullable
  public ITypeLoader getTypeLoader() {
    return null;
  }

  public boolean isInterface() {
    return false;
  }

  public boolean isEnum() {
    return false;
  }

  public IType[] getInterfaces() {
    return EMPTY_TYPE_ARRAY;
  }

  @Nullable
  public IType getSupertype() {
    return getEnclosingType();
  }

  @Nullable
  public PackageType getEnclosingType() {
//    if (_strParent == null) {
//      return null;
//    }
//
//    return PackageType.create(_strParent, );
    return null;
  }

  @Nullable
  public IType getGenericType() {
    return null;
  }

  public boolean isFinal() {
    return true;
  }

  public boolean isParameterizedType() {
    return false;
  }

  public boolean isGenericType() {
    return false;
  }

  @NotNull
  public IGenericTypeVariable[] getGenericTypeVariables() {
    return new IGenericTypeVariable[0];
  }

  @Nullable
  public IType getParameterizedType(IType... ofType) {
    return null;
  }

  @NotNull
  public IType[] getTypeParameters() {
    return IType.EMPTY_ARRAY;
  }

  public Set<? extends IType> getAllTypesInHierarchy() {
    return Collections.singleton(this);
  }

  public boolean isArray() {
    return false;
  }

  public boolean isPrimitive() {
    return false;
  }

  @NotNull
  public IType getArrayType() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public Object makeArrayInstance(int iLength) {
    throw new UnsupportedOperationException();
  }

  @Nullable
  public Object getArrayComponent(Object array, int iIndex) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    return null;
  }

  public void setArrayComponent(Object array, int iIndex, Object value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
  }

  public int getArrayLength(Object array) throws IllegalArgumentException {
    return 0;
  }

  @Nullable
  public IType getComponentType() {
    return null;
  }

  public boolean isAssignableFrom(IType type) {
    return type == this;
  }

  public boolean isMutable() {
    return false;
  }

  @Nullable
  public ITypeInfo getTypeInfo() {
    if (_typeInfo == null) {
      _typeInfo = new TypeInPackageTypeInfo(this);
    }
    return _typeInfo;
  }

  public void unloadTypeInfo() {
    _typeInfo = null;
  }

  public Object readResolve() throws ObjectStreamException {
    return this;
  }

  public boolean isValid() {
    return true;
  }

  public int getModifiers() {
    return Modifier.PUBLIC;
  }

  public boolean isAbstract() {
    return false;
  }

  public boolean isDiscarded() {
    return _bDiscarded;
  }

  public void setDiscarded(boolean bDiscarded) {
    _bDiscarded = bDiscarded;
  }

  public boolean isCompoundType() {
    return false;
  }

  @Nullable
  public Set<IType> getCompoundTypeComponents() {
    return null;
  }

  @Override
  public IModule getModule() {
    return _typeName.getModule();
  }

  public Icon getIcon() {
    return PlatformIcons.CLASS_ICON;
  }

  @Override
  public IMetaType getMetaType() {
    throw new RuntimeException("Not supported");
  }

  @Override
  public IMetaType getLiteralMetaType() {
    throw new RuntimeException("Not supported");
  }
}
