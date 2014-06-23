/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import gw.plugin.ij.filetypes.GosuProgramFileProvider;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.patterns.PlatformPatterns.or;
import static com.intellij.patterns.PlatformPatterns.psiElement;

public class GosuIndentProcessor extends GosuElementTypes {
  @NotNull
  public static List<ASTNode> getNonEmptyASTNodes(@NotNull ASTNode... nodes) {
    final List<ASTNode> result = new ArrayList<>();
    for (ASTNode node : nodes) {
      if (hasContent(node)) {
        result.add(node);
      }
    }
    return result;
  }

  private static boolean hasContent(@NotNull final ASTNode node) {
    return node.getText().trim().length() > 0;
  }

  @Nullable
  private static PatternCondition<PsiElement> hasSiblingBefore(final IElementType type) {
    return new PatternCondition<PsiElement>("hasSiblingBefore") {
      @Override
      public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
        psiElement = psiElement.getPrevSibling();
        while (psiElement != null) {
          final ASTNode node = psiElement.getNode();
          if (node != null && node.getElementType().equals(type)) {
            return true;
          }
          psiElement = psiElement.getPrevSibling();
        }
        return false;
      }
    };
  }

  @Nullable
  private static PatternCondition<PsiElement> hasSiblingAfter(final IElementType type) {
    return new PatternCondition<PsiElement>("hasSiblingAfter") {
      @Override
      public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
        psiElement = psiElement.getNextSibling();
        while (psiElement != null) {
          final ASTNode node = psiElement.getNode();
          if (node != null && node.getElementType().equals(type)) {
            return true;
          }
          psiElement = psiElement.getNextSibling();
        }
        return false;
      }
    };
  }

  public static final ElementPattern<PsiElement> ALL_DEFINITIONS = or(
      psiElement(CLASS_DEFINITION),
      psiElement(ANONYMOUS_CLASS_DEFINITION),
      psiElement(INTERFACE_DEFINITION),
      psiElement(ENUM_DEFINITION),
      psiElement(ENHANCEMENT_DEFINITION),
      psiElement(ANNOTATION_DEFINITION));

  @NotNull
  public static Indent getChildIndent(@NotNull final GosuBlock parent, @Nullable final ASTNode prevNode, @NotNull final ASTNode node) {
    final PsiElement psi = node.getPsi();
    final PsiElement prevPsi = prevNode != null ? prevNode.getPsi() : null;
    final PsiElement parentPsi = psi.getParent();

    // Top level elements
    final boolean insideProgram = GosuProgramFileProvider.isProgram(psi.getContainingFile().getVirtualFile());
    if (insideProgram && parentPsi instanceof PsiClass && parentPsi.getParent() instanceof PsiFile) {
      return Indent.getNoneIndent();
    }

    if (parentPsi instanceof PsiFile) {
      return Indent.getNoneIndent();
    }

    // UsesStatement & StatementList
    if (or(psiElement(ELEM_TYPE_UsesStatement),
        psiElement(ELEM_TYPE_StatementList))
        .accepts(psi)) {
      return Indent.getNoneIndent();
    }

    // Inside definitions or statement lists
    if (psiElement()
            .withParent(ALL_DEFINITIONS)
            .with(hasSiblingBefore(TT_OP_brace_left))
            .with(hasSiblingAfter(TT_OP_brace_right))
            .accepts(psi) ||
        psiElement()
            .withParent(psiElement(ELEM_TYPE_StatementList))
            .with(hasSiblingBefore(TT_OP_brace_left))
            .with(hasSiblingAfter(TT_OP_brace_right))
            .accepts(psi)) {
      return Indent.getNormalIndent();
    }

    // Comments
    if (psiElement(JavaDocElementType.DOC_COMMENT)
        .accepts(psi)) {
      return Indent.getNoneIndent();
    }

    if (or(psiElement(JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS),
           psiElement(JavaDocTokenType.DOC_COMMENT_END))
        .accepts(psi)) {
      return Indent.getSpaceIndent(1);
    }

    // Annoations
    if (psiElement(ELEM_TYPE_AnnotationExpression)
        .accepts(psi)) {
      return Indent.getNoneIndent();
    }

    // Modifiers (none indent after modifier)
    if (psiElement()
                   .withElementType(ELEM_TYPE_ModifierListClause)
                   .accepts(prevPsi) ||
        psiElement()
                   .withElementType(GosuTokenSets.MODIFIERS)
                   .accepts(prevPsi)) {
      return Indent.getNoneIndent();
    }

    // Braces
    if (or(psiElement(TT_OP_brace_left),
        psiElement(TT_OP_brace_right))
        .accepts(psi)) {
      return Indent.getNoneIndent();
    }

    // Parameters (because of alignment)
    if (psiElement()
        .withParent(psiElement(ELEM_TYPE_ParameterListClause))
        .accepts(psi)) {
      return Indent.getNoneIndent();
    }
    if (psiElement(ELEM_TYPE_ModifierListClause)
        .accepts( psi.getPrevSibling())) {
      return Indent.getNoneIndent();
    }
    // * [INDENT] else
    if (psiElement(TT_else)
        .accepts(psi)) {
      return Indent.getNoneIndent();
    }

    // else [INDENT] *
    if (psiElement(TT_else)
        .accepts(prevPsi)) {
      return Indent.getNormalIndent(); // StatementList was already processed with none indent
    }

    // if/while/for() [INDENT] *
    if (psiElement(TT_OP_paren_right)
        .accepts(prevPsi) &&
        psiElement()
            .withParent(or(psiElement(ELEM_TYPE_IfStatement),
                psiElement(ELEM_TYPE_WhileStatement),
                psiElement(ELEM_TYPE_ForEachStatement)))
            .accepts(psi)) {
      return Indent.getNormalIndent();
    }

    // [INDENT] case *
    if (psiElement(ELEM_TYPE_CaseClause).accepts(psi)) {
      return Indent.getNormalIndent();
    }

    // ) [INDENT] ;
    if (or(psiElement(TT_OP_paren_right),
           psiElement(TT_OP_semicolon))
        .accepts(psi)) {
      return Indent.getNoneIndent();
    }

    if (psiElement(ELEM_TYPE_AnnotationExpression)
        .accepts(prevPsi)) {
      return Indent.getNoneIndent();
    }

    // General case with continuation indent and none indent
    final boolean hasParentAndNoPrevSibling = parentPsi != null && psi.getPrevSibling() == null;
    return hasParentAndNoPrevSibling ? Indent.getNoneIndent() : Indent.getContinuationIndent();
  }

  @Nullable
  public static Alignment getChildAlignment(GosuBlock parent, ASTNode prevNode, @NotNull ASTNode node, Alignment parentAlignment) {
    final PsiElement nodePsi = node.getPsi();

    // Parameters
    if (psiElement(ELEM_TYPE_ParameterListClause).accepts(nodePsi)) {
      return Alignment.createChildAlignment(parentAlignment);
    }

    return null;
  }

  @Nullable
  public static ChildAttributes getChildAttributes(@NotNull GosuBlock parent, final int newChildIndex) {
    final ASTNode node = parent.getNode();
    final List<ASTNode> children = getNonEmptyASTNodes(node.getChildren(null));
    final PsiElement nodePsi = node.getPsi();
    final int normalIndent = parent.getSettings().getIndentSize(nodePsi.getContainingFile().getFileType());

    // if (true) [CURSOR] *
    // while(true) [CURSOR] *
    // for(i in 1..10) [CURSOR] *
    if (newChildIndex > 0) {
      final PsiElement prevPsi = children.get(newChildIndex - 1).getPsi();
      if (or(psiElement(ELEM_TYPE_IfStatement),
          psiElement(ELEM_TYPE_WhileStatement),
          psiElement(ELEM_TYPE_ForEachStatement)).accepts(prevPsi)) {
        if (!psiElement()
            .afterLeaf(psiElement(TT_OP_paren_right))
            .accepts(prevPsi.getLastChild())) {
          return new ChildAttributes(Indent.getSpaceIndent(2 * normalIndent), null);
        }
      }
    }

    // Other
    if (ALL_DEFINITIONS.accepts(nodePsi)) {
      final ASTNode leftBrace = node.findChildByType(GosuTokenTypes.TT_OP_brace_left);
      final ASTNode rightBrace = node.findChildByType(GosuTokenTypes.TT_OP_brace_right);
      if (leftBrace != null && rightBrace != null) {
        final List<ASTNode> nodeChildren = getNonEmptyASTNodes(node.getChildren(null));
        final int leftBraceIndex = nodeChildren.indexOf(leftBrace);
        final int rightBraceIndex = nodeChildren.indexOf(rightBrace);
        if (leftBraceIndex < newChildIndex && newChildIndex <= rightBraceIndex) {
          return new ChildAttributes(Indent.getNormalIndent(), null);
        }
      }
    }

    if (or(psiElement(ELEM_TYPE_StatementList),
        psiElement(ELEM_TYPE_IfStatement),
        psiElement(ELEM_TYPE_WhileStatement),
        psiElement(ELEM_TYPE_ForEachStatement),
        psiElement(ELEM_TYPE_SwitchStatement))
        .accepts(nodePsi)) {
      return new ChildAttributes(Indent.getNormalIndent(), null);
    }

    return new ChildAttributes(Indent.getNoneIndent(), null);
  }
}
