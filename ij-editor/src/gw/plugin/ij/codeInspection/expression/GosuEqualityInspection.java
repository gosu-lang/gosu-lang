/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInspection.expression;


import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.BaseLocalInspectionTool;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IEqualityExpression;
import gw.plugin.ij.intentions.EqualityQuickFix;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class GosuEqualityInspection extends BaseLocalInspectionTool {

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return GosuBundle.message( "inspection.group.name.equality.issues" );
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GosuBundle.message( "inspection.equality.obsolete.operator" );
  }

  @Override
  public boolean isEnabledByDefault() {
    // Must turn this on explicitly, too much of an impact on performance for regular use
    return false;
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuEqualityInspection";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor( @NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session ) {
    return new GosuElementVisitor() {
      @Override
      public void visitElement( PsiElement elem ) {
        if( elem instanceof IGosuExpression ) {
          IGosuExpression expr = (IGosuExpression)elem;
          IParsedElement parsedElement = expr.getParsedElement();
          if( parsedElement instanceof IExpression ) {
            IExpression pe = (IExpression) parsedElement;
            if( pe instanceof IEqualityExpression ) {
              LeafPsiElement notEqualsOp = (LeafPsiElement)expr.getNode().findChildByType( GosuElementTypes.TT_OP_not_equals_for_losers );
              if( notEqualsOp != null ) {
                holder.registerProblem( notEqualsOp, GosuBundle.message( "inspection.equality.obsolete.operator" ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new EqualityFix( notEqualsOp ) );
              }
            }
          }
        }
      }
    };
  }

  private class EqualityFix implements LocalQuickFix {
    private final EqualityQuickFix _quickFix;

    public EqualityFix( PsiElement id ) {
      _quickFix = new EqualityQuickFix( id );
    }

    @NotNull
    public String getName() {
      return _quickFix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return GosuBundle.message( "inspection.group.name.equality.issues" );
    }

    public void applyFix( @NotNull Project project, @NotNull ProblemDescriptor descriptor ) {
      PsiElement element = descriptor.getPsiElement();
      if( element == null ) {
        return;
      }
      final PsiFile psiFile = element.getContainingFile();
      if( _quickFix.isAvailable( project, null, psiFile ) ) {
        _quickFix.invoke( project, null, psiFile );
      }
    }
  }

}
