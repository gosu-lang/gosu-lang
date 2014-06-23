/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.codeInsight.editorActions.moveUpDown.LineMover;
import com.intellij.codeInsight.editorActions.moveUpDown.LineRange;
import com.intellij.lang.Language;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.JspPsiUtil;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiBlockStatement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassInitializer;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDoWhileStatement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiWhileStatement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import gw.plugin.ij.lang.GosuLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


class GosuStatementMover extends LineMover {
  private static final Logger LOG = Logger.getInstance("gw.plugin.ij.actions.GosuStatementMover");

  private PsiElement statementToSurroundWithCodeBlock;

  @NotNull
  public static PsiElement[] findStatementsInRange(@NotNull PsiFile file, int startOffset, int endOffset) {
    Language language = GosuLanguage.instance();
    if (language == null) return PsiElement.EMPTY_ARRAY;
    FileViewProvider viewProvider = file.getViewProvider();
    PsiElement element1 = viewProvider.findElementAt(startOffset, language);
    PsiElement element2 = viewProvider.findElementAt(endOffset - 1, language);
    if (element1 instanceof PsiWhiteSpace) {
      startOffset = element1.getTextRange().getEndOffset();
      element1 = file.findElementAt(startOffset);
    }
    if (element2 instanceof PsiWhiteSpace) {
      endOffset = element2.getTextRange().getStartOffset();
      element2 = file.findElementAt(endOffset - 1);
    }
    if (element1 == null || element2 == null) return PsiElement.EMPTY_ARRAY;

    PsiElement parent = PsiTreeUtil.findCommonParent(element1, element2);
    if (parent == null) return PsiElement.EMPTY_ARRAY;
    while (true) {
      if (parent instanceof PsiStatement) {
        parent = parent.getParent();
        break;
      }
      if (parent instanceof PsiCodeBlock) break;
      if (JspPsiUtil.isInJspFile(parent) && parent instanceof PsiFile) break;
      if (parent instanceof PsiCodeFragment) break;
      if (parent == null || parent instanceof PsiFile) return PsiElement.EMPTY_ARRAY;
      parent = parent.getParent();
    }

    if (!parent.equals(element1)) {
      while (!parent.equals(element1.getParent())) {
        element1 = element1.getParent();
      }
    }
    if (startOffset != element1.getTextRange().getStartOffset()) return PsiElement.EMPTY_ARRAY;

    if (!parent.equals(element2)) {
      while (!parent.equals(element2.getParent())) {
        element2 = element2.getParent();
      }
    }
    if (endOffset != element2.getTextRange().getEndOffset()) return PsiElement.EMPTY_ARRAY;

    if (parent instanceof PsiCodeBlock && parent.getParent() instanceof PsiBlockStatement &&
            element1 == ((PsiCodeBlock) parent).getLBrace() && element2 == ((PsiCodeBlock) parent).getRBrace()) {
      return new PsiElement[]{parent.getParent()};
    }

    PsiElement[] children = parent.getChildren();
    ArrayList<PsiElement> array = new ArrayList<>();
    boolean flag = false;
    for (PsiElement child : children) {
      if (child.equals(element1)) {
        flag = true;
      }
      if (flag && !(child instanceof PsiWhiteSpace)) {
        array.add(child);
      }
      if (child.equals(element2)) {
        break;
      }
    }

    for (PsiElement element : array) {
      if (!(element instanceof PsiStatement || element instanceof PsiWhiteSpace || element instanceof PsiComment)) {
        return PsiElement.EMPTY_ARRAY;
      }
    }

    return PsiUtilBase.toPsiElementArray(array);
  }

