/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiType;
import com.intellij.psi.stubs.StubElement;
import gw.lang.parser.IExpression;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GosuRawExpression extends GosuBaseElementImpl<IExpression, StubElement> implements IGosuExpression, PsiModifierListOwner {
  public GosuRawExpression(GosuCompositeElement node) {
    super(node);
  }

  public void accept( GosuElementVisitor visitor ) {
    visitor.visitExpression( this );
  }

  // PsiModifierListOwner
  @Override
  public PsiModifierList getModifierList() {
    return findChildByClass(PsiModifierList.class);
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    final PsiModifierListOwner child = findChildByClass(PsiModifierListOwner.class);
    return child != null && child.hasModifierProperty(name);
  }

  // PsiExpression
  @Override
  public PsiType getType() {
    final IExpression expression = getParsedElement();
    return expression != null ? createType(expression.getType()) : null;
  }

  @NotNull
  public String toString() {
    return "[raw] " + super.toString();
  }
}
