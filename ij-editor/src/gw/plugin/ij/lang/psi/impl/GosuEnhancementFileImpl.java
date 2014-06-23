/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.FileViewProvider;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.parser.GosuAstTransformer;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuEnhancementFileImpl extends AbstractGosuClassFileImpl {

  public GosuEnhancementFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, GosuLanguage.instance());
  }

  public ASTNode parse(@NotNull ASTNode chameleon) {
    return GosuAstTransformer.instance().transformClass(chameleon, parseType(false)).getFirstChildNode();
  }

  @NotNull
  @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
  protected GosuEnhancementFileImpl clone() {
    return (GosuEnhancementFileImpl) super.clone();
  }

  public IGosuClass parseType(String strClassName, String contents, int completionMarkerOffset) {
    return parseGosuClassDirectly(strClassName, contents, completionMarkerOffset, ClassType.Enhancement);
  }

  @Override
  protected Icon getElementIcon(@IconFlags int flags) {
    return GosuIcons.FILE_ENHANCEMENT;
  }
}
