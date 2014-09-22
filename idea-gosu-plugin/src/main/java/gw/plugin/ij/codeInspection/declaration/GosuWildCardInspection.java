package gw.plugin.ij.codeInspection.declaration;

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
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeParameterListClause;
import gw.lang.parser.resources.Res;
import gw.plugin.ij.intentions.GosuRemoveWildcardFix;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;


public class GosuWildCardInspection extends BaseLocalInspectionTool {

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
    return GosuBundle.message("inspection.wildcard");
  }

  @Override
  public boolean isEnabledByDefault() {
    // Must turn this on explicitly, it's useful just for upgrades
    return false;
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuWildCardInspection";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor( @NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session ) {
    return new GosuElementVisitor() {

      @Override
      public void visitTypeParameterList( IGosuTypeParameterList typeParameterList) {
        IParsedElement pe = typeParameterList.getParsedElement();
         if (pe instanceof ITypeParameterListClause && pe.hasParseException(Res.MSG_NO_WILDCARDS)) {
          PsiElement[] children = typeParameterList.getChildren();
          ITypeLiteralExpression[] literalExpressions = ((ITypeParameterListClause) pe).getTypes();
          for(int i = 0; i < literalExpressions.length; i++) {
            if(literalExpressions[i].hasImmediateParseIssue(Res.MSG_NO_WILDCARDS) &&
               literalExpressions[i].getParseExceptions().size() != 0 &&
               literalExpressions[i] instanceof ITypeLiteralExpression &&
               children[i] instanceof GosuTypeLiteralImpl &&
               children[i].getTextLength() != 0
              )
            {
              String typeParam = (String) literalExpressions[i].getParseExceptions().get(0).getMessageArgs()[0];
              holder.registerProblem(children[i], GosuBundle.message("inspection.wildcard"), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new WildcardFix((GosuTypeLiteralImpl) children[i], typeParam));
            }
          }
        }
      }
    };
  }

  private class WildcardFix implements LocalQuickFix {
    private final GosuRemoveWildcardFix _quickFix;

    public WildcardFix(GosuTypeLiteralImpl typeLiteral, String typeParam) {
      _quickFix = new GosuRemoveWildcardFix(typeLiteral, typeParam);
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
        _quickFix.invoke(project, null, psiFile);
      }
    }
  }

}
