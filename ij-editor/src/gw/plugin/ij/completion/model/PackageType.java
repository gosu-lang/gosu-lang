/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Set;

/**
 */
public class PackageType implements IType {
  @Nullable
  private PackageTypeInfo _typeInfo;
  private boolean _bDiscarded;
  private INamespaceType _namespace;

  public PackageType(INamespaceType namespaceType) {
    _namespace = namespaceType;
  }

  public String getName() {
    return _namespace.getName();
  }

  public String getDisplayName() {
    return getName();
  }

  public String getRelativeName() {
    return _namespace.getRelativeName();
  }

  public String getNamespace() {
    return _namespace.getEnclosingType().getName();
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
  public PackageType getSupertype() {
    return getEnclosingType();
  }

  @Nullable
  public PackageType getEnclosingType() {
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
      _typeInfo = new PackageTypeInfo(this);
    }
    return _typeInfo;
  }

  public void unloadTypeInfo() {
    _typeInfo = null;
  }

  public Object readResolve() throws ObjectStreamException {
    return null;
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

  public String toString() {
    return _namespace.toString();
  }

  public static PackageType create(IGosuPsiElement namespace, IModule module) {
    String namespaceText = namespace.getText();
    INamespaceType namespaceType = TypeSystem.getNamespace(namespaceText, module);
    if (namespaceType == null) {
      IParsedElement parsedElement = namespace.getParsedElement();
      if (parsedElement != null) {
        IGosuClass gosuClass = parsedElement.getGosuClass();
        ITypeUsesMap typeUsesMap = getTypeUsesMap(gosuClass);
        for (String typeUse : typeUsesMap.getNamespaces()) {
          namespaceType = TypeSystem.getNamespace(typeUse + namespaceText, module);
          if (namespaceType != null) {
            break;
          }
        }
      }
    }
    return namespaceType != null ? new PackageType(namespaceType) : null;
  }

  private static ITypeUsesMap getTypeUsesMap(IGosuClass type) {
    while (type.getEnclosingType() != null) {
      type = (IGosuClass) type.getEnclosingType();
    }
    return type.getTypeUsesMap();
  }

  public INamespaceType getNamespaceType() {
    return _namespace;
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
