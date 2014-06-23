/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.refactor.inline;

import com.intellij.codeInsight.TargetElementUtilBase;
import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.intention.QuickFixFactory;
import com.intellij.lang.Language;
import com.intellij.lang.refactoring.InlineActionHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiArrayAccessExpression;
import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiPostfixExpression;
import com.intellij.psi.PsiPrefixExpression;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.controlFlow.DefUseUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.refactoring.util.InlineUtil;
import com.intellij.refactoring.util.RefactoringMessageDialog;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.intellij.util.Query;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GosuInlineLocalHandler extends InlineActionHandler {
  private static final Logger LOG = Logger.getInstance("#com.intellij.refactoring.inline.InlineLocalHandler");

  private static final String REFACTORING_NAME = RefactoringBundle.message("inline.variable.title");


  @Override
  public boolean isEnabledForLanguage(Language l) {
    return l instanceof GosuLanguage;
  }

  public boolean canInlineElement(PsiElement element) {
    return element != null && element.getLanguage() instanceof GosuLanguage && element instanceof PsiLocalVariable;
  }

  public void inlineElement(Project project, Editor editor, PsiElement element) {
    final PsiReference psiReference = TargetElementUtilBase.findReference(editor);
    final IGosuReferenceExpression refExpr = psiReference instanceof IGosuReferenceExpression ? ((IGosuReferenceExpression) psiReference) : null;
    invoke(project, editor, (PsiLocalVariable) element, refExpr);
  }

  /**
   * should be called in AtomicAction
   */
  public static void invoke(@NotNull final Project project, final Editor editor, final PsiLocalVariable local, IGosuReferenceExpression refExpr) {
    if (!CommonRefactoringUtil.checkReadOnlyStatus(project, local)) {
      return;
    }

    final HighlightManager highlightManager = HighlightManager.getInstance(project);

    final String localName = local.getName();

    final Query<PsiReference> query = ReferencesSearch.search(local, GlobalSearchScope.allScope(project), false);
    if (query.findFirst() == null) {
      LOG.assertTrue(refExpr == null);
      String message = RefactoringBundle.message("variable.is.never.used", localName);
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INLINE_VARIABLE);
      return;
    }

    final PsiClass containingClass = PsiTreeUtil.getParentOfType(local, PsiClass.class);
    final List<PsiClass> innerClassesWithUsages = Collections.synchronizedList(new ArrayList<PsiClass>());
    final List<PsiElement> innerClassUsages = Collections.synchronizedList(new ArrayList<PsiElement>());
    query.forEach(new Processor<PsiReference>() {
      public boolean process(final PsiReference psiReference) {
        final PsiElement element = psiReference.getElement();
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        while (psiClass != containingClass && psiClass != null) {
          final PsiClass parentPsiClass = PsiTreeUtil.getParentOfType(psiClass, PsiClass.class, true);
          if (parentPsiClass == containingClass) {
            innerClassesWithUsages.add(psiClass);
            innerClassUsages.add(element);
          }
          psiClass = parentPsiClass;
        }
        return true;
      }
    });

    final PsiCodeBlock containerBlock = PsiTreeUtil.getParentOfType(local, PsiCodeBlock.class);
    LOG.assertTrue(containerBlock != null, local);

    final PsiExpression defToInline = innerClassesWithUsages.isEmpty()
            ? getDefToInline(local, refExpr, containerBlock)
            : getDefToInline(local, innerClassesWithUsages.get(0), containerBlock);
    if (defToInline == null) {
      final String key = refExpr == null ? "variable.has.no.initializer" : "variable.has.no.dominating.definition";
      String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message(key, localName));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INLINE_VARIABLE);
      return;
    }

    final List<PsiElement> refsToInlineList = new ArrayList<>();
    Collections.addAll(refsToInlineList, DefUseUtil.getRefs(containerBlock, local, defToInline));
    for (PsiElement innerClassUsage : innerClassUsages) {
      if (!refsToInlineList.contains(innerClassUsage)) {
        refsToInlineList.add(innerClassUsage);
      }
    }
    if (refsToInlineList.size() == 0) {
      String message = RefactoringBundle.message("variable.is.never.used.before.modification", localName);
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INLINE_VARIABLE);
      return;
    }
    final PsiElement[] refsToInline = PsiUtilBase.toPsiElementArray(refsToInlineList);

    EditorColorsManager manager = EditorColorsManager.getInstance();
    final TextAttributes attributes = manager.getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
    final TextAttributes writeAttributes = manager.getGlobalScheme().getAttributes(EditorColors.WRITE_SEARCH_RESULT_ATTRIBUTES);
    if (refExpr != null && PsiUtil.isAccessedForReading(refExpr) && ArrayUtil.find(refsToInline, refExpr) < 0) {
      final PsiElement[] defs = DefUseUtil.getDefs(containerBlock, local, refExpr);
      LOG.assertTrue(defs.length > 0);
      highlightManager.addOccurrenceHighlights(editor, defs, attributes, true, null);
      String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("variable.is.accessed.for.writing", localName));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INLINE_VARIABLE);
      WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
      return;
    }

    PsiFile workingFile = local.getContainingFile();
    for (PsiElement ref : refsToInline) {
      final PsiFile otherFile = ref.getContainingFile();
      if (!otherFile.equals(workingFile)) {
        String message = RefactoringBundle.message("variable.is.referenced.in.multiple.files", localName);
        CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INLINE_VARIABLE);
        return;
      }
    }

    for (final PsiElement ref : refsToInline) {
      final PsiElement[] defs = DefUseUtil.getDefs(containerBlock, local, ref);
      boolean isSameDefinition = true;
      for (PsiElement def : defs) {
        isSameDefinition &= isSameDefinition(def, defToInline);
      }
      if (!isSameDefinition) {
        highlightManager.addOccurrenceHighlights(editor, defs, writeAttributes, true, null);
        highlightManager.addOccurrenceHighlights(editor, new PsiElement[]{ref}, attributes, true, null);
        String message =
                RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("variable.is.accessed.for.writing.and.used.with.inlined", localName));
        CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INLINE_VARIABLE);
        WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
        return;
      }
    }

    final PsiElement writeAccess = checkRefsInAugmentedAssignmentOrUnaryModified(refsToInline);
    if (writeAccess != null) {
      HighlightManager.getInstance(project).addOccurrenceHighlights(editor, new PsiElement[]{writeAccess}, writeAttributes, true, null);
      String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("variable.is.accessed.for.writing", localName));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INLINE_VARIABLE);
      WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
      return;
    }

    if (editor != null && !ApplicationManager.getApplication().isUnitTestMode()) {
      // TODO : check if initializer uses fieldNames that possibly will be hidden by other
      // locals with the same names after inlining
      highlightManager.addOccurrenceHighlights(
              editor,
              refsToInline,
              attributes, true, null
      );
      int occurrencesCount = refsToInline.length;
      String occurencesString = RefactoringBundle.message("occurences.string", occurrencesCount);
      final String promptKey = isInliningVariableInitializer(defToInline)
              ? "inline.local.variable.prompt" : "inline.local.variable.definition.prompt";
      final String question = RefactoringBundle.message(promptKey, localName) + " " + occurencesString;
      RefactoringMessageDialog dialog = new RefactoringMessageDialog(
              REFACTORING_NAME,
              question,
              HelpID.INLINE_VARIABLE,
              "OptionPane.questionIcon",
              true,
              project);
      dialog.show();
      if (!dialog.isOK()) {
        WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
        return;
      }
    }

    final Runnable runnable = new Runnable() {
      public void run() {
        try {
          PsiExpression[] exprs = new PsiExpression[refsToInline.length];
          for (int idx = 0; idx < refsToInline.length; idx++) {
            PsiJavaCodeReferenceElement refElement = (PsiJavaCodeReferenceElement) refsToInline[idx];
            exprs[idx] = InlineUtil.inlineVariable(local, defToInline, refElement);
          }

          if (!isInliningVariableInitializer(defToInline)) {
            defToInline.getParent().delete();
          } else {
            defToInline.delete();
          }

          if (ReferencesSearch.search(local).findFirst() == null) {
            QuickFixFactory.getInstance().createRemoveUnusedVariableFix(local).invoke(project, editor, local.getContainingFile());
          }

          if (editor != null && !ApplicationManager.getApplication().isUnitTestMode()) {
            highlightManager.addOccurrenceHighlights(editor, exprs, attributes, true, null);
            WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
          }

          for (final PsiExpression expr : exprs) {
            InlineUtil.tryToInlineArrayCreationForVarargs(expr);
          }
        } catch (IncorrectOperationException e) {
          LOG.error(e);
        }
      }
    };

    CommandProcessor.getInstance().executeCommand(project, new Runnable() {
      public void run() {
        ApplicationManager.getApplication().runWriteAction(runnable);
      }
    }, RefactoringBundle.message("inline.command", localName), null);
  }

  @Nullable
  public static PsiElement checkRefsInAugmentedAssignmentOrUnaryModified(final PsiElement[] refsToInline) {
    for (PsiElement element : refsToInline) {

      PsiElement parent = element.getParent();
      if (parent instanceof PsiArrayAccessExpression) {
        element = parent;
        parent = parent.getParent();
      }

      if (parent instanceof PsiAssignmentExpression && element == ((PsiAssignmentExpression) parent).getLExpression()
              || isUnaryWriteExpression(parent)) {

        return element;
      }
    }
    return null;
  }

  private static boolean isUnaryWriteExpression(PsiElement parent) {
    IElementType tokenType = null;
    if (parent instanceof PsiPrefixExpression) {
      tokenType = ((PsiPrefixExpression) parent).getOperationTokenType();
    }
    if (parent instanceof PsiPostfixExpression) {
      tokenType = ((PsiPostfixExpression) parent).getOperationTokenType();
    }
    return tokenType == JavaTokenType.PLUSPLUS || tokenType == JavaTokenType.MINUSMINUS;
  }

  private static boolean isSameDefinition(final PsiElement def, final PsiExpression defToInline) {
    if (def instanceof PsiLocalVariable) {
      return defToInline.equals(((PsiLocalVariable) def).getInitializer());
    }
    final PsiElement parent = def.getParent();
    return parent instanceof PsiAssignmentExpression && defToInline.equals(((PsiAssignmentExpression) parent).getRExpression());
  }

  private static boolean isInliningVariableInitializer(final PsiExpression defToInline) {
    return defToInline.getParent() instanceof PsiVariable;
  }

  @Nullable
  private static PsiExpression getDefToInline(final PsiLocalVariable local,
                                              final PsiElement refExpr,
                                              final PsiCodeBlock block) {
    if (refExpr != null) {
      PsiElement def;
      if (refExpr instanceof PsiReferenceExpression && PsiUtil.isAccessedForWriting((PsiExpression) refExpr)) {
        def = refExpr;
      } else {
        final PsiElement[] defs = DefUseUtil.getDefs(block, local, refExpr);
        if (defs.length == 1) {
          def = defs[0];
        } else {
          return null;
        }
      }

      if (def instanceof PsiReferenceExpression && def.getParent() instanceof PsiAssignmentExpression) {
        final PsiAssignmentExpression assignmentExpression = (PsiAssignmentExpression) def.getParent();
        if (assignmentExpression.getOperationTokenType() != JavaTokenType.EQ) {
          return null;
        }
        final PsiExpression rExpr = assignmentExpression.getRExpression();
        if (rExpr != null) {
          return rExpr;
        }
      }
    }
    return local.getInitializer();
  }
}
