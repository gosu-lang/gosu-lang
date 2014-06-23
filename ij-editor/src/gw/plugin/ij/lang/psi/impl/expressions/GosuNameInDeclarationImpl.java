/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.expressions.INameInDeclaration;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuNameInDeclarationImpl extends GosuPsiElementImpl<INameInDeclaration> {
  public GosuNameInDeclarationImpl(GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitNameInDeclaration(this);
  }

  public String getName() {
    final PsiElement child = getFirstChild();
    return child != null ? child.getText() : "";
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitNameInDeclaration(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
