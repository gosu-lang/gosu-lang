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
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiMatcherImpl;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatementList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuClassParseData;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.lang.psi.util.LeafPsiMatcher;
import gw.plugin.ij.util.ClassLord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.intellij.psi.util.PsiMatchers.hasClass;
import static com.intellij.psi.util.PsiMatchers.hasText;
import static gw.plugin.ij.util.ClassLord.purgeClassName;
import static java.lang.String.format;

public class ChangeMethodTypeFix extends LocalQuickFixAndIntentionActionOnPsiElement {

  private final String typeName;

  public ChangeMethodTypeFix(@Nullable PsiElement element, @NotNull String typeName) {
    super(element);
    this.typeName = typeName;
  }

  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {

    if( !CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile()) ) {
      return;
    }
    
    IGosuStatementList stmtList = (IGosuStatementList) new PsiMatcherImpl(startElement)
            .ancestor(hasClass(IGosuMethod.class))
            .descendant(hasClass(IGosuStatementList.class))
            .getElement();

    if (stmtList == null) {
      return;
    }
    String typeName = this.typeName;
    
    Map<String, String> imports = new LinkedHashMap<String, String>();
    typeName = ClassLord.simplifyTypes(typeName, (IGosuPsiElement) file, true, imports);
    PsiElement returnLiteral = GosuPsiParseUtil.parseTypeLiteral(typeName, file);
    
    PsiElement func = stmtList.getParent();
    func.getNode().addLeaf(GosuTokenTypes.TT_OP_colon, ":", stmtList.getNode());
    func.addBefore(returnLiteral, stmtList);

    for (String imp : imports.values()) {
      ClassLord.doImportAndStick(imp, file);
    }

    PsiElement closingBrace = new LeafPsiMatcher(startElement)
          .ancestor(hasClass(IGosuMethod.class))
          .descendant(hasText(")")).getElement();

    CodeStyleManager styleManager = CodeStyleManager.getInstance(project);
    if (closingBrace != null) {
      styleManager.reformatText(file, 
                                closingBrace.getTextOffset() + 
                                closingBrace.getTextLength(), 
                                stmtList.getTextOffset());
    }
  }

  private PsiFile getFile() {
    return getStartElement().getContainingFile();
  }

  public IGosuClass getGosuClass() {
    PsiFile file = getFile();
    if( !(file instanceof AbstractGosuClassFileImpl) ) {
      return null;
    }

    AbstractGosuClassFileImpl gosuClassFile = (AbstractGosuClassFileImpl) file;
    GosuClassParseData parseData = gosuClassFile.getParseData();
    if (parseData != null) {
      IClassFileStatement classFileStatement = parseData.getClassFileStatement();
      if (classFileStatement != null) {
        return classFileStatement.getGosuClass();
      }
    }
    return null;
  }

  @NotNull
  @Override
  public String getText() {
    return format("Change method return type to '%s'", purgeClassName(typeName));
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return "Change method return type";
  }
}
