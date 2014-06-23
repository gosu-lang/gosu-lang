/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.tree.java.IJavaElementType;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;

public class GosuMemberAssignmentStatementImpl extends GosuBaseStatementImpl<IMemberAssignmentStatement> implements IGosuStatement, PsiAssignmentExpression {
  public GosuMemberAssignmentStatementImpl( GosuCompositeElement node ) {
    super(node);
  }

  @NotNull
  @Override
  public PsiExpression getLExpression() {
    return findChildByClass( PsiExpression.class );
  }

  @Override
  public PsiExpression getRExpression() {
    for( PsiElement elem = findAssignAop(); elem != null; elem = elem.getNextSibling() ) {
      if( elem instanceof PsiExpression ) {
        return (PsiExpression)elem;
      }
    }
    return null;
  }

  @NotNull
  @Override
  public PsiJavaToken getOperationSign() {
    PsiJavaToken assignAop = (PsiJavaToken) findAssignAop();
    if(assignAop == null) {
      assignAop = new PsiJavaTokenImpl(new IJavaElementType("ERROR"), "ERROR");
    }
    return assignAop;
  }

  @NotNull
  @Override
  public IElementType getOperationTokenType() {
    return getOperationSign().getTokenType();
  }

  @Override
  public PsiType getType() {
    return getLExpression().getType();
  }

  private PsiElement findAssignAop() {
    ASTNode node = getNode().findChildByType(
      TokenSet.create(
        GosuElementTypes.TT_OP_assign,
        GosuElementTypes.TT_OP_assign_plus,
        GosuElementTypes.TT_OP_assign_minus,
        GosuElementTypes.TT_OP_assign_multiply,
        GosuElementTypes.TT_OP_assign_divide,
        GosuElementTypes.TT_OP_assign_modulo,
        GosuElementTypes.TT_OP_assign_and,
        GosuElementTypes.TT_OP_assign_logical_and,
        GosuElementTypes.TT_OP_assing_or,
        GosuElementTypes.TT_OP_assing_logical_or,
        GosuElementTypes.TT_OP_assign_xor,
        GosuElementTypes.TT_OP_assign_shift_left,
        GosuElementTypes.TT_OP_assign_shift_right,
        GosuElementTypes.TT_OP_assign_shift_right
      ) );
    return node == null ? null : node.getPsi();
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitMemberAssignmentStatement(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
