/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuEnhancementFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuTemplateFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuTypeDefinitionImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GosuIconProvider extends IconProvider {
  @Override
  public Icon getIcon(@NotNull PsiElement element, @Iconable.IconFlags int flags) {
    if (element instanceof AbstractGosuClassFileImpl) {
      if( element instanceof GosuTemplateFileImpl ) {
        return GosuIcons.FILE_TEMPLATE;
      }
      else if( element instanceof GosuProgramFileImpl ) {
        return GosuIcons.FILE_PROGRAM;
      }
      else if( element instanceof GosuEnhancementFileImpl ) {
        return GosuIcons.FILE_ENHANCEMENT;
      }
      for (PsiElement child : element.getChildren()) {
        if (child instanceof GosuTypeDefinitionImpl) {
          return child.getIcon(flags);
        }
      }
    }
    return null;
  }
}
