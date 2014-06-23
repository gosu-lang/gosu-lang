/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.custom;

import com.intellij.extapi.psi.PsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.HierarchicalMethodSignature;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodReceiver;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.PsiTypeParameterList;
import com.intellij.psi.impl.light.LightParameter;
import com.intellij.psi.impl.light.LightParameterListBuilder;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.util.IncorrectOperationException;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GosuXMLMethod extends PsiElementBase implements PsiMethod {
  @NotNull
  private final IMethodInfo mi;
  private final CustomGosuClass psiClass;

  public GosuXMLMethod(@NotNull IMethodInfo mi, CustomGosuClass psiClass) {
    this.mi = mi;
    this.psiClass = psiClass;
    putUserData(GosuModuleUtil.KEY_GOSU_MODULE, mi.getOwnersType().getTypeLoader().getModule());
  }


  @NotNull
  @Override
  public String getName() {
    return mi.getDisplayName();
  }

  @Override
  public PsiType getReturnType() {
    return GosuBaseElementImpl.createType(mi.getReturnType(), psiClass);
  }

  @Override
  public PsiTypeElement getReturnTypeElement() {
    return null;
  }

  @NotNull
  @Override
  public PsiParameterList getParameterList() {
    LightParameterListBuilder builder = new LightParameterListBuilder(psiClass.getManager(), getLanguage());
    for (IParameterInfo parameter : mi.getParameters()) {
      PsiType type = GosuBaseElementImpl.createType(parameter.getFeatureType(), this);
      builder.addParameter(new LightParameter(parameter.getName(), type, this, getLanguage()));
    }
    return builder;
  }

  @NotNull
  @Override
  public PsiReferenceList getThrowsList() {
    return null;
  }

  @Override
  public PsiCodeBlock getBody() {
    return null;
  }

  @Override
  public boolean isConstructor() {
    return false;
  }

  @Override
  public boolean isVarArgs() {
    return false;
  }

  @NotNull
  @Override
  public MethodSignature getSignature(@NotNull PsiSubstitutor substitutor) {
    return null;
  }

  @Override
  public PsiIdentifier getNameIdentifier() {
    return new CustomGosuIdentifier(this);
  }

  @NotNull
  @Override
  public PsiMethod[] findSuperMethods() {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiMethod[] findSuperMethods(boolean checkAccess) {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiMethod[] findSuperMethods(PsiClass parentClass) {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public List<MethodSignatureBackedByPsiMethod> findSuperMethodSignaturesIncludingStatic(boolean checkAccess) {
    return null;
  }

  @Override
  public PsiMethod findDeepestSuperMethod() {
    return null;
  }

  @NotNull
  @Override
  public PsiMethod[] findDeepestSuperMethods() {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiModifierList getModifierList() {
    return null;
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    return false;
  }

  @Nullable
  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return null;
  }

  @NotNull
  @Override
  public HierarchicalMethodSignature getHierarchicalMethodSignature() {
    return null;
  }

  @Override
  public PsiType getReturnTypeNoResolve() {
    return null;
  }

  @Override
  public PsiDocComment getDocComment() {
    return null;
  }

  @Override
  public boolean isDeprecated() {
    return false;
  }

  @Override
  public boolean hasTypeParameters() {
    return false;
  }

  @Override
  public PsiTypeParameterList getTypeParameterList() {
    return null;
  }

  @NotNull
  @Override
  public PsiTypeParameter[] getTypeParameters() {
    return PsiTypeParameter.EMPTY_ARRAY;
  }

  @Override
  public PsiClass getContainingClass() {
    return psiClass;
  }

  @NotNull
  @Override
  public Language getLanguage() {
    return GosuLanguage.instance();
  }

  @NotNull
  @Override
  public PsiElement[] getChildren() {
    return PsiElement.EMPTY_ARRAY;
  }

  @Override
  public PsiElement getParent() {
    return psiClass;
  }

  @Override
  public PsiElement getFirstChild() {
    return null;
  }

  @Override
  public PsiElement getLastChild() {
    return null;
  }

  @Override
  public PsiElement getNextSibling() {
    return null;
  }

  @Override
  public PsiElement getPrevSibling() {
    return null;
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    return new TextRange(0, 1);
  }

  @Override
  public int getStartOffsetInParent() {
    return 0;
  }

  @Override
  public int getTextLength() {
    return 0;
  }

  @Override
  public PsiElement findElementAt(int offset) {
    return null;
  }

  @Override
  public int getTextOffset() {
    return 0;
  }

  @NotNull
  @Override
  public String getText() {
    return CustomGosuClass.getSignature(mi);
  }

  @NotNull
  @Override
  public char[] textToCharArray() {
    return new char[0];
  }

  @Override
  public boolean textContains(char c) {
    return false;
  }

  @Override
  public ASTNode getNode() {
    return psiClass.getContainingFile().getNode();
  }

  @Override
  public boolean isWritable() {
    return false;
  }

  @Nullable
  @Override
  public PsiFile getContainingFile() {
    return null;
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
    return psiClass.getNavigationElement();
  }
}
