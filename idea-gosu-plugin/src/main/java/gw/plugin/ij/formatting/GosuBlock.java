/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.ILazyParseableElementType;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.impl.statements.GosuStatementListImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GosuBlock extends GosuElementTypes implements Block {
  @NotNull
  protected final ASTNode myNode;
  @Nullable
  protected final Alignment myAlignment;
  @NotNull
  protected final Indent myIndent;
  @Nullable
  protected final Wrap myWrap;
  protected final CodeStyleSettings mySettings;

  protected List<Block> mySubBlocks;

  public GosuBlock(@NotNull final ASTNode node, @Nullable final Alignment alignment, @NotNull final Indent indent, @Nullable final Wrap wrap, final CodeStyleSettings settings) {
    myNode = node;
    myAlignment = alignment;
    myIndent = indent;
    myWrap = wrap;
    mySettings = settings;
  }

  @NotNull
  public ASTNode getNode() {
    return myNode;
  }

  @NotNull
  public CodeStyleSettings getSettings() {
    return mySettings;
  }

  @NotNull
  public TextRange getTextRange() {
    return myNode.getTextRange();
  }

  @NotNull
  public List<Block> getSubBlocks() {
    if (mySubBlocks == null) {
      mySubBlocks = generateSubBlocks();
    }
    return mySubBlocks;
  }

  @NotNull
  protected List<Block> generateSubBlocks() {
    final PsiElement nodePsi = myNode.getPsi();
    final ArrayList<Block> blocks = new ArrayList<>();
    ASTNode prevChildNode = null;
    for (ASTNode childNode : GosuIndentProcessor.getNonEmptyASTNodes(myNode.getChildren(null))) {
      final Indent childIndent = GosuIndentProcessor.getChildIndent(this, prevChildNode, childNode);
      final Alignment childAlignment = GosuIndentProcessor.getChildAlignment(this, prevChildNode, childNode,
          myAlignment);
      blocks.add(new GosuBlock(childNode, nodePsi instanceof GosuStatementListImpl ? null : childAlignment, childIndent, myWrap, mySettings));
      prevChildNode = childNode;
    }
    return blocks;
  }

  @Nullable
  public Wrap getWrap() {
    return myWrap;
  }

  @Nullable
  public Indent getIndent() {
    return myIndent;
  }

  @Nullable
  public Alignment getAlignment() {
    return myAlignment;
  }

  @Nullable
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    final GosuBlock gosuChild1 = (GosuBlock) child1;
    final GosuBlock gosuChild2 = (GosuBlock) child2;

    // FIXME-isd: do something... Seems like in IJ12 we can get null we are not expecting
    if (gosuChild1 == null) {
      return null;
    }

    Spacing spacing;
    if ((spacing = GosuWrappingAndBracesProcessor.getSpacing(gosuChild1, gosuChild2, mySettings)) != null ||
        (spacing = GosuBlankLinesProcessor.getSpacing(gosuChild1, gosuChild2, mySettings)) != null ||
        (spacing = GosuSpacingProcessor.getSpacing(gosuChild1, gosuChild2, mySettings)) != null) {
      return spacing;
    }
    return null;
  }

  @NotNull
  public ChildAttributes getChildAttributes(int newChildIndex) {
    return GosuIndentProcessor.getChildAttributes(this, newChildIndex);
  }

  public boolean isIncomplete() {
    return isIncomplete(myNode);
  }

  public boolean isIncomplete(@NotNull final ASTNode node) {
    if (node.getElementType() instanceof ILazyParseableElementType) {
      return false;
    }

    ASTNode lastChild = node.getLastChildNode();
    while (lastChild != null &&
        !(lastChild.getElementType() instanceof ILazyParseableElementType) &&
        (lastChild.getPsi() instanceof PsiWhiteSpace || lastChild.getPsi() instanceof PsiComment)) {
      lastChild = lastChild.getTreePrev();
    }

    return lastChild != null && (lastChild.getPsi() instanceof PsiErrorElement || isIncomplete(lastChild));
  }

  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null;
  }

  @NotNull
  @Override
  public String toString() {
    return myNode.getTextRange() + ": " + myNode;
  }
}
