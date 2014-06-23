/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiType;
import gw.lang.parser.IHasType;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.ITypeAsExpression;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GosuTypeAsExpressionImpl extends GosuPsiElementImpl<ITypeAsExpression> implements IGosuExpression {
  public GosuTypeAsExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public PsiType getType() {
    IParsedElement pe = getParsedElementImpl();
    return createType(pe instanceof IHasType ? ((IHasType)pe).getType() : JavaTypes.pVOID());
  }

  @Nullable
  public IGosuPsiElement getLhs() {
    ITypeAsExpression pe = getParsedElement();
    if (pe == null || pe.getLHS() != null) {
      return (IGosuPsiElement) getChildren()[0];
    }
    return null;
  }

  @Nullable
  public GosuTypeLiteralImpl getRhs() {
    List<PsiElement> children = findChildrenByType(GosuElementTypes.ELEM_TYPE_TypeLiteral);
    if (children.size() > 0) {
      return (GosuTypeLiteralImpl)
          (children.get(0) != getLhs()
              ? children.get(0)
              : children.size() > 1
              ? children.get(1)
              : null);
    }
    return null;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitTypeAsExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
