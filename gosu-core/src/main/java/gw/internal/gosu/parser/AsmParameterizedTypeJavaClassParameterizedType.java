/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.source.tree.Tree;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassParameterizedType;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.IAsmType;
import gw.lang.reflect.module.IModule;

import java.util.List;

public class AsmParameterizedTypeJavaClassParameterizedType extends AsmTypeJavaClassType implements IJavaClassParameterizedType {

  public AsmParameterizedTypeJavaClassParameterizedType( IAsmType parameterizedType, IModule module ) {
    super(parameterizedType, module);
  }

  @Override
  public IJavaClassType[] getActualTypeArguments() {
    List<AsmType> rawTypes = getType().getTypeParameters();
    IJavaClassType[] types = new IJavaClassType[rawTypes.size()];
    for (int i = 0; i < rawTypes.size(); i++) {
      types[i] = AsmTypeJavaClassType.createType( getType(), rawTypes.get( i ), _module );
    }
    return types;
  }

  @Override
  public IJavaClassType getConcreteType() {
    return AsmTypeJavaClassType.createType( getType().getRawType(), _module );
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public IJavaClassType getComponentType() {
    return null;
  }

  @Override
  public String getSimpleName() {
    return getType().getSimpleName();
  }

  @Override
  public Tree getTree()
  {
    IJavaClassType concreteType = getConcreteType();
    if( concreteType instanceof AsmClassJavaClassInfo )
    {
      return ((AsmClassJavaClassInfo)concreteType).getTree();
    }
    return null;
  }

  @Override
  public IJavaClassInfo getEnclosingClass()
  {
    IJavaClassType concreteType = getConcreteType();
    if( concreteType instanceof IJavaClassInfo )
    {
      return ((IJavaClassInfo)concreteType).getEnclosingClass();
    }
    return null;
  }

  @Override
  public IJavaClassInfo getDeclaringClass()
  {
    IJavaClassType concreteType = getConcreteType();
    if( concreteType instanceof IJavaClassInfo )
    {
      return (IJavaClassInfo)concreteType;
    }
    return null;
  }
}
