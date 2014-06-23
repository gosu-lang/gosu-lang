/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiMatcherImpl;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.expressions.NumericLiteral;
import gw.lang.parser.IStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.IWhileStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.impl.statements.GosuForEachStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuWhileStatementImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiMatchers.hasClass;

public class WhileToForFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  String ident;
  Expression rhs;
  private IGosuVariable declarationEqualToZero;
  private IAssignmentStatement increment;

  public WhileToForFix(PsiElement whileStmt, String ident, Expression rhs, IGosuVariable declarationEqualToZero, IAssignmentStatement increment) {
    super(whileStmt);
    this.ident = ident;
    this.rhs = rhs;
    this.declarationEqualToZero = declarationEqualToZero;
    this.increment = increment;
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())) {
      return;
    }
    IWhileStatement parsedElement = ((GosuWhileStatementImpl) startElement).getParsedElement();
    if (parsedElement == null) {
      return;
    }
    IStatement statement = parsedElement.getStatement();
    IStatement[] statements = ((IStatementList) statement).getStatements();
    StringBuilder forStmt = new StringBuilder();
    forStmt.append("for (");
    forStmt.append(ident);
    forStmt.append(" in 0..");
    if(rhs instanceof NumericLiteral) {
      Object res = rhs.evaluate();
      if(res instanceof Integer) {
        forStmt.append(((Integer)res)-1);
      }
    } else {
      forStmt.append("|" + rhs);
    }
    forStmt.append(") {\n");
    String indent = getIndet(parsedElement, statements);
    for (IStatement statement1 : statements) {
      if (statement1 != increment) {
        forStmt.append(indent);
        forStmt.append(statement1.getLocation().getTextFromTokens());
        forStmt.append("\n");
      }
    }
    forStmt.append("}");
    PsiElement stub = GosuPsiParseUtil.parseProgramm(forStmt.toString(), startElement, file.getManager(), null);
    PsiElement newForStmt = new PsiMatcherImpl(stub)
                                      .descendant(hasClass(GosuForEachStatementImpl.class))
                                      .getElement();
    if (newForStmt != null) {
      declarationEqualToZero.delete();
      startElement.replace(newForStmt);
    }
  }

  private String getIndet(IWhileStatement parsedElement, IStatement[] statements) {
    int whileColum = parsedElement.getLocation().getColumn();
    int column = statements[1].getLocation().getColumn() - whileColum;
    if(column < 0) {
      return "  ";
    }
    StringBuilder out = new StringBuilder();
    for(int i = 0; i <= column; i++) {
      out.append(" ");
    }
    return out.toString();
  }

  private void removeVarDecl(PsiElement whileStmt, String ident) {
    PsiElement prev = whileStmt.getPrevSibling();
    while (prev instanceof PsiWhiteSpace) {
      prev = prev.getPrevSibling();
    }
    if (prev instanceof IGosuVariable && ((IGosuVariable) prev).getName().equals(ident)) {
      prev.delete();
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    return startElement instanceof GosuWhileStatementImpl;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.while.to.for");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message("inspection.group.name.statement.issues");
  }
}

