/*
 * Copyright 2014 Guidewire Software, Inc.
 */

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
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.expressions.InferredNewExpression;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.parser.expressions.NumericLiteral;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.IWarningSuppressor;
import gw.lang.parser.expressions.IImplicitTypeAsExpression;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.intentions.varInferenceFix;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GosuInferTypeInDeclarationInspection extends BaseLocalInspectionTool implements IWarningSuppressor {

  public static final String SUPPRESS_WARNING_CODE = "InferTypeInDeclaration";

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return GosuBundle.message("inspection.group.name.declaration.issues");
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GosuBundle.message("inspection.variable.type.inferred");
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuInferTypeInDeclaration";
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
      public void visitField(IGosuField field) {
        IParsedElement parsedElement = field.getParsedElement();
        if(parsedElement == null) {
          return;
        }
        if( parsedElement.isSuppressed( GosuInferTypeInDeclarationInspection.this ) ) {
          return;
        }
        boolean isProgramVariable = parsedElement.getGosuClass() instanceof IGosuProgram;
        if (parsedElement instanceof VarStatement) {
          VarStatement varStmt = (VarStatement) parsedElement;
          if (isProgramVariable || varStmt.isPrivate()) {
            IGosuExpression init = field.getInitializerGosu();
            if (init == null) {
              return;
            }
            Expression expr = (Expression) init.getParsedElement();
            if (isFixable(varStmt, expr)) {
              holder.registerProblem(field, GosuBundle.message("inspection.variable.type.inferred"), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new TypeInferenceFix(field));
            }
          }
        }
      }

      @Override
      public void visitVariable(IGosuVariable variable) {
        IParsedElement parsedElement = variable.getParsedElement();
        if (parsedElement instanceof VarStatement) {
          if( parsedElement.isSuppressed( GosuInferTypeInDeclarationInspection.this ) ) {
            return;
          }
          VarStatement varStmt = (VarStatement) parsedElement;
          Expression expr = varStmt.getAsExpression();
          if (isFixable(varStmt, expr)) {
            holder.registerProblem(variable, GosuBundle.message("inspection.variable.type.inferred"), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new TypeInferenceFix(variable));
          }
        }
      }
    };
  }

  private boolean isFixable(VarStatement varStmt, Expression expr) {
    IModule module = varStmt.getModule();
    if( module == null ) {
      return false;
    }
    TypeSystem.pushModule( module );
    try {
      TypeLiteral typeLiteral = varStmt.getTypeLiteral();
      boolean fixable = false;
      if (expr != null && typeLiteral != null && varStmt.getType().isAssignableFrom(expr.getType()))
      {
        fixable = true;
        if( expr instanceof NewExpression && expr.getLocation() != null )
        {
          // prevent fix in cases like this: var a : Integer =  new(1)
          List<IParseTree> children = expr.getLocation().getChildren();
          if( !children.isEmpty() && children.get(0).getTextFromTokens().equals("") )
          {
            fixable = false;
          }
        }
        if( expr instanceof InferredNewExpression &&
            expr.getLocation().getTextFromTokens().startsWith("{"))
        {
          // prevent fix in cases like this: var a : Integer[] = {1,2} or
          //                                 var a : Set<String> = {}
          fixable = false;
        } else if( expr instanceof NumericLiteral &&
                  !((NumericLiteral) expr).isExplicitlyTyped())
        {
          // prevent fix in cases like this: var a : float = 10
          //                                 var a : BigDecimal = 1
          fixable = false;
        } else if(expr instanceof NullExpression)
        {
          // prevent fix in cases like this: var a : String = null
          fixable = false;
        } else if(expr instanceof IImplicitTypeAsExpression)
        {
          // prevent fix in cases like this: var _subtype : typekey.State = "CA"
          fixable = false;
        } else if(expr instanceof IMemberAccessExpression && !expr.getLocation().getTextFromTokens().contains("."))
        {
          // prevent fix in cases like this: var c : Currency = TC_USD
          fixable = false;
        } else if(varStmt.hasProperty())
        {
          // prevent fix in cases like this: var x : String as Name = "Gosu"
          fixable = false;
        }
        else {
          IType type = typeLiteral.getType().getType();
          if( type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder() &&
              !(expr.getType() instanceof IPlaceholder && ((IPlaceholder)expr.getType()).isPlaceholder()) ) {
            // prevent fix in cases like this:  var dyn : Dynamic = foo
            fixable = false;
          }
        }
      }
      return fixable;
    }
    finally {
      TypeSystem.popModule( module );
    }
  }

  @Override
  public boolean isSuppressed( String warningCode ) {
    return SUPPRESS_WARNING_CODE.equals( warningCode ) || "all".equals( warningCode );
  }

  private class TypeInferenceFix implements LocalQuickFix {
    private final varInferenceFix myQuickFix;

    public TypeInferenceFix(IGosuVariable variable) {
      myQuickFix = new varInferenceFix(variable);
    }

    @NotNull
    public String getName() {
      return myQuickFix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return GosuBundle.message("inspection.group.name.declaration.issues");
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
