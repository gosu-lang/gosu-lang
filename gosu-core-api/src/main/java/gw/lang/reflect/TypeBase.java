/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.util.concurrent.LockingLazyVar;

import java.io.ObjectStreamException;
import gw.util.Array;
import java.util.HashSet;
import java.util.Set;

public abstract class TypeBase extends AbstractType implements IType
{
  private static final IGenericTypeVariable[] EMPTY_TYPE_VARIABLES_ARRAY = new IGenericTypeVariable[0];

  private transient final LockingLazyVar<Set<? extends IType>> _allTypesInHierarchyCache;
  private transient final LockingLazyVar<IType> _arrayTypeCache;
  protected transient final IJavaClassInfo _arrayComponentClass;
  private transient boolean _bDiscarded;

  protected TypeBase(IJavaClassInfo arrayComponentClass) {
    _arrayComponentClass = arrayComponentClass;
    _allTypesInHierarchyCache = new LockingLazyVar<Set<? extends IType>>() {
      protected Set<? extends IType> init() {
        return loadAllTypesInHierarchy();
      }
    };
    _arrayTypeCache = new LockingLazyVar<IType>() {
      protected IType init() {
        IType arrayType = makeArrayType();
        return TypeSystem.getOrCreateTypeReference(arrayType);
      }
    };
  }

  protected TypeBase(Class<?> arrayComponentClass) {
    this(TypeSystem.getJavaClassInfo(arrayComponentClass));
  }

  protected TypeBase() {
    this(Object.class);
  }

  protected IType makeArrayType() {
    return new DefaultArrayType(TypeSystem.getOrCreateTypeReference(TypeBase.this), getArrayComponentClass(), getTypeLoader());
  }

  public IType getEnclosingType()
  {
    return null;
  }

  public IType getGenericType()
  {
    return null;
  }

  public boolean isFinal()
  {
    return false;
  }

  public boolean isInterface()
  {
    return false;
  }

  public boolean isEnum()
  {
    return false;
  }

  public boolean isParameterizedType()
  {
    return false;
  }

  public boolean isGenericType()
  {
    return false;
  }

  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    return EMPTY_TYPE_VARIABLES_ARRAY;
  }

  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  public IType[] getTypeParameters()
  {
    return IType.EMPTY_TYPE_ARRAY;
  }

  public boolean isArray()
  {
    return false;
  }

  public boolean isPrimitive()
  {
    return false;
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return ((Object[]) array)[iIndex];
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    ((Object[]) array)[iIndex] = value;
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return ((Object[]) array).length;
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isMutable()
  {
    return true;
  }

  public Object readResolve() throws ObjectStreamException
  {
    return TypeSystem.getByFullName( getName() );
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return Modifier.PUBLIC;
  }

  public boolean isAbstract()
  {
    return Modifier.isAbstract( getModifiers() );
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String toString()
  {
    return getName();
  }

  public Set<? extends IType> getAllTypesInHierarchy() {
    return _allTypesInHierarchyCache.get();
  }

  protected Set<? extends IType> loadAllTypesInHierarchy() {
    Set<IType> allTypes;
    if( isArray() )
    {
      allTypes = getArrayVersionsOfEachType( getComponentType().getAllTypesInHierarchy() );
    }
    else
    {
      allTypes = getAllClassesInClassHierarchyAsIntrinsicTypes( TypeSystem.getOrCreateTypeReference( this ) );
    }
    return allTypes;
  }

  protected static Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( IType type )
  {
    HashSet<IType> typeSet = new HashSet<>();
    addAllClassesInClassHierarchy( type, typeSet );
    return typeSet;
  }

  private static void addAllClassesInClassHierarchy(IType type, Set<IType> set) {
    if (!set.add(type)) {
      return;
    }

    for (IType iface : type.getInterfaces()) {
      addAllClassesInClassHierarchy(iface, set);
    }

    if (type.getSupertype() != null) {
      addAllClassesInClassHierarchy(type.getSupertype(), set);
    }

    if (type.isParameterizedType()) {
      addAllClassesInClassHierarchy(type.getGenericType(), set);
    }

    if (type.isGenericType() || !type.isParameterizedType()) {
      addAllClassesInClassHierarchy(TypeSystem.getDefaultParameterizedType( type ), set);
    }
  }

  protected Set<IType> getArrayVersionsOfEachType( Set<? extends IType> componentTypes )
  {
    Set<IType> allTypes = new HashSet<>( 1 + componentTypes.size() );
    allTypes.add( JavaTypes.OBJECT() );
    for( IType componentType : componentTypes )
    {
      allTypes.add( componentType.getArrayType() );
    }
    return allTypes;
  }

  public boolean isAssignableFrom(IType type) {
    return type.getAllTypesInHierarchy().contains( TypeSystem.getOrCreateTypeReference( this ) );
  }

  public IType getArrayType() {
    return _arrayTypeCache.get();
  }

  public Object makeArrayInstance(int iLength) {
    return Array.newInstance(getArrayComponentClass().getBackingClass(), iLength);
  }

  protected IJavaClassInfo getArrayComponentClass() {
    return _arrayComponentClass;
  }

  public void unloadTypeInfo() {
    _allTypesInHierarchyCache.clear();
    if (_arrayTypeCache.isLoaded()) {
      _arrayTypeCache.get().unloadTypeInfo();
    }
  }

  public boolean isDiscarded()
  {
    return _bDiscarded;
  }

  public void setDiscarded( boolean bDiscarded )
  {
    _bDiscarded = bDiscarded;
  }

  public boolean isCompoundType() {
    return false;
  }

  public Set<IType> getCompoundTypeComponents() {
    return null;
  }
}
