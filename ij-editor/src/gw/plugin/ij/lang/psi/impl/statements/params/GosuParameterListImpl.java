/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.params;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.IParameterListClause;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GosuParameterListImpl extends GosuPsiElementImpl<IParameterListClause> implements IGosuParameterList {
  private static final Logger LOG = Logger.getInstance("#gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterListImpl");

  public GosuParameterListImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitParameterList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitParameterList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
  @NotNull
  public IGosuParameter[] getParameters() {
    return findChildrenByClass(IGosuParameter.class);
  }

  public int getParameterIndex(@NotNull PsiParameter parameter) {
    LOG.assertTrue(parameter.getParent() == this);
    return PsiImplUtil.getParameterIndex(parameter, this);
  }

  public int getParametersCount() {
    return getParameters().length;
  }

  public void addParameterToEnd(@NotNull IGosuParameter parameter) {
    IGosuParameter[] params = getParameters();
    if (params.length == 0) {
      add(parameter);
    } else {
      IGosuParameter last = params[params.length - 1];
      addAfter(parameter, last);
    }
  }

  public void addParameterToHead(@NotNull IGosuParameter parameter) {
    IGosuParameter[] params = getParameters();
    if (params.length == 0) {
      add(parameter);
    } else {
      IGosuParameter first = params[0];
      addBefore(parameter, first);
    }
  }

  public int getParameterNumber(final IGosuParameter parameter) {
    for (int i = 0; i < getParameters().length; i++) {
      IGosuParameter param = getParameters()[i];
      if (param == parameter) {
        return i;
      }
    }
    return -1;
  }

  @NotNull
  @Override
  public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    IGosuParameter[] params = getParameters();

    if (params.length == 0) {
      add(element);
    } else {
      element = super.addAfter(element, anchor);
      final ASTNode astNode = getNode();
      if (anchor != null) {
        astNode.addLeaf(GosuTokenTypes.TT_OP_comma, ",", element.getNode());
      } else {
        astNode.addLeaf(GosuTokenTypes.TT_OP_comma, ",", element.getNextSibling().getNode());
      }
      CodeStyleManager.getInstance(getManager().getProject()).reformat(this);
    }

    return element;
  }

  @NotNull
  @Override
  public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    IGosuParameter[] params = getParameters();

    if (params.length == 0) {
      add(element);
    } else {
      element = super.addBefore(element, anchor);
      final ASTNode astNode = getNode();
      if (anchor != null) {
        astNode.addLeaf(GosuTokenTypes.TT_OP_comma, ",", anchor.getNode());
      } else {
        astNode.addLeaf(GosuTokenTypes.TT_OP_comma, ",", element.getNode());
      }
      CodeStyleManager.getInstance(getManager().getProject()).reformat(this);
    }

    return element;
  }

  @Override
  public PsiModifierList getModifierList() {
    return null;
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    return false;
  }
}