  @Override
  public boolean checkAvailable(@NotNull final Editor editor, @NotNull final PsiFile file, @NotNull final MoveInfo info, final boolean down) {
    if (file instanceof PsiJavaFile) return false;
    final boolean available = super.checkAvailable(editor, file, info, down);
    if (!available) return false;
    LineRange range = info.toMove;

    range = expandLineRangeToCoverPsiElements(range, editor, file);
    if (range == null) return false;
    info.toMove = range;
    final int startOffset = editor.logicalPositionToOffset(new LogicalPosition(range.startLine, 0));
    final int endOffset = editor.logicalPositionToOffset(new LogicalPosition(range.endLine, 0));
    final PsiElement[] statements = findStatementsInRange(file, startOffset, endOffset);
    if (statements.length == 0) return false;
    range.firstElement = statements[0];
    range.lastElement = statements[statements.length - 1];

    if (!checkMovingInsideOutside(file, editor, range, info, down)) {
      info.toMove2 = null;
      return true;
    }
    return true;
  }

  private boolean calcInsertOffset(PsiFile file, final Editor editor, LineRange range, @NotNull final MoveInfo info, final boolean down) {
    int line = down ? range.endLine + 1 : range.startLine - 1;
    int startLine = down ? range.endLine : range.startLine - 1;
    if (line < 0 || startLine < 0) return false;
    while (true) {
      final int offset = editor.logicalPositionToOffset(new LogicalPosition(line, 0));
      PsiElement element = firstNonWhiteElement(offset, file, true);

      while (element != null && !(element instanceof PsiFile)) {
        if (!element.getTextRange().grown(-1).shiftRight(1).contains(offset)) {
          PsiElement elementToSurround = null;
          boolean found = false;
          if ((element instanceof PsiStatement || element instanceof PsiComment)
                  && statementCanBePlacedAlong(element)) {
            found = true;
            if (!(element.getParent() instanceof PsiCodeBlock)) {
              elementToSurround = element;
            }
          } else if (element instanceof LeafPsiElement
                  && element.getParent() instanceof PsiCodeBlock) {
            String rBrace = ((LeafPsiElement) element).getElementType().toString();
            if ("}".equals(rBrace)) {
              // before code block closing brace
              found = true;
            }
          }
          if (found) {
            statementToSurroundWithCodeBlock = elementToSurround;
            info.toMove = range;
            int endLine = line;
            if (startLine > endLine) {
              int tmp = endLine;
              endLine = startLine;
              startLine = tmp;
            }

            info.toMove2 = down ? new LineRange(startLine, endLine) : new LineRange(startLine, endLine + 1);
            return true;
          }
        }
        element = element.getParent();
      }
      line += down ? 1 : -1;
      if (line == 0 || line >= editor.getDocument().getLineCount()) {
        return false;
      }
    }
  }

  private static boolean statementCanBePlacedAlong(final PsiElement element) {
    if (element instanceof PsiBlockStatement) return false;
    final PsiElement parent = element.getParent();
    if (parent instanceof PsiCodeBlock) return true;
    if (parent instanceof PsiIfStatement &&
            (element == ((PsiIfStatement) parent).getThenBranch() || element == ((PsiIfStatement) parent).getElseBranch())) {
      return true;
    }
    if (parent instanceof PsiWhileStatement && element == ((PsiWhileStatement) parent).getBody()) {
      return true;
    }
    if (parent instanceof PsiDoWhileStatement && element == ((PsiDoWhileStatement) parent).getBody()) {
      return true;
    }
    // know nothing about that
    return false;
  }

