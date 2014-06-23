/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.search.GlobalSearchScope;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GosuClassReferenceType extends PsiClassType {
  private final IGosuReferenceExpression myReferenceElement;

  public GosuClassReferenceType(IGosuReferenceExpression referenceElement) {
    super(LanguageLevel.JDK_1_5);
    myReferenceElement = referenceElement;
  }

  public GosuClassReferenceType(IGosuReferenceExpression referenceElement, LanguageLevel languageLevel) {
    super(languageLevel);
    myReferenceElement = referenceElement;
  }

  @Nullable
  public PsiClass resolve() {
    PsiElement psiElement = myReferenceElement.resolve();
    return psiElement instanceof PsiClass ? (PsiClass) psiElement : null;
    //## todo: use this
//    ResolveResult[] results = multiResolve();
//    if( results.length == 1 )
//    {
//      PsiElement only = results[0].getElement();
//      return only instanceof PsiClass ? (PsiClass)only : null;
//    }
//
//    return null;
  }

  //reference resolve is cached
  @NotNull
  private IGosuResolveResult[] multiResolve() {
    return (IGosuResolveResult[]) myReferenceElement.multiResolve(false);
  }

  @Nullable
  public String getClassName() {
    return myReferenceElement.getReferenceName();
  }

  @NotNull
  public PsiType[] getParameters() {
    return myReferenceElement.getTypeArguments();
  }

  @NotNull
  public ClassResolveResult resolveGenerics() {
    return new ClassResolveResult() {
      public PsiClass getElement() {
        return resolve();
      }

      public PsiSubstitutor getSubstitutor() {
        final IGosuResolveResult[] results = multiResolve();
        if (results.length != 1) {
          return PsiSubstitutor.UNKNOWN;
        }
        return results[0].getSubstitutor().putAll(
            (PsiClass) results[0].getElement(), GosuClassReferenceType.this.getParameters());
      }

      public boolean isPackagePrefixPackageReference() {
        return false;
      }

      public boolean isAccessible() {
        final IGosuResolveResult[] results = multiResolve();
        for (IGosuResolveResult result : results) {
          if (result.isAccessible()) {
            return true;
          }
        }
        return false;
      }

      public boolean isStaticsScopeCorrect() {
        return true; //TODO
      }

      @Nullable
      public PsiElement getCurrentFileResolveScope() {
        return null; //TODO???
      }

      public boolean isValidResult() {
        return isStaticsScopeCorrect() && isAccessible();
      }
    };
  }

  @NotNull
  public PsiClassType rawType() {
    final PsiClass clazz = resolve();
    if (clazz != null) {
      final PsiElementFactory factory = JavaPsiFacadeUtil.getElementFactory(clazz.getProject());
      return factory.createType(clazz, factory.createRawSubstitutor(clazz));
    }

    return this;
  }

  public String getPresentableText() {
    if( Arrays.asList( myReferenceElement.getTypeArguments() ).contains( null ) ) {
      return myReferenceElement.getReferenceName();
    }
    return PsiNameHelper.getPresentableText(myReferenceElement.getReferenceName(), myReferenceElement.getTypeArguments());
  }

  @Nullable
  public String getCanonicalText() {
    PsiClass resolved = resolve();
    if (resolved == null) {
      return null;
    }
    if (resolved instanceof PsiTypeParameter) {
      return resolved.getName();
    }
    final String qName = resolved.getQualifiedName();
    if (isRaw()) {
      return qName;
    }

    final PsiType[] typeArgs = myReferenceElement.getTypeArguments();
    if (typeArgs.length == 0) {
      return qName;
    }

    StringBuilder builder = new StringBuilder();
    builder.append(qName).append("<");
    for (int i = 0; i < typeArgs.length; i++) {
      if (i > 0) {
        builder.append(", ");
      }
      final String typeArgCanonical = typeArgs[i].getCanonicalText();
      if (typeArgCanonical != null) {
        builder.append(typeArgCanonical);
      } else {
        return null;
      }
    }
    builder.append(">");
    return builder.toString();
  }

  @Nullable
  public String getInternalCanonicalText() {
    return getCanonicalText();
  }

  public boolean isValid() {
    return myReferenceElement.isValid();
  }

  public boolean equalsToText(@NotNull @NonNls String text) {
    return text.endsWith(getPresentableText()) && //optimization
        text.equals(getCanonicalText());
  }

  @NotNull
  public GlobalSearchScope getResolveScope() {
    return myReferenceElement.getResolveScope();
  }

  @NotNull
  public LanguageLevel getLanguageLevel() {
    return myLanguageLevel;
  }

  @NotNull
  public PsiClassType setLanguageLevel(final LanguageLevel languageLevel) {
    return new GosuClassReferenceType(myReferenceElement, languageLevel);
  }
}
