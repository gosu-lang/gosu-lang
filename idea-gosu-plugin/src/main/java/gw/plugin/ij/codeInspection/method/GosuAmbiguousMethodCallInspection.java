/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInspection.method;

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
import com.intellij.psi.PsiType;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ICoercionIssue;
import gw.lang.parser.expressions.IArgumentListClause;
import gw.lang.parser.resources.Res;
import gw.plugin.ij.intentions.GosuAddTypeCastFix;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.expressions.GosuExpressionListImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GosuAmbiguousMethodCallInspection extends BaseLocalInspectionTool {

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
    return GosuBundle.message( "inspection.ambiguous.call" );
  }

  @Override
  public boolean isEnabledByDefault() {
    // Must turn this on explicitly, it's useful just for upgrades
    return false;
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuAmbiguousMethodCallInspection";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor( @NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session ) {
    return new GosuElementVisitor() {

      @Override
      public void visitExpressionList(GosuExpressionListImpl expressionList) {
        IParsedElement pe = expressionList.getParsedElement();
        if (pe instanceof IArgumentListClause && pe.getParent().hasParseException(Res.MSG_AMBIGUOUS_METHOD_INVOCATION)) {
          final List<IParseIssue> issues = pe.getParseExceptions();
          final PsiElement[] children = expressionList.getChildren();

          if (issues == null) {
            return;
          }
          for (IParseIssue issue : issues) {
            if (issue instanceof ICoercionIssue) {
              PsiElement psiElem = findArg(issue, children);
              if (psiElem instanceof IGosuPsiElement) {
                PsiType type = GosuBaseElementImpl.createType(((ICoercionIssue) issue).getTypeToCoerceTo(), psiElem);
                if (type != null) {
                  holder.registerProblem(psiElem, GosuBundle.message("inspection.ambiguous.call"), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new AmbiguousMethodCallFix(type, (IGosuPsiElement) psiElem));
                }
              }
            }
          }
        }
      }

      private PsiElement findArg(IParseIssue issue, PsiElement[] children) {
        IParsedElement pe = issue.getSource();
        int s = pe.getLocation().getOffset();
        int e = pe.getLocation().getExtent() + 1;

        for(PsiElement c : children) {
          if(c.getTextRange().equalsToRange(s, e)) {
            return c;
          }
        }
        return null;
      }
    };
  }

  private class AmbiguousMethodCallFix implements LocalQuickFix {
    private final GosuAddTypeCastFix _quickFix;

    public AmbiguousMethodCallFix(PsiType type, IGosuPsiElement expression) {
      _quickFix = new GosuAddTypeCastFix(type, expression);
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
        _quickFix.invokeImpl(project, null, psiFile);
      }
    }
  }

}
