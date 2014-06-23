/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements;

import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.IGosuNamedElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import org.jetbrains.annotations.Nullable;

public interface IGosuVariable extends PsiVariable, IGosuNamedElement, PsiStatement {
  IGosuVariable[] EMPTY_ARRAY = new IGosuVariable[0];

  @Nullable
  IGosuExpression getInitializerGosu();

  void setType(PsiType type) throws IncorrectOperationException;

  @Nullable
  IGosuTypeElement getTypeElementGosu();
}
