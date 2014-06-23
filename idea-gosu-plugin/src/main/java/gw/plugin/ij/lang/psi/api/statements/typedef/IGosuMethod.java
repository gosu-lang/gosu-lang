/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.util.ArrayFactory;
import gw.plugin.ij.lang.psi.IGosuNamedElement;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.statements.IGosuParametersOwner;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameterList;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterListOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGosuMethod extends IGosuMembersDeclaration, PsiMethod, IGosuNamedElement, IGosuMember, IGosuParametersOwner, IGosuTypeParameterListOwner
{
  Key<Boolean> BUILDER_METHOD = Key.create("BUILDER_METHOD");

  IGosuMethod[] EMPTY_ARRAY = new IGosuMethod[0];

  ArrayFactory<IGosuMethod> ARRAY_FACTORY = new ArrayFactory<IGosuMethod>() {
    @NotNull
    public IGosuMethod[] create(int count) {
      return count == 0 ? EMPTY_ARRAY : new IGosuMethod[count];
    }
  };

  IGosuTypeElement getReturnTypeElementGosu();

  /**
   * @return the static return type, which will appear in the compiled Gosu class
   */
  @Nullable
  PsiType getReturnType();

  @Nullable
  IGosuTypeElement setReturnType(PsiType newReturnType);

  @NotNull
  String getName();

  @NotNull
  IGosuParameterList getParameterList();

  @NotNull
  IGosuModifierList getModifierList();

  String[] getNamedParametersArray();

  boolean isForProperty();

  boolean isForPropertySetter();

  boolean isForPropertyGetter();
}
