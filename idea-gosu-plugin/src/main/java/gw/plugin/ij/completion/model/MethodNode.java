/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.JavaTypes;
import org.jetbrains.annotations.NotNull;

public class MethodNode extends BeanInfoNode {
  private final IMethodInfo methodInfo;

  public MethodNode(IMethodInfo methodInfo) {
    super(TypeSystem.getOrCreateFunctionType(methodInfo));

    this.methodInfo = methodInfo;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return methodInfo.getDisplayName() + getParameterDisplay() + " : " + getTypeName(getReturnType()) + " - " + getOwnersTypeName();
  }

  private String getOwnersTypeName() {
    IType ownersType = methodInfo.getOwnersType();
    if (ownersType instanceof IMetaType) {
      ownersType = ((IMetaType) ownersType).getType();
    }
    if (ownersType instanceof IGosuClass && ((IGosuClass) ownersType).isAnonymous()) {
      IType supertype = ownersType.getSupertype();
      if (supertype == null) {
        supertype = ownersType.getInterfaces()[0];
      }
      return ownersType.getEnclosingType().getName() + ".new " + supertype.getRelativeName() + "(){...}";
    } else {
      return ownersType.getDisplayName();
    }
  }

  public IType getReturnType() {
    return ((IFunctionType)getType()).getReturnType();
  }

  public String getReturnTypeName() {
    return getTypeName(getReturnType());
  }

  @NotNull
  @Override
  public String getName() {
    return methodInfo.getDisplayName() + getParameterDisplay();
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return methodInfo;
  }

  @Override
  protected int getTypePriority() {
    return methodInfo.isStatic() ? 4 : 1;
  }

  public IMethodInfo getMethodDescriptor() {
    return methodInfo;
  }

  public String getParameterDisplay() {
    IParameterInfo[] pd = (methodInfo instanceof IJavaMethodInfo) ? ((IJavaMethodInfo) methodInfo).getGenericParameters() : methodInfo.getParameters();
    if (pd == null || pd.length == 0) {
      return TypeInfoUtil.getTypeVarList(methodInfo, true) + "()";
    }

    final StringBuilder sb = new StringBuilder();
    sb.append(TypeInfoUtil.getTypeVarList(methodInfo, true)).append("(");
    for (int i = 0; i < pd.length; i++) {
      String strName = pd[i].getName();
      sb.append(i == 0 ? "" : ", ").append(strName);
      boolean bBlock = pd[i].getFeatureType() instanceof IBlockType;
      String strType = bBlock ? ((IBlockType) pd[i].getFeatureType()).getRelativeNameSansBlock() : pd[i].getFeatureType().getRelativeName();
      if (!strName.equalsIgnoreCase(strType)) {
        if (bBlock) {
          sb.append(strType);
        } else {
          sb.append(": ").append(strType);
        }
      }
    }
    sb.append(")");

    return sb.toString();
  }
}
