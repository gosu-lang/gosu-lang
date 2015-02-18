/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.StandardCoercionManager;
import gw.util.DynamicArray;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MethodList extends DynamicArray<IMethodInfo> {
  public static final MethodList EMPTY = new MethodList();

  private HashMap<String, DynamicArray<IMethodInfo>> map = new HashMap<String, DynamicArray<IMethodInfo>>();

  public MethodList() {
  }

  public MethodList(List<IMethodInfo> methods) {
    addAll(methods);
  }

  public MethodList(int size) {
    super(size);
  }

  public MethodList filterMethods(IRelativeTypeInfo.Accessibility accessibility) {
    MethodList ret = new MethodList();
    for (IMethodInfo method : this) {
      if (FeatureManager.isFeatureAccessible(method, accessibility)) {
        ret.add(method);
      }
    }
    ret.trimToSize();
    return ret;
  }


  @Override
  public boolean add(IMethodInfo method) {
    addToMap(method);
    return super.add(method);
  }

  @Override
  public boolean addAll(Collection<? extends IMethodInfo> c) {
    for (IMethodInfo method : c) {
      addToMap(method);
    }
    return super.addAll(c);
  }

  private void addToMap(IMethodInfo method) {
    String displayName = (String)method.getDisplayName();
    DynamicArray<IMethodInfo> methods = map.get(displayName);
    if (methods == null) {
      methods = new DynamicArray<IMethodInfo>(1);
      map.put(displayName, methods);
    }
    methods.add(method);
  }

  @Override
  public IMethodInfo remove(int index) {
    IMethodInfo oldMethod = get(index);
    String displayName = (String)oldMethod.getDisplayName();
    DynamicArray<IMethodInfo> methods = map.get(displayName);
    int i = methods.indexOf(oldMethod);
    methods.remove(i);

    return super.remove(index);
  }

  @Override
  public IMethodInfo set(int index, IMethodInfo method) {
    IMethodInfo oldMethod = get(index);
    String displayName = (String)method.getDisplayName();
    DynamicArray<IMethodInfo> methods = map.get(displayName);
    int i = methods.indexOf(oldMethod);
    methods.set(i, method);

    return super.set(index, method);
  }


  public DynamicArray<? extends IMethodInfo> getMethods(String name) {
    DynamicArray<IMethodInfo> methodInfoList = map.get( name );
    return methodInfoList != null ? methodInfoList : DynamicArray.EMPTY;
  }

  public static MethodList singleton(IMethodInfo theOneMethod) {
    MethodList infos = new MethodList(1);
    infos.add(theOneMethod);
    return infos;
  }

  @Override
  public void add(int index, IMethodInfo method) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean addAll(int index, Collection<? extends IMethodInfo> c) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean remove(Object o) {
    throw new RuntimeException("Not supported");
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public Object clone() {
    return super.clone();
  }

  @Override
  public void clear() {
    super.clear();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new RuntimeException("Not supported");
  }

  public IMethodInfo findAssignableMethod( IMethodInfo miTo, boolean bStatic ) {
    IMethodInfo foundMethod = null;
    String mname = miTo.getDisplayName();
    IParameterInfo[] toParams = miTo.getParameters();
    int iTopScore = 0;
    outer:
    for( IMethodInfo miFrom : this ) {
      if( miFrom.isStatic() == bStatic && miFrom.getDisplayName().equals( mname ) ) {
        IType fromReturnType = miFrom.getReturnType();
        IType toReturnType = miTo.getReturnType();
        fromReturnType = TypeSystem.replaceTypeVariableTypeParametersWithBoundingTypes( fromReturnType, miFrom.getOwnersType() );
        toReturnType = TypeSystem.replaceTypeVariableTypeParametersWithBoundingTypes( toReturnType, miTo.getOwnersType() );
        if( !toReturnType.equals( fromReturnType ) &&
            !toReturnType.isAssignableFrom( fromReturnType ) &&
            !StandardCoercionManager.arePrimitiveTypesAssignable( toReturnType, fromReturnType ) &&
            !TypeSystem.isBoxedTypeFor( toReturnType, fromReturnType ) &&
            !TypeSystem.isBoxedTypeFor( fromReturnType, toReturnType ) ) {
          continue;
        }
        IParameterInfo[] fromParams = miFrom.getParameters();
        if( fromParams.length == toParams.length ) {
          if( fromParams.length == 0 ) {
            foundMethod = miFrom;
          }
          int iScore = 0;
          for( int ip = 0; ip < fromParams.length; ip++ ) {
            IParameterInfo fromParam = fromParams[ip];
            IParameterInfo toParam = toParams[ip];
            IType fromParamType = TypeSystem.replaceTypeVariableTypeParametersWithBoundingTypes( fromParam.getFeatureType(), miFrom.getOwnersType() );
            IType toParamType = TypeSystem.replaceTypeVariableTypeParametersWithBoundingTypes( toParam.getFeatureType(), miTo.getOwnersType() );
            if( fromParamType.equals( toParamType ) ) {
              // types are the same
              iScore += 2;
            }
            else if( fromParamType.isAssignableFrom( toParamType ) ||
                     StandardCoercionManager.arePrimitiveTypesAssignable( fromParamType, toParamType ) ||
                     TypeSystem.isBoxedTypeFor( toParamType, fromParamType ) ||
                     TypeSystem.isBoxedTypeFor( fromParamType, toParamType ) ) {
              // types are contravariant
              iScore += 1;
            }
            else {
              continue outer;
            }
          }
          if( iTopScore < iScore ) {
            foundMethod = miFrom;
            iTopScore = iScore;
          }
        }
      }
    }
    return foundMethod;
  }
}
