/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.AbstractType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.DefaultArrayType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.util.concurrent.LockingLazyVar;
import gw.util.GosuClassUtil;

import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectStreamException;

public abstract class WrappingTestTypeBase extends AbstractType implements IWrappingTestType {

  private IType _wrappedType;
  private String _name;
  private ITypeLoader _loader;
  private IType _arrayType;
  private LockingLazyVar<ITypeInfo> _typeInfo;
  private List<Class> _wrappedInterfaces;
  private List<IWrappingTestType.PropertyListener> _propListeners;
  private List<IWrappingTestType.MethodListener> _methodListeners;
  private List<IWrappingTestType.ConstructorListener> _constructorListeners;
  private Set<IType> _allTypesInHierarchy;

  /**
   * @param loader of this type
   * @param name of this type
   * @param wrappedType that this type wraps
   * @param wrappedInterfaces interfaces that instances of the wrapped type implement
   *        (which will be implemented by instances of this type)
   */
  public void init(ITypeLoader loader, String name, IType wrappedType, Class... wrappedInterfaces) {
    _loader = loader;
    _name = name;
    _wrappedType = wrappedType;
    _typeInfo = new LockingLazyVar<ITypeInfo>() {
      @Override
      protected ITypeInfo init() {
        return makeTypeInfo();
      }
    };
    _wrappedInterfaces = Arrays.asList(wrappedInterfaces);
    _propListeners = new ArrayList<IWrappingTestType.PropertyListener>();
    _methodListeners = new ArrayList<IWrappingTestType.MethodListener>();
    _constructorListeners = new ArrayList<IWrappingTestType.ConstructorListener>();
    _allTypesInHierarchy = makeTypeHierarchy();
    _arrayType = new DefaultArrayType( TypeSystem.getOrCreateTypeReference( this ), TypeSystem.getJavaClassInfo(Object.class), _loader );
  }

  protected abstract Set<IType> makeTypeHierarchy();

  protected abstract ITypeInfo makeTypeInfo();

  public Closeable withPropertyListener(final IWrappingTestType.PropertyListener listener) {
    _propListeners.add(listener);
    return new Closeable(){
      @Override
      public void close() throws IOException {
        _propListeners.remove(listener);
      }
    };
  }

  public void firePropertyAccess(IPropertyInfo pi, Object ctx, boolean set, Object value) {
    for (IWrappingTestType.PropertyListener listener : _propListeners) {
      listener.onPropertyAccess(pi, ctx, set, value);
    }
  }

  public Closeable withMethodListener(final IWrappingTestType.MethodListener listener) {
    _methodListeners.add(listener);
    return new Closeable(){
      @Override
      public void close() throws IOException {
        _methodListeners.remove(listener);
      }
    };
  }

  public void fireMethodCalled(IMethodInfo info, Object o, Object[] args) {
    for (IWrappingTestType.MethodListener listener : _methodListeners) {
      listener.onMethodCall(info, o, args);
    }
  }

  public Closeable withConstructorListener(final IWrappingTestType.ConstructorListener listener) {
    _constructorListeners.add(listener);
    return new Closeable(){
      @Override
      public void close() throws IOException {
        _constructorListeners.remove(listener);
      }
    };
  }


  public void fireConstructorCalled(Object[] args) {
    for (IWrappingTestType.ConstructorListener listener : _constructorListeners) {
      listener.onConstructor(args);
    }
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getRelativeName() {
    return GosuClassUtil.getNameNoPackage(_name);
  }

  @Override
  public String getNamespace() {
    return GosuClassUtil.getPackage(_name);
  }

  @Override
  public ITypeLoader getTypeLoader() {
    return _loader;
  }

  @Override
  public IType getSupertype() {
    return null;
  }

  @Override
  public IType getEnclosingType() {
    return null;
  }

  @Override
  public IType getGenericType() {
    return null;
  }

  @Override
  public boolean isFinal() {
    return false;
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public boolean isEnum() {
    return false;
  }

  @Override
  public IType[] getInterfaces() {
    return EMPTY_TYPE_ARRAY;
  }

  @Override
  public boolean isParameterizedType() {
    return false;
  }

  @Override
  public boolean isGenericType() {
    return false;
  }

  @Override
  public IGenericTypeVariable[] getGenericTypeVariables() {
    return new IGenericTypeVariable[0];
  }

  @Override
  public IType getParameterizedType(IType... ofType) {
    return null;
  }

  @Override
  public IType[] getTypeParameters() {
    return new IType[0];
  }

  @Override
  public Set<? extends IType> getAllTypesInHierarchy() {
    return _allTypesInHierarchy;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  @Override
  public IType getArrayType() {
    return TypeSystem.getOrCreateTypeReference( _arrayType );
  }

  @Override
  public Object makeArrayInstance(int iLength) {
    return new Object[iLength];
  }

  @Override
  public Object getArrayComponent(Object array, int iIndex) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    return ((Object[]) array)[iIndex];
  }

  @Override
  public void setArrayComponent(Object array, int iIndex, Object value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    // Why does intellij warn on this cast?
    //noinspection RedundantCast
    ((Object[]) array)[iIndex] = value;
  }

  @Override
  public int getArrayLength(Object array) throws IllegalArgumentException {
    return ((Object[]) array).length;
  }

  @Override
  public IType getComponentType() {
    return null;
  }

  @Override
  public boolean isAssignableFrom(IType type) {
    return type.equals(this);
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public ITypeInfo getTypeInfo() {
    return _typeInfo.get();
  }

  @Override
  public void unloadTypeInfo() {
    _typeInfo.clear();
  }

  @Override
  public Object readResolve() throws ObjectStreamException {
    return null;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public int getModifiers() {
    return Modifier.PUBLIC;
  }

  @Override
  public boolean isAbstract() {
    return false;
  }

  @Override
  public boolean isDiscarded() {
    return false;
  }

  @Override
  public void setDiscarded(boolean bDiscarded) {
  }

  @Override
  public boolean isCompoundType() {
    return false;
  }

  @Override
  public Set<IType> getCompoundTypeComponents() {
    return Collections.emptySet();
  }

  public IType getWrappedType() {
    return _wrappedType;
  }

  public List<Class> getWrappedInterfaces() {
    return _wrappedInterfaces;
  }

}
