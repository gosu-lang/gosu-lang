/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.java.JavaFormattingModelBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormattingDocumentModelImpl;
import com.intellij.psi.formatter.common.AbstractBlock;
import gw.plugin.ij.lang.GosuLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GosuFormattingModelBuilder extends JavaFormattingModelBuilder {
  @NotNull
  public FormattingModel createModel(@NotNull final PsiElement element, final CodeStyleSettings settings) {
    final PsiFile file = element.getContainingFile().getViewProvider().getPsi(GosuLanguage.instance());
    Block block;

    if (!file.getVirtualFile().getExtension().equals("gr")) {
      block = new GosuBlock(
          file.getNode(),
          Alignment.createAlignment(),
          Indent.getAbsoluteNoneIndent(),
          null,
          settings);
    } else {
      block = new AbstractBlock(file.getNode(), null, null) {
        @Override
        protected List<Block> buildChildren() {
          return Collections.emptyList();
        }

        @Override
        public Spacing getSpacing(Block child1, Block child2) {
          return null;
        }

        @Override
        public boolean isLeaf() {
          return true;
        }
      };

    }
    return new GosuFormattingModel(file, block, FormattingDocumentModelImpl.createOn(file));
  }

  @Nullable
  public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
    return null;
  }
}
