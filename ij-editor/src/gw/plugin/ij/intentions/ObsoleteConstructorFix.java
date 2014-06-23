/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameterList;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodBaseImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

public class ObsoleteConstructorFix extends BaseIntentionAction {
  private final GosuMethodImpl method;

  public ObsoleteConstructorFix(GosuMethodImpl method) {
    this.method = method;
  }

  @NotNull
  @Override
  public String getText() {
    return "Fix obsolete constructor syntax";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  public void invokeImpl(@NotNull final Project project, final Editor editor, @NotNull final PsiFile file) {

    final IModule module = GosuModuleUtil.findModuleForPsiElement(method);

    final GosuMethodBaseImpl constructor = (GosuMethodBaseImpl) GosuPsiParseUtil.parseDeclaration("construct(){}", method.getManager(), module);

    CodeEditUtil.saveWhitespacesInfo(method.getNode());
    CodeEditUtil.setOldIndentation(constructor.getNode(), CodeEditUtil.getOldIndentation(method.getNode()));


    final PsiCodeBlock oldBody = method.getBody();
    if (oldBody != null) {
      constructor.getBody().replace(oldBody);
    }

    final IGosuParameterList oldParameters = method.getParameterList();
    constructor.getParameterList().replace(oldParameters);

    method.replace(constructor);

    ((AbstractGosuClassFileImpl) file).reparseGosuFromPsi();
  }
}
