/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.*;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuRawExpression;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuStringLiteralImpl extends GosuRawExpression implements PsiLiteralExpression, ContributedReferenceHost {
  public GosuStringLiteralImpl(GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitStringLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitStringLiteral(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

  @Override
  public PsiReference getReference() {
    PsiReference[] refs = getReferences();
    return refs.length == 0 ? null : refs[0];
  }

  @NotNull
  @Override
  public PsiReference[] getReferences() {
    return PsiReferenceService.getService().getContributedReferences(this);
  }

  @Nullable
  @Override
  public String getValue() {
    String text = getText();
    return text.length() < 2 ? text : text.substring(1, text.length() - 1);
  }
}
