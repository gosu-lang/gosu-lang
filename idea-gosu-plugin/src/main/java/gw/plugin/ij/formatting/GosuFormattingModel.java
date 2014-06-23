/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Block;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.FormattingDocumentModelImpl;
import com.intellij.psi.formatter.PsiBasedFormattingModel;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import gw.plugin.ij.lang.GosuTokenTypes;
import org.jetbrains.annotations.NotNull;

public class GosuFormattingModel extends PsiBasedFormattingModel {
  public GosuFormattingModel(PsiFile file, @NotNull Block rootBlock, FormattingDocumentModelImpl documentModel) {
    super(file, rootBlock, documentModel);
  }

  @Override
  protected String replaceWithPsiInLeaf(TextRange textRange, String whiteSpace, @NotNull ASTNode leafElement) {
    if (!myCanModifyAllWhiteSpaces) {
      if (leafElement.getElementType() == GosuTokenTypes.TT_WHITESPACE) {
        return null;
      }
    }

    IElementType elementTypeToUse = TokenType.WHITE_SPACE;
    ASTNode prevNode = TreeUtil.prevLeaf(leafElement);
    if (prevNode != null && GosuTokenTypes.TT_WHITESPACE == prevNode.getElementType()) {
      elementTypeToUse = prevNode.getElementType();
    }
    FormatterUtil.replaceWhiteSpace(whiteSpace, leafElement, elementTypeToUse, textRange);
    return whiteSpace;
  }
}