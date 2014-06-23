/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.refactor.signature;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.refactoring.changeSignature.MethodDescriptor;
import com.intellij.refactoring.changeSignature.ParameterInfoImpl;
import com.intellij.util.VisibilityUtil;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GosuMethodDescriptor implements MethodDescriptor<ParameterInfoImpl, String> {

  private final IGosuMethod myMethod;
  private final ChangeSignatureHandler changeSignatureHandler;

  public GosuMethodDescriptor(IGosuMethod method, ChangeSignatureHandler changeSignatureHandler) {
    myMethod = method;
    this.changeSignatureHandler = changeSignatureHandler;
  }

  @Override
  public String getName() {
    return myMethod.getName();
  }

  @Override
  public List<ParameterInfoImpl> getParameters() {
    final ArrayList<ParameterInfoImpl> result = new ArrayList<ParameterInfoImpl>();
    final IGosuParameter[] parameters = myMethod.getParameterList().getParameters();
    for (int i = 0; i < parameters.length; i++) {
      IGosuParameter parameter = parameters[i];
      ChangeSignatureHandler.ParamInfo paramInfo = new ChangeSignatureHandler.ParamInfo(parameter.getType(), parameter.getName());
      paramInfo = changeSignatureHandler.replaceParameter(paramInfo);
      ParameterInfoImpl info = new ParameterInfoImpl(i, paramInfo.paramName, paramInfo.type);
      info.setDefaultValue("");
      result.add(info);
    }
    return result;
  }

  @Override
  public String getVisibility() {
    return VisibilityUtil.getVisibilityModifier(myMethod.getModifierList());
  }

  @Override
  public PsiMethod getMethod() {
    return myMethod;
  }

  @Override
  public int getParametersCount() {
    return myMethod.getParameterList().getParametersCount();
  }

  @Nullable
  public String getReturnTypeText() {
    IGosuTypeElement typeElement = myMethod.getReturnTypeElementGosu();
    if (typeElement == null) {
      return null;
    }
    PsiType type = typeElement.getType();
    String qName = type.getCanonicalText();
    String replacedType = changeSignatureHandler.replaceReturnType(qName);
    if (!replacedType.equals(qName)) {
      return replacedType;
    } else {
      return typeElement.getText();
    }
  }

  @Override
  public boolean canChangeVisibility() {
    PsiClass containingClass = myMethod.getContainingClass();
    return containingClass != null && !containingClass.isInterface();
  }

  @Override
  public boolean canChangeParameters() {
    return true;
  }

  @Override
  public ReadWriteOption canChangeReturnType() {
    return myMethod.isConstructor() ? ReadWriteOption.None : ReadWriteOption.ReadWrite;
  }

  @Override
  public boolean canChangeName() {
    return !myMethod.isConstructor();
  }
}
