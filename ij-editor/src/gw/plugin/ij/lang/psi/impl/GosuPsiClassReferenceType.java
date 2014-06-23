/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.impl.light.LightClassReference;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiUtil;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import org.jetbrains.annotations.NotNull;

public class GosuPsiClassReferenceType extends PsiClassReferenceType {

  public GosuPsiClassReferenceType(@org.jetbrains.annotations.NotNull LightClassReference reference, LanguageLevel languageLevel) {
    super(reference, languageLevel);
  }

  public GosuPsiClassReferenceType(@NotNull PsiJavaCodeReferenceElement reference, LanguageLevel languageLevel, PsiAnnotation[] annotations) {
    super(reference, languageLevel, annotations);
  }

  @Override
  @NotNull
  public PsiClassType rawType() {
    PsiElement resolved = getReference().resolve();
    if (resolved instanceof PsiClass) {
      PsiClass aClass = (PsiClass) resolved;
      if (!PsiUtil.typeParametersIterable(aClass).iterator().hasNext()) return this;
      PsiManager manager = getReference().getManager();
      final PsiElementFactory factory = JavaPsiFacade.getInstance(manager.getProject()).getElementFactory();
      final PsiSubstitutor rawSubstitutor = factory.createRawSubstitutor(aClass);
      return factory.createType(aClass, rawSubstitutor, getLanguageLevel(), getAnnotations());
    }
    String qualifiedName = getReference().getQualifiedName();
    String name = getReference().getReferenceName();
    if (name == null) name = "";
    LightClassReference reference = new LightClassReference(getReference().getManager(), name, qualifiedName, getReference().getResolveScope());
    return new GosuPsiClassReferenceType(reference, null, getAnnotations());
  }

  @Override
  public boolean equals(Object obj) {
    return isEntityType(obj) ? true : super.equals(obj);
  }

  // this crazy case has to do with treating as equal "entity.Check" with
  // "com.guidewire.cc.domain.financials.check.Check", for example
  private boolean isEntityType(Object obj) {
    String thisQualifiedName = getReference().getQualifiedName();
    if (obj instanceof PsiClassReferenceType && thisQualifiedName.startsWith("entity.")) {
      PsiElement resolve = ((PsiClassReferenceType) obj).getReference().resolve();
      if (resolve instanceof PsiClass) {
        IType type = TypeSystem.getByFullNameIfValid(((PsiClass) resolve).getQualifiedName(), TypeSystem.getGlobalModule());
        if (type != null && type.getName().equals(thisQualifiedName)) {
          return true;
        }
      }
    }
    return false;
  }

}
