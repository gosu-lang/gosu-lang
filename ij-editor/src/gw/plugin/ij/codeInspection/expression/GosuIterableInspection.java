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
import gw.internal.gosu.parser.BeanAccess;
import gw.lang.parser.IExpression;
import gw.lang.parser.resources.Res;
import gw.lang.parser.statements.IForEachStatement;
import gw.plugin.ij.intentions.HandleExpectingIterableFix;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.statements.GosuForEachStatementImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class GosuIterableInspection extends BaseLocalInspectionTool {

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return GosuBundle.message("inspection.group.name.upgrade.issues");
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GosuBundle.message( "inspection.expecting.iterable" );
  }

  @Override
  public boolean isEnabledByDefault() {
    // Must turn this on explicitly, it's useful just for upgrades
    return false;
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuIterableInspection";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor( @NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session ) {
    return new GosuElementVisitor() {

      @Override
      public void visitForEachStatement(GosuForEachStatementImpl forEachStatement) {
        final IForEachStatement pe = forEachStatement.getParsedElement();
        if (pe != null && pe.hasParseException(Res.MSG_EXPECTING_ARRAYTYPE_FOREACH)) {
          final IExpression inExpression = pe.getInExpression();

          if (inExpression != null && BeanAccess.isNumericType(inExpression.getType())) {
            PsiElement elem = null;
            for(PsiElement x : forEachStatement.getChildren()) {
              if(x instanceof IGosuExpression) {
                elem = x;
                break;
              }
            }
            if(elem != null) {
              holder.registerProblem( elem, GosuBundle.message( "inspection.expecting.iterable" ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new IterableFix( elem ) );
            }
          }
        }
      }
    };
  }

  private class IterableFix implements LocalQuickFix {
    private final HandleExpectingIterableFix _quickFix;

    public IterableFix( PsiElement id ) {
      _quickFix = new HandleExpectingIterableFix( id );
    }

    @NotNull
    public String getName() {
      return _quickFix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return GosuBundle.message( "inspection.group.name.upgrade.issues" );
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
