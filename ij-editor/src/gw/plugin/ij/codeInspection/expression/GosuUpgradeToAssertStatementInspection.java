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
import gw.plugin.ij.intentions.AssertKeywordQuickFix;
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;


public class GosuUpgradeToAssertStatementInspection extends BaseLocalInspectionTool {

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
    return GosuBundle.message( "inspection.assert.is.reserved" );
  }

  @Override
  public boolean isEnabledByDefault() {
    // Must turn this on explicitly, it's useful just for upgrades
    return false;
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuUpgradeToAssertStatementInspection";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor( @NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session ) {
    return new GosuElementVisitor() {
      @Override
      public void visitElement( PsiElement elem ) {
        if( elem instanceof GosuTokenImpl && elem.getText().equals("assert") ) {
          holder.registerProblem( elem, GosuBundle.message( "inspection.assert.is.reserved" ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new AssertKeywordFix( elem ) );
        }
      }
    };
  }

  private class AssertKeywordFix implements LocalQuickFix {
    private final AssertKeywordQuickFix _quickFix;

    public AssertKeywordFix( PsiElement id ) {
      _quickFix = new AssertKeywordQuickFix( id );
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
