/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import gw.lang.parser.statements.IStatementList;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatementList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import org.jetbrains.annotations.NotNull;

public class GosuStatementListImpl extends AbstractStatementWithLocalDeclarationsImpl<IStatementList> implements IGosuStatementList {
  public GosuStatementListImpl(GosuCompositeElement node) {
    super(node);
  }

  @NotNull
  @Override
  public PsiStatement[] getStatements() {
    return findChildrenByClass(IGosuStatement.class);
  }

  @Override
  public PsiElement getFirstBodyElement() {
    final PsiStatement[] stmts = getStatements();
    return stmts.length > 0 ? stmts[0] : null;
  }

  @Override
  public PsiElement getLastBodyElement() {
    final PsiStatement[] stmts = getStatements();
    return stmts.length > 0 ? stmts[stmts.length - 1] : null;
  }

  @Override
  public PsiJavaToken getLBrace() {
    return (PsiJavaToken) findChildByType(GosuTokenTypes.TT_OP_brace_left);
  }

  @Override
  public PsiJavaToken getRBrace() {
    return (PsiJavaToken) findChildByType(GosuTokenTypes.TT_OP_brace_right);
  }

  @Override
  public TreeElement addInternal(ASTNode first, ASTNode last, ASTNode anchor, Boolean before) {
    if (anchor == null) {
      if (before == null || before.booleanValue()) {
        anchor = (ASTNode) getRBrace();
        before = Boolean.TRUE;
      } else {
        anchor = (ASTNode) getLBrace();
        before = Boolean.FALSE;
      }
    }

    if (before == Boolean.TRUE) {
      while (isNonGosuStatement(anchor)) {
        anchor = anchor.getTreePrev();
        before = Boolean.FALSE;
      }
    } else if (before == Boolean.FALSE) {
      while (isNonGosuStatement(anchor)) {
        anchor = anchor.getTreeNext();
        before = Boolean.TRUE;
      }
    }

    ASTNode anchorBefore;
    if (anchor != null) {
      anchorBefore = before.booleanValue() ? anchor : anchor.getTreeNext();
    } else {
      anchorBefore = before == null || before.booleanValue() ? null : getNode().getFirstChildNode();
    }

    return (TreeElement) CodeEditUtil.addChildren(getNode(), first, last, anchorBefore);
  }

  private static boolean isNonGosuStatement(ASTNode anchor) {
    final PsiElement psi = anchor.getPsi();
    return GosuRefactoringUtil.isStatement(psi) && psi.getLanguage() != GosuLanguage.instance();
  }

  @Override
  public boolean shouldChangeModificationCount(PsiElement psiElement) {
    PsiElement pparent = getParent();
    return !(pparent instanceof PsiMethod || pparent instanceof PsiClassInitializer);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitStatementList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
