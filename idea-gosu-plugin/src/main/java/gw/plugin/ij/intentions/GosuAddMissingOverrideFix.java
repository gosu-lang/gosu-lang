/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.util.IncorrectOperationException;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodBaseImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

public class GosuAddMissingOverrideFix extends BaseIntentionAction {
  private final GosuMethodBaseImpl _method;

  public GosuAddMissingOverrideFix(GosuMethodBaseImpl methodBase) {
    _method = methodBase;
  }

  @NotNull
  @Override
  public String getText() {
    return "Add 'override' keyword";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return _method.isValid() && _method.getManager().isInProject(_method);
  }

  @Override
  public void invokeImpl(@NotNull Project project, Editor editor, @NotNull PsiFile file) throws IncorrectOperationException {
    addOverride();
    ((AbstractGosuClassFileImpl) file).reparseGosuFromPsi();
  }

  private void addOverride() throws IncorrectOperationException {
    IModule module = GosuModuleUtil.findModuleForPsiElement(_method);
    GosuMethodBaseImpl methodDecl = (GosuMethodBaseImpl) GosuPsiParseUtil.parseDeclaration("override function test() {}", _method.getManager(), module);
    ASTNode[] children = _method.getNode().getChildren(null);
    ASTNode overrideKeyword = methodDecl.getNode().getChildren(null)[0];
    CodeEditUtil.setOldIndentation((TreeElement) overrideKeyword, 0); // this is to avoid a stupid exception
    _method.getNode().addChildren(overrideKeyword, overrideKeyword.getTreeNext().getTreeNext(), children[0]);
  }
}
