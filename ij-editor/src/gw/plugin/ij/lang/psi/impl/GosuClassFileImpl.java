/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.parser.GosuAstTransformer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GosuClassFileImpl extends AbstractGosuClassFileImpl {
  public GosuClassFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, GosuLanguage.instance());
  }

  public ASTNode parse(@NotNull ASTNode chameleon) {
    return GosuAstTransformer.instance().transformClass(chameleon, parseType(false)).getFirstChildNode();
  }

  public IGosuClass parseType(String strClassName, String contents, int completionMarkerOffset) {
    return parseGosuClassDirectly(strClassName, contents, completionMarkerOffset, ClassType.Class);
  }

  @Override
  protected Icon getElementIcon(@IconFlags int flags) {
    return GosuIcons.FILE_CLASS;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitClassFile(this);
    }
    else {
      visitor.visitFile( this );
    }
  }
}
