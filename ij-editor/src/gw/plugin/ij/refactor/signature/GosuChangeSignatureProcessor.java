/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.refactoring.changeSignature.ParameterInfoImpl;
import com.intellij.refactoring.changeSignature.ThrownExceptionInfo;
import com.intellij.refactoring.util.CanonicalTypes;
import com.intellij.usageView.UsageInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GosuChangeSignatureProcessor extends ChangeSignatureProcessor {

  private ChangeSignatureHandler changeSignatureHandler;

  public GosuChangeSignatureProcessor(Project project,
                                  PsiMethod method,
                                  boolean generateDelegate,
                                  @PsiModifier.ModifierConstant String newVisibility,
                                  String newName,
                                  CanonicalTypes.Type newType,
                                  @NotNull ParameterInfoImpl[] parameterInfo,
                                  ThrownExceptionInfo[] thrownExceptions,
                                  Set<PsiMethod> propagateParametersMethods,
                                  Set<PsiMethod> propagateExceptionsMethods,
                                  ChangeSignatureHandler changeSignatureHandler) {

    super(project, new GosuChangeInfoImpl(newVisibility, method, newName, newType, parameterInfo,thrownExceptions, generateDelegate,
            propagateParametersMethods, propagateExceptionsMethods));
    this.changeSignatureHandler = changeSignatureHandler;
  }

  @Override
  protected void performRefactoring(UsageInfo[] usages) {
    super.performRefactoring(usages);
    changeSignatureHandler.afterRefactoring();
  }
}
