/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.SymbolNotFoundException;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.parser.GosuAstTransformer;
import gw.plugin.ij.lang.psi.api.IGosuPackageDefinition;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GosuTemplateFileImpl extends AbstractGosuClassFileImpl {
  public GosuTemplateFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, GosuLanguage.instance());
  }

  @NotNull
  @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
  protected GosuTemplateFileImpl clone() {
    return (GosuTemplateFileImpl) super.clone();
  }

  public ASTNode parse(@NotNull ASTNode chameleon) {
    return GosuAstTransformer.instance().transformProgram(chameleon, (IGosuProgram) parseType(false)).getFirstChildNode();
  }

  public IGosuProgram parseType(String strClassName, String contents, int completionMarkerOffset) {
    final IModule module = getModule();
    TypeSystem.pushModule(module);
    // DP: this line should execute outside the TS lock to avoid deadlock
    final IGosuParser parser = createParser(contents);
    TypeSystem.lock();
    try {
      refreshTheOldType();

      ITemplateType templateType;
      IClassFileStatement classFileStmt;
      ISymbolTable snapshotSymbols = null;
      IType ctxType = null;
      try {
        final ParserOptions options = new ParserOptions()
            .withParser(parser)
            .withFileContext(new ModuleFileContext(module, strClassName));

        templateType = (ITemplateType) GosuParserFactory
            .createProgramParser()
            .parseTemplate(contents, parser.getSymbolTable(), options)
            .getProgram();
        classFileStmt = templateType.getClassStatement().getClassFileStatement();
      } catch (ParseResultsException ex) {
        classFileStmt = (IClassFileStatement) ex.getParsedElement();
        IClassStatement classStatement = classFileStmt.getClassStatement();
        templateType = classStatement != null ? (ITemplateType) classStatement.getGosuClass() : null;


        SymbolNotFoundException issue = getExceptionWithSymbolTable(ex);
        if (issue != null) {
          snapshotSymbols = issue.getSymbolTable();
          ctxType = issue.getExpectedType();
        }
      }

      if (getOriginalFile().getVirtualFile() != null) {
        final GosuClassParseData data = getParseData();
        data.setClassFileStatement(classFileStmt);
        data.setSource(contents);
        if (isForComplection(contents)) {
          data.setSnapshotSymbols(snapshotSymbols);
          data.setContextType(ctxType);
        }
      }

      return templateType;
    } finally {
      TypeSystem.unlock();
      TypeSystem.popModule(module);
    }
  }

  @Override
  public void addImport(String qualifiedName) {
    final PsiElement usesStmt = GosuPsiParseUtil.parseTemplateImport(qualifiedName, this);
    final IGosuUsesStatementList uses = findChildByClass(IGosuUsesStatementList.class);

    final PsiElement psiElement;
    if (uses == null) {
      psiElement = addAfter(usesStmt, findChildByClass(IGosuPackageDefinition.class));
    } else {
      psiElement = uses.add(usesStmt.getChildren()[0]);
    }
    CodeStyleManager.getInstance(getProject()).reformat(psiElement);
  }

  @Override
  protected Icon getElementIcon(@IconFlags int flags) {
    return GosuIcons.FILE_TEMPLATE;
  }
}
