/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.impl.PsiSubstitutorImpl;
import gw.lang.reflect.IType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GosuPsiSubstitutor implements PsiSubstitutor {
  private final PsiClass owner;
  private final Map<String, IType> typeVarMap;
  private PsiSubstitutor actualSubstitutor;

  public GosuPsiSubstitutor(Map<String, IType> typeVarMap, PsiClass owner) {
    this.typeVarMap = typeVarMap;
    this.owner = owner;
  }

  public PsiSubstitutor getActualSubstitutor() {
    if (actualSubstitutor == null) {
      Map<PsiTypeParameter, PsiType> map = new HashMap<>();
      PsiTypeParameter[] typeParameters = owner.getTypeParameters();
      for (PsiTypeParameter typeParameter : typeParameters) {
        IType type = typeVarMap.get(typeParameter.getName());
        PsiType psiType = GosuBaseElementImpl.createType(type, typeParameter);
        map.put(typeParameter, psiType);
      }
      actualSubstitutor = PsiSubstitutorImpl.createSubstitutor(map);
    }
    return actualSubstitutor;
  }

  public Map<String, IType> getTypeVarMap() {
    return typeVarMap;
  }

  @Override
  public PsiType substitute(@NotNull PsiTypeParameter typeParameter) {
    return getActualSubstitutor().substitute(typeParameter);
  }

  @Override
  public PsiType substitute(@Nullable PsiType type) {
    return getActualSubstitutor().substitute(type);
  }

  @Override
  public PsiType substituteWithBoundsPromotion(PsiTypeParameter typeParameter) {
    return getActualSubstitutor().substituteWithBoundsPromotion(typeParameter);
  }

  @Override
  public PsiSubstitutor put(PsiTypeParameter classParameter, PsiType mapping) {
    return getActualSubstitutor().put(classParameter, mapping);
  }

  @Override
  public PsiSubstitutor putAll(PsiClass parentClass, PsiType[] mappings) {
    return getActualSubstitutor().putAll(parentClass, mappings);
  }

  @Override
  public PsiSubstitutor putAll(PsiSubstitutor another) {
    return getActualSubstitutor().putAll(another);
  }

  @NotNull
  @Override
  public Map<PsiTypeParameter, PsiType> getSubstitutionMap() {
    return getActualSubstitutor().getSubstitutionMap();
  }

  @Override
  public boolean isValid() {
    return getActualSubstitutor().isValid();
  }
}
