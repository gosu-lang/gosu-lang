/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInspection.statement;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.BaseLocalInspectionTool;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.NumericLiteral;
import gw.internal.gosu.parser.expressions.RelationalExpression;
import gw.internal.gosu.parser.statements.WhileStatement;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.exceptions.IWarningSuppressor;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.intentions.WhileToForFix;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuWhileStatementImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class GosuWhileToForInspection extends BaseLocalInspectionTool implements IWarningSuppressor {

  public static final String SUPPRESS_WARNING_CODE = "WhileToFor";

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return GosuBundle.message("inspection.group.name.statement.issues");
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GosuBundle.message("inspection.while.to.for");
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuWhileToForInspection";
  }


  @Override
  public boolean isEnabledByDefault() {
    return true;
  }


  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
    return new GosuElementVisitor() {

        @Override
        public void visitWhileStatement(GosuWhileStatementImpl whileStatement) {
          IParsedElement parsedElement = whileStatement.getParsedElement();
          PsiElement problemTarget = whileStatement.getFirstChild();
          if (parsedElement instanceof WhileStatement && problemTarget != null) {
            {
              WhileStatement whileStmt = (WhileStatement) parsedElement;
              Expression expr = whileStmt.getExpression();
              if(expr instanceof RelationalExpression && ((RelationalExpression) expr).getOperator().equals("<") ) {
                TypeSystem.pushModule(parsedElement.getModule());
                try {
                  if( whileStmt.isSuppressed( GosuWhileToForInspection.this ) ) {
                    return;
                  }
                }
                finally{
                  TypeSystem.popModule( parsedElement.getModule() );
                }
                RelationalExpression cond = (RelationalExpression) expr;
                Expression lhs = cond.getLHS();
                if(!lhs.getType().equals(JavaTypes.INTEGER()) &&
                   !lhs.getType().equals(JavaTypes.pINT()) &&
                   !(lhs instanceof Identifier))
                {
                  return;
                }
                String ident = lhs.toString();
                IGosuVariable declarationEqualToZero = findDeclarationEqualToZero(ident, whileStatement);
                if( declarationEqualToZero == null) {
                  return;
                }
                if(!isDeclarationDeletable(ident, whileStatement, declarationEqualToZero.getTextOffset())) {
                  return;
                }
                Expression rhs = cond.getRHS();
                if((rhs instanceof NumericLiteral && isPositive((NumericLiteral) rhs)) ||
                    rhs.toString().endsWith(".size()") ||
                    rhs.toString().endsWith(".length()") ||
                    rhs.toString().endsWith(".length"))
                {
                  Statement s = whileStmt.getStatement();
                  if(s instanceof IStatementList) {
                    IStatement[] statements = ((IStatementList) s).getStatements();
                    if(statements == null || statements.length <= 1) {
                      return;
                    }
                    IAssignmentStatement increment = findOnlyUpdateInStatements(ident, statements, whileStatement);
                    if(increment != null && increment.getLocation().getTextFromTokens().equals(ident+"++")) {
                      holder.registerProblem(problemTarget, GosuBundle.message("inspection.while.to.for"),
                              ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                              new WhileFix(whileStatement, ident, rhs, declarationEqualToZero, increment));
                    }
                  }
                }
              }

            }
          }
        }

      private boolean isDeclarationDeletable(final String ident, GosuWhileStatementImpl whileStmt, final int declLine) {
        TextRange whileTextRange = whileStmt.getTextRange();

        Collection<GosuIdentifierImpl> identifiers = PsiTreeUtil.collectElementsOfType(whileStmt.getParent(), GosuIdentifierImpl.class);
        for (GosuIdentifierImpl i : identifiers) {
          PsiElement parent = i.getParent();
          if (i.getText().equals(ident)) {
              int parOff = parent.getTextOffset();
              if (parOff != declLine && !whileTextRange.contains(parOff)) {
                return false;
              }
          }
        }
        return true;
      }

      private  IAssignmentStatement findOnlyUpdateInStatements(String ident, IStatement[] statements, GosuWhileStatementImpl whileStatement) {
        ArrayList<IAssignmentStatement> l = new ArrayList<>();
        for (IStatement statement : statements) {
          if (statement instanceof IAssignmentStatement &&
              ((IAssignmentStatement) statement).getIdentifier().toString().equals(ident)) {
            l.add(((IAssignmentStatement) statement));
          }
        }
        if(l.size() == 1) {
          IAssignmentStatement incr = l.get(0);
          int incrOff = incr.getLocation().getOffset();
          Collection<GosuIdentifierImpl> identifiers = PsiTreeUtil.collectElementsOfType(whileStatement, GosuIdentifierImpl.class);
          for (GosuIdentifierImpl i : identifiers) {
            PsiElement parent = i.getParent();
            if (i.getText().equals(ident)) {
              int parOff = parent.getNode().getStartOffset();
              if (parOff > incrOff) {
                return null;
              }
            }
          }
          return incr;
        }
        return null;
      }

      private IGosuVariable findDeclarationEqualToZero(String ident, GosuWhileStatementImpl whileStatement) {
       PsiElement prev = whileStatement.getPrevSibling();
       while(prev != null){
         if(prev instanceof IGosuVariable)  {
           IGosuVariable var = (IGosuVariable) prev;
           PsiExpression initializer = var.getInitializer();
           if(var.getName().equals(ident) && initializer != null && initializer.getText().equals("0")) {
             return var;
           }
         }
         prev = prev.getPrevSibling();
       }
       return null;
      }

      private boolean isPositive(NumericLiteral num) {
        Object res = num.evaluate();
        return res instanceof Integer && (Integer) res > 0;
      }

    };
  }

  @Override
  public boolean isSuppressed( String warningCode ) {
    return SUPPRESS_WARNING_CODE.equals( warningCode ) || "all".equals( warningCode );
  }

  private class WhileFix implements LocalQuickFix {
    private final WhileToForFix myQuickFix;

    public WhileFix(PsiElement whileStmt, String ident, Expression rhs, IGosuVariable declarationEqualToZero, IAssignmentStatement increment) {
      myQuickFix = new WhileToForFix(whileStmt, ident, rhs, declarationEqualToZero, increment);
    }

    @NotNull
    public String getName() {
      return myQuickFix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return GosuBundle.message("inspection.group.name.statement.issues");
    }

    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      PsiElement element = descriptor.getPsiElement();
      if (element == null) return;
      final PsiFile psiFile = element.getContainingFile();
      if (myQuickFix.isAvailable(project, null, psiFile)) {
        myQuickFix.invoke(project, null, psiFile);
      }
    }
  }

}