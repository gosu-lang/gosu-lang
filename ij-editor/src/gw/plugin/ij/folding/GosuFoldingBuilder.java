/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import gw.plugin.ij.filetypes.GosuTemplateFileProvider;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatementList;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.patterns.PlatformPatterns.*;
import static com.intellij.patterns.StandardPatterns.not;

public class GosuFoldingBuilder extends GosuElementTypes implements FoldingBuilder, DumbAware {
  private static final String LAMBDA = "\u03BB";

  @Nullable
  private static TextRange getBracesTextRange(@NotNull ASTNode node) {
    final ASTNode left = node.findChildByType(GosuTokenTypes.TT_OP_brace_left);
    final ASTNode right = node.findChildByType(GosuTokenTypes.TT_OP_brace_right);
    if (left != null && right != null) {
      return new TextRange(left.getStartOffset(), right.getStartOffset() + 1);
    } else {
      return null;
    }
  }

  @Nullable
  private  static FoldingDescriptor getFoldingDescriptor(@NotNull PsiElement element, @NotNull TextRange range) {
    return range.getLength() > 1 ? new FoldingDescriptor(element, range) : null;
  }

  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
    final List<FoldingDescriptor> descriptors = new ArrayList<>();
    appendDescriptors(node.getPsi(), descriptors);
    return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
  }

  @Nullable
  private static FoldingDescriptor processUsesStatementList(@NotNull PsiElement element) {
    if (psiElement(ELEM_TYPE_UsesStatementList).accepts(element)) {
      final IGosuUsesStatementList list = (IGosuUsesStatementList) element;
      if (list.getUsesStatements().length > 1) {
        final TextRange range = element.getTextRange();
        return new FoldingDescriptor(element,
            new TextRange("uses ".length() + range.getStartOffset(), range.getEndOffset()));
      }
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processMethodDefinition(@NotNull PsiElement element) {
    if (psiElement(METHOD_DEFINITION).accepts(element)) {
      final IGosuStatementList list = PsiTreeUtil.getChildOfType(element, IGosuStatementList.class);
      if (list != null) {
       final TextRange range = getBracesTextRange(list.getNode());
       if (range != null) {
         return new FoldingDescriptor(element, range);
       }
      }
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processNestedDefinition(@NotNull PsiElement element) {
    if (psiElement()
        .withElementType(elementType().oneOf(CLASS_DEFINITION,
                                             INTERFACE_DEFINITION,
                                             ENUM_DEFINITION,
                                             ENHANCEMENT_DEFINITION,
                                             ANNOTATION_DEFINITION))
        .withParent(not(psiFile()))
        .accepts(element)) {
      final TextRange range = getBracesTextRange(element.getNode());
      if (range != null) {
        return new FoldingDescriptor(element, range);
      }
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processAnonymousClassDefiniition(@NotNull PsiElement element) {
    if (psiElement(ANONYMOUS_CLASS_DEFINITION).accepts(element)) {
      return getFoldingDescriptor(element, element.getTextRange());
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processBlockExpression(@NotNull PsiElement element) {
    if (psiElement(ELEM_TYPE_BlockExpression).accepts(element)) {
      return getFoldingDescriptor(element, element.getTextRange());
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processCollectionInitializer(@NotNull PsiElement element) {
    if (psiElement(ELEM_TYPE_CollectionInitializerExpression).accepts(element)) {
      return getFoldingDescriptor(element, element.getTextRange());
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processCommentMultiline(@NotNull PsiElement element) {
    if (or(psiElement(TT_COMMENT_MULTILINE),
           psiElement(JavaDocElementType.DOC_COMMENT))
        .accepts(element)) {
      return getFoldingDescriptor(element, element.getTextRange());
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processSeveral(@NotNull ElementPattern<PsiElement> pattern, @NotNull PsiElement element) {
    if (pattern.accepts(element)) {
      if (!pattern.accepts(PsiTreeUtil.skipSiblingsBackward(element, PsiWhiteSpace.class))) {
        int count = 1;
        PsiElement prev = element;
        while(true) {
          final PsiElement next = PsiTreeUtil.skipSiblingsForward(prev, PsiWhiteSpace.class);
          if (!pattern.accepts(next)) {
            if (count > 1) {
              final int startOffset = element.getTextRange().getStartOffset();
              final int endOffset = prev.getTextRange().getEndOffset();
              return new FoldingDescriptor(element, new TextRange(startOffset, endOffset));
            } else {
              return null;
            }
          }
          prev = next;
          count += 1;
        }
      }
    }
    return null;
  }

  @Nullable
  private static FoldingDescriptor processCommentLine(@NotNull PsiElement element) {
     return processSeveral(psiElement(TT_COMMENT_LINE), element);
  }

  @Nullable
  private static FoldingDescriptor processAnnotation(@NotNull PsiElement element) {
     return processSeveral(psiElement(ELEM_TYPE_AnnotationExpression), element);
  }

  private static void appendDescriptors(@NotNull PsiElement element, @NotNull List<FoldingDescriptor> descriptors) {
    if( GosuTemplateFileProvider.inTemplateFile( element )) {
      return;
    }

    FoldingDescriptor desc;
    if ((desc = processUsesStatementList(element)) != null ||
        (desc = processMethodDefinition(element)) != null ||
        (desc = processNestedDefinition(element)) != null ||
        (desc = processAnonymousClassDefiniition(element)) != null ||
        (desc = processBlockExpression(element)) != null ||
        (desc = processCollectionInitializer(element)) != null ||
        (desc = processAnnotation(element)) != null ||
        (desc = processCommentMultiline(element)) != null ||
        (desc = processCommentLine(element)) != null) {
      descriptors.add(desc);
    }

    PsiElement child = element.getFirstChild();
    while(child != null) {
      appendDescriptors(child, descriptors);
      child = child.getNextSibling();
    }
  }

  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    final IElementType type = node.getElementType();
    if (type == JavaDocElementType.DOC_COMMENT) {
      return "/**...*/";
    } else if (type == TT_COMMENT_MULTILINE) {
      return "/*...*/";
    } else if (type == METHOD_DEFINITION) {
      return "{...}";
    } else if (type == CLASS_DEFINITION ||
        type == INTERFACE_DEFINITION ||
        type == ENUM_DEFINITION ||
        type == ENHANCEMENT_DEFINITION ||
        type == ANNOTATION_DEFINITION ||
        type == ANONYMOUS_CLASS_DEFINITION ||
        type == ELEM_TYPE_CollectionInitializerExpression) {
      return "{...}";
    } else if (type == ELEM_TYPE_AnnotationExpression) {
      return "@{...}";
    } else if (type == ELEM_TYPE_BlockExpression) {
      return LAMBDA + "...";
    }

    return null;
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    final IElementType type = node.getElementType();
    if (type == ELEM_TYPE_UsesStatementList) {
      return true;
    }

    return false;
  }
}