  private boolean checkMovingInsideOutside(PsiFile file, final Editor editor, LineRange range, @NotNull final MoveInfo info, final boolean down) {
    final int offset = editor.getCaretModel().getOffset();

    PsiElement elementAtOffset = file.getViewProvider().findElementAt(offset, GosuLanguage.instance());
    if (elementAtOffset == null) return false;

    PsiElement guard = elementAtOffset;
    do {
      guard = PsiTreeUtil.getParentOfType(guard, PsiMethod.class, PsiClassInitializer.class, PsiClass.class, PsiComment.class);
    }
    while (guard instanceof PsiAnonymousClass);

    PsiElement brace = itIsTheClosingCurlyBraceWeAreMoving(file, editor);
    if (brace != null) {
      int line = editor.getDocument().getLineNumber(offset);
      final LineRange toMove = new LineRange(line, line + 1);
      toMove.firstElement = toMove.lastElement = brace;
      info.toMove = toMove;
    }

    // cannot move in/outside method/class/initializer/comment
    if (!calcInsertOffset(file, editor, info.toMove, info, down)) return false;
    int insertOffset = down ? getLineStartSafeOffset(editor.getDocument(), info.toMove2.endLine) : editor.getDocument().getLineStartOffset(info.toMove2.startLine);
    PsiElement elementAtInsertOffset = file.getViewProvider().findElementAt(insertOffset, GosuLanguage.instance());
    PsiElement newGuard = elementAtInsertOffset;
    do {
      newGuard = PsiTreeUtil.getParentOfType(newGuard, PsiMethod.class, PsiClassInitializer.class, PsiClass.class, PsiComment.class);
    }
    while (newGuard instanceof PsiAnonymousClass);

    if (brace != null && PsiTreeUtil.getParentOfType(brace, PsiCodeBlock.class, false) !=
            PsiTreeUtil.getParentOfType(elementAtInsertOffset, PsiCodeBlock.class, false)) {
      info.indentSource = true;
    }
    if (newGuard == guard && isInside(insertOffset, newGuard) == isInside(offset, guard)) return true;

    // moving in/out nested class is OK
    if (guard instanceof PsiClass && guard.getParent() instanceof PsiClass) return true;
    if (newGuard instanceof PsiClass && newGuard.getParent() instanceof PsiClass) return true;

    return false;
  }

  private static boolean isInside(final int offset, final PsiElement guard) {
    if (guard == null) return false;
    TextRange inside = guard instanceof PsiMethod
                       ? ((PsiMethod) guard).getBody().getTextRange()
                       : guard instanceof PsiClassInitializer
                         ? ((PsiClassInitializer) guard).getBody().getTextRange()
                         : guard instanceof PsiClass && ((PsiClass) guard).getLBrace() != null && ((PsiClass) guard).getRBrace() != null
                           ? new TextRange(((PsiClass) guard).getLBrace().getTextOffset(), ((PsiClass) guard).getRBrace().getTextOffset())
                           : guard.getTextRange();
    return inside != null && inside.contains(offset);
  }

  private static LineRange expandLineRangeToCoverPsiElements(final LineRange range, Editor editor, final PsiFile file) {
    Pair<PsiElement, PsiElement> psiRange = getElementRange(editor, file, range);
    if (psiRange == null) return null;
    final PsiElement parent = PsiTreeUtil.findCommonParent(psiRange.getFirst(), psiRange.getSecond());
    Pair<PsiElement, PsiElement> elementRange = getElementRange(parent, psiRange.getFirst(), psiRange.getSecond());
    if (elementRange == null) return null;
    int endOffset = elementRange.getSecond().getTextRange().getEndOffset();
    Document document = editor.getDocument();
    if (endOffset > document.getTextLength()) {
      LOG.assertTrue(!PsiDocumentManager.getInstance(file.getProject()).isUncommited(document));
      LOG.assertTrue(PsiDocumentManagerImpl.checkConsistency(file, document));
    }
    int endLine;
    if (endOffset == document.getTextLength()) {
      endLine = document.getLineCount();
    } else {
      endLine = editor.offsetToLogicalPosition(endOffset).line + 1;
      endLine = Math.min(endLine, document.getLineCount());
    }
    int startLine = Math.min(range.startLine, editor.offsetToLogicalPosition(elementRange.getFirst().getTextOffset()).line);
    endLine = Math.max(endLine, range.endLine);
    return new LineRange(startLine, endLine);
  }

  private static PsiElement itIsTheClosingCurlyBraceWeAreMoving(final PsiFile file, final Editor editor) {
    LineRange range = getLineRangeFromSelection(editor);
    if (range.endLine - range.startLine != 1) return null;
    int offset = editor.getCaretModel().getOffset();
    Document document = editor.getDocument();
    int line = document.getLineNumber(offset);
    int lineStartOffset = document.getLineStartOffset(line);
    String lineText = document.getText().substring(lineStartOffset, document.getLineEndOffset(line));
    if (!lineText.trim().equals("}")) return null;

    return file.findElementAt(lineStartOffset + lineText.indexOf('}'));
  }
}

