/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.util.ArrayFactory;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMember;
import gw.plugin.ij.lang.psi.stubs.GosuFieldStub;
import org.jetbrains.annotations.NotNull;

public interface IGosuField extends IGosuVariable, IGosuMember, PsiField, StubBasedPsiElement<GosuFieldStub> {
  IGosuField[] EMPTY_ARRAY = new IGosuField[0];

  ArrayFactory<IGosuField> ARRAY_FACTORY = new ArrayFactory<IGosuField>() {
    @NotNull
    public IGosuField[] create(final int count) {
      return count == 0 ? EMPTY_ARRAY : new IGosuField[count];
    }
  };

  String getPropertyName();

  PsiElement getPropertyElement();
}
